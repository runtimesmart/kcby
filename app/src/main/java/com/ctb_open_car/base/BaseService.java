package com.ctb_open_car.base;

import android.app.Service;

public abstract class BaseService extends Service {
    public abstract Object getTag();

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
