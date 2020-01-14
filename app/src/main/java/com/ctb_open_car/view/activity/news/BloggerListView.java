package com.ctb_open_car.view.activity.news;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class BloggerListView extends ListView {
    public BloggerListView(Context context) {
        super(context);
    }

    public BloggerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BloggerListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
