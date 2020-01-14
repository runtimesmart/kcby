package com.ctb_open_car.view.fragment.im;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.areacode.AreaCodeBean;
import com.ctb_open_car.bean.areacode.AreaCodeDtoBean;
import com.ctb_open_car.bean.im.CarStyleBean;
import com.ctb_open_car.bean.im.TagListBean;
import com.ctb_open_car.customview.azlist.AZItemEntity;
import com.ctb_open_car.customview.azlist.AZTitleDecoration;
import com.ctb_open_car.customview.azlist.AZWaveSideBarView;
import com.ctb_open_car.customview.azlist.LettersComparator;
import com.ctb_open_car.customview.azlist.PinyinUtils;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.ImCityListApi;
import com.ctb_open_car.engine.net.api.ImGroupLableApi;
import com.ctb_open_car.utils.AliossUtils;
import com.ctb_open_car.view.activity.im.AddGroupInfoActivity;
import com.ctb_open_car.view.activity.im.CreateGroupActivity;
import com.ctb_open_car.view.adapter.im.ProvinceItemAdapter;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.http.HttpManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static com.ctb_open_car.utils.StringUtils.generateUUID;
import static com.ctb_open_car.utils.StringUtils.getFileSuffix;

/**
 * 省份
 * <p>省份</p>
 */
public class ImProvinceFragment extends BaseFragment {
    @BindView(R.id.recycler_list)
    RecyclerView mRecyclerList;
    @BindView(R.id.bar_list)
    AZWaveSideBarView mBarList;
    @BindView(R.id.gps_city_name)
    TextView mGPSName;

    private String mGPSCity = "";
    private String mProvinceName;
    private ProvinceItemAdapter mAdapter;
    private List<AreaCodeDtoBean> mAreaCodeList = new ArrayList<>();
    private AreaCodeDtoBean mAreaCodeBean;
    private List<AZItemEntity<AreaCodeDtoBean>> mDateList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_im_province, container, false);
        ButterKnife.bind(this, view);
        initView();
        initEvent();
        getCityList();
        getLatlon();
        return view;
    }

    @Override
    protected String getTAG() {
        return null;
    }

    @OnClick({R.id.ic_back, R.id.gps_city_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                ((AddGroupInfoActivity)getActivity()).onBackPressed();
                break;
            case R.id.gps_city_name:
                if (mAreaCodeBean != null) {
                    ((AddGroupInfoActivity) getActivity()).setProvinceName(mAreaCodeBean, mProvinceName);
                }
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
        mAdapter = new ProvinceItemAdapter(mDateList);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerList.addItemDecoration(new AZTitleDecoration(new AZTitleDecoration.TitleAttributes(getContext())));
        mRecyclerList.setAdapter(mAdapter);
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
        mDateList = fillData(mAreaCodeList);
        Collections.sort(mDateList, new Comparator<AZItemEntity<AreaCodeDtoBean>>() {
            @Override
            public int compare(AZItemEntity<AreaCodeDtoBean> o1, AZItemEntity<AreaCodeDtoBean> o2) {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        });

        mAdapter.setDataList(mDateList);
        mAdapter.setOnItemListener(new ProvinceItemAdapter.OnItemListener() {
            @Override
            public void onItemListener(AreaCodeDtoBean position) {
                ((AddGroupInfoActivity)getActivity()).startImGroupCityFragment(position);
            }
        });

        new MyThread().start();
   }

    private List<AZItemEntity<AreaCodeDtoBean>> fillData(List<AreaCodeDtoBean> date) {
        List<AZItemEntity<AreaCodeDtoBean>> sortList = new ArrayList<>();
        for (AreaCodeDtoBean aDate : date) {
            AZItemEntity<AreaCodeDtoBean> item = new AZItemEntity<>();
            item.setValue(aDate);
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(aDate.getAreaName());
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

    private void getLatlon(){
        GeocodeSearch geocodeSearch=new GeocodeSearch(getActivity());
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (regeocodeResult != null) {
                    RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                    mProvinceName = regeocodeAddress.getProvince();
                    mGPSCity = regeocodeAddress.getCity();
                    mGPSName.setText(mProvinceName + "-" + mGPSCity);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                    if (geocodeResult!=null && geocodeResult.getGeocodeAddressList()!=null && geocodeResult.getGeocodeAddressList().size()>0){
                        GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                        String province = geocodeAddress.getProvince();
                        String city = geocodeAddress.getCity();
                        mGPSName.setText(province +"-" + city);
                      //  mLatitude = geocodeAddress.getLatLonPoint().getLatitude();//纬度
                     //   mLongititude = geocodeAddress.getLatLonPoint().getLongitude();//经度
                    }
            }
        });
        LatLonPoint latLonPoint = new LatLonPoint(mRxInstance.mHeadBean.getLatitude(), mRxInstance.mHeadBean.getLongitude());
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLonPoint, 1000, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
    }

    public void getCityList() {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("areaLevel", 2);
        ImCityListApi myInfoApi = new ImCityListApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity<AreaCodeBean> baseResultEntity = (BaseResultEntity<AreaCodeBean>) object;
                if (baseResultEntity.getRet().equals("0")) {
                    mAreaCodeList = baseResultEntity.getData().getAreaCodeList();
                    initData();
                } else {
                    Toasty.info(getContext(), baseResultEntity.getMsg()).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.e("e = " + e.getMessage());
            }
        }, (AddGroupInfoActivity) getActivity());
        myInfoApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(myInfoApi);
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            boolean thread = true;

            while (thread) {
                if(mAreaCodeList.size() == 0 && TextUtils.isEmpty(mGPSCity)) {
                    try {
                        Thread.sleep(500);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    thread = false;
                }
            }

            for (AreaCodeDtoBean aDate : mAreaCodeList) {
                for (int i = 0; aDate.getChildAreaCode() != null && i < aDate.getChildAreaCode().size(); i++) {
                    if (mGPSCity.equals(aDate.getAreaName()) || mGPSCity.equals(aDate.getChildAreaCode().get(i).getAreaName())) {
                        mAreaCodeBean = aDate.getChildAreaCode().get(i);
                        return;
                    }
                }
            }
        }
    }
}
