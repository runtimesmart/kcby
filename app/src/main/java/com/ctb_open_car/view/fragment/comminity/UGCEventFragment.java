package com.ctb_open_car.view.fragment.comminity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.community.response.EventData;
import com.ctb_open_car.presenter.EventPresenter;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.view.adapter.community.EventsAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UGCEventFragment extends BaseFragment {
    public EventsAdapter eventsAdapter;
    @BindView(R.id.ugc_content_refresh)
    SmartRefreshLayout mRefreshLayout;

    //    动态内容列表
    @BindView(R.id.ugc_content_recycleview)
    RecyclerView mFocusContentRecycleView;
    private Unbinder unbinder;
    private EventPresenter mEventPresenter;
    private int pageNum = 1;
    private RecycleViewDivider mDivider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventPresenter = new EventPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ugc_events_layout, null);
        unbinder = ButterKnife.bind(this, view);
        setSmartRefreshLayout();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        getLoadArgs();

        initFocusView();
        if (TextUtils.isEmpty(mLoadId)) {
            requesetNewEvent();
        } else {
            mEventPresenter.requestPersonEventList(pageNum, mLoadId);
        }
    }

    //adapter切换本地和最新使用
    public void requesetNewEvent() {
        mEventPresenter.requestEventList(1, 1);

    }

    //adapter切换本地和最新使用
    public void requesetLocalEvent() {
        mEventPresenter.requestEventList(1, 2);

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

        eventsAdapter = new EventsAdapter(this,mLoadId);
        eventsAdapter.setData(new EventData());
        mFocusContentRecycleView.setAdapter(eventsAdapter);
    }

    //    更新活动列表
    public void updateEventList(EventData activityList) {
        eventsAdapter.setData(activityList);
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
                if (TextUtils.isEmpty(mLoadId)) {
                    mEventPresenter.requestEventList(1, mEventPresenter.eventType);
                } else {
                    mEventPresenter.requestPersonEventList(1, mLoadId);
                }
                refreshlayout.finishRefresh(200/*,false*/);//传入false表示刷新失败

            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                ++pageNum;
                if (TextUtils.isEmpty(mLoadId)) {
                    mEventPresenter.requestEventList(pageNum, 1);
                } else {
                    mEventPresenter.requestPersonEventList(pageNum, mLoadId);
                }
                refreshlayout.finishLoadMore(200/*,false*/);//传入false表示加载失败

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected String getTAG() {
        return null;
    }
}
