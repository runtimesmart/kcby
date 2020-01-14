package com.ctb_open_car.utils.frescoUtil;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.ctb_open_car.utils.frescoUtil.exception.FPNullPointerException;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.io.File;

public class FrescoCacheUtil {
    public static File getDiskLruCacheDir(Context context) {
        if (context == null)
            throw new FPNullPointerException("context can not be null");
        if (!(context instanceof Application))
            context = context.getApplicationContext();
        File cacheDir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cacheDir = getSDFreeSize() > 100 ? context.getExternalCacheDir() : context.getCacheDir();
        } else {
            cacheDir = context.getCacheDir();
        }
        return cacheDir;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDFreeSize() {
        File path = Environment.getExternalStorageDirectory();
        if (path != null && path.exists() && path.isDirectory()) {
            StatFs sf = new StatFs(path.getPath());
            long blockSize = sf.getBlockSizeLong();
            long freeBlocks = sf.getAvailableBlocksLong();
            return (freeBlocks * blockSize) / 1024 / 1024;
        }
        return -1;
    }

    /**
     * 清除缓存
     */
    public static void clearMemoryCaches() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
    }

    /**
     * 清除硬盘缓存
     */
    public static void clearDiskCaches() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearDiskCaches();
    }

    /**
     * 清除所有缓存
     */
    public static void clearAllCache() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();
        imagePipeline.clearCaches();
    }
}
