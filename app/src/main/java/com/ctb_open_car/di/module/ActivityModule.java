package com.ctb_open_car.di.module;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.ctb_open_car.di.annotation.ActivityContext;
import com.ctb_open_car.di.annotation.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @ActivityScope
    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }
}
