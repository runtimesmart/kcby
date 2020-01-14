package com.ctb_open_car.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatRadioButton;

public class DrawCenterButton extends AppCompatRadioButton {

    public DrawCenterButton(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawCenterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawCenterButton(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableLeft = drawables[0];
        Drawable drawableRight = drawables[2];
        int gravity = getGravity();
        if (null != drawableLeft) {
            int left = 0;
            if (gravity == Gravity.CENTER) {
                left = ((int) (getWidth() - drawableLeft.getIntrinsicWidth() - getPaint().measureText(getText().toString()))
                        / 4);
            }
            drawableLeft.setBounds(left, 0, left + drawableLeft.getIntrinsicWidth(), drawableLeft.getIntrinsicHeight());

        } else if (null != drawableRight) {
            float textWidth = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth = 0;
            drawableWidth = drawableRight.getIntrinsicWidth();
            float bodyWidth = textWidth + drawableWidth + drawablePadding;
            setPadding(0, 0, (int)(getWidth() - bodyWidth), 0);

            canvas.translate((getWidth() - bodyWidth) / 2, 0);
        }
        super.onDraw(canvas);

    }
}
