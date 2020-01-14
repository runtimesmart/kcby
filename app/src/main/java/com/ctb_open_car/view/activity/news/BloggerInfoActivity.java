package com.ctb_open_car.view.activity.news;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.R;
import com.ctb_open_car.bean.newsbean.ColumnInfoBean;
import com.ctb_open_car.bean.newsbean.NewContentBean;
import com.ctb_open_car.bean.newsbean.NewsBean;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.NewshomeApi;
import com.ctb_open_car.utils.DateUtils;
import com.ctb_open_car.view.activity.login.LoginActivity;
import com.ctb_open_car.view.adapter.newsadapter.BloggerListAdpater;
import com.ctb_open_car.view.adapter.newsadapter.BloggerRecyclerAdapter;
import com.ctb_open_car.view.adapter.newsadapter.ColumnInfoRecyclerAdapter;
import com.ctb_open_car.view.dialog.InputTextMsgDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.library.BottomDialog;
import com.library.Item;
import com.library.OnItemClickListener;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.RxRetrofitApp;
import com.rxretrofitlibrary.http.HttpManager;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.socialize.UMShareAPI;

import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

/****   资讯博文详情页   ****/
public class BloggerInfoActivity extends RxAppCompatActivity implements BloggerRecyclerAdapter.AdapterClickListener{
    @BindView(R.id.ic_back)
    ImageView mIcBack;
    @BindView(R.id.title_tv)
    TextView mTtitleTv;
    @BindView(R.id.message_title)
    TextView mMessageTitle;
    @BindView(R.id.column_titile)
    TextView mColumnTitle;
    @BindView(R.id.message_time)
    TextView mMessageTime;
    @BindView(R.id.message_from_lay)
    LinearLayout mMessageFromLay;

