package com.ctb_open_car.base;

import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;

public class BaseSearchActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener, AMapLocationListener {

    protected PoiResult poiResult; // poi返回的结果
    protected int currentPage = 0;// 当前页面，从0开始计数
    protected PoiSearch.Query query;// Poi查询条件类
    //    private LatLonPoint latLonPoint;
    protected PoiSearch poiSearch;
    protected List<PoiItem> poiItems;// poi数据
    protected LatLng mCurrentLoc;
    protected String keyWord;
    protected String mCurrentCity;
    protected boolean mNearbySearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void startOnceLocation() {
        AMapLocationClient mlocationClient = new AMapLocationClient(this);
        mlocationClient.setLocationListener(this);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keyWord, String currentCity) {
//        latLonPoint = new LatLonPoint();// 116.472995,39.993743

        currentPage = 0;
        String poiSearchType = "汽车服务|汽车销售|" +
                "//汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|" +
                "//住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|" +
                "//金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";

        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query = new PoiSearch.Query(keyWord, poiSearchType, currentCity);
        query.setDistanceSort(true);
        query.setPageSize(30);// 设置每页最多返回多少条poiItem
        query.setPageNum(currentPage);// 设置查第一页
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        LatLonPoint latLonPoint;
        if (mNearbySearch) {
            latLonPoint = new LatLonPoint(mCurrentLoc.latitude, mCurrentLoc.longitude);
            poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 30000, true));//设置搜索范围
        }

//            poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 3000, true));//设置搜索范围
        poiSearch.searchPOIAsyn();// 异步搜索

    }

    @Override
    public Object getTag() {
        return null;
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }
}
