package com.ctb_open_car.view.fragment.comminity;

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

import com.ctb_open_car.CTBApplication;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.community.response.NearbyData;
import com.ctb_open_car.presenter.NearbyFeedPresenter;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.view.adapter.community.NearbyFeedsAdapter;
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

public class UGCNearbyFragment extends BaseFragment {

    public NearbyFeedsAdapter nearbyFeedsAdapter;
    @BindView(R.id.ugc_content_refresh)
    SmartRefreshLayout mRefreshLayout;
    //    动态内容列表
    @BindView(R.id.ugc_content_recycleview)
    RecyclerView mNearbyContentRecycleView;
    private Unbinder unbinder;
    private int pageNum = 1;
    private NearbyFeedPresenter mNearbyPresenter;
    private RecycleViewDivider mDivider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNearbyPresenter = new NearbyFeedPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ugc_nearby_layout, null);
        unbinder = ButterKnife.bind(this, view);
        setSmartRefreshLayout();
        return view;
    }

    public void updateNearbyFeedList(NearbyData nearbyData) {
        nearbyFeedsAdapter.setData(nearbyData);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mNearbyPresenter.requestNearbyList(pageNum);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initNearbyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == CTBApplication.getInstance().getRxApp().mHeadBean.getLongitude()) {
            return;
        }
        mNearbyPresenter.requestNearbyList(pageNum);
    }

    /**
     * 动态内容列表
     */
    private void initNearbyView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mNearbyContentRecycleView.setLayoutManager(layoutManager);
        mNearbyContentRecycleView.setLayoutManager(layoutManager);
        /**防止fragment缓存item 导致每次加载divider导致 间隔不断变大*/
        if (null == mDivider) {
            mDivider = new RecycleViewDivider(getContext(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 50);
            mNearbyContentRecycleView.addItemDecoration(mDivider);
        }

        nearbyFeedsAdapter = new NearbyFeedsAdapter(getActivity());

        mNearbyContentRecycleView.setAdapter(nearbyFeedsAdapter);

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
                mNearbyPresenter.requestNearbyList(pageNum = 1);
                refreshlayout.finishRefresh(200/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                mNearbyPresenter.requestNearbyList(++pageNum);

                refreshlayout.finishLoadMore(200/*,false*/);//传入false表示加载失败
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected String getTAG() {
        return null;
    }
}
