package com.net.rxretrofit.download;

import com.net.rxretrofit.RxRetrofitApplication;
import com.net.rxretrofit.base.BaseNetUrl;
import com.net.rxretrofit.download.DownLoadListener.DownloadInterceptor;
import com.net.rxretrofit.exception.HttpTimeException;
import com.net.rxretrofit.exception.RetryWhenNetworkException;
import com.net.rxretrofit.http.HTTPSTrustManager;
import com.net.rxretrofit.http.HttpsFactory;
import com.net.rxretrofit.subscribers.ProgressDownSubscriber;
import com.net.rxretrofit.utils.DownloadDbManager;
import com.net.rxretrofit.utils.NetRequestUtil;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * http下载处理类
 */
public class HttpDownManager {
    /*记录下载数据*/
    private List<DownInfo> downInfos;
    /*回调sub队列*/
    private HashMap<String, ProgressDownSubscriber> subMap;
    /*数据库类*/
    private DownloadDbManager db;

    private HttpDownManager() {
        downInfos = new CopyOnWriteArrayList<>();
        subMap = new HashMap<>();
        db = DownloadDbManager.getInstance();
    }

    private static class HttpDownManagerHolder {
        private static final HttpDownManager INSTANCE = new HttpDownManager();
    }

    /**
     * 获取单例
     */
    public static HttpDownManager getInstance() {
        return HttpDownManagerHolder.INSTANCE;
    }

    /**
     * 开始下载
     */
    public void startDown(final DownInfo info) {
        /*正在下载不处理*/
        if (info == null || subMap.get(info.getUrl()) != null) {
            subMap.get(info.getUrl()).setDownInfo(info);
            return;
        }
        if (info.isShare()) {
            waitAll();
        }
        /*添加回调处理类*/
        ProgressDownSubscriber subscriber = new ProgressDownSubscriber(info);
        /*记录回调sub*/
        subMap.put(info.getUrl(), subscriber);
        /*获取service，多次请求公用一个sercie*/
        HttpDownService httpService;
        if (downInfos.contains(info)) {
            httpService = info.getService();
        } else {
            DownloadInterceptor interceptor = new DownloadInterceptor(subscriber);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //手动创建一个OkHttpClient并设置超时时间
            builder.connectTimeout(info.getConnectonTime(), TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(NetRequestUtil.getBasUrl(info.getRealUrl()))
                    .build();
            httpService = retrofit.create(HttpDownService.class);
            info.setService(httpService);
            downInfos.add(info);
        }

        /*得到rx对象-上一次下載的位置開始下載*/
        httpService.download("bytes=" + info.getReadLength() + "-", info.getRealUrl())
            /*指定线程*/
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
               /*失败后的retry配置*/
            .retryWhen(new RetryWhenNetworkException())
            /*读取下载写入文件*/
            .map(new Func1<ResponseBody, DownInfo>() {
                @Override
                public DownInfo call(ResponseBody responseBody) {
                    writeCaches(responseBody, new File(info.getSavePath()), info);
                    return info;
                }
            })
            /*回调线程*/
            .observeOn(AndroidSchedulers.mainThread())
            /*数据回调*/
            .subscribe(subscriber);
    }

    /**
     * 暂停下载
     *
     * @param info
     */
    public void pause(DownInfo info) {
        if (info == null) return;
        info.setState(DownState.PAUSE);

        if (subMap.containsKey(info.getUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getUrl());
            subscriber.updateDownInfo(info);
            subscriber.unsubscribe();
            subMap.remove(info.getUrl());
        }

        if (downInfos.contains(info)) {
            if (downInfos.get(downInfos.indexOf(info)) != null
                    && downInfos.get(downInfos.indexOf(info)).getListener() != null) {
                downInfos.get(downInfos.indexOf(info)).getListener().onPause(info);
            }
        }

        downInfos.remove(info);
        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
        db.update(info);
    }

    /**
     * 暂停正在下载
     */
    public void pauseAll() {
        for (DownInfo downInfo : downInfos) {
            pause(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }

    /**
     * 全部下载等待
     */
    public void waitAll() {
        for (DownInfo downInfo : downInfos) {
            if (downInfo == null) {
                continue;
            } else {
                if (DownState.START == downInfo.getState() || DownState.WAIT == downInfo.getState() || DownState.DOWN == downInfo.getState()) {
                    downInfo.setState(DownState.STOP);
                    if (downInfo.getListener() != null) {
                        downInfo.getListener().onStop(downInfo);
                    }
                    if (subMap.containsKey(downInfo.getUrl())) {
                        ProgressDownSubscriber subscriber = subMap.get(downInfo.getUrl());
                        subscriber.unsubscribe();
                        subMap.remove(downInfo.getUrl());
                    }
                    db.update(downInfo);
                }
            }
        }
    }

    /**
     * 返回正在下载的数据
     *
     * @return
     */
    public List<DownInfo> getDowningInfos() {
        List<DownInfo> downing = new ArrayList<>();
        for (DownInfo downInfo : downInfos) {
            if (DownState.DOWN == downInfo.getState() || DownState.START == downInfo.getState() || DownState.WAIT == downInfo.getState()) {
                downing.add(downInfo);
            }
        }
        return downing;
    }

    /**
     * 开始等待下载任务
     */
    public void startWaitDown() {
        DownInfo info;
        for (int i = downInfos.size() - 1; i >= 0; i--) {
            if (downInfos.get(i).isShare() && DownState.STOP == downInfos.get(i).getState()) {
                info = downInfos.get(i);
                downInfos.remove(info);
                info.setState(DownState.WAIT);
                startDown(info);
                return;
            }
        }
        for (int i = 0; i < downInfos.size(); i++) {
            if (DownState.STOP == downInfos.get(i).getState() && getDowningInfos().size() <= 0) {
                info = downInfos.get(i);
                downInfos.remove(info);
                i -= 1;
                info.setState(DownState.WAIT);
                startDown(info);
            }
        }
    }

    /**
     * 移除下载数据
     *
     * @param info
     */
    public void remove(DownInfo info) {
        subMap.remove(info.getUrl());
        downInfos.remove(info);
    }

    /**
     * 写入文件
     *
     * @param file
     * @param info
     * @throws Exception
     */
    public void writeCaches(ResponseBody responseBody, File file, DownInfo info) {
        try {
            RandomAccessFile randomAccessFile = null;
            InputStream inputStream = null;
            try {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();

                inputStream = responseBody.byteStream();
                randomAccessFile = new RandomAccessFile(file, "rwd");
                byte[] buffer = new byte[1024 * 1024];
                int len;

                randomAccessFile.seek(info.getReadLength());
                while ((len = inputStream.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, len);
                }
            } catch (Exception e) {
                throw new HttpTimeException(e.getMessage());
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
        } catch (Exception e) {
            throw new HttpTimeException(e.getMessage());
        }
    }

}
