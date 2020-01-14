package com.ctb_open_car.engine.net.api;


import com.ctb_open_car.engine.HttpListener;
import com.ctb_open_car.engine.net.HttpRequestInterface;
import com.rxretrofitlibrary.Api.BaseApi;
import com.rxretrofitlibrary.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;


import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

public class NewshomeApi extends BaseApi {
    private HashMap<String, String> mQueryMap;
    private int mType = 0;
    public NewshomeApi(HttpListener listener, RxAppCompatActivity rxAppCompatActivity, int type) {
        super(listener, rxAppCompatActivity);
        mType = type;
        setCache(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpRequestInterface mRequestInterface = retrofit.create(HttpRequestInterface.class);
        switch (mType) {
            case 0: // 资讯首页
                return mRequestInterface.getNewsHomeTabList(mQueryMap);
            case 1: // 订阅资讯
                return mRequestInterface.getNewsColumnFollow(mQueryMap);
            case 2: // 资讯栏目信息
                return mRequestInterface.getNewsColumnInfoDynamic(mQueryMap);
            case 3: // 资讯内容详情页
                return mRequestInterface.getNewsBloggerInfo(mQueryMap);
            case 4: // 资讯点赞
                return mRequestInterface.newsLikesPost(mQueryMap);
            case 5: // 资讯评论
                return mRequestInterface.newsCommentPost(mQueryMap);
            case 6: // 资讯点赞评论
                return mRequestInterface.newsCommentLinktPost(mQueryMap);

        }
        return mRequestInterface.getNewsHomeTabList(mQueryMap);
    }

    @Override
    public Object call(Object o) {
        return o;
    }

    public void setRequestBody(HashMap<String, String> body) {
        this.mQueryMap = body;
    }
}
