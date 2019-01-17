package com.IMeeting.entity;

/**
 * Created by gjw on 2018/9/28.
 */
public class Dom<T> {

    private Object data;
    public Dom() {
        this.data = null;
    }
    public Dom(T data) {
        this.data = data;
    }
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}