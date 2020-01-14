package com.ctb_open_car.di.component;

import android.app.Application;
import android.content.Context;


import com.ctb_open_car.di.annotation.ApplicationContext;
import com.ctb_open_car.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

}
