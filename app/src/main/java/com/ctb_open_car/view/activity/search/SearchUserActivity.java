package com.ctb_open_car.view.activity.search;

import android.os.Bundle;
import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.presenter.SearchUserPresenter;
import com.ctb_open_car.ui.search.SearchUserView;


public class SearchUserActivity extends BaseActivity {



    private SearchUserPresenter mSearchUserPresenter;
    private SearchUserView mSearchUserView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user_layout);
        mSearchUserView=new SearchUserView(this);
        mSearchUserPresenter=new SearchUserPresenter(this,mSearchUserView);
        mSearchUserView.setPresenter(mSearchUserPresenter);
    }



    @Override
    public Object getTag() {
        return null;
    }
}
