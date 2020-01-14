package com.ctb_open_car.view.activity.map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.base.BaseMapActivity;
import com.ctb_open_car.base.BaseSearchActivity;
import com.ctb_open_car.constraints.AppContraint;
import com.ctb_open_car.engine.manager.GsonManager;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.ctb_open_car.utils.PreferenceUtils;
import com.ctb_open_car.view.ClearEditTextView;
import com.ctb_open_car.view.adapter.map.PoiResultAdapter;
import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import timber.log.Timber;

public class LocSearchActivity extends BaseSearchActivity implements
        TextWatcher, TextView.OnEditorActionListener {

    @BindView(R.id.search_edit_text)
    EditText mSearchEditTxt;
    @BindView(R.id.search_edit_delete)
    ImageView mTxtClear;

    @BindView(R.id.lsv_SuggestResult)
    RecyclerView mRectyclerVuiew;

    @BindView(R.id.search_food)
    TextView mSearchFood;

    @BindView(R.id.search_gas)
    TextView mSearchGas;

    @BindView(R.id.search_supermarket)
    TextView mSearchMarket;

    @BindView(R.id.search_wc)
    TextView mSearchWC;

    private PoiResultAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_search);
        ButterKnife.bind(this);
        initView();
        mCurrentCity = getIntent().getStringExtra("current_city");
        mCurrentLoc = getIntent().getParcelableExtra("lat_lng");
    }

    public void initView() {
        mSearchEditTxt.addTextChangedListener(this);
        mSearchEditTxt.setOnEditorActionListener(this);

        String searchHistroy = PreferenceUtils.getString(this, AppContraint.SearchHistory.NAVI_HISTORY_KEY);
        LinkedList<PoiItem> historyList;
        if (TextUtils.isEmpty(searchHistroy)) {
            historyList = new LinkedList<>();
        } else {
            historyList = GsonManager.getInstance().GsonToLinkedList(searchHistroy, PoiItem.class);
        }


        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setOrientation(RecyclerView.VERTICAL);
        mRectyclerVuiew.addItemDecoration(new RecycleViewDivider(this, DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 2));
        mRectyclerVuiew.setLayoutManager(ll);
        mAdapter = new PoiResultAdapter(this);
        mRectyclerVuiew.setAdapter(mAdapter);
        mSearchEditTxt.requestFocus();


        mAdapter.setData(historyList);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!TextUtils.isEmpty(charSequence)) {
            mTxtClear.setVisibility(View.VISIBLE);

        } else {
            mTxtClear.setVisibility(View.GONE);

        }
    }


    /**
     * 搜索结果返回
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int r) {
        Timber.e(poiResult.getPois().toString() + "" + r);
        if (r == 1000) {
            List<PoiItem> datas = new ArrayList<>();
            ArrayList<PoiItem> pois = poiResult.getPois();

            mAdapter.setData(pois);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            keyWord = mSearchEditTxt.getText().toString().trim();
            doSearchQuery(keyWord, mCurrentCity);
            return true;
        }
        return false;
    }

    @OnClick({R.id.search_food, R.id.search_supermarket
            , R.id.search_gas, R.id.search_wc})
    void onFilterClick(View v) {
        int id = v.getId();
        mNearbySearch = true;
        switch (id) {
            case R.id.search_food:
                doSearchQuery("美食", mCurrentCity);
                break;
            case R.id.search_gas:
                doSearchQuery("加油站", mCurrentCity);
                break;
            case R.id.search_wc:
                doSearchQuery("厕所", mCurrentCity);
                break;
            case R.id.search_supermarket:
                doSearchQuery("超市", mCurrentCity);
                break;
        }
    }

    @OnClick(R.id.search_collect)
    void toCollect(View v) {
        Intent i = new Intent(this, CollectPoiActivity.class);
        i.putExtra("current_city", mCurrentCity);
        i.putExtra("lat_lng", mCurrentLoc);
        startActivity(i);

    }

    @Override
    public void afterTextChanged(Editable editable) {
        keyWord = mSearchEditTxt.getText().toString().trim();
        mNearbySearch = false;
        doSearchQuery(keyWord, mCurrentCity);

    }

    @OnClick(R.id.search_edit_delete)
    void clearTxt() {
        mSearchEditTxt.setText("");
    }

    @OnClick(R.id.search_cancel)
    void cancelSearch() {
        this.finish();
    }


    @Override
    public Object getTag() {
        return null;
    }


}
