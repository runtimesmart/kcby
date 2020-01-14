package com.ctb_open_car.view.fragment.im;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.im.TagDtoBean;
import com.ctb_open_car.bean.im.TagListBean;
import com.ctb_open_car.bean.userInfo.UserInfoBean;
import com.ctb_open_car.customview.azlist.AZItemEntity;
import com.ctb_open_car.customview.azlist.AZTitleDecoration;
import com.ctb_open_car.customview.azlist.AZWaveSideBarView;
import com.ctb_open_car.customview.azlist.LettersComparator;
import com.ctb_open_car.customview.azlist.PinyinUtils;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.ImGroupLableApi;
import com.ctb_open_car.engine.net.api.MyInfoApi;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.im.AddGroupInfoActivity;
import com.ctb_open_car.view.adapter.im.LableListAdapter;
import com.ctb_open_car.view.adapter.im.ProvinceItemAdapter;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.http.HttpManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * 车友群标签
 * <p>车友群标签</p>
 */
public class ImGroupLableFragment extends BaseFragment {
    @BindView(R.id.recycler_list)
    RecyclerView mRecyclerList;

    private LableListAdapter mAdpater;
    private List<TagDtoBean> mLableList = new ArrayList<>();

    private List<TagDtoBean> mLablBeanList = new ArrayList<>();
    private TagListBean mTagListBean = new TagListBean();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_im_group_lable, container, false);
        ButterKnife.bind(this, view);
        initView();
        getMyInfoData();
        return view;
    }

    @Override
    protected String getTAG() {
        return null;
    }

    @OnClick({R.id.ic_back, R.id.statusbar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                ((AddGroupInfoActivity)getActivity()).onBackPressed();
                break;
            case R.id.statusbar_right:
                mTagListBean.setTagList(mLablBeanList);
                ((AddGroupInfoActivity)getActivity()).setLableName(mTagListBean);
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
        mRecyclerList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mAdpater = new LableListAdapter(getActivity(), mLableList);
        mRecyclerList.setAdapter(mAdpater);
        mAdpater.setOnItemListener(new LableListAdapter.OnItemListener() {
            @Override
            public void onItemListener(TagDtoBean lable, boolean isAdd) {
                if (isAdd) {
                    mLablBeanList.add(lable);
                } else {
                    mLablBeanList.remove(lable);
                }
            }
        });
    }

    private void getMyInfoData() {
        // showDialog("正在获取数据");
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("tagCategory", 1);
        ImGroupLableApi myInfoApi = new ImGroupLableApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity<TagListBean> baseResultEntity = (BaseResultEntity<TagListBean>) object;
                if (baseResultEntity.getRet().equals("0")) {
                    mLableList = baseResultEntity.getData().getTagList();
                    TagListBean tagListBean = ((AddGroupInfoActivity)getActivity()).mTagListBean;
                    for (int i = 0; tagListBean != null && i < mLableList.size(); i ++) {
                        for (int j = 0; j < tagListBean.getTagList().size(); j ++ ) {
                            if (mLableList.get(i).getTagName().equals(tagListBean.getTagList().get(j).getTagName())) {
                                mLableList.get(i).setSelect(true);
                                mLablBeanList.add(mLableList.get(i));
                            }
                        }
                    }
                    mAdpater.setData(mLableList, mLablBeanList);
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


}
