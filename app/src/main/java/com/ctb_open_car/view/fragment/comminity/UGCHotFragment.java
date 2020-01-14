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
import com.ctb_open_car.bean.community.response.HotFeedData;
import com.ctb_open_car.presenter.HotFeedPresenter;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.view.adapter.community.HotFeedsAdapter;
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

public class UGCHotFragment extends BaseFragment {

    public HotFeedsAdapter hotFeedsAdapter;
    @BindView(R.id.ugc_content_refresh)
    SmartRefreshLayout mRefreshLayout;

    private HotFeedPresenter hotFeedPresenter;
    private int pageNum = 1;
    //    动态内容列表
    @BindView(R.id.ugc_content_recycleview)
    RecyclerView mHotContentRecycleView;
    private Unbinder unbinder;
    private RecycleViewDivider mDivider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hotFeedPresenter = new HotFeedPresenter(this);
    }

    public void updateHotFeedList(HotFeedData hotFeedData) {
        hotFeedsAdapter.setData(hotFeedData);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ugc_hotlist_feed_layout, null);
        unbinder = ButterKnife.bind(this, view);
        setSmartRefreshLayout();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoadArgs();
        buildHotContentView();

        if (TextUtils.isEmpty(mLoadId)) {
            hotFeedPresenter.requestFeedList(pageNum);
        } else {
            hotFeedPresenter.requestPersonFeedList(pageNum, mLoadId);
        }
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
                    hotFeedPresenter.requestFeedList(pageNum = 1);
                } else {
                    hotFeedPresenter.requestPersonFeedList(pageNum = 1, mLoadId);
                }
                refreshlayout.finishRefresh(20/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if (TextUtils.isEmpty(mLoadId)) {
                    hotFeedPresenter.requestFeedList(++pageNum);
                } else {
                    hotFeedPresenter.requestPersonFeedList(++pageNum, mLoadId);
                }
                refreshlayout.finishLoadMore(20/*,false*/);//传入false表示加载失败

            }
        });
    }

    /**
     * 动态内容列表
     */
    private void buildHotContentView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mHotContentRecycleView.setLayoutManager(layoutManager);
//        mHotContentRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        if (null == mDivider) {
            mDivider = new RecycleViewDivider(getContext(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 50);
            mHotContentRecycleView.addItemDecoration(mDivider);
        }
        hotFeedsAdapter = new HotFeedsAdapter(this,mLoadId);
        mHotContentRecycleView.setAdapter(hotFeedsAdapter);

    }


    @Override
    protected String getTAG() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
