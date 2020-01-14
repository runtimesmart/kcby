package com.ctb_open_car.view.adapter.im;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.GroupRecommend;
import com.ctb_open_car.bean.community.response.group.GroupDto;
import com.ctb_open_car.ui.GridViewDivider;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.view.activity.im.TagGroupMoreActivity;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherGoupListAdapter extends RecyclerView.Adapter {


    private SoftReference<AppCompatActivity> mActivity;
    private List<GroupRecommend> mGroupRecommendList;

    public OtherGoupListAdapter(AppCompatActivity activity) {
        mActivity = new SoftReference<>(activity);
    }

    public void setRecommendData(List<GroupRecommend> recommendGroupList) {
        this.mGroupRecommendList = recommendGroupList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (ROW_GROUP == viewType) {
        View v = LayoutInflater.from(mActivity.get()).inflate(R.layout.im_chat_group_list, null, false);
        return new OtherGroupListViewHolder(v);
//        } else {
//            View v = LayoutInflater.from(mActivity.get()).inflate(R.layout.im_chat_group_tag, parent, false);
//            return new GroupTagViewHolder(v);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupRecommend groupDataDto = getGroupDataItem(position);

//        if (ROW_TAG == getItemViewType(position)) {
//            GroupTagViewHolder tagHolder = (GroupTagViewHolder) holder;
//            tagHolder.mGroupTag.setText(groupDataDto.getTagName());
//        } else if (ROW_GROUP == getItemViewType(position)) {
        OtherGroupListViewHolder viewHolder = (OtherGroupListViewHolder) holder;
        viewHolder.mGroupTag.setText(groupDataDto.getTagName());
//            GroupDto groupDto = getGroupItem(position);
//            viewHolder.mGroupName.setText(groupDto.getGroupName());
//            Glide.with(mActivity.get()).load(groupDto.getGroupIcon())
//                    .placeholder(R.drawable.icon_group)
//                    .transform(new RoundedCorners(Device.dip2px(70)))
//                    .into(viewHolder.mGroupThumb);
//        }
        GridLayoutManager otherLayoutManager = new GridLayoutManager(mActivity.get(), 2);
        otherLayoutManager.setOrientation(RecyclerView.VERTICAL);
        viewHolder.mGroupList.setLayoutManager(otherLayoutManager);
        /**防止fragment缓存item 导致每次加载divider导致 间隔不断变大*/
        if (null == viewHolder.mOtherDivider) {
            viewHolder.mOtherDivider = new GridViewDivider(2, 15);
            viewHolder.mGroupList.addItemDecoration(viewHolder.mOtherDivider);
        }

        HotGoupListAdapter groupListAdapter = new HotGoupListAdapter(mActivity.get());
        viewHolder.mGroupList.setAdapter(groupListAdapter);
        groupListAdapter.setData(getGroupDataItem(position).getGroupList());
    }


//    @Override
//    public int getItemViewType(int position) {
//        if ((position & 1) == 1) {
//            return ROW_TAG;
//        } else {
//            return ROW_GROUP;
//        }
//    }

    public GroupRecommend getGroupDataItem(int position) {
        return mGroupRecommendList.get(position);
    }

    public GroupDto getGroupItem(int position) {
        return getGroupDataItem(position).getGroupList().get(position);
    }

    @Override
    public int getItemCount() {
        if (null == mGroupRecommendList) {
            return Collections.EMPTY_LIST.size();
        }
        return mGroupRecommendList.size();
    }

    public class OtherGroupListViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.im_group_tag_group_list)
        RecyclerView mGroupList;


        public GridViewDivider mOtherDivider;

//        @BindView(R.id.im_group_avatar)
//        ImageView mGroupThumb;
//
//        @BindView(R.id.im_group_city)
//        TextView mGroupCity;
//
//        @BindView(R.id.im_group_car_series)
//        TextView mCarSeria;
//
//        @BindView(R.id.im_group_name)
//        TextView mGroupName;

        @BindView(R.id.im_group_tag_name)
        TextView mGroupTag;
        @BindView(R.id.im_group_more)
        TextView mGroupMore;

        public OtherGroupListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick(R.id.im_group_more)
        void toMoreGroup(View v) {
            int position = getAdapterPosition();
            GroupRecommend groupRecommend = getGroupDataItem(position);
            List<GroupDto> groupDtos = groupRecommend.getGroupList();
            Intent i = new Intent(mActivity.get(), TagGroupMoreActivity.class);
            i.putExtra("tag_group_list", (Serializable) groupDtos);
            i.putExtra("tag_type", groupRecommend.getTagName());
            mActivity.get().startActivity(i);
        }


    }

    public class GroupTagViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.im_group_tag_name)
        TextView mGroupTag;
        @BindView(R.id.im_group_more)
        TextView mGroupMore;

        public GroupTagViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.im_group_more)
        void toMoreGroup(View v) {

        }


    }
}
