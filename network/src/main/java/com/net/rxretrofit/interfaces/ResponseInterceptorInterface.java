package com.net.rxretrofit.interfaces;


import com.net.rxretrofit.base.BaseResultEntity;

public interface ResponseInterceptorInterface {
    boolean handleAbnormalCode(BaseResultEntity result);
}
