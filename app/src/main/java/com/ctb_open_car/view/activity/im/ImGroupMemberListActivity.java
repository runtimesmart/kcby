package com.ctb_open_car.view.activity.im;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.im.CarLibaryBean;
import com.ctb_open_car.bean.im.CarStyleBean;
import com.ctb_open_car.bean.im.EmchatGroupMemberDto;
import com.ctb_open_car.bean.im.GroupMemberListBean;
import com.ctb_open_car.customview.azlist.AZItemEntity;
import com.ctb_open_car.customview.azlist.AZTitleDecoration;
import com.ctb_open_car.customview.azlist.AZWaveSideBarView;
import com.ctb_open_car.customview.azlist.PinyinUtils;
import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.api.ImCarLibaryApi;
import com.ctb_open_car.engine.net.api.ImGroupUserListApi;
import com.ctb_open_car.view.activity.person.PersonHomeActivity;
import com.ctb_open_car.view.adapter.im.CarStyleItemAdapter;
import com.ctb_open_car.view.adapter.im.ImGroupListAdapter;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.rxretrofitlibrary.Api.BaseResultEntity;
import com.rxretrofitlibrary.http.HttpManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

public class ImGroupMemberListActivity extends BaseActivity {
    private static final int CODE_MEMBER_SELECT = 19;
    private static final int CODE_CHAT_SELECT = 20;
    @BindView(R.id.recyuclerview)
    RecyclerView mRecyclerList;
    @BindView(R.id.bar_list)
    AZWaveSideBarView mBarList;
    @BindView(R.id.statusbar_title)
    TextView mTitleBar;

    @BindView(R.id.text_item_name)
    TextView mGroupOwnerName;
    @BindView(R.id.logo_image)
    ImageView mGroupOwnerImage;
    @BindView(R.id.search_edit_text)
    EditText mSearchEdit;
    @BindView(R.id.group_owner)
    View mGroupOwnerLay;

