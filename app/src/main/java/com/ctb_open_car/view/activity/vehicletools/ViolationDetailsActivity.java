package com.ctb_open_car.view.activity.vehicletools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.vehicletools.VehicleToolsBean;
import com.ctb_open_car.view.adapter.vehicletoolsadpter.ViolationDetailsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViolationDetailsActivity extends BaseActivity {
    @BindView(R.id.ic_back)
    ImageView mBack;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.title_bar)
    TextView mTitleBar;
    @BindView(R.id.recyuclerview)
    RecyclerView mRecyclerview;

    @BindView(R.id.vehicle_number)
    TextView mViolationNum;
    @BindView(R.id.vehicle_penalty)
    TextView mViolationPenalty;
    @BindView(R.id.vehicle_buckle)
    TextView mViolationBuckle;

    private VehicleToolsBean.CarInfoBean mCarInfoBeans;
    private ViolationDetailsAdapter mAdpater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_violation_details);
        ButterKnife.bind(this);

        mCarInfoBeans = (VehicleToolsBean.CarInfoBean)getIntent().getSerializableExtra("mCarInfoBeans");

        mTitleTv.setText(getString(R.string.vehicle_shear));
        mTitleTv.setVisibility(View.VISIBLE);
        mTitleBar.setText(mCarInfoBeans.getPlate());
        mViolationNum.setText(getString(R.string.vehicle_number,mCarInfoBeans.getViolateCount()));
        mViolationPenalty.setText(getString(R.string.penalty_num, mCarInfoBeans.getPunishAmountCount()));
        mViolationBuckle.setText(getString(R.string.buckle_fraction, mCarInfoBeans.getPointCount()));

        mAdpater = new ViolationDetailsAdapter(this, mCarInfoBeans.getViolateList());
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(mAdpater);
    }

    @Override
    public Object getTag() {
        return null;
    }

    @OnClick({R.id.ic_back, R.id.violation_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                onBackPressed();
                break;
            case R.id.violation_bt:
                //  "https://m.jiaofabao.com/in/checkResult.html?cc=ctb_app&carId=902876"
                Intent intent = new Intent(ViolationDetailsActivity.this, ViolationWebViewActivity.class);
                intent.putExtra("violateDzyH5Url", mCarInfoBeans.getViolateDzyH5Url());
                startActivity(intent);
                break;
        }
    }
}
