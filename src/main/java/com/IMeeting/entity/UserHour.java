package com.IMeeting.entity;

/**
 * Created by gjw on 2019/2/11.
 */
public class UserHour {
    private int id;
    private String userName;
    private double hour;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getHour() {
        return hour;
    }

    public void setHour(double hour) {
        this.hour = hour;
    }
}
