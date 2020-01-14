package com.ctb_open_car.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.MainActivity;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.newsbean.CategoryName;
import com.ctb_open_car.bean.newsbean.ColumnInfoBean;
import com.ctb_open_car.bean.newsbean.NewsInfoBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.NewshomeApi;
import com.ctb_open_car.view.activity.news.BloggerInfoActivity;
import com.ctb_open_car.view.activity.news.ColumnActivity;
import com.ctb_open_car.view.adapter.newsadapter.ChildRecyclerAdapter;
import com.ctb_open_car.view.adapter.newsadapter.OnLayoutListenter;
import com.ctb_open_car.view.adapter.newsadapter.ParentRecyclerAdapter;
import com.ctb_open_car.view.adapter.newsadapter.ParentViewHolder;
import com.ctb_open_car.view.dialog.NearbLocationDialog;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import timber.log.Timber;

/**
 * 消息内容页
 */
public class NewsContentFragment extends BaseFragment implements ParentRecyclerAdapter.NewsBeanClickListener{
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyuclerview)
    RecyclerView mRecyclerView;

    private CategoryName categoryName;
    private ParentRecyclerAdapter mAdapterParent;
    private ChildRecyclerAdapter mAdapterChild;
    private List<NewsInfoBean.Category> mListParent = new ArrayList<NewsInfoBean.Category>();
    private int mPage = 1;
    private boolean mRefresh = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        categoryName = (CategoryName)bundle.getSerializable("categoryName");
        if (categoryName != null && categoryName.getCategoryId() == 0) {
            mListParent = bundle.getParcelableArrayList("ListParent");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_content, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    protected String getTAG() {
        if (categoryName != null) {
            return categoryName.getCategoryName();
        }
        return null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && categoryName != null && mListParent.size() == 0) {
            initData(null);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && categoryName != null && mListParent.size() == 0) {
            initData(null);
        }
    }

    private void initView( ) {
        mAdapterParent = new ParentRecyclerAdapter(getContext(), mListParent);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapterParent);
        mAdapterParent.setOnClickListener(this);
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        //设置 Footer 为 球脉冲 样式
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()).setSpinnerStyle(SpinnerStyle.Translate));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mRefresh = true;
                mPage = 1;
                mListParent.clear();
                initData(refreshlayout);
               // refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                mRefresh = false;
                initData(refreshlayout);
              //  refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });


        mAdapterParent.setListenter(new OnLayoutListenter() {
            @Override
            public void layoutChild(ParentViewHolder holder, int position) {
                if (mListParent.size() > 0) {
                    mAdapterChild = new ChildRecyclerAdapter(getContext(), mListParent.get(position).getColumnTheme().getInformationList());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    holder.rv_child.setLayoutManager(linearLayoutManager);
//                                holder.rv_child.addItemDecoration(
//                                        new DividerItemDecoration(getContext(),
//                                                LinearLayoutManager.HORIZONTAL));

                    holder.rv_child.setAdapter(mAdapterChild);
                    mAdapterChild.setOnClickListener(new ChildRecyclerAdapter.BloggerClickListener() {
                        @Override
                        public void onClickListenerBlogger(NewsInfoBean.Information bloggerInfo) {
                            Intent intent = new Intent(getContext(), BloggerInfoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("columnName", mListParent.get(position).getColumnTheme().getThemeName());

                            ColumnInfoBean.Information information = new ColumnInfoBean.Information();
                            information.setTitle(bloggerInfo.getTitle());
                            information.setCoverUrl(bloggerInfo.getCoverUrl());
                            information.setLikes(bloggerInfo.getLikes());
                            information.setPublishTime(bloggerInfo.getPublishTime());
                            information.setComments(bloggerInfo.getComments());
                            //information.setBloggerInfoList(bloggerInfo.get);
                            information.setInformationId(bloggerInfo.getInformationId());
                            intent.putExtra("ColumnInfo",information);
                            getContext().startActivity(intent);
                        }
                    });
                }
            }
        });
    }
    private synchronized void initData(RefreshLayout refreshlayout) {
        if (categoryName != null) {
            HashMap<String, String> queryMap = new HashMap<>();

            queryMap.put("currentPage",String.valueOf(mPage));
            queryMap.put("categoryId",String.valueOf(categoryName.getCategoryId()));
            NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
                @Override
                public void onNext(Object object) {
                    super.onNext(object);
                    mPage++;
                    if(refreshlayout != null) {
                        if (mRefresh) {
                            mRefresh = false;
                            refreshlayout.finishRefresh(20/*,false*/);
                        } else {
                            refreshlayout.finishLoadMore(20/*,false*/);
                        }
                    }
                    BaseResultEntity<NewsInfoBean> baseResultEntity = (BaseResultEntity<NewsInfoBean>)object;
                    NewsInfoBean newsInfoBean = baseResultEntity.getData();
                    List<NewsInfoBean.Category> listParent = newsInfoBean.getCategoryList();
                    if (listParent != null && listParent.size() > 0) {
                        mListParent.addAll(listParent);
                    }

                    Timber.e("mListParent =  %s " , mListParent.size());
//                    mRecyclerView.addItemDecoration(
//                            new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                    mAdapterParent.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e){
                    mRefresh = false;
                    Timber.e("mListNewBean = %s " , e.toString());
                }
            }, (MainActivity) getActivity(), 0);
            hostFeedsApi.setRequestBody(queryMap);
            HttpManager.getInstance().doHttpDeal(hostFeedsApi);
        }
    }

    @Override
    public void onClickListenerNewsBean(NewsInfoBean.Category newsBean) {
        Intent intent = new Intent(getContext(), ColumnActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("theme", newsBean.getColumnTheme());
        startActivity(intent);
    }
}
