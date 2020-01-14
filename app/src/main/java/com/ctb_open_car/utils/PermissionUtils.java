package com.ctb_open_car.utils;

import android.Manifest;
import android.app.Activity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import io.reactivex.functions.Consumer;
public class PermissionUtils {
    /**
     * 初始化的时候使用，请求固定的权限列表
     * @param activity
     */
    public static void requestPermissionList(RxAppCompatActivity activity) {
        new RxPermissions(activity)
                .requestEach(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA
                ).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {

            }
        });
    }


    /**
     * 当调用某个放法，如打开相机的时候，如果之前没有授权，需要再次获取权限，然后产生回调。
     * @param activity
     */
    public static void requestPermissionSingle(RxAppCompatActivity activity, String permission, Consumer<Permission> consumer) {
        new RxPermissions(activity)
                .requestEach(permission
                ).subscribe(consumer);
    }


    /**
     * 当调用某个放法，如获取deviceId时候，如果之前没有授权，需要再次获取权限，不需要回调。
     * @param activity
     */
    public static void requestPermissionSingle(RxAppCompatActivity activity, String permission) {
        new RxPermissions(activity)
                .request(permission
                ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {

            }
        });
    }
}
