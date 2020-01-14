package com.ctb_open_car.view.adapter.newsadapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;

/**
 * project name RecyclerText
 * package name com.text.recyclertext
 */
public class ChildViewHolder extends RecyclerView.ViewHolder {
    public TextView bloggerName;
    public TextView bloggerDes;
    public ImageView bloggerAvatar;
    public ImageView bloggerDesIcon;
    public RelativeLayout mRelativeLayout;

    public ChildViewHolder(View itemView) {
        super(itemView);
        bloggerName = (TextView) itemView.findViewById(R.id.blogger_name);
        bloggerDes = (TextView) itemView.findViewById(R.id.blogger_des);
        bloggerAvatar = (ImageView) itemView.findViewById(R.id.blogger_avatar);
        bloggerDesIcon = (ImageView) itemView.findViewById(R.id.blogger_des_icon);
        mRelativeLayout  = (RelativeLayout) itemView.findViewById(R.id.blogger_lay);
    }
}
