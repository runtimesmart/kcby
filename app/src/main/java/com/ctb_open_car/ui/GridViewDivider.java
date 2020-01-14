package com.ctb_open_car.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridViewDivider extends RecyclerView.ItemDecoration {
    private int mSpace = 1;     //间隔
    private int mSpan = 2;     //间隔
    private Rect mRect = new Rect(0, 0, 0, 0);


    public GridViewDivider(int span, int space) {
        if (space > 0) {
            mSpace = space;
        }
        mSpan = span;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        int itemPosition = parent.getChildAdapterPosition(view);
        int eachWidth = (mSpan - 1) * mSpace / mSpan;
//        int left = itemPosition % mSpan * (mSpace - eachWidth);
//        int right = eachWidth-left;

        int left=(mSpace-eachWidth)/2;
        int right=mSpace-left;
        outRect.set(left,mSpace,right,mSpace);
    }
}
