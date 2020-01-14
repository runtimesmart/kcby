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

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.community.response.user.UserCardDto;
import com.ctb_open_car.presenter.FanListPresenter;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.view.adapter.community.FanListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FanListFragment extends BaseFragment {


    public FanListAdapter mAdapter;
    @BindView(R.id.ugc_content_refresh)
    SmartRefreshLayout mRefreshLayout;


    @BindView(R.id.ugc_fan_layout)
    RecyclerView mRecycleView;

    private int pageNum = 1;

    private FanListPresenter mFanListPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFanListPresenter = new FanListPresenter(this);
    }


    public void updateFanList(List<UserCardDto> focusUserList) {
        mAdapter.setData(focusUserList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ugc_focus_list_layout, null);
        ButterKnife.bind(this, view);
        setSmartRefreshLayout();

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buildHotContentView();

        getLoadArgs();
//        if (TextUtils.isEmpty(mLoadId)) {
//            mFocusListPresenter.requesetHostFocusList(pageNum);
//        } else {
        mFanListPresenter.requesetFanList(mLoadId, pageNum);
//        }
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
//                if (TextUtils.isEmpty(mLoadId)) {
//                    mFocusListPresenter.requestFeedList(pageNum = 1);
//                } else {
                mFanListPresenter.requesetFanList(mLoadId, pageNum = 1);
//                }
                refreshlayout.finishRefresh(20/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                if (TextUtils.isEmpty(mLoadId)) {
//                    hotFeedPresenter.requestFeedList(++pageNum);
//                } else {
                mFanListPresenter.requesetFanList(mLoadId, ++pageNum);
//                }
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
        mRecycleView.setLayoutManager(layoutManager);
//        mHotContentRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mRecycleView.addItemDecoration(new RecycleViewDivider(getContext(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 50));
        mAdapter = new FanListAdapter(getContext());
        mRecycleView.setAdapter(mAdapter);

    }

    @Override
    protected String getTAG() {
        return null;
    }
}
