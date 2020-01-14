package com.ctb_open_car.utils;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.AliossStsTokenApi;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import timber.log.Timber;

public class AliossUtils {

    private static OSS mOss;
    private static String mExpiration;
    private volatile static AliossUtils singleton;

    public static AliossUtils getSingleton() {
        if (singleton == null) {
            synchronized (AliossUtils.class) {
                if (singleton == null) {
                    singleton = new AliossUtils();
                }
            }
        }
        return singleton;
    }

    private AliossUtils() {
    }

    public OSS getOss() {
        return mOss;
    }

    public String getExpiration() {
        return mExpiration;
    }

    public void initALiOss(String accessKeyId, String accessKeySecret, String securityToken, String expiration) {
        Timber.e("accessKeySecret = %s ", accessKeySecret);
        Timber.e("securityToken = %s ", securityToken);
        Timber.e("expiration = %s ", expiration);

        mExpiration = expiration;
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // connction time out default 15s
        conf.setSocketTimeout(15 * 1000); // socket timeout，default 15s
        conf.setMaxConcurrentRequest(5); // synchronous request number，default 5
        conf.setMaxErrorRetry(2); // retry，default 2
        OSSLog.enableLog(); //write local log file ,path is SDCard_path\OSSLog\logs.csv
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken);
        mOss = new OSSClient(CTBApplication.getContext(), endpoint, credentialProvider, conf);
    }

    // 这里的objectKey其实就是服务器上的路径，即目录+文件名
    public String updateImage(String objectKey, String filePath) {

        PutObjectRequest put = new PutObjectRequest("ctb-public-file", objectKey, filePath);
        try {
            PutObjectResult putResult = mOss.putObject(put);
            Timber.e("putResult.getETag() = %s ", putResult.getETag());
            Timber.e(putResult.getETag());
            Timber.e(putResult.getRequestId());
            return putResult.getETag();
        } catch (ClientException e) {
            // 本地异常，如网络异常等。
            e.printStackTrace();
            Timber.e("本地异常 getMessage = %s", e.getMessage());
        } catch (ServiceException e) {
            // 服务异常。
            e.printStackTrace();
            Timber.e(e.getErrorCode());
            Timber.e(e.getRawMessage());
            Timber.e("服务异常  getMessage = %s", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Timber.e("未知异常 getMessage = %s", e.getMessage());
        }
        return "";
    }

    public void getAliStsToken(RxAppCompatActivity rxApp) {

        AliossStsTokenApi aliossStsTokenApi = new AliossStsTokenApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                JsonObject jsonObject = (JsonObject)object;
                String code = jsonObject.get("code").toString();
                int codeIndex = Integer.parseInt(code.replace("\"", ""));
                if (codeIndex == 0){
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    JsonObject aliyunSTSTokenDto = data.get("aliyunSTSTokenDto").getAsJsonObject();
                    String accessKeyId = aliyunSTSTokenDto.get("accessKeyId").toString().replace("\"", "");
                    String accessKeySecret = aliyunSTSTokenDto.get("accessKeySecret").toString().replace("\"", "");
                    String securityToken = aliyunSTSTokenDto.get("securityToken").toString().replace("\"", "");
                    mExpiration = aliyunSTSTokenDto.get("expiration").toString().replace("\"", "");
                    AliossUtils.getSingleton().initALiOss(accessKeyId, accessKeySecret, securityToken, mExpiration);
                }
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("mListNewBean " + e.toString());
            }
        }, rxApp);
        HttpManager.getInstance().doHttpDeal(aliossStsTokenApi);
    }

}
