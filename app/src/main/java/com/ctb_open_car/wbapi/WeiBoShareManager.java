package com.ctb_open_car.wbapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.ctb_open_car.R;
//import com.sina.weibo.sdk.api.ImageObject;
//import com.sina.weibo.sdk.api.MultiImageObject;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.VideoSourceObject;
//import com.sina.weibo.sdk.api.WebpageObject;
//import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.utils.Utility;

import java.io.File;
import java.util.ArrayList;


public class WeiBoShareManager {
//    /**
//     * 第三方应用发送请求消息到微博，唤起微博分享界面。
//     */
//    public static void sendMessage(boolean hasText, boolean hasImage) {
////        sendMultiMessage(hasText, hasImage);
//    }
//
//    /**
//     * 第三方应用发送请求消息到微博，唤起微博分享界面。
//     */
//    public static void sendMultiMessage(boolean hasText, boolean hasImage,Context context) {
//
//
////        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
////        if (hasText) {
////            weiboMessage.textObject = getTextObj(context);
////        }
////        if (hasImage) {
////            weiboMessage.imageObject = getImageObj(context);
////        }
////        if(multiImageCheckbox.isChecked()){
////            weiboMessage.multiImageObject = getMultiImageObject();
////        }
////        if(videoCheckbox.isChecked()){
////            weiboMessage.videoSourceObject = getVideoObject();
////        }
////        shareHandler.shareMessage(weiboMessage, false);
//
//    }
//
//
//
//
//
//    /**
//     * 创建文本消息对象。
//     * @return 文本消息对象。
//     */
//    public static TextObject getTextObj(int resId,Context context) {
//        TextObject textObject = new TextObject();
//        textObject.text = getSharedText(resId,context);
//        textObject.title = "xxxx";
//        textObject.actionUrl = "http://www.baidu.com";
//        return textObject;
//    }
//
//    /**
//     * 获取分享的文本模板。
//     */
//    public static String getSharedText(int resId,Context context) {
//        String format = context.getString(resId);
//        String text = format;
//        return text;
//    }
//    /**
//     * 创建图片消息对象。
//     * @return 图片消息对象。
//     */
//    public static ImageObject getImageObj(Context context) {
//        ImageObject imageObject = new ImageObject();
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_launcher);
//        imageObject.setImageObject(bitmap);
//        return imageObject;
//    }
//
//    /**
//     * 创建多媒体（网页）消息对象。
//     *
//     * @return 多媒体（网页）消息对象。
//     */
//    public static WebpageObject getWebpageObj(ShareBean shareBean,Context context) {
//        WebpageObject mediaObject = new WebpageObject();
//        mediaObject.identify = Utility.generateGUID();
//        mediaObject.title =shareBean.getTitle();
//        mediaObject.description = shareBean.getDescription();
//        Bitmap  bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_launcher);
//        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
//        mediaObject.setThumbImage(bitmap);
//        mediaObject.actionUrl = shareBean.getActionUrl();
//        mediaObject.defaultText = shareBean.getDefaultText();
//        return mediaObject;
//    }
//
//    /***
//     * 创建多图
//     * @return
//     */
//    public MultiImageObject getMultiImageObject(Context context){
//        MultiImageObject multiImageObject = new MultiImageObject();
//        //pathList设置的是本地本件的路径,并且是当前应用可以访问的路径，现在不支持网络路径（多图分享依靠微博最新版本的支持，所以当分享到低版本的微博应用时，多图分享失效
//        // 可以通过WbSdk.hasSupportMultiImage 方法判断是否支持多图分享,h5分享微博暂时不支持多图）多图分享接入程序必须有文件读写权限，否则会造成分享失败
//        ArrayList<Uri> pathList = new ArrayList<Uri>();
//        pathList.add(Uri.fromFile(new File(context.getExternalFilesDir(null)+"/aaa.png")));
//        pathList.add(Uri.fromFile(new File(context.getExternalFilesDir(null)+"/bbbb.jpg")));
//        pathList.add(Uri.fromFile(new File(context.getExternalFilesDir(null)+"/ccc.JPG")));
//        pathList.add(Uri.fromFile(new File(context.getExternalFilesDir(null)+"/ddd.jpg")));
//        pathList.add(Uri.fromFile(new File(context.getExternalFilesDir(null)+"/fff.jpg")));
//        pathList.add(Uri.fromFile(new File(context.getExternalFilesDir(null)+"/ggg.JPG")));
//        pathList.add(Uri.fromFile(new File(context.getExternalFilesDir(null)+"/eee.jpg")));
//        pathList.add(Uri.fromFile(new File(context.getExternalFilesDir(null)+"/hhhh.jpg")));
//        pathList.add(Uri.fromFile(new File(context.getExternalFilesDir(null)+"/kkk.JPG")));
//        multiImageObject.setImageList(pathList);
//        return multiImageObject;
//    }
//
//    public static VideoSourceObject getVideoObject(Context context){
//        //获取视频
//        VideoSourceObject videoSourceObject = new VideoSourceObject();
//        videoSourceObject.videoPath = Uri.fromFile(new File(context.getExternalFilesDir(null)+"/eeee.mp4"));
//        return videoSourceObject;
//    }
}
