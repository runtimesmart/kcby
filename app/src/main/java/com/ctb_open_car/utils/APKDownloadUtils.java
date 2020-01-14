package com.ctb_open_car.utils;


import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ctb_open_car.base.CTBBaseApplication;
import com.ctb_open_car.engine.manager.FileDownloadManager;


public class APKDownloadUtils {
    private long downloadAPKId;

    private static class PKDownloadHolder {
        private static final APKDownloadUtils INSTANCE = new APKDownloadUtils();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static APKDownloadUtils getInstance() {
        return PKDownloadHolder.INSTANCE;
    }

    public void downloadAPK(String apkUrl) {
        FileDownloadManager downloadManager = FileDownloadManager.getInstance(CTBBaseApplication.getInstance().getCurrentActivity());
        downloadAPKId = downloadManager.startDownload(apkUrl, "smarthome", "升级", "smarthome");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CTBBaseApplication.getInstance().getCurrentActivity());
        sp.edit().putLong(DownloadManager.EXTRA_DOWNLOAD_ID, downloadAPKId).commit();
    }

    //检查下载状态
    public int checkStatus() {
        return FileDownloadManager.getInstance(CTBBaseApplication.getInstance()).getDownloadStatus(downloadAPKId);
    }


    /**
     * 刪除安裝包
     * 如果一个下载被取消了，所有相关联的文件，部分下载的文件和完全下载的文件都会被删除。
     */
    public void deleteAPK() {
        FileDownloadManager.getInstance(CTBBaseApplication.getInstance()).getDownloadManager().remove(downloadAPKId);
    }
}
