package com.ctb_open_car.view.fragment.im;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.im.CarModelBean;
import com.ctb_open_car.view.activity.im.AddGroupInfoActivity;
import com.ctb_open_car.view.adapter.im.CarModelAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆型号
 */
public class ImCarModelFragment extends BaseFragment {
    @BindView(R.id.recyuclerview)
    RecyclerView mRecyclerView;

    private CarModelAdapter mAdapter;
    private List<CarModelBean> mCarModel = new ArrayList<>();   //车辆型号 类如：奥迪A6，奔驰S600

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_im_car_model, container, false);
        ButterKnife.bind(this, view);
        initDate();
        return view;
    }

    public void initDate() {
        mCarModel =  ((AddGroupInfoActivity)getActivity()).mCarStyleBean.getDetail();
        mAdapter = new CarModelAdapter(getActivity(), mCarModel);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    protected String getTAG() {
        return null;
    }

    @OnClick(R.id.ic_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                ((AddGroupInfoActivity)getActivity()).onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

}
