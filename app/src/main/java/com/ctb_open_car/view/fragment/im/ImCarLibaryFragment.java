package com.ctb_open_car.view.fragment.im;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.im.CarLibaryBean;
import com.ctb_open_car.bean.im.CarModelBean;
import com.ctb_open_car.bean.im.CarStyleBean;
import com.ctb_open_car.customview.azlist.AZItemEntity;
import com.ctb_open_car.customview.azlist.AZTitleDecoration;
import com.ctb_open_car.customview.azlist.AZWaveSideBarView;
import com.ctb_open_car.customview.azlist.LettersComparator;
import com.ctb_open_car.customview.azlist.PinyinUtils;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.CreateImGroupApi;
import com.ctb_open_car.engine.net.api.ImCarLibaryApi;
import com.ctb_open_car.view.activity.im.AddGroupInfoActivity;
import com.ctb_open_car.view.activity.im.CreateGroupActivity;
import com.ctb_open_car.view.adapter.im.CarStyleItemAdapter;
import com.ctb_open_car.view.adapter.im.ProvinceItemAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.http.HttpManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

/**
 * 省份
 * <p>省份</p>
 */
public class ImCarLibaryFragment extends BaseFragment {
    @BindView(R.id.recycler_list)
    RecyclerView mRecyclerList;
    @BindView(R.id.bar_list)
    AZWaveSideBarView mBarList;

    private CarStyleItemAdapter mAdapter;
    private List<CarStyleBean> mCarStyleList = new ArrayList<>();  //车辆类型 类如：奥迪，奔驰
    private List<CarModelBean> mCarModel = new ArrayList<>();   //车辆型号 类如：奥迪A6，奔驰S600

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_im_car_libary, container, false);
        ButterKnife.bind(this, view);
        initView();
        initEvent();
        getCarLibary();

        return view;
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

    private void initView() {
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        mRecyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerList.addItemDecoration(new AZTitleDecoration(new AZTitleDecoration.TitleAttributes(getContext())));
    }

    private void initEvent() {

        mBarList.setOnLetterChangeListener(new AZWaveSideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int position = mAdapter.getSortLettersFirstPosition(letter);
                if (position != -1) {
                    if (mRecyclerList.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerList.getLayoutManager();
                        manager.scrollToPositionWithOffset(position, 0);
                    } else {
                        mRecyclerList.getLayoutManager().scrollToPosition(position);
                    }
                }
            }
        });
    }

    private void initData() {
        List<AZItemEntity<CarStyleBean>> dateList = fillData(mCarStyleList);
        Collections.sort(dateList, new Comparator<AZItemEntity<CarStyleBean>>() {
            @Override
            public int compare(AZItemEntity<CarStyleBean> o1, AZItemEntity<CarStyleBean> o2) {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        });

        mAdapter = new CarStyleItemAdapter(getContext(),dateList);
        mRecyclerList.setAdapter(mAdapter);
        mAdapter.setOnItemListener(new CarStyleItemAdapter.OnItemListener() {
            @Override
            public void onItemListener(CarStyleBean position) {
                ((AddGroupInfoActivity)getActivity()).startImCarModelFragment(position);
            }
        });

   }

    private List<AZItemEntity<CarStyleBean>> fillData(List<CarStyleBean> date) {
        List<AZItemEntity<CarStyleBean>> sortList = new ArrayList<>();
        for (CarStyleBean aDate : date) {
            AZItemEntity<CarStyleBean> item = new AZItemEntity<>();
            item.setValue(aDate);
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(aDate.getName());
            //取第一个首字母
            String letters = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (letters.matches("[A-Z]")) {
                item.setSortLetters(letters.toUpperCase());
            } else {
                item.setSortLetters("#");
            }
            sortList.add(item);
        }
        return sortList;
    }

    public void getCarLibary() {
        ImCarLibaryApi myInfoApi = new ImCarLibaryApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity<CarLibaryBean> baseResultEntity = (BaseResultEntity<CarLibaryBean>) object;

                if (baseResultEntity.getRet().equals("0")) {
                    mCarStyleList = baseResultEntity.getData().getCarBrandList();

                    Gson gson = new Gson();
                    String aa = gson.toJson(mCarStyleList.get(0).getDetail());
                    Log.e("xxx","aa getCarLibary  = " + aa);

                    initData();
                } else {
                  //  dismissDiaLog();
                    Toasty.info(getContext(), "数据获取 失败").show();
                }
            }

            @Override
            public void onError(Throwable e) {
                //((AddGroupInfoActivity)getActivity()).dismissDiaLog();
                Toasty.info(getContext(), "车友群 创建失败").show();
                Timber.e("e = " + e.getMessage());
            }
        }, (AddGroupInfoActivity)getActivity());
        HttpManager.getInstance().doHttpDeal(myInfoApi);
    }
}
