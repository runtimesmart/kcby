package com.ctb_open_car.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.ctb_open_car.MainActivity;
import com.ctb_open_car.utils.Device;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import es.dmoral.toasty.Toasty;

public class ImageGridLayout extends NineGridLayout {
    private Context mContext;

    public ImageGridLayout(Context context) {
        super(context);
    }

    public ImageGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

    }

    @Override
    protected boolean displayOneImage(RatioImageView imageView, String url, int parentWidth) {
        Glide.with(mContext).load(url).into(imageView);
        setOneImageLayoutParams(imageView,parentWidth,Device.dip2px(168));
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        Glide.with(mContext).load(url).into(imageView);
    }

    @Override
    protected void onClickImage(int position, String url, List<String> urlList) {
         List<ImageInfo> imageInfoList = new ArrayList<>();
//        for (String image : urlList) {
//            ImageInfo imageInfo = new ImageInfo();
//            imageInfo.setOriginUrl(image);// 原图url
//            imageInfoList.add(imageInfo);
//        }
        ImagePreview
                .getInstance()
                .setEnableDragClose(true)
                // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
                .setContext(mContext)

                // 设置从第几张开始看（索引从0开始）
                .setIndex(position)

                //=================================================================================================
                // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                // 1：第一步生成的imageInfo List
//                .setImageInfoList(imageInfoList)

                // 2：直接传url List
                .setImageList(urlList)

                // 3：只有一张图片的情况，可以直接传入这张图片的url
                //.setImage(String image)
                //=================================================================================================

                // 开启预览
                .start();
//        Toasty.info(mContext, "点击了第" + (position + 1) + "张图片").show();
    }
}
