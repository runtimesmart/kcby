package com.ctb_open_car.view.fragment.comminity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.community.response.GroupRecommend;
import com.ctb_open_car.bean.community.response.group.GroupDto;
import com.ctb_open_car.presenter.GroupHotListPresenter;
import com.ctb_open_car.presenter.GroupOtherListPresenter;
import com.ctb_open_car.presenter.MyGroupListPresenter;
import com.ctb_open_car.ui.GridViewDivider;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.activity.im.CreateGroupActivity;
import com.ctb_open_car.view.activity.im.GroupSearchActivity;
import com.ctb_open_car.view.activity.im.MyGroupMoreActivity;
import com.ctb_open_car.view.activity.im.TagGroupMoreActivity;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.adapter.im.HotGoupListAdapter;
import com.ctb_open_car.view.adapter.im.MyGroupListAdapter;
import com.ctb_open_car.view.adapter.im.OtherGoupListAdapter;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class CarGroupFragment extends BaseFragment implements EMMessageListener {

    @BindView(R.id.im_group_hot)
    LinearLayout mHotLayout;

    @BindView(R.id.im_group_tip)
    LinearLayout mMyGroupLayout;

    @BindView(R.id.im_group_action_tip)
    TextView mMyGroupActionTip;

    @BindView(R.id.im_group_action)
    TextView mMyGroupAction;

    /**
     * 热门群-更多按钮
     */
    @BindView(R.id.hot_group_more)
    TextView mHotGroupMore;

    /**
     * 我的群-更多按钮
     */
    @BindView(R.id.my_group_more)
    TextView mMyGroupMore;

    @BindView(R.id.im_group_mine_list)
    RecyclerView mMyGroupListView;

    @BindView(R.id.im_group_hot_list)
    RecyclerView mHotGroupListView;

    @BindView(R.id.im_group_list)
    RecyclerView mOtherGroupListView;

    @BindView(R.id.group_list_refresh)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.group_list_panel)
    NestedScrollView mGroupLayout;

    @BindView(R.id.search_txt)
    TextView mTextViewSearch;
    @BindView(R.id.search_edit_text)
    EditText mEditTextSearch;

    private RecycleViewDivider mMyDivider;
    private GridViewDivider mHotDivider;
    private RecycleViewDivider mOtherDivider;

    private MyGroupListAdapter mMyGoupListAdapter;
    private HotGoupListAdapter mHotGoupListAdapter;
    private OtherGoupListAdapter mOtherGoupListAdapter;

    private List<GroupRecommend> mOtherGroupList;
    private List<GroupDto> mMyGroupList;
    private List<GroupDto> mHotGroupList;
    @Inject
    GroupHotListPresenter mGroupHotListPresenter;
    @Inject
    GroupOtherListPresenter mGroupOtherListPresenter;
    @Inject
    MyGroupListPresenter mMyGroupListPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentComponent().inject(this);
        EMOptions emOptions = new EMOptions();
        EaseUI.getInstance().init(getContext(), emOptions);

        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ugc_im_goup_layout, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkEmptyGroupTip()) {
            fetchMyGroupData();
        }
        fetchHotData();
        fetchOtherData();
    }

    private void initView() {
        mTextViewSearch.setVisibility(View.VISIBLE);
        mEditTextSearch.setVisibility(View.GONE);
        initMyGroup();
        initHotGroup();
        initOtherGroup();
        setSmartRefreshLayout();
    }

    @OnClick(R.id.hot_group_more)
    void onHotGroupClick(View v) {
        Intent i = new Intent(getContext(), TagGroupMoreActivity.class);
        i.putExtra("tag_group_list", (Serializable) mHotGroupList);
        i.putExtra("tag_type", "热门推荐");
        getContext().startActivity(i);
    }

    @OnClick(R.id.search_txt)
    void toGroupSearch(View v) {
        Intent i = new Intent(getContext(), GroupSearchActivity.class);
        getContext().startActivity(i);
    }

    @OnClick(R.id.my_group_more)
    void onMyGroupClick(View v) {
        Intent i = new Intent(getContext(), MyGroupMoreActivity.class);
        i.putExtra("my_group_bean", (Serializable) mMyGroupList);
        getContext().startActivity(i);
    }

    /**
     * 设置上下拉样式
     */
    private void setSmartRefreshLayout() {

        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        //设置 Footer 为 球脉冲 样式
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()).setSpinnerStyle(SpinnerStyle.Translate));
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                fetchHotData();
                fetchOtherData();
                fetchMyGroupData();

                refreshlayout.finishRefresh(20/*,false*/);//传入false表示刷新失败
            }
        });
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(20/*,false*/);//传入false表示加载失败
                //刷新
            }
        });
    }
