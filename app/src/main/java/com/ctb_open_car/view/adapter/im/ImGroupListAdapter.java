package com.ctb_open_car.view.adapter.im;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.im.CarStyleBean;
import com.ctb_open_car.bean.im.EmchatGroupMemberDto;
import com.ctb_open_car.customview.azlist.AZItemEntity;

import java.util.List;
import java.util.Map;

public class ImGroupListAdapter extends AZBaseAdapter<EmchatGroupMemberDto, ImGroupListAdapter.ItemHolder> {
	private OnItemListener mListener;
	private Context mContext;
	private Map mMap;
	public ImGroupListAdapter(Context context, List<AZItemEntity<EmchatGroupMemberDto>> dataList, Map map) {
		super(dataList);
		mContext = context;
        mMap = map;
	}

	@Override
	public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.im_group_member_list_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ItemHolder holder, int position) {
		holder.mTextName.setText(mDataList.get(position).getValue().getNickName());
		Integer num = (Integer)mMap.get(mDataList.get(position).getSortLetters());
		int number = ((num == null)? 1: num.intValue());
		if(position == 0) {
			holder.mTextLetters.setVisibility(View.VISIBLE);
			holder.mTextLetters.setText(mDataList.get(position).getSortLetters() + "(" +number+ ")");
		} else {
			if (!mDataList.get(position-1).getSortLetters().equals(mDataList.get(position).getSortLetters())) {
				holder.mTextLetters.setVisibility(View.VISIBLE);
                holder.mTextLetters.setText(mDataList.get(position).getSortLetters() + "(" + number+ ")");
				holder.mLineView.setVisibility(View.GONE);
			} else {
				holder.mTextLetters.setVisibility(View.GONE);
				holder.mLineView.setVisibility(View.VISIBLE);
			}
		}

		Glide.with(mContext).asBitmap().circleCrop().load(mDataList.get(position).getValue().getUserIcon().getResourceUrl()).placeholder( R.drawable.default_avar_icon).error(R.drawable.default_avar_icon).into(new SimpleTarget<Bitmap>() {
			@Override
			public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
				holder.mImageView.setImageBitmap(resource);
			}
		});

		holder.mTextName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                if (mListener != null) {
					mListener.onItemListener(position);
				}
			}
		});
	}

	public void upData( List<AZItemEntity<EmchatGroupMemberDto>> dataList, Map map) {
		setDataList(dataList);
		mMap = map;
	}

	public void setOnItemListener(OnItemListener listener) {
		this.mListener = listener;
	}

	public interface OnItemListener {
		void onItemListener(int position);
	}
	static class ItemHolder extends RecyclerView.ViewHolder {

		TextView mTextName;
		TextView mTextLetters;
		ImageView mImageView;
		View mLineView;
		ItemHolder(View itemView) {
			super(itemView);
			mTextName = itemView.findViewById(R.id.text_item_name);
			mTextLetters = itemView.findViewById(R.id.text_item_letters);
			mLineView = itemView.findViewById(R.id.line);
			mImageView = itemView.findViewById(R.id.logo_image);
		}
	}
}
