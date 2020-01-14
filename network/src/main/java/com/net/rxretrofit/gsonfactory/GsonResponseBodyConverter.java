package com.net.rxretrofit.gsonfactory;

import com.net.rxretrofit.RxRetrofitApplication;
import com.net.rxretrofit.base.BaseResultEntity;
import com.net.rxretrofit.exception.AbnormalCodeException;
import com.net.rxretrofit.exception.HttpTimeException;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    /**
     * 针对数据返回成功、错误不同类型字段处理
     */
    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            BaseResultEntity result = gson.fromJson(response, BaseResultEntity.class);
            int code = result.getCode();
            if (code == 0) {
                return gson.fromJson(response, type);
            } else {
                //处理异常code
                if (RxRetrofitApplication.getInstance().getReponseInterceptor() != null) {
                    boolean resultAbnormal = RxRetrofitApplication.getInstance().getReponseInterceptor().handleAbnormalCode(result);
                    if (resultAbnormal) {
                        throw new AbnormalCodeException();
                    }
                }
                throw new HttpTimeException(result);
            }
        }finally {
            value.close();
        }
    }
}
