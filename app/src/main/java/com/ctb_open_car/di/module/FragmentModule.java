package com.ctb_open_car.di.module;


import androidx.fragment.app.Fragment;

import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.di.annotation.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Provides
    @FragmentScope
    Fragment provideFragment(){
        return this.mFragment;
    }

}
