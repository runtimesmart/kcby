package com.ctb_open_car.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.ctb_open_car.R;
import com.ctb_open_car.utils.Device;

public class SearchView  extends AppCompatEditText {

    private Drawable searchDrawable;
    private int offset;
    private int searchWidth;
    private String hintString;
    private int w;
    private int flag = 0;

    public SearchView(Context context) {
        super(context);
//        init();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init();
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();
    }

    private void init() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点
                    setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);

                } else {
                    // 失去焦点
                    setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                }
            }
        });
        setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //圈1
        searchWidth = getMeasuredWidth();
        hintString = getHint().toString();
        //圈2
        Paint paint = new Paint();
        Rect rect = new Rect();
        paint.getTextBounds(hintString, 0, hintString.length(), rect);
        w = Device.dip2px(rect.width());
        offset = searchWidth / 2 - w / 2 - Device.dip2px(24);
        if (flag == 0) {
            //圈3  //圈4
            setTextDrawable();
        }
        flag++;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (searchDrawable == null) {
            getDrawable();
        }
        if (length() > 0) {
            setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
            setCompoundDrawables(null, null, null, null);
        } else if (length() == 0) {
            setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            setTextDrawable();
        }
    }

    private void getDrawable() {
        //圈5
        Drawable[] compoundDrawables = getCompoundDrawables();
        searchDrawable = compoundDrawables[0];
    }

    private void setTextDrawable() {
        searchDrawable.setBounds(offset, 0, offset + searchDrawable.getIntrinsicWidth(), searchDrawable.getIntrinsicHeight());
        setCompoundDrawables(searchDrawable, null, null, null);


    }
}
