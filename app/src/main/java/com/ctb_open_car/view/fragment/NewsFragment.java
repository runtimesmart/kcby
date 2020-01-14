package com.ctb_open_car.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.newsbean.CategoryName;
import com.ctb_open_car.bean.newsbean.NewsInfoBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.NewshomeApi;
import com.ctb_open_car.view.adapter.newsadapter.NewsContentFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * 资讯
 * <p>资讯首页</p>
 */
public class NewsFragment extends BaseFragment {
    @BindView(R.id.community_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.title_fragment)
    TextView mTitleTV;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.more_bt)
    ImageButton mMoreBt;

    private NewsContentFragmentAdapter mAdapter;
    private List<CategoryName> names = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new NewsContentFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(viewPager);

        getTablayoutDataList();
        //   mTabLayout.selectTab(mTabLayout.getTabAt(5));
        return view;
    }

    @Override
    protected String getTAG() {
        return null;
    }

    @OnClick(R.id.more_bt)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more_bt:
                break;
            default:
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getTablayoutDataList();
        }
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        if (getUserVisibleHint()) {
//            getTablayoutDataList();
//        }
//        super.onActivityCreated(savedInstanceState);
//    }

    public void getTablayoutDataList() {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("currentPage","1");
        queryMap.put("categoryId","0");
        NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity<NewsInfoBean> baseResultEntity = (BaseResultEntity<NewsInfoBean>)object;
                NewsInfoBean newsInfoBean = baseResultEntity.getData();
                List<NewsInfoBean.Category> listParent = newsInfoBean.getCategoryList();

                names = newsInfoBean.getCategoryNameList();

                CategoryName categoryName = new CategoryName();
                categoryName.setCategoryId(0);
                categoryName.setCategoryName("全部");
                names.add(0, categoryName);

//                CategoryName category = new CategoryName();
//                category.setCategoryId(-1);
//                category.setCategoryName("关注");
//                names.add(1, category);
                Timber.e("mListNewBean names = %s", names.size());
                mAdapter.setList(names, listParent);
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("mListNewBean " + e.toString());
            }
        }, (MainActivity) getActivity(), 0);
        hostFeedsApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(hostFeedsApi);
    }
}
