package com.ctb_open_car.view.adapter.im;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.areacode.AreaCodeDtoBean;
import com.ctb_open_car.customview.azlist.AZItemEntity;
import com.ctb_open_car.customview.azlist.AZWaveSideBarView;

import java.util.List;

public class ProvinceItemAdapter extends AZBaseAdapter<AreaCodeDtoBean, ProvinceItemAdapter.ItemHolder> {
	private OnItemListener mListener;
	public ProvinceItemAdapter(List<AZItemEntity<AreaCodeDtoBean>> dataList) {
		super(dataList);
	}

	@Override
	public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.im_city_item_adapter, parent, false));
	}

	@Override
	public void onBindViewHolder(ItemHolder holder, int position) {
		holder.mTextName.setText(mDataList.get(position).getValue().getAreaName());
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
		void onItemListener(AreaCodeDtoBean position);
	}
	static class ItemHolder extends RecyclerView.ViewHolder {

		TextView mTextName;
		TextView mTextLetters;
		View mLineView;
		ItemHolder(View itemView) {
			super(itemView);
			mTextName = itemView.findViewById(R.id.text_item_name);
			mTextLetters = itemView.findViewById(R.id.text_item_letters);
			mLineView = itemView.findViewById(R.id.line);
		}
	}
}
