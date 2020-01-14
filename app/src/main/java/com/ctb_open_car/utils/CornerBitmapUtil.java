package com.ctb_open_car.utils;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 裁剪bitmap成圆角bitmap
 */
public class CornerBitmapUtil {

    public static final int CORNER_NONE = 0;
    public static final int CORNER_TOP_LEFT = 1;
    public static final int CORNER_TOP_RIGHT = 1 << 1;
    public static final int CORNER_BOTTOM_LEFT = 1 << 2;
    public static final int CORNER_BOTTOM_RIGHT = 1 << 3;
    public static final int CORNER_ALL = CORNER_TOP_LEFT | CORNER_TOP_RIGHT | CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT;
    public static final int CORNER_TOP = CORNER_TOP_LEFT | CORNER_TOP_RIGHT;
    public static final int CORNER_BOTTOM = CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT;
    public static final int CORNER_LEFT = CORNER_TOP_LEFT | CORNER_BOTTOM_LEFT;
    public static final int CORNER_RIGHT = CORNER_TOP_RIGHT | CORNER_BOTTOM_RIGHT;

    /**
     * @param bitmap  原图
     * @param roundPx 圆角大小
     * @param corners 圆角类型
     * @return 圆角图
     */
    public static Bitmap fillet(Bitmap bitmap, int roundPx, int corners) {
        if (bitmap != null) {

            try {
                final int width = bitmap.getWidth();
                final int height = bitmap.getHeight();

                Bitmap paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(paintingBoard);
                canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);

                final Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(Color.BLACK);

                //画出4个圆角
                final RectF rectF = new RectF(0, 0, width, height);
                canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

                //把不需要的圆角去掉
                int notRoundedCorners = corners ^ CORNER_ALL;
                if ((notRoundedCorners & CORNER_TOP_LEFT) != 0) {
                    clipTopLeft(canvas, paint, roundPx, width, height);
                }
                if ((notRoundedCorners & CORNER_TOP_RIGHT) != 0) {
                    clipTopRight(canvas, paint, roundPx, width, height);
                }
                if ((notRoundedCorners & CORNER_BOTTOM_LEFT) != 0) {
                    clipBottomLeft(canvas, paint, roundPx, width, height);
                }
                if ((notRoundedCorners & CORNER_BOTTOM_RIGHT) != 0) {
                    clipBottomRight(canvas, paint, roundPx, width, height);
                }
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                final Rect src = new Rect(0, 0, width, height);
                final Rect dst = src;
                canvas.drawBitmap(bitmap, src, dst, paint);
                return paintingBoard;
            } catch (Exception exp) {
                return bitmap;
            }
        } else {
            return bitmap;
        }
    }

    private static void clipTopLeft(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, 0, offset, offset);
        canvas.drawRect(block, paint);
    }

    private static void clipTopRight(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(width - offset, 0, width, offset);
        canvas.drawRect(block, paint);
    }

    private static void clipBottomLeft(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, height - offset, offset, height);
        canvas.drawRect(block, paint);
    }

    private static void clipBottomRight(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(width - offset, height - offset, width, height);
        canvas.drawRect(block, paint);
    }
}
