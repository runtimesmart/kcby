package com.ctb_open_car.di.component;


import androidx.appcompat.app.AppCompatActivity;

import com.ctb_open_car.di.annotation.ActivityScope;
import com.ctb_open_car.di.module.ActivityModule;
import com.ctb_open_car.view.activity.im.ChatGroupActivity;
import com.ctb_open_car.view.activity.im.GroupSearchActivity;
import com.ctb_open_car.view.activity.map.CollectPoiActivity;
import com.ctb_open_car.view.activity.im.GroupSettingsActivity;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {
    AppCompatActivity getActivity();

    void inject(CollectPoiActivity collectPoiActivity);

    void inject(ChatGroupActivity chatGroupActivity);

    void inject(GroupSettingsActivity groupSettingsActivity);

    void inject(GroupSearchActivity groupSearchActivity);

    void inject(PersonHomeActivity personHomeActivity);
}
