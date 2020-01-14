package com.ctb_open_car.view.adapter.im;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.ctb_open_car.bean.im.CarModelBean;
import com.ctb_open_car.bean.im.CarStyleBean;
import com.ctb_open_car.customview.azlist.AZItemEntity;
import com.google.gson.Gson;

import java.util.List;

public class CarStyleItemAdapter extends AZBaseAdapter<CarStyleBean, CarStyleItemAdapter.ItemHolder> {
	private OnItemListener mListener;
	private Context mContext;
	public CarStyleItemAdapter(Context context, List<AZItemEntity<CarStyleBean>> dataList) {
		super(dataList);
		mContext = context;
	}

	@Override
	public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.im_car_style_item, parent, false));
	}

	@Override
	public void onBindViewHolder(ItemHolder holder, int position) {
		holder.mTextName.setText(mDataList.get(position).getValue().getName());
		if(position == 0) {
			holder.mTextLetters.setVisibility(View.VISIBLE);
			holder.mTextLetters.setText(mDataList.get(position).getSortLetters());
		} else {
			if (!mDataList.get(position-1).getSortLetters().equals(mDataList.get(position).getSortLetters())) {
				holder.mTextLetters.setVisibility(View.VISIBLE);
				holder.mTextLetters.setText(mDataList.get(position).getSortLetters());
				holder.mLineView.setVisibility(View.GONE);
			} else {
				holder.mTextLetters.setVisibility(View.GONE);
				holder.mLineView.setVisibility(View.VISIBLE);
			}
		}

		Glide.with(mContext).asBitmap().circleCrop().load(mDataList.get(position).getValue().getRemoteLogo()).placeholder( R.drawable.default_avar_icon).error(R.drawable.default_avar_icon).into(new SimpleTarget<Bitmap>() {
			@Override
			public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
				holder.mImageView.setImageBitmap(resource);
			}
		});

		holder.mTextName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                if (mListener != null) {
					mListener.onItemListener(mDataList.get(position).getValue());
				}
			}
		});
	}

	public void setOnItemListener(OnItemListener listener) {
		this.mListener = listener;
	}

	public interface OnItemListener {
		void onItemListener(CarStyleBean position);
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
