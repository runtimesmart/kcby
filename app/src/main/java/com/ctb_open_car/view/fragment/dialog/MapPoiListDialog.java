package com.ctb_open_car.view.fragment.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ctb_open_car.R;
import com.ctb_open_car.engine.manager.GsonManager;
import com.ctb_open_car.ui.RecycleViewDivider;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MapPoiListDialog extends BottomSheetDialog {

    @BindView(R.id.map_poi_list)
    RecyclerView mPoiListView;
    private Context mContext;

    private MapPoiAdapter poiAdapter;

    public MapPoiListDialog(@NonNull Context context, Bundle b) {
        super(context);
        String currentKey = b.getString("current_city");
        String searchTxt = b.getString("search_key");
        search(searchTxt, currentKey);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void search(String key, String currentCity) {

        String poiSearchType = "汽车服务|汽车销售|" +
                "//汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|" +
                "//住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|" +
                "//金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";

        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        PoiSearch.Query query = new PoiSearch.Query(key, poiSearchType, currentCity);
        query.setPageSize(30);// 设置每页最多返回多少条poiItem
        query.setPageNum(1);// 设置查第一页

        PoiSearch poiSearch = new PoiSearch(getContext(), query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                Timber.d(GsonManager.getInstance().GsonString(poiResult));
//                poiAdapter.setData(poiResult.getPois());
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
//            poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 3000, true));//设置搜索范围
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    @Override
    public void setContentView(View view) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.map_poi_dialog_layout, null);

        super.setContentView(v);
    }



    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mPoiListView.setLayoutManager(layoutManager);
        mPoiListView.addItemDecoration(new RecycleViewDivider(getContext(), DividerItemDecoration.VERTICAL, Color.parseColor("#F5F5F5"), 5));
        poiAdapter = new MapPoiAdapter(getContext());
        mPoiListView.setAdapter(poiAdapter);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Window window = getDialog().getWindow();
//        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        // 设置动画
//        window.setWindowAnimations(R.style.bottomDialog);
//
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.gravity = Gravity.BOTTOM;
//        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
//        params.width = getResources().getDisplayMetrics().widthPixels;
//        window.setAttributes(params);
//
//    }

}