//    private void initGroupList() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        mGroupListView.setLayoutManager(layoutManager);
//        /**防止fragment缓存item 导致每次加载divider导致 间隔不断变大*/
//        if (null == mDivider) {
//            mDivider = new RecycleViewDivider(getContext(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 10);
//            mGroupListView.addItemDecoration(mDivider);
//        }
//       mOtherGoupListAdapter = new OtherGoupListAdapter(getContext());
//
//        mGroupListView.setAdapter();
//    }

    /**
     * 其他推荐群
     */
    private void initOtherGroup() {
//        GridLayoutManager otherLayoutManager = new GridLayoutManager(getContext(),2);
//        otherLayoutManager.setOrientation(RecyclerView.VERTICAL);
        LinearLayoutManager otherLayoutManager = new LinearLayoutManager(getContext());
        otherLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mOtherGroupListView.setLayoutManager(otherLayoutManager);
        /**防止fragment缓存item 导致每次加载divider导致 间隔不断变大*/
        if (null == mOtherDivider) {
            mOtherDivider = new RecycleViewDivider(getContext(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 10);
            mOtherGroupListView.addItemDecoration(mOtherDivider);
        }

        mOtherGoupListAdapter = new OtherGoupListAdapter((AppCompatActivity) getActivity());
        mOtherGroupListView.setAdapter(mOtherGoupListAdapter);
    }

    /**
     * 我的群
     */
    private void initMyGroup() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mMyGroupListView.setLayoutManager(layoutManager);
        /**防止fragment缓存item 导致每次加载divider导致 间隔不断变大*/
        if (null == mMyDivider) {
            mMyDivider = new RecycleViewDivider(getContext(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 10);
            mMyGroupListView.addItemDecoration(mMyDivider);
        }

        mMyGoupListAdapter = new MyGroupListAdapter((AppCompatActivity) getActivity());
        mMyGroupListView.setAdapter(mMyGoupListAdapter);
    }

    /**
     * 热门群
     */
    private void initHotGroup() {
        GridLayoutManager hotLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        mHotGroupListView.setLayoutManager(hotLayoutManager);
        /**防止fragment缓存item 导致每次加载divider导致 间隔不断变大*/
        if (null == mHotDivider) {
            mHotDivider = new GridViewDivider(2, 15);
            mHotGroupListView.addItemDecoration(mHotDivider);
        }

        mHotGroupListView.setLayoutManager(hotLayoutManager);
        mHotGoupListAdapter = new HotGoupListAdapter((AppCompatActivity) getActivity());
        mHotGroupListView.setAdapter(mHotGoupListAdapter);
    }

    /**
     * 获取我的群信息
     */
    private void fetchMyGroupData() {
        mMyGroupListPresenter.requestMyGroupList(new MyGroupListPresenter.UpdateListener() {
            @Override
            public void update(List<GroupDto> groupList) {
                if (0 == groupList.size()) {
                    setEmptyGroupTip(true);
                } else {
                    setEmptyGroupTip(false);
                }
                mMyGroupList = groupList;
                mMyGoupListAdapter.setData(groupList);
                Timber.d("获取群数量" + groupList.size());
            }
        });

    }

    /**
     * 获取其他群信息
     */
    private void fetchOtherData() {
        mGroupOtherListPresenter.requestOtherGroupList(new GroupOtherListPresenter.UpdateListener() {
            @Override
            public void update(List<GroupRecommend> groupList) {
                mOtherGoupListAdapter.setRecommendData(groupList);
                mOtherGroupList = groupList;
            }
        });

    }

    /**
     * 获取热门群信息
     */
    private void fetchHotData() {
        mGroupHotListPresenter.requestHotGroupList(new GroupHotListPresenter.UpdateListener() {
            @Override
            public void update(List<GroupDto> groupList) {
                mHotGoupListAdapter.setData(groupList);
                mHotGroupList = groupList;
            }
        });
    }

    private boolean checkEmptyGroupTip() {
        if (TextUtils.isEmpty(PreferenceUtils.getString(getContext(), "em_id"))) {
            setNotLoginGroupTip(true);
            return true;
        }
        setNotLoginGroupTip(false);
        return false;
    }

    private void setNotLoginGroupTip(boolean show) {
        if (show) {
            mMyGroupLayout.setVisibility(View.VISIBLE);
            mMyGroupAction.setText(R.string.my_group_login);
            mMyGroupActionTip.setText(R.string.my_group_login_tip);
            mMyGroupListView.setVisibility(View.GONE);

            mMyGroupAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                }
            });
        } else {
            mMyGroupLayout.setVisibility(View.GONE);
            mMyGroupListView.setVisibility(View.VISIBLE);
        }
    }

    private void setEmptyGroupTip(boolean show) {
        if (show) {
            mMyGroupLayout.setVisibility(View.VISIBLE);
            mMyGroupAction.setText(R.string.my_group_create);
            mMyGroupActionTip.setText(R.string.my_group_create_tip);
            mMyGroupListView.setVisibility(View.GONE);
            mMyGroupAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), CreateGroupActivity.class);
                    startActivity(i);
                }
            });
        } else {
            mMyGroupLayout.setVisibility(View.GONE);
            mMyGroupListView.setVisibility(View.VISIBLE);
        }

    }


    @OnClick(R.id.im_group_create)
    void toCreateGruop(View v) {
        Intent i = new Intent(getActivity(), CreateGroupActivity.class);
        startActivity(i);
    }

    @Override
    protected String getTAG() {
        return null;
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        Timber.d("收到消息---");
        for (EMMessage message : messages) {
            EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
        }
        fetchMyGroupData();
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {

    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {

    }
}
