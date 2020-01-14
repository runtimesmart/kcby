package com.ctb_open_car.view.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ctb_open_car.R;
import com.ctb_open_car.base.BaseActivity;
import com.ctb_open_car.presenter.UserInfoPresenter;
import com.ctb_open_car.ui.person.PersonHomeView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonHomeActivity extends BaseActivity {

    private UserInfoPresenter mUserPresenter;
    public PersonHomeView mPersonView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_home);
        ButterKnife.bind(this);
        Intent i = getIntent();
        long userId = i.getLongExtra("user_id",0);
        mUserPresenter= new UserInfoPresenter(this);
        mPersonView=new PersonHomeView(this,userId+"");
        mPersonView.setPresenter(mUserPresenter);
    }

    @OnClick(R.id.btn_back)
    public void onBackClick(View v) {
        this.finish();
    }
    @Override
    public Object getTag() {
        return null;
    }
}
