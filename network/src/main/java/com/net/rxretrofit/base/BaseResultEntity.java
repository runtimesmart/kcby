package com.net.rxretrofit.base;

/**
 * 回调信息统一封装类
 */
public class BaseResultEntity<T> {
    //  判断标示
    private int code;
    //    提示信息
    private String msg;
    //显示数据（用户需要关心的数据）
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T obj) {
        this.data = obj;
    }
}
