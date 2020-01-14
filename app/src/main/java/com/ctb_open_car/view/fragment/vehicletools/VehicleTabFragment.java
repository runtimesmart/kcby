package com.ctb_open_car.view.fragment.vehicletools;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.vehicletools.VehicleToolsBean;
import com.ctb_open_car.view.activity.vehicletools.ViolationDetailsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车主工具-车辆信息
 * <p>车主工具-首页</p>
 */
public class VehicleTabFragment extends BaseFragment {

    @BindView(R.id.vehicle_number_message)
    TextView mVehicleNumMessage;
    private VehicleToolsBean.CarInfoBean mCarInfoBeans;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mCarInfoBeans = (VehicleToolsBean.CarInfoBean) bundle.getSerializable("CarInfoBeans");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_info, container, false);
        ButterKnife.bind(this, view);

        if (mCarInfoBeans.getPointCount() > 0) {
            String str = getString(R.string.vehicle_number_message, mCarInfoBeans.getViolateCount());
            SpannableStringBuilder ssb = new SpannableStringBuilder(str);
            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor( getContext(),R.color.color_FF0000)),str.indexOf(" "),str.lastIndexOf(" "), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mVehicleNumMessage.setText(ssb);
        } else {
            String str = getString(R.string.vehicle_number_message_no);
            mVehicleNumMessage.setText(Html.fromHtml(str));
            mVehicleNumMessage.setTextColor(ContextCompat.getColor( getContext(),R.color.color_0C0C0C));
        }

        return view;
    }

    @Override
    protected String getTAG() {
        return null;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }


    @OnClick(R.id.vehicle_number_message)
    public void onViewClicked() {
        if (mCarInfoBeans.getPointCount() > 0) {
            Intent intent = new Intent(getContext(), ViolationDetailsActivity.class);
            intent.putExtra("mCarInfoBeans", mCarInfoBeans);
            getActivity().startActivity(intent);
        }
    }
}
