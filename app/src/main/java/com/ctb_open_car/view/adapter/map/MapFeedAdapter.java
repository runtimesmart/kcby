package com.ctb_open_car.view.adapter.map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.FeedDetailData;
import com.ctb_open_car.bean.community.response.map.SnsMapFeedDto;
import com.ctb_open_car.utils.DateUtils;
import com.ctb_open_car.view.ImageGridLayout;
import com.ctb_open_car.view.activity.community.FeedsDetailActivity;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapFeedAdapter extends RecyclerView.Adapter {
    public BottomSheetDialog mBottomSheetDialog;
    private List<SnsMapFeedDto> mVisibleList;

    public MapFeedAdapter(BottomSheetDialog bottomSheetDialog) {
        this.mBottomSheetDialog = bottomSheetDialog;
    }

    public void setData(List<SnsMapFeedDto> visibleList) {
        this.mVisibleList = visibleList;
        notifyDataSetChanged();
    }


    private SnsMapFeedDto getItem(int position) {
        return mVisibleList.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mBottomSheetDialog.getContext()).inflate(R.layout.map_feed_item_layout, null);

        return new MapFeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MapFeedViewHolder mapFeedViewHolder = (MapFeedViewHolder) holder;
        SnsMapFeedDto snsMapFeedDto = getItem(position);
        mapFeedViewHolder.mUserName.setText(snsMapFeedDto.getFeedUser().getNickName());
        mapFeedViewHolder.mFeedContent.setText(snsMapFeedDto.getFeedContent());
        mapFeedViewHolder.mUserSign.setText(snsMapFeedDto.getFeedUser().getUserSign());
        mapFeedViewHolder.mPubDate.setText(DateUtils.convertTimeToFormat(snsMapFeedDto.getPublishTime()));
        Glide.with(mBottomSheetDialog.getContext()).load(snsMapFeedDto.getFeedUser().userIcon.getResourceUrl())
                .apply(new RequestOptions().bitmapTransform(new RoundedCorners(20)))
                .into(mapFeedViewHolder.mUserAvatar);
//        mapFeedViewHolder.mFeedPics.setUrlList(snsMapFeedDto.ge);
        if (0 == snsMapFeedDto.getFeedUser().userAuthStatus) {
            mapFeedViewHolder.mUserAuthState.setVisibility(View.GONE);
        } else {
            mapFeedViewHolder.mUserAuthState.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mVisibleList) {
            return Collections.EMPTY_LIST.size();
        }
        return mVisibleList.size();
    }

    public class MapFeedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ugc_user_avatar)
        ImageView mUserAvatar;

        @BindView(R.id.ugc_user_auth_status)
        ImageView mUserAuthState;

        @BindView(R.id.ugc_news_user)
        TextView mUserName;

        @BindView(R.id.ugc_news_signature)
        TextView mUserSign;

        @BindView(R.id.feeds_pics)
        ImageGridLayout mFeedPics;

        @BindView(R.id.ugc_pub_date)
        TextView mPubDate;

        @BindView(R.id.ugc_feeds_current_txt)
        QMUISpanTouchFixTextView mFeedContent;


        public MapFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.user_name_state_layout, R.id.feed_user_layout})
        void toPersonClick(View v) {
            SnsMapFeedDto snsMapFeedDto = getItem(getAdapterPosition());
            Intent i = new Intent(mBottomSheetDialog.getContext(), PersonHomeActivity.class);
            i.putExtra("user_id", snsMapFeedDto.getFeedUser().getUserId());
            mBottomSheetDialog.getContext().startActivity(i);
        }

        @OnClick(R.id.feed_content_origin)
        void toDetailClick(View v) {
            SnsMapFeedDto snsMapFeedDto = getItem(getAdapterPosition());

            Intent i = new Intent(mBottomSheetDialog.getContext(), FeedsDetailActivity.class);
            i.putExtra("feedId", snsMapFeedDto.getFeedId());
            mBottomSheetDialog.getContext().startActivity(i);
        }



    }
}
