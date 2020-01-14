package com.ctb_open_car.view.adapter.im;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.group.GroupDto;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.utils.DeviceUtils;
import com.ctb_open_car.view.TagView;
import com.ctb_open_car.view.activity.im.ChatGroupActivity;
import com.ctb_open_car.view.activity.im.ImGroupInfoActivity;
import com.ctb_open_car.view.activity.im.TagGroupMoreActivity;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HotGoupListAdapter extends RecyclerView.Adapter {


    private SoftReference<AppCompatActivity> mActivity;
    private List<GroupDto> mGroupList;
    private final int ROW_TAG = 1;
    private final int ROW_GROUP = 2;


    public HotGoupListAdapter(AppCompatActivity appCompatActivity) {
        mActivity = new SoftReference<>(appCompatActivity);
    }

    public void setData(List<GroupDto> groupList) {
        this.mGroupList = groupList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity.get()).inflate(R.layout.im_chat_group_flow_item, parent, false);
        return new HotGroupListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupDto groupDto = getGroupItem(position);

        HotGroupListViewHolder viewHolder = (HotGroupListViewHolder) holder;

        viewHolder.mGroupName.setText(groupDto.getGroupName());
        viewHolder.mGroupCity.setText(groupDto.getCityCode());
        viewHolder.mCarSeria.setText(groupDto.getCarModelName());
        Glide.with(mActivity.get()).load(groupDto.getGroupIcon())
                .placeholder(R.drawable.icon_group)
                .transform(new RoundedCorners(Device.dip2px(70)))
                .into(viewHolder.mGroupThumb);

        String[] tags = groupDto.getTagName().split("、");
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewHolder.mGroupTagLayout.removeAllViews();

//        viewHolder.mGroupInfoLayout.removeAllViews();
//        TextView cityText = new TextView(mContext.get());
//        cityText.setText(groupDto.getCityCode());
//        cityText.setTextColor(ContextCompat.getColor(mContext.get(), R.color.color_4A4A4A));
//        cityText.setTextSize(12);
//        cityText.setLayoutParams(lp);
//        viewHolder.mGroupInfoLayout.addView(cityText);
//
//        TextView seriaText = new TextView(mContext.get());
//        seriaText.setTextColor(ContextCompat.getColor(mContext.get(), R.color.color_4A4A4A));
//        seriaText.setTextSize(12);
//        seriaText.setText(groupDto.getCarModelName());
//        seriaText.setLayoutParams(lp);
//        viewHolder.mGroupInfoLayout.addView(seriaText);


        for (String tag : tags) {
            if (TextUtils.isEmpty(tag)) {
                continue;
            }
            TextView tagTextView = new TextView(mActivity.get());
            tagTextView.setText(tag);

            tagTextView.setTextSize(14);
            tagTextView.setTextColor(ContextCompat.getColor(mActivity.get(), R.color.color_F95E59));
            tagTextView.setBackgroundResource(R.drawable.round_pink_border);
            int horizontal = 14;
            int vertical = 4;
            tagTextView.setPadding(horizontal, vertical, horizontal, vertical);
            tagTextView.setLayoutParams(lp);
            viewHolder.mGroupTagLayout.addView(tagTextView);
        }

    }

    public GroupDto getGroupItem(int position) {
        return mGroupList.get(position);
    }

    @Override
    public int getItemCount() {
        if (null == mGroupList) {
            return Collections.EMPTY_LIST.size();
        }
        if (mActivity.get() instanceof TagGroupMoreActivity) {
            return mGroupList.size();
        } else if (mGroupList.size() <= 2) {
            return mGroupList.size();
        } else if (mGroupList.size() <= 4) {
            return mGroupList.size() / 2 * 2;
        } else {
            return 4;
        }
    }

    public class HotGroupListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.im_group_avatar)
        ImageView mGroupThumb;
        @BindView(R.id.im_group_city)
        TextView mGroupCity;

        @BindView(R.id.im_group_car_series)
        TextView mCarSeria;

        @BindView(R.id.im_group_name)
        TextView mGroupName;

        @BindView(R.id.im_group_tag_layout)
        TagView mGroupTagLayout;

        @BindView(R.id.group_info_layout)
        LinearLayout mGroupInfoLayout;


        public HotGroupListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.im_group_panel)
        void toGoupNext() {
            int position = getAdapterPosition();
            GroupDto groupDto = getGroupItem(position);
            Intent i = new Intent();
            if (0 == groupDto.getIfInside()) {
                i.setClass(mActivity.get(), ImGroupInfoActivity.class);
                i.putExtra("group_id", groupDto.getGroupId());
            } else {
                i.putExtra("group_id", groupDto.getGroupId());
                i.setClass(mActivity.get(), ChatGroupActivity.class);
            }
            mActivity.get().startActivity(i);
        }
    }

}
