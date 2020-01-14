package com.ctb_open_car.utils.frescoUtil;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.ctb_open_car.utils.frescoUtil.exception.FPNullPointerException;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;


public class FrescoPlusInitializer {
    private static volatile FrescoPlusInitializer mInstance = null;
    private Context mContext;
    private boolean isDebug;
    private String logTag;

    private FrescoPlusInitializer() {
    }

    public static FrescoPlusInitializer getInstance() {
        if (mInstance == null) {
            synchronized (FrescoPlusInitializer.class) {
                if (mInstance == null) {
                    mInstance = new FrescoPlusInitializer();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        init(context, null);
    }

    public void init(Context context, FrescoPlusConfig frescoPlusConfig) {
        if (context == null) {
            throw new FPNullPointerException("WDImage initialize error,cause:context is null");
        }
        mContext = !(context instanceof Application) ? context.getApplicationContext() : context;
        initialize(context, frescoPlusConfig);
    }

    private void initialize(Context context, FrescoPlusConfig config) {
        final FrescoPlusConfig frescoPlusConfig;
        if (config == null)
            config = FrescoPlusConfig.newBuilder(mContext).build();
        frescoPlusConfig = config;

        isDebug = frescoPlusConfig.isDebug();
        logTag = frescoPlusConfig.getLogTag();

        printWDImageConfigLog(frescoPlusConfig);

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryName(DefaultConfigCentre.DEFAULT_DISK_CACHE_DIR_NAME)
                .setBaseDirectoryPath(frescoPlusConfig.getDiskCacheDir())
                .setMaxCacheSize(frescoPlusConfig.getMaxDiskCacheSize() * DefaultConfigCentre.MB)
                .setMaxCacheSizeOnLowDiskSpace(DefaultConfigCentre.DEFAULT_LOW_SPACE_DISK_CACHE_SIZE)
                .setMaxCacheSizeOnVeryLowDiskSpace(DefaultConfigCentre.DEFAULT_VERY_LOW_SPACE_DISK_CACHE_SIZE)
                .build();

        ImagePipelineConfig pipelineConfig = ImagePipelineConfig.newBuilder(mContext)
                .setBitmapsConfig(frescoPlusConfig.getBitmapConfig())
                .setResizeAndRotateEnabledForNetwork(true)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setDownsampleEnabled(true)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())//渐进式加载图片 避免网速过慢 解码慢 长时间等待
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(context, pipelineConfig);
    }


    public Context getContext() {
        return mContext;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public String getLogTag() {
        return logTag;
    }

    /**
     * print FrescoPlusConfig log
     *
     * @param frescoPlusConfig config
     */
    private void printWDImageConfigLog(FrescoPlusConfig frescoPlusConfig) {
        if (isDebug) {
            Log.d(FrescoPlusInitializer.getInstance().getLogTag(), "FrescoPlusInitializer init...Config:"
                    + "DiskCacheDir->" + frescoPlusConfig.getDiskCacheDir()
                    + ",MaxDiskCacheSize->" + frescoPlusConfig.getMaxDiskCacheSize()
                    + ",BitmapConfig->" + frescoPlusConfig.getBitmapConfig()
                    + ",IsDebug->" + frescoPlusConfig.isDebug()
                    + ",Tag->" + frescoPlusConfig.getLogTag());
        }
    }
}
