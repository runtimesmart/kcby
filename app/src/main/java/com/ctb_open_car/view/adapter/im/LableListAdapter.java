package com.ctb_open_car.view.adapter.im;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.im.TagDtoBean;
import com.ctb_open_car.view.activity.im.AddGroupInfoActivity;
import com.ctb_open_car.view.activity.im.CreateGroupActivity;
import com.ctb_open_car.view.adapter.map.PoiCollectAdapter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class LableListAdapter extends RecyclerView.Adapter<LableListAdapter.LableListViewHolder> {

    private List<TagDtoBean> mLableList;
    private Context mContext;
    private OnItemListener mListener;
    private List<TagDtoBean> mSelectLablList = new ArrayList<>();
    public LableListAdapter(Context context, List<TagDtoBean> lableList){
        mContext = context;
        mLableList = lableList;
    }
    public void setData(List<TagDtoBean> lableList, List<TagDtoBean> selectLableList){
        mLableList = lableList;
        mSelectLablList = selectLableList;
        notifyDataSetChanged();
    }

    @Override
    public LableListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.im_lable_list_item, parent, false);
        return new LableListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LableListViewHolder holder, int position) {
        holder.textView.setText(mLableList.get(position).getTagName());

        if (mLableList.get(position).isSelect()) {
            holder.textView.setBackgroundResource(R.drawable.im_lable_bg_select);
            holder.textView.setTextColor(ContextCompat.getColor(mContext,R.color.color_FFFFFF));
        } else {
            holder.textView.setBackgroundResource(R.drawable.im_lable_bg);
            holder.textView.setTextColor(ContextCompat.getColor(mContext,R.color.color_4A4A4A));
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectLablList.size()< 3 || mLableList.get(position).isSelect()) {
                    mLableList.get(position).setSelect(!mLableList.get(position).isSelect());
                    notifyItemChanged(position);
//                    if(mLableList.get(position).isSelect()) {
//                        mSelectLablList.add(mLableList.get(position));
//                    } else {
//                        mSelectLablList.remove(mLableList.get(position));
//                    }
                    mListener.onItemListener(mLableList.get(position), mLableList.get(position).isSelect());
                } else {
                    Toasty.info(mContext, "最多选择三个标签").show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLableList.size();
    }

    public class LableListViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View view;
        public LableListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.city_name);
            view = itemView.findViewById(R.id.list_item);
        }
    }

    public void setOnItemListener(OnItemListener listener) {
        this.mListener = listener;
    }

    public interface OnItemListener {
        void onItemListener(TagDtoBean lable, boolean isAdd);
    }
}
