package com.net.rxretrofit.exception;

import com.net.rxretrofit.base.BaseResultEntity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class OkHttpException extends HttpException {

    BaseResultEntity baseResultEntity;
    public OkHttpException(Response<?> response) {
        super(response);
        setServerMsgBody(response);
    }

    public void setServerMsgBody(Response response) {
        if (null != response) {
            ResponseBody responseBody = response.errorBody();
            if (null != responseBody) {
                try {
                    String json = responseBody.string();
                    baseResultEntity = new Gson().fromJson(json, BaseResultEntity.class);

                } catch (IOException e) {
                }
            }
        }
    }
    public  String getErrorMsg() {
        String result = "";
        if(null!=baseResultEntity){
            result=  baseResultEntity.getMsg();
        }
        return result;
    }
    public  int getErrorCode() {
        int code=0;
        if(null!=baseResultEntity){
          code= baseResultEntity.getCode();
        }
        return code;
    }

}
