package com.ctb_open_car.view;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import cc.shinichi.library.glide.sunfusheng.progress.ProgressManager;

public class CTBGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
                                   @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);

        // 替换底层网络框架为okhttp3，这步很重要！
        // 如果您的app中已经存在了自定义的GlideModule，您只需要把这一行代码，添加到对应的重载方法中即可。
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
    }
}
