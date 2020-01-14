package com.ctb_open_car.view.fragment.dialog;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.PoiItem;
import com.ctb_open_car.R;
import com.ctb_open_car.utils.Device;
import com.ctb_open_car.view.activity.activities.ReleaseActivitiesActivity;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.activity.map.NaviActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapPoiAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<PoiItem> mPoiList;
    private LatLng mCurrentPosition;

    public MapPoiAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<PoiItem> poiList, LatLng latLng) {
        this.mCurrentPosition = latLng;
        this.mPoiList = poiList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_map_poi_item, null);
        return new MapPoiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PoiItem poiItem = getItem(position);
        MapPoiViewHolder mapPoiViewHolder = (MapPoiViewHolder) holder;
        if (0 == position) {
            String originTitle = poiItem.getTitle();
            String appendTitle = mapPoiViewHolder.mPoiName.getContext().getResources().getString(R.string.poi_nearest);
            String allTitle = originTitle + appendTitle;
            SpannableString spannableString = new SpannableString(allTitle);
            spannableString.setSpan(new AbsoluteSizeSpan(Device.dip2px(13)), originTitle.length(), allTitle.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(mapPoiViewHolder.mPoiName.getContext().getResources().getColor(R.color.color_F0BB5D)),
                    originTitle.length(), allTitle.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mapPoiViewHolder.mPoiName.setText(spannableString);
        } else {
            mapPoiViewHolder.mPoiName.setText(poiItem.getTitle());
        }
//        LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
//        float distance = AMapUtils.calculateLineDistance(latLng, mCurrentPosition) / 1000;
//        BigDecimal bigDecimal = new BigDecimal(distance).setScale(2, RoundingMode.UP);

        mapPoiViewHolder.mPoiDistance.setText(getFormatDistance(poiItem.getDistance()));
        mapPoiViewHolder.mPoiAdress.setText(poiItem.getSnippet());
        if (TextUtils.isEmpty(poiItem.getTel())) {
            mapPoiViewHolder.mPoiPhoneCall.setVisibility(View.GONE);
        } else {
            mapPoiViewHolder.mPoiPhoneCall.setVisibility(View.VISIBLE);

        }
    }

    private String getFormatDistance(int distance) {
        String friendly = "";
        if (distance >= 1000) {
            friendly = distance / 1000 + "公里";
        } else {
            friendly = distance + "米";
        }
        return friendly;

    }

    @Override
    public int getItemCount() {
        if (null == mPoiList) {
            return Collections.EMPTY_LIST.size();
        }
        return mPoiList.size();
    }

    public PoiItem getItem(int position) {
        return mPoiList.get(position);
    }

    /**
     * 最新活动消息Holder
     */
    public class MapPoiViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poi_name)
        TextView mPoiName;

        @BindView(R.id.poi_distance)
        TextView mPoiDistance;

        @BindView(R.id.poi_loc_txt)
        TextView mPoiAdress;

        @BindView(R.id.poi_phone_call)
        ImageView mPoiPhoneCall;

        @BindView(R.id.poi_to_navi)
        ImageView mPoiToNavi;

        public MapPoiViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


        public double getDistance(double lat1, double lon1, double lat2, double lon2) {
            float[] results = new float[1];

            Location.distanceBetween(lat1, lon1, lat2, lon2, results);
            return results[0];
        }

        @OnClick(R.id.poi_phone_call)
        void makePhoneCall(View v) {

            String phoneNumber = getItem(getAdapterPosition()).getTel();
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));//跳转到拨号界面，同时传递电话号码
            mContext.startActivity(dialIntent);
        }

        @OnClick({R.id.poi_to_navi, R.id.poi_item})
        void toNavi(View v) {
            NaviLatLng naviLatLng = new NaviLatLng();
            naviLatLng.setLatitude(getItem(getAdapterPosition()).getLatLonPoint().getLatitude());
            naviLatLng.setLongitude(getItem(getAdapterPosition()).getLatLonPoint().getLongitude());

            Intent naviIntent = new Intent(mContext, NaviActivity.class);
            naviIntent.putExtra("lat_lng", naviLatLng);
            mContext.startActivity(naviIntent);

        }


    }
}
