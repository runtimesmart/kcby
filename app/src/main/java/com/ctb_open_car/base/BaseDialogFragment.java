package com.ctb_open_car.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.DialogFragment;

import com.umeng.analytics.MobclickAgent;

public abstract class BaseDialogFragment extends DialogFragment {
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getTag());
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(getTag());
        super.onPause();
    }

    protected abstract String getTAG();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
