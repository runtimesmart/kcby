package com.ctb_open_car.view.fragment.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amap.api.maps.model.LatLng;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseFragment;
import com.ctb_open_car.bean.newsbean.ColumnDataBean;
import com.ctb_open_car.bean.newsbean.ColumnInfoBean;
import com.ctb_open_car.bean.newsbean.ColumnTheme;
import com.ctb_open_car.bean.newsbean.NewsBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.NewshomeApi;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.activity.news.BloggerInfoActivity;
import com.ctb_open_car.view.activity.news.ColumnActivity;
import com.ctb_open_car.view.adapter.newsadapter.ColumnDaRenRecyclerAdapter;
import com.ctb_open_car.view.adapter.newsadapter.ColumnInfoRecyclerAdapter;
import com.ctb_open_car.view.dialog.InputTextMsgDialog;
import com.google.gson.JsonObject;
import com.library.BottomDialog;
import com.library.Item;
import com.library.OnItemClickListener;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.yinglan.scrolllayout.content.ContentRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

/**
 * 栏目(动态&热门)详情页
 */
public class ColumnFragment extends BaseFragment implements ColumnInfoRecyclerAdapter.ColumnInfoAdapterClickListener{
    @BindView(R.id.column_info_list)
    ContentRecyclerView mColumnInfoList;
    private String mName;
    private ColumnTheme mColumnTheme;
    private ColumnDataBean mColumnDataBean;
    private ColumnDaRenRecyclerAdapter mDaRenAdpater;
    private ColumnInfoRecyclerAdapter mInfoAdpater;
    private List<ColumnInfoBean.Information> mListParent = new ArrayList<ColumnInfoBean.Information>();
    private RxRetrofitApp mRxInstance;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();
        Bundle bundle = getArguments();
        mName = bundle.getString("name");
        mColumnTheme = (ColumnTheme)bundle.getSerializable("theme");
        if (mName == null) {
            mName = "参数非法";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment_column, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mColumnTheme != null) {
            if (mName.equals(getString(R.string.dynamic))) {
                getColumnDynamicDataList("1");
            } else {
                getColumnDynamicDataList("2");
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getUserVisibleHint()) {
            if ( mColumnTheme != null) {
                if (mName.equals(getString(R.string.dynamic))) {
                    getColumnDynamicDataList("1");
                } else {
                    getColumnDynamicDataList("2");
                }
            }
        }
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    protected String getTAG() {
        return null;
    }

    public void initData() {

        if (mColumnDataBean.getInformationList() != null && mColumnDataBean.getInformationList().size() > 0) {
            mListParent.clear();
            mListParent.addAll(mColumnDataBean.getInformationList());
            if ( mColumnDataBean.getFollow() != null &&
                    mColumnDataBean.getFollow().getFollowers() != null &&
                    mColumnDataBean.getFollow().getFollowers().size() > 0) {
                ColumnInfoBean.Information newsBean = new ColumnInfoBean.Information();
                newsBean.setBloggerInfoList(mColumnDataBean.getFollow().getFollowers());
                newsBean.setType(1);
                mListParent.add(0, newsBean);
            }
        }

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mColumnInfoList.setLayoutManager(linearLayoutManager1);
        mInfoAdpater = new ColumnInfoRecyclerAdapter(getContext(), mListParent, mColumnDataBean.getTheme());
        mInfoAdpater.setOnClickListener(this);
        mColumnInfoList.setAdapter(mInfoAdpater);
    }

    //获取动态 和 热门的 数据
    public void getColumnDynamicDataList(String type) {
        HashMap<String, String> queryMap = new HashMap<>();

        queryMap.put("themeId",String.valueOf(mColumnTheme.getThemeId()));
        queryMap.put("type",type);

        NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity<ColumnDataBean> beanBaseResultEntity = (BaseResultEntity<ColumnDataBean>)object;
                mColumnDataBean = beanBaseResultEntity.getData();
                initData();
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("mListNewBean " + e.toString());
            }
        }, (ColumnActivity)getActivity(), 2);
        hostFeedsApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(hostFeedsApi);
    }

    //获取动态 和 热门的 数据
    public void newsLikesPost(long informationId, int position) {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("informationId",String.valueOf(informationId));

        NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                JsonObject jsonObject = (JsonObject)object;
                String code = jsonObject.get("code").toString();
                int codeIndex = Integer.parseInt(code.replace("\"", ""));
                if (codeIndex == 0){
                    mListParent.get(position).setLikes(mListParent.get(position).getLikes() + 1);
                    mInfoAdpater.notifyItemChanged(position);
                    Toasty.info(getContext(), getString(R.string.likes_success)).show();
                } else {
                    Toasty.info(getContext(), getString(R.string.likes_failded)).show();
                }
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("mListNewBean " + e.toString());
            }
        }, (ColumnActivity)getActivity(), 4);
        hostFeedsApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(hostFeedsApi);
    }

    private void setComment(long informationId, String strContent, int position) {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("informationId",String.valueOf(informationId));
        queryMap.put("commentContent",strContent);
        NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                JsonObject jsonObject = (JsonObject)object;
                String code = jsonObject.get("code").toString();
                int codeIndex = Integer.parseInt(code =code .replace("\"", ""));
                if (codeIndex == 0)  {
                    mListParent.get(position).setComments(mListParent.get(position).getComments() + 1);
                    mInfoAdpater.notifyItemChanged(position);
                    Toasty.info(getContext(), getString(R.string.likes_success)).show();
                } else {
                    Toasty.info(getContext(), getString(R.string.comment_failded)).show();
                }
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("setComment %s", e.toString());
            }
        }, (ColumnActivity)getActivity(), 5);
        hostFeedsApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(hostFeedsApi);
    }

    @Override
    public void onClickListenerColumnInfo(ColumnInfoBean.Information bloggerInfo, int position, String type) {
        switch (type) {
            case "like":
                if (isLoginApp()) {
                    newsLikesPost(bloggerInfo.getInformationId(), position);
                }
               break;
            case "comment":
                if (isLoginApp()) {
                    InputTextMsgDialog dialog = new InputTextMsgDialog(getString(R.string.input_tips), new InputTextMsgDialog.SendBackListener() {
                        @Override
                        public void sendBack(String inputText) {
                            setComment(bloggerInfo.getInformationId(), inputText, position);
                        }
                    });
                    dialog.show(getChildFragmentManager(), "");
                }
                break;
            case "share":
                if (isLoginApp()) {
                    new BottomDialog(getContext())
                            .orientation(BottomDialog.HORIZONTAL)
                            .inflateMenu(R.menu.menu_share, new OnItemClickListener() {
                                @Override
                                public void click(Item item) {
                                    Toasty.info(getContext(), item.getTitle()).show();
                                }
                            }).show();
                }
                break;
        }
    }

    public boolean isLoginApp() {
        if (mRxInstance.mHeadBean.getUserId() > 0) {
          return true;
        } else {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LatLng mCurrentLoc = new LatLng(mRxInstance.mHeadBean.getLatitude(), mRxInstance.mHeadBean.getLongitude());
            intent.putExtra("LatLng", mCurrentLoc);
            startActivity(intent);
            return false;
        }
    }
}
