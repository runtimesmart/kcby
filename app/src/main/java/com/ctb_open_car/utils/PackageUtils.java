package com.ctb_open_car.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.List;

public class PackageUtils {


    /**
     * [获取应用程序包相关信息]
     *
     * @param context 上下文对象
     * @return 当前应用包相关信息
     */
    public static synchronized PackageInfo getPackageInfo(Context context) {
        if (null == context) {
            return null;
        }
        String packageName = context.getPackageName();
        PackageInfo info;
        PackageManager manager = context.getPackageManager();
        // 根据packageName获取packageInfo
        try {
            info = manager.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch (Exception e) {
            return null;
        }
        return info;
    }

    /**
     * [获取应用程序名字]
     *
     * @param context 上下文对象
     * @return 当前应用的名字
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称]
     *
     * @param context 上下文对象
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本号]
     *
     * @param context 上下文对象
     * @return 当前应用的版本号
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * [获取当前应用程序的包名]
     *
     * @param context 上下文对象
     * @return 返回当前应用程序的包名
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过正在运行的进程信息获取当前应用程序的包名
     *
     * @param context 上下文对象
     * @return 返回当前应用程序的包名
     */
    public static synchronized String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return null;
    }

    /**
     * [获取当前应用程序的应用图标]
     *
     * @param context 上下文对象
     * @return 返回当前应用程序的应用图标，Bitmap格式
     */
    public static Bitmap getBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }

        Drawable d = packageManager.getApplicationIcon(applicationInfo);
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    /**
     * [获取当前应用程序的应用图标]
     *
     * @param context  上下文对象
     * @param packname 应用包名
     * @return 返回当前应用程序的应用图标，Drawable格式
     */
    public Drawable getAppIcon(Context context, String packname) {
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            //获取到应用信息
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取当前应用程序的权限]
     *
     * @param context  上下文对象
     * @param packname 应用包名
     * @return 返回当前应用程序的权限列表
     */
    public String[] getAllPermissions(Context context, String packname) {
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
            //获取到所有的权限
            return packinfo.requestedPermissions;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取当前应用程序的签名]
     *
     * @param context  上下文对象
     * @param packname 应用包名
     * @return 返回当前应用程序的签名
     */
    public static String getAppSignature(Context context, String packname) {
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //获取当前应用签名
            return packinfo.signatures[0].toCharsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packname;
    }

    /**
     * [获取当前展示的Activity名称]
     *
     * @param context 上下文对象
     * @return 返回当前展示的Activity名称
     */
    private static String getCurrentActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }
}
