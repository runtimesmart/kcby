package com.ctb_open_car.view.adapter.im;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.im.CarGroupSearchDto;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.view.TagView;
import com.ctb_open_car.view.activity.im.ChatGroupActivity;
import com.ctb_open_car.view.activity.im.ImGroupInfoActivity;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupSearchAdapter extends RecyclerView.Adapter {


    private SoftReference<Context> mContext;
    private List<CarGroupSearchDto> mGroupList;
    private final int ROW_TAG = 1;
    private final int ROW_GROUP = 2;


    public GroupSearchAdapter(Context context) {
        mContext = new SoftReference<>(context);
    }


    public void setData(List<CarGroupSearchDto> groupList) {
        this.mGroupList = groupList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext.get()).inflate(R.layout.layout_group_search_item, parent, false);
        return new SearchGroupListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CarGroupSearchDto groupDto = getGroupItem(position);

        SearchGroupListViewHolder viewHolder = (SearchGroupListViewHolder) holder;

        viewHolder.mGroupName.setText(groupDto.getGroupName());
//        viewHolder.mGroupCity.setText(groupDto.getCityCode());
//        viewHolder.mCarSeria.setText(groupDto.getCarModelName());
        Glide.with(mContext.get()).load(groupDto.getGroupIcon())
                .placeholder(R.drawable.icon_group)
                .transform(new RoundedCorners(Device.dip2px(70)))
                .into(viewHolder.mGroupThumb);

        String[] tags = groupDto.getTagName().split("、");
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        viewHolder.mGroupTagLayout.removeAllViews();

        TextView cityText=new TextView(mContext.get());
        cityText.setTextColor(ContextCompat.getColor(mContext.get(), R.color.color_B6B9C2));
        cityText.setText(groupDto.getCityCode());
        cityText.setTextSize(13);
        cityText.setLayoutParams(lp);
        viewHolder.mGroupTagLayout.addView(cityText);

        TextView seriaText=new TextView(mContext.get());
        seriaText.setText(groupDto.getCarModelName());
        seriaText.setTextColor(ContextCompat.getColor(mContext.get(), R.color.color_B6B9C2));
        lp.leftMargin=5;
        seriaText.setLayoutParams(lp);
        viewHolder.mGroupTagLayout.addView(seriaText);


        for (String tag : tags) {
            if (TextUtils.isEmpty(tag)) {
                continue;
            }
            TextView tagTextView = new TextView(mContext.get());
            tagTextView.setText("#" + tag);

            tagTextView.setTextSize(13);
            tagTextView.setTextColor(ContextCompat.getColor(mContext.get(), R.color.color_3240DB));
            int horizontal = 14;
            int vertical = 4;
            tagTextView.setPadding(horizontal, vertical, horizontal, vertical);
            tagTextView.setLayoutParams(lp);
            viewHolder.mGroupTagLayout.addView(tagTextView);
        }

    }

    public CarGroupSearchDto getGroupItem(int position) {
        return mGroupList.get(position);
    }

    @Override
    public int getItemCount() {
        if (null == mGroupList) {
            return Collections.EMPTY_LIST.size();
        }
        if (mGroupList.size() <= 2) {
            return mGroupList.size();
        } else {
            return mGroupList.size() / 2 * 2;
        }
    }

    public class SearchGroupListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.group_icon)
        ImageView mGroupThumb;

//        @BindView(R.id.city_area)
//        TextView mGroupCity;
//
//        @BindView(R.id.car_series)
//        TextView mCarSeria;

        @BindView(R.id.group_name)
        TextView mGroupName;

        @BindView(R.id.group_info_layout)
        TagView mGroupTagLayout;

        @BindView(R.id.group_desc)
        TextView mGroupDesc;


        public SearchGroupListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.group_search_panel)
        void toGoupInfo() {
            int position = getAdapterPosition();
            CarGroupSearchDto groupDto = getGroupItem(position);
            Intent i = new Intent();
            if (0 == groupDto.getIfInside()) {
                i.setClass(mContext.get(), ImGroupInfoActivity.class);
                i.putExtra("group_id", groupDto.getGroupId());
            } else {
                i.putExtra("group_id", groupDto.getGroupId());
                i.setClass(mContext.get(), ChatGroupActivity.class);
            }
            mContext.get().startActivity(i);
        }
    }
}
