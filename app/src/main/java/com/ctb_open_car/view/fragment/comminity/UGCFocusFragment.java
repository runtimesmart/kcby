package com.ctb_open_car.view.fragment.comminity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.community.response.FocusFeedData;
import com.ctb_open_car.bean.community.response.UserData;
import com.ctb_open_car.eventbus.MessageEvent;
import com.ctb_open_car.presenter.FocusFeedPresenter;
import com.ctb_open_car.presenter.UserInfoPresenter;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.adapter.community.FocusFeedsAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UGCFocusFragment extends BaseFragment {
    public FocusFeedsAdapter focusFeedsAdapter;
    @BindView(R.id.ugc_content_refresh)
    SmartRefreshLayout mRefreshLayout;
    private int pageNum = 1;
    //    动态内容列表
    private FocusFeedPresenter mFocusFeedPresenter;
    private UserInfoPresenter mUserInfoPresenter;
    @BindView(R.id.ugc_content_recycleview)
    RecyclerView mFocusContentRecycleView;
    private Unbinder unbinder;
    private RecycleViewDivider mDivider;

    private boolean mUserVisibile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mFocusFeedPresenter = new FocusFeedPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ugc_focus_layout, null);
        unbinder = ButterKnife.bind(this, view);
        setSmartRefreshLayout();
        return view;
    }

    public void updateFocusFeedList(FocusFeedData focusData) {
        focusFeedsAdapter.setData(focusData);
    }

    public void updateHostData(UserData userData) {
        focusFeedsAdapter.setHostData(userData);
    }

    /**
     * 查询更多失败是回调
     */
    public void loadMoreFailed() {
        pageNum--;
        if (pageNum <= 0) {
            pageNum = 1;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initFocusView();
        //请求数据

        mFocusFeedPresenter.requesetUserInfo(PreferenceUtils.getLong(CTBApplication.getInstance(), "userId") + "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEventBusMsg(MessageEvent event) {
        if (null == event.getType()) {
            return;
        }
//        if ("RequestFeedList".equals(event.getType())) {
//            mFocusFeedPresenter.requestFeedList(pageNum);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUserVisibile) {
            mFocusFeedPresenter.requestFeedList(pageNum);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mUserVisibile = isVisibleToUser;
        if (mUserVisibile) {
            mFocusFeedPresenter.requestFeedList(pageNum);
        }
    }

    /**
     * 动态内容列表
     */
    private void initFocusView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mFocusContentRecycleView.setLayoutManager(layoutManager);
        /**防止fragment缓存item 导致每次加载divider导致 间隔不断变大*/
        if (null == mDivider) {
            mDivider = new RecycleViewDivider(getContext(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 50);
            mFocusContentRecycleView.addItemDecoration(mDivider);
        }
        focusFeedsAdapter = new FocusFeedsAdapter(getActivity());
        mFocusContentRecycleView.setAdapter(focusFeedsAdapter);

    }

    /**
     * 设置上下拉样式
     */
    private void setSmartRefreshLayout() {

        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        //设置 Footer 为 球脉冲 样式
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()).setSpinnerStyle(SpinnerStyle.Translate));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mFocusFeedPresenter.requestFeedList(pageNum = 1);
                mFocusFeedPresenter.requesetUserInfo(PreferenceUtils.getLong(CTBApplication.getInstance(), "userId") + "");
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                mFocusFeedPresenter.requestFeedList(++pageNum);

                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    protected String getTAG() {
        return null;
    }
}
