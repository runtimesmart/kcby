package com.net.rxretrofit.exception;

import com.net.rxretrofit.base.BaseResultEntity;

/**
 * 自定义错误信息，统一处理返回处理
 */
public class HttpTimeException extends RuntimeException {

    public static final int NO_DATA = 0x2;

    private BaseResultEntity httpResult;

    public HttpTimeException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public HttpTimeException(String detailMessage) {
        super(detailMessage);
    }

    public <T> HttpTimeException(BaseResultEntity<T> httpResult) {
        this.httpResult = httpResult;

    }

    public BaseResultEntity getHttpResult() {
        return httpResult;
    }

    /**
     * 转换错误数据
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code) {
        String message = "";
        switch (code) {
            case NO_DATA:
                message = "无数据";
                break;
            default:
                message = "error";
                break;

        }
        return message;
    }

    public BaseResultEntity getResultEntity() {
        return httpResult;
    }

}

