package com.ctb_open_car.ui;

public interface BaseView<T> {
    void setPresenter(T presenter);

    void drawTitleBar();

    void unbind();
}
