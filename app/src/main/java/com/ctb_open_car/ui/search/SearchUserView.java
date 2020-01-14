package com.ctb_open_car.ui.search;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ctb_open_car.R;
import com.ctb_open_car.bean.community.response.user.UserCardDto;
import com.ctb_open_car.presenter.SearchUserPresenter;
import com.ctb_open_car.ui.BaseView;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.view.activity.search.SearchUserActivity;
import com.ctb_open_car.view.adapter.map.PoiResultAdapter;
import com.ctb_open_car.view.adapter.search.SearchUserAdapter;
import com.library.InputTextMsgDialog;

import java.lang.ref.SoftReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchUserView implements BaseView, TextView.OnEditorActionListener {

    @BindView(R.id.user_name_search)
    EditText mEdittext;

    @BindView(R.id.search_empty_tip)
    LinearLayout mEmptyTip;

    @BindView(R.id.search_user_list)
    RecyclerView mResultList;

    @BindView(R.id.user_search_cancel)
    TextView mCancelSearch;

    private SearchUserAdapter mAdapter;
    private SoftReference<SearchUserActivity> mActivity;
    private CharSequence mSearchKey;
    private SearchUserPresenter searchUserPresenter;


    public SearchUserView(SearchUserActivity activity) {
        mActivity = new SoftReference<>(activity);
        ButterKnife.bind(this,mActivity.get());
        initView();
    }

    private void initView() {
        mEdittext.setOnEditorActionListener(this);

        LinearLayoutManager ll = new LinearLayoutManager(mActivity.get());
        ll.setOrientation(RecyclerView.VERTICAL);
        mResultList.addItemDecoration(new RecycleViewDivider(mActivity.get(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 2));
        mResultList.setLayoutManager(ll);
        mAdapter = new SearchUserAdapter(mActivity.get());
        mResultList.setAdapter(mAdapter);
    }

    private void initData() {
        mSearchKey = mActivity.get().getIntent().getCharSequenceExtra("search_key");
        if(!TextUtils.isEmpty(mSearchKey)) {
            mEdittext.setText(mSearchKey);
        }
        mEdittext.requestFocus();
    }

    @Override
    public void setPresenter(Object presenter) {
        searchUserPresenter = (SearchUserPresenter) presenter;
        initData();
        searchUserPresenter.seachUser(mSearchKey.toString());
    }

    public void updateSearchData(List<UserCardDto> searchResult) {
        if (0 == searchResult.size()) {
            mEmptyTip.setVisibility(View.VISIBLE);
            mResultList.setVisibility(View.GONE);

        } else {
            mEmptyTip.setVisibility(View.GONE);
            mResultList.setVisibility(View.VISIBLE);
            mAdapter.setData(searchResult);
        }
    }

    @OnClick(R.id.user_search_cancel)
    void cancelSearch(View v) {
        mActivity.get().finish();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (!TextUtils.isEmpty(v.getText().toString().trim())) {
            searchUserPresenter.seachUser(v.getText().toString());
        }

        return false;
    }

    @Override
    public void drawTitleBar() {

    }

    @Override
    public void unbind() {

    }


}
