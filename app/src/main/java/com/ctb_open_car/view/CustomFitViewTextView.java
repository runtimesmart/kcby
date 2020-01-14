package com.ctb_open_car.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import com.ctb_open_car.R;

/**
 * 自定义自适应文字内容控件
 *
 * @attr CustomFitViewTextView
 */
public class CustomFitViewTextView extends AppCompatTextView {
    private static final String TAG = "CustomFitViewTextView";
    //控件的宽
    private int mViewWidth;
    //可以设置的最小文字
    private float mMinTextSize = 10;

    public CustomFitViewTextView(Context context) {
        this(context, null);
    }

    public CustomFitViewTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomFitViewTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * 初始化自定义属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFitViewTextView);
        mMinTextSize = mTypedArray.getDimension(R.styleable.CustomFitViewTextView_customMinTextSize, mMinTextSize);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        TextPaint mTextPaint = CustomFitViewTextView.this.getPaint();
        //获取当前设置文字的大小
        float mSourceTextSize = CustomFitViewTextView.this.getTextSize();
        //获取内容
        String mLoadContent = CustomFitViewTextView.this.getText().toString().trim();
        //测量内容的长度
        float mSourceTextContentSize = mTextPaint.measureText(mLoadContent);
        if (mSourceTextContentSize > mViewWidth) {//文字内容超过了控件的宽度
            //计算文字长度与文字大小的比例
            float scale = mSourceTextContentSize / mSourceTextSize;
            //根据控件长度计算合适的文字大小
            float newTextSize = mViewWidth / scale;
            //增加最小限制
            if (newTextSize < mMinTextSize) {
                newTextSize = mMinTextSize;
            }
            //重新设置文字尺寸
            CustomFitViewTextView.this.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算控件的宽度（如果设置了padding值，需要减去左右的padding值）
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
    }
}