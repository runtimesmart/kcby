package com.ctb_open_car.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * 返回unix时间戳 (1970年至今的秒数)
     * @return
     */
    public static long getUnixStamp(){
        return System.currentTimeMillis()/1000;
    }

    /**
     * 得到昨天的日期
     * @return
     */
    public static String getYestoryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    /**
     * 得到今天的日期
     * @return
     */
    public static  String getTodayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    /**
     * 时间戳转化为时间格式
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 得到日期   yyyy-MM-dd
     * @param timeStamp  时间戳
     * @return
     */
    public static String formatDate(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(timeStamp);
        return date;
    }

    /**
     * 得到时间  HH:mm:ss
     * @param timeStamp   时间戳
     * @return
     */
    public static String getTime(long timeStamp) {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        String[] split = date.split("\\s");
        if ( split.length > 1 ){
            time = split[1];
        }
        return time;
    }

    /**
     * 格式化时分秒的格式  大于10 直接显示，小于10 就在前面补0
     *
     * @param time 要格式的时间
     * @return
     */
    private static String formatTime(int time) {
        return time < 10 ? "0" + time : "" + time;
    }
    public static String secToTime(int second) {
        second = Math.abs(second);
        StringBuffer time = new StringBuffer();
        int hour = second / 3600;
        if (hour == 0) {
            time.append("");
        }
        if (hour > 0) {
            time.append(formatTime(hour) + "小时");
        }
        hour = second % 3600;
        int minute = (hour + 59) / 60;
        time.append(formatTime(minute) + "分钟");
        return time.toString();
    }
    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime =System.currentTimeMillis();
        long time = (curTime - timeStamp)/1000;
        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
            return sdf.format(timeStamp);
        }  else {
            return "刚刚";
        }
//        if (time < 60 && time >= 0) {
//            return "刚刚";
//        } else if (time >= 60 && time < 3600) {
//            return time / 60 + "分钟前";
//        } else if (time >= 3600 && time < 3600 * 24) {
//            return time / 3600 + "小时前";
//        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
//            return time / 3600 / 24 + "天前";
//        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
//            return time / 3600 / 24 / 30 + "个月前";
//        } else if (time >= 3600 * 24 * 30 * 12) {
//            return time / 3600 / 24 / 30 / 12 + "年前";
//        } else {
//            return "刚刚";
//        }
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，(多少分钟)
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToFormat(long timeStamp) {
        long curTime =System.currentTimeMillis() / (long) 1000 ;
        long time = curTime - timeStamp;
        return time/60 + "";
    }

}
