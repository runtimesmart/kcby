package com.ctb_open_car.utils;

import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;

import java.util.Calendar;
import java.util.HashMap;

public class ClickUtils {

    //防止多次点击
    /******************************************************************************************************************************************************/
    /**
     *  第一种 添加key值来
     *  开始 和 结束 来限制点击次数
     */
    /******************************************************************************************************************************************************/

    private static HashMap<String, Boolean> clickMap = new HashMap<>();

    public static void setFastDoubleClickStart(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        clickMap.put(key, true);
    }


    public static void setFastDoubleClickEnd(String key) {
        if (TextUtils.isEmpty(key) || !clickMap.containsKey(key)) {
            return;
        }
        clickMap.remove(key);
    }

    public static boolean isDoubleClick(String key) {
        if (clickMap.containsKey(key)) {
            return clickMap.get(key);
        }
        return false;
    }

    public static void clickClear() {
        clickMap.clear();
    }


    /******************************************************************************************************************************************************/
    /**
     *  第二种 设置view 的 setClickable
     */
    /******************************************************************************************************************************************************/


    public static void setFastDoubleClickStart(View view) {
        if (view == null) {
            return;
        }
        view.setClickable(false);
    }


    public static void setFastDoubleClickEnd(View view) {
        if (view == null) {
            return;
        }
        view.setClickable(true);
    }

    /******************************************************************************************************************************************************/
    /**
     * 第三种 使用计时方法 500毫秒
     */
    /******************************************************************************************************************************************************/

    private final static int setTime = 1000 / 2;

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = SystemClock.uptimeMillis(); // 此方法仅用于Android
        if (time - lastClickTime < setTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    /******************************************************************************************************************************************************/
    /**
     *  第四种 实现NoDoubleClickListener
     */
    /******************************************************************************************************************************************************/
    public static abstract class NoDoubleClickListener implements View.OnClickListener {

        public static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNoDoubleClick(v);
            }
        }

        protected abstract void onNoDoubleClick(View v);
    }

    /******************************************************************************************************************************************************/
    /**
     *  第五种 设置view 的 setClickable
     */
    /******************************************************************************************************************************************************/


    public static void setFastDoubleClickEnabledkStart(View view) {
        if (view == null) {
            return;
        }
        view.setEnabled(false);
    }


    public static void setFastDoubleClicEnabledkEnd(View view) {
        if (view == null) {
            return;
        }
        view.setEnabled(true);
    }


}
