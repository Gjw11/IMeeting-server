package com.IMeeting.entity;

/**
 * Created by gjw on 2019/2/11.
 */
public class DepartHour {
    private int id;
    private String departName;
    private double hour;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public double getHour() {
        return hour;
    }

    public void setHour(double hour) {
        this.hour = hour;
    }
}