    @BindView(R.id.like_image)
    ImageView mLikeImage;
    @BindView(R.id.like_num)
    TextView mLikeNum;
    @BindView(R.id.comment_mun)
    TextView mCommentMun;
  //  @BindView(R.id.listview)
  //  ListView mListview;
    @BindView(R.id.html_text)
    HtmlTextView mHtmlTextView;
    @BindView(R.id.cover_image)
    ImageView mCoverImage;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.gv_goods_list)
    RecyclerView mRecyclerView;

    private BloggerListAdpater mBloggerListAdpater;
    private BloggerRecyclerAdapter mBloggerRecyclerAdapter;

    private List<NewContentBean.ContentInfo>  mList = new ArrayList<NewContentBean.ContentInfo>();

    private String mColumnName;
    private ColumnInfoBean.Information mColumnInformation;
    private NewContentBean mNewContentBean;

    private RxRetrofitApp mRxInstance;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogger_info);
        ButterKnife.bind(this);

        mRxInstance = RxRetrofitApp.Singleton.INSTANCE.get();

        Intent intent = getIntent();
        mColumnName = intent.getStringExtra("columnName");
        mColumnInformation = (ColumnInfoBean.Information)intent.getSerializableExtra("ColumnInfo");
        // 初始化页卡
        initData();
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        mMessageTitle.setText(mColumnInformation.getTitle());
        mColumnTitle.setText(mColumnName);
        mMessageTime.setText(DateUtils.convertTimeToFormat(mColumnInformation.getPublishTime()));
        mLikeNum.setText(String.valueOf(mColumnInformation.getLikes()));
        mCommentMun.setText(String.valueOf(mColumnInformation.getComments()));
        Glide.with(this).asBitmap().load(mColumnInformation.getCoverUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                mCoverImage.setImageBitmap(resource);
            }
        });

       // mBloggerListAdpater = new BloggerListAdpater(this, mList, mColumnInformation.getInformationId());
     //   mListview.setFocusable(false);
     //   mListview.setAdapter(mBloggerListAdpater);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager1);
        mBloggerRecyclerAdapter = new BloggerRecyclerAdapter(this,  mList, mColumnInformation.getInformationId());
        mBloggerRecyclerAdapter.setOnClickListener(this);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setAdapter(mBloggerRecyclerAdapter);
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY > 0) {
//                    mIcBack.setVisibility(View.GONE);
//                } else {
//                    mIcBack.setVisibility(View.VISIBLE);
//                }
            }
        });
    }

    private void initData() {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("informationId",String.valueOf(mColumnInformation.getInformationId()));
        NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                JsonObject jsonObject = (JsonObject)object;
                String data = jsonObject.get("data").toString();
                Gson gson = new Gson();

                mNewContentBean = gson.fromJson(data, NewContentBean.class);
                mMessageTime.setText(DateUtils.convertTimeToFormat(mNewContentBean.getPublishTime()));
                mHtmlTextView.setHtml(mNewContentBean.getContent() , new HtmlHttpImageGetter(mHtmlTextView));
                mCommentMun.setText(String.valueOf(mNewContentBean.getComments()));
                mLikeNum.setText(String.valueOf(mNewContentBean.getLikes()));
                if (mNewContentBean.getCommentList() != null) {
                    mList.clear();
                    mList.addAll(mNewContentBean.getCommentList());
                    mBloggerRecyclerAdapter.notifyDataSetChanged();
                }

                if (mNewContentBean.getLikeStatus() == 0) {
                    mLikeImage.setImageResource(R.drawable.like);
                } else {
                    mLikeImage.setImageResource(R.drawable.like_enable);
                }
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("initData %s", e.toString());
            }
        }, BloggerInfoActivity.this, 3);
        hostFeedsApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(hostFeedsApi);
    }
    private void setFollow() {
            HashMap<String, String> queryMap = new HashMap<>();
            queryMap.put("informationId",String.valueOf(mColumnInformation.getInformationId()));
            NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
                @Override
                public void onNext(Object object) {
                    super.onNext(object);
                    JsonObject jsonObject = (JsonObject)object;
                    String code = jsonObject.get("code").toString();
                    int codeIndex = Integer.parseInt(code =code .replace("\"", ""));
                    if (codeIndex == 0) {
                        int like = Integer.parseInt(mLikeNum.getText().toString()) + 1;
                        mLikeNum.setText(String.valueOf(like));
                        mLikeImage.setImageResource(R.drawable.like_enable);
                        mNewContentBean.setLikeStatus(1);
                        Toasty.info(BloggerInfoActivity.this, getString(R.string.likes_success)).show();
                    } else {
                        Toasty.info(BloggerInfoActivity.this, getString(R.string.likes_failded)).show();
                    }
                }

                @Override
                public  void onError(Throwable e){
                    Timber.e("setFollow %s", e.toString());
                }
            }, BloggerInfoActivity.this, 4);
            hostFeedsApi.setRequestBody(queryMap);
            HttpManager.getInstance().doHttpDeal(hostFeedsApi);
    }

    private void setComment(String strContent) {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("informationId",String.valueOf(mColumnInformation.getInformationId()));
        queryMap.put("commentContent",strContent);
        NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                JsonObject jsonObject = (JsonObject)object;
                String code = jsonObject.get("code").toString();
                int codeIndex = Integer.parseInt(code .replace("\"", ""));
                if (codeIndex == 0)  {
                    int like = Integer.parseInt(mCommentMun.getText().toString()) + 1;
                    mCommentMun.setText(String.valueOf(like));
                    initData();
                    Toasty.info(BloggerInfoActivity.this, getString(R.string.comment_success)).show();
                } else {
                    Toasty.info(BloggerInfoActivity.this,  getString(R.string.comment_failded)).show();
                }
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("setComment %s", e.toString());
            }
        }, BloggerInfoActivity.this, 5);
        hostFeedsApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(hostFeedsApi);
    }

    private void setCommentFollow(int position, String commentId) {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("informationId",String.valueOf(mColumnInformation.getInformationId()));
        queryMap.put("commentId",commentId);
        NewshomeApi hostFeedsApi = new NewshomeApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                JsonObject jsonObject = (JsonObject)object;
                String code = jsonObject.get("code").toString();
                int codeIndex = Integer.parseInt(code .replace("\"", ""));
                if (codeIndex == 0) {
                    Toasty.info(BloggerInfoActivity.this, getString(R.string.likes_success)).show();
                    mList.get(position).setCommentPraiseCnt(mList.get(position).getCommentPraiseCnt() +1);
                    mList.get(position).setAlreadyCommentPraise(1);
                    mBloggerRecyclerAdapter.notifyItemChanged(position);
                } else {
                    Toasty.info(BloggerInfoActivity.this, getString(R.string.likes_failded)).show();
                }
            }

            @Override
            public  void onError(Throwable e){
                Timber.e("getColumnFollow %s", e.toString());
            }
        }, BloggerInfoActivity.this, 6);
        hostFeedsApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(hostFeedsApi);
    }

    private void  share() {
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,mPermissionList,123);
        }
    }
    @OnClick({R.id.ic_back, R.id.like_lay, R.id.comment_lay, R.id.share_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                onBackPressed();
                break;
            case R.id.like_lay:
                if (isLoginApp()) {
                    if (mNewContentBean.getLikeStatus() == 0) {
                        setFollow();
                    } else {
                        Toasty.info(BloggerInfoActivity.this, getString(R.string.likes_yes)).show();
                    }
                }
                break;
            case R.id.comment_lay:
                if (isLoginApp()) {
                    InputTextMsgDialog dialog = new InputTextMsgDialog(getString(R.string.input_tips), new InputTextMsgDialog.SendBackListener() {
                        @Override
                        public void sendBack(String inputText) {
                            setComment(inputText);
                        }
                    });
                    dialog.show(getSupportFragmentManager(), "comment");
                }
                break;
            case R.id.share_lay:
                if (isLoginApp()) {
                    new BottomDialog(this)
                            .orientation(BottomDialog.HORIZONTAL)
                            .inflateMenu(R.menu.menu_share, new OnItemClickListener() {
                                @Override
                                public void click(Item item) {
                                    Toasty.info(BloggerInfoActivity.this, item.getTitle()).show();
                                }
                            }).show();
                }
                break;
        }
    }

    @Override
    public void onClickListenerLink(int position, String commentId) {
        if (isLoginApp()) {
            setCommentFollow(position, commentId);
        }
    }

    public boolean isLoginApp() {
        if (mRxInstance.mHeadBean.getUserId() > 0) {
            return true;
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LatLng mCurrentLoc = new LatLng(mRxInstance.mHeadBean.getLatitude(), mRxInstance.mHeadBean.getLongitude());
            intent.putExtra("LatLng", mCurrentLoc);
            startActivity(intent);
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
    }
}
