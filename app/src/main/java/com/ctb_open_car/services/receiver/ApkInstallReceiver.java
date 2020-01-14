package com.ctb_open_car.services.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.ctb_open_car.engine.manager.FileDownloadManager;


public class ApkInstallReceiver extends BroadcastReceiver {
    private static class ApkInstallHolder {
        private static final ApkInstallReceiver INSTANCE = new ApkInstallReceiver();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static ApkInstallReceiver getInstance() {
        return ApkInstallHolder.INSTANCE;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        }
    }


}
