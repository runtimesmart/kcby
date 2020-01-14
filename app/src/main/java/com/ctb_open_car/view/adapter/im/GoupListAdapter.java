//package com.ctb_open_car.view.adapter.im;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.ctb_open_car.R;
//import com.ctb_open_car.bean.community.response.GroupRecommend;
//import com.ctb_open_car.bean.community.response.group.GroupDto;
//
//import java.lang.ref.SoftReference;
//import java.util.Collections;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class GoupListAdapter extends RecyclerView.Adapter {
//
//
//    private SoftReference<Context> mContext;
//    private List<GroupRecommend> mGroupRecommendList;
//    private List<GroupDto> mGroupHotList;
//    private List<GroupDto> mGroupMyList;
//    private final int ROW_SEARCH = 1;
//    private final int ROW_MY_TAG = 2;
//    private final int ROW_MY_GROUP = 3;
//    private final int ROW_HOT_TAG = 4;
//    private final int ROW_HOT_GROUP = 5;
//    private final int ROW_OTHEAR = 6;
//
//
//    public GoupListAdapter(Context context) {
//        mContext = new SoftReference<>(context);
//    }
//
//    public void setRecommendData(List<GroupRecommend> recommendGroupList) {
//        this.mGroupRecommendList = recommendGroupList;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (ROW_SEARCH == viewType) {
//
//        } else if(ROW_MY_TAG==viewType) {
//            View v = LayoutInflater.from(mContext.get()).inflate(R.layout.im_chat_group_flow_item, parent, false);
//            return new OtherGroupListViewHolder(v);
//            View v = LayoutInflater.from(mContext.get()).inflate(R.layout.im_chat_group_tag, parent, false);
//            return new GroupTagViewHolder(v);
//        }else if(ROW_MY_GROUP==viewType){
//
//        }else if(ROW_HOT_TAG==viewType){
//
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        GroupRecommend groupDataDto = getGroupDataItem(position);
//
////        if (ROW_TAG == getItemViewType(position)) {
////            GroupTagViewHolder tagHolder = (GroupTagViewHolder) holder;
////            tagHolder.mGroupTag.setText(groupDataDto.getTagName());
////        } else if (ROW_GROUP == getItemViewType(position)) {
////            OtherGroupListViewHolder viewHolder = (OtherGroupListViewHolder) holder;
////
////            GroupDto groupDto = getGroupItem(position);
////            viewHolder.mGroupName.setText(groupDto.getGroupName());
////            Glide.with(mContext.get()).load(groupDto.getGroupIcon()).into(viewHolder.mGroupThumb);
////        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if ((position & 1) == 1) {
//            return ROW_GROUP;
//    } else {
//        return ROW_TAG;
//    }
////        if (position == 0) {
////            return ROW_SEARCH;
////        } else if(position==1) {
////            return ROW_MY_TAG;
////        }else if(position==2) {
////            return ROW_MY_GROUP;
////        }else if(position==mGroupMyList.size()+1) {
////            return ROW_HOT_TAG;
////        }else if(position==mGroupMyList.size()+2) {
////            return ROW_HOT_GROUP;
////        }else if(position==)
////            return ROW_OTHEAR;
//    }
//
//    public GroupRecommend getGroupDataItem(int position) {
//        return mGroupRecommendList.get(position);
//    }
//
//    public GroupDto getGroupItem(int position) {
//        return getGroupDataItem(position).getGroupList().get(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        if (null == mGroupRecommendList) {
//            return Collections.EMPTY_LIST.size();
//        }
//        return mGroupRecommendList.size();
//    }
//
//    public class OtherGroupListViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.im_group_avatar)
//        ImageView mGroupThumb;
//        @BindView(R.id.im_group_city)
//        TextView mGroupCity;
//
//        @BindView(R.id.im_group_car_series)
//        TextView mCarSeria;
//
//        @BindView(R.id.im_group_name)
//        TextView mGroupName;
//
//
//        public OtherGroupListViewHolder(@NonNull View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
//
//    public class GroupTagViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.im_group_tag_name)
//        TextView mGroupTag;
//        @BindView(R.id.im_my_group)
//        TextView mGroupMore;
//
//        public GroupTagViewHolder(@NonNull View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//
//        @OnClick(R.id.im_my_group)
//        void toMoreGroup(View v) {
//
//        }
//    }
//}
