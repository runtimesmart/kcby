package com.ctb_open_car.contract;


import com.ctb_open_car.presenter.BasePresenter;
import com.ctb_open_car.ui.BaseView;

public interface AccountLoginContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {

        void toLogin(String userName, String userPws);

    }
}