    private ImGroupListAdapter mAdapter;
    private List<EmchatGroupMemberDto> mGroupMemberList = new ArrayList<>();
    private EmchatGroupMemberDto mMemberDto;
    private String mStrGroupId;
    private String mGroupUserNum;
    private Map mMap = new HashMap();
    private boolean mIsHost;
    private boolean mIsSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_group_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.color_transparent00));

        ButterKnife.bind(this);
        initEvent();
        initView();
    }

    @Override
    public Object getTag() {
        return null;
    }

    private void initView() {
        mStrGroupId = (String) getIntent().getStringExtra("group_id");
        mGroupUserNum = (String) getIntent().getStringExtra("GroupUserNum");
        mIsHost = getIntent().getBooleanExtra("is_host", false);
        mIsSelect = getIntent().getBooleanExtra("is_select", false);
        mTitleBar.setText(getString(R.string.group_list_title, mGroupUserNum));
        mRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerList.addItemDecoration(new AZTitleDecoration(new AZTitleDecoration.TitleAttributes(this)));
        getGroupUserList();
    }

    private void initEvent() {

        mBarList.setOnLetterChangeListener(new AZWaveSideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                if (mAdapter != null) {
                    int position = mAdapter.getSortLettersFirstPosition(letter);
                    if (position != -1) {
                        if (mRecyclerList.getLayoutManager() instanceof LinearLayoutManager) {
                            LinearLayoutManager manager = (LinearLayoutManager) mRecyclerList.getLayoutManager();
                            manager.scrollToPositionWithOffset(position, 0);
                        } else {
                            mRecyclerList.getLayoutManager().scrollToPosition(position);
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.ic_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                onBackPressed();
                break;
        }
    }

    private void initData() {
        List<AZItemEntity<EmchatGroupMemberDto>> dateList = fillData(mGroupMemberList);
        Collections.sort(dateList, new Comparator<AZItemEntity<EmchatGroupMemberDto>>() {
            @Override
            public int compare(AZItemEntity<EmchatGroupMemberDto> o1, AZItemEntity<EmchatGroupMemberDto> o2) {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        });

        mAdapter = new ImGroupListAdapter(this, dateList, mMap);
        mRecyclerList.setAdapter(mAdapter);
        mAdapter.setOnItemListener(new ImGroupListAdapter.OnItemListener() {
            @Override
            public void onItemListener(int position) {
                if (mIsHost) {
                    if (!dateList.get(position).getValue().isPreset()) {
                        Intent memberSelect = new Intent();
                        AZItemEntity<EmchatGroupMemberDto> azItemEntities = dateList.get(position);
                        EmchatGroupMemberDto memberDto = azItemEntities.getValue();
                        memberSelect.putExtra("member_id", memberDto.getEmId());
                        memberSelect.putExtra("member_name", memberDto.getNickName());
                        setResult(CODE_MEMBER_SELECT, memberSelect);
                        finish();
                    } else {
                        Toasty.info(ImGroupMemberListActivity.this, "群主不能为：" + dateList.get(position).getValue().getNickName()).show();
                    }
                }else if(mIsSelect){
                    Intent memberSelect = new Intent();
                    AZItemEntity<EmchatGroupMemberDto> azItemEntities = dateList.get(position);
                    EmchatGroupMemberDto memberDto = azItemEntities.getValue();
                    memberSelect.putExtra("member_id", memberDto.getEmId());
                    memberSelect.putExtra("member_name", memberDto.getNickName());
                    setResult(CODE_CHAT_SELECT, memberSelect);
                    finish();
                } else {
                    Intent i = new Intent(ImGroupMemberListActivity.this, PersonHomeActivity.class);
                    i.putExtra("user_id", dateList.get(position).getValue().getUserId());
                    startActivity(i);
                }
                //  ((AddGroupInfoActivity)getActivity()).startImCarModelFragment(mCarStyleList.get(position));
            }
        });

        if (mMemberDto != null) {
            mGroupOwnerName.setText(mMemberDto.getNickName());
            Glide.with(this).asBitmap().circleCrop().load(mMemberDto.getUserIcon().getResourceUrl()).placeholder(R.drawable.default_avar_icon).error(R.drawable.default_avar_icon).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    mGroupOwnerImage.setImageBitmap(resource);
                }
            });
        } else {
            mGroupOwnerLay.setVisibility(View.GONE);
        }

        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mGroupOwnerLay.setVisibility(View.VISIBLE);
                    resetData(mGroupMemberList);
                } else {
                    mGroupOwnerLay.setVisibility(View.GONE);
                    resetData(search(s.toString()));
                }
            }
        });
    }

    private List<AZItemEntity<EmchatGroupMemberDto>> fillData(List<EmchatGroupMemberDto> date) {
        List<AZItemEntity<EmchatGroupMemberDto>> sortList = new ArrayList<>();
        for (EmchatGroupMemberDto aDate : date) {
            AZItemEntity<EmchatGroupMemberDto> item = new AZItemEntity<>();
            item.setValue(aDate);
            if(aDate.isPreset()) {
                aDate.setNickName("系统通知");
            }
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(aDate.getNickName());
            //取第一个首字母
            String letters = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (letters.matches("[A-Z]")) {
                item.setSortLetters(letters.toUpperCase());
            } else {
                item.setSortLetters("#");
            }

            Iterator keys = mMap.keySet().iterator();
            Integer count = (Integer) mMap.get(letters.toUpperCase());
            mMap.put(letters.toUpperCase(), (count == null) ? 1 : count + 1);
            if (aDate.getEmGroupRole() == 1) {
                mMemberDto = aDate;
            }
            sortList.add(item);
        }
        return sortList;
    }

    public void getGroupUserList() {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("groupId", mStrGroupId);
        ImGroupUserListApi myInfoApi = new ImGroupUserListApi(new HttpListener() {
            @Override
            public void onNext(Object object) {
                super.onNext(object);
                BaseResultEntity<GroupMemberListBean> baseResultEntity = (BaseResultEntity<GroupMemberListBean>) object;
                if (baseResultEntity.getRet().equals("0")) {
                    mGroupMemberList = baseResultEntity.getData().getGroupMemberList();
                    initData();
                } else {
                    //  dismissDiaLog();
                    Toasty.info(ImGroupMemberListActivity.this, "数据获取 失败").show();
                }
            }

            @Override
            public void onError(Throwable e) {
                //((AddGroupInfoActivity)getActivity()).dismissDiaLog();
                Toasty.info(ImGroupMemberListActivity.this, "数据获取 失败").show();
                Timber.e("e = " + e.getMessage());
            }
        }, ImGroupMemberListActivity.this);
        myInfoApi.setRequestBody(queryMap);
        HttpManager.getInstance().doHttpDeal(myInfoApi);
    }

    public List<EmchatGroupMemberDto> search(String name) {
        List<EmchatGroupMemberDto> results = new ArrayList();
        Pattern pattern = Pattern.compile(name);
        for (int i = 0; i < mGroupMemberList.size(); i++) {
            Matcher matcher = pattern.matcher(mGroupMemberList.get(i).getNickName());
            if (matcher.find()) {
                results.add(mGroupMemberList.get(i));
            }
        }
        return results;
    }

    public void resetData(List<EmchatGroupMemberDto> groupMemberList) {
        mMap.clear();
        List<AZItemEntity<EmchatGroupMemberDto>> dateList = fillData(groupMemberList);
        Collections.sort(dateList, new Comparator<AZItemEntity<EmchatGroupMemberDto>>() {
            @Override
            public int compare(AZItemEntity<EmchatGroupMemberDto> o1, AZItemEntity<EmchatGroupMemberDto> o2) {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        });
        mAdapter.upData(dateList, mMap);
    }
}
