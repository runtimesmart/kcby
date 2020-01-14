package com.ctb_open_car.view.adapter.newsadapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;

/**
 * project name RecyclerText
 * package name com.text.recyclertext
 * Create by lxg
 * on 2017/8/21
 * at 13:37
 */
public class ParentViewHolder extends RecyclerView.ViewHolder {
    public RecyclerView rv_child;
    public TextView columnName;
    public TextView columnDes;
    public ImageView columnUrl;
    public LinearLayout groupHead;

    public ParentViewHolder(View itemView) {
        super(itemView);
        columnName = (TextView) itemView.findViewById(R.id.tv_column_name);
        columnDes = (TextView) itemView.findViewById(R.id.tv_column_des);
        columnUrl = (ImageView) itemView.findViewById(R.id.group_icon);
        rv_child = (RecyclerView) itemView.findViewById(R.id.rv_child);
        groupHead = (LinearLayout) itemView.findViewById(R.id.group_head);
    }
}
