package com.ctb_open_car.view.activity.im;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.bean.im.CarGroupSearchDto;
import com.ctb_open_car.presenter.SearchGroupPresenter;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.view.adapter.im.GroupSearchAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupSearchActivity extends BaseActivity {
    @BindView(R.id.group_search_tip)
    TextView mGroupTip;
    @BindView(R.id.im_group_create)
    LinearLayout mGroupCreate;
    @BindView(R.id.search_txt)
    TextView mTextViewSearch;
    @BindView(R.id.search_edit_text)
    EditText mEditTextSearch;

    @BindView(R.id.im_txt_create)
    TextView mCreateTxt;

    @BindView(R.id.im_icon_create)
    ImageView mCreateImage;
    @BindView(R.id.group_search_list)
    RecyclerView mRecyclerView;

    @Inject
    SearchGroupPresenter mSearchGroupPresenter;

    private GroupSearchAdapter mGroupSearchAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        initView();
    }

    private void initView() {
        mCreateImage.setVisibility(View.GONE);
        mTextViewSearch.setVisibility(View.GONE);
        mEditTextSearch.setVisibility(View.VISIBLE);
        mCreateTxt.setText("取消");
        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 5));

        mRecyclerView.setLayoutManager(ll);
        mGroupSearchAdapter = new GroupSearchAdapter(this);
        mRecyclerView.setAdapter(mGroupSearchAdapter);
        mEditTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String word = v.getText().toString().trim();
                    if (!TextUtils.isEmpty(word)) {
                        searchGroup(word);
                    }
                }
                return true;
            }
        });
    }

    /**
     * 搜索
     **/
    private void searchGroup(String keyword) {
        mSearchGroupPresenter.requestMyGroupList(keyword, new SearchGroupPresenter.UpdateListener() {
            @Override
            public void update(List<CarGroupSearchDto> groupList) {
                mGroupTip.setVisibility(View.GONE);
                mGroupSearchAdapter.setData(groupList);
            }
        });
    }

    @OnClick(R.id.im_txt_create)
    void cancelClick(View v) {
        onBackPressed();
    }

    @Override
    public Object getTag() {
        return null;
    }
}
