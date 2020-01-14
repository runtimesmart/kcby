package com.ctb_open_car.eventbus;

import java.util.List;

public class MessageEvent {

    private int id;
    private String type;
    private Object object;
    public MessageEvent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

}
