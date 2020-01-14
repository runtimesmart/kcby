package com.ctb_open_car.di.component;

import androidx.fragment.app.Fragment;

import com.ctb_open_car.di.annotation.FragmentScope;
import com.ctb_open_car.di.module.FragmentModule;
import com.ctb_open_car.view.fragment.comminity.CarGroupFragment;

import dagger.Component;


@FragmentScope
@Component(modules = FragmentModule.class, dependencies = ActivityComponent.class)
public interface FragmentComponent {
    Fragment getFragment();

    void inject(CarGroupFragment fansGroupFragment);
}
