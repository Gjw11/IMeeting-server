package com.IMeeting.entity;

/**
 * Created by gjw on 2019/1/20.
 */
public class CoordinateResult {
    private Integer CoordinateId;
    private String beginTime;
    private String overTime;
    private String note;
    private String peopleName;
    private String peoplePhone;

    public Integer getCoordinateId() {
        return CoordinateId;
    }

    public void setCoordinateId(Integer coordinateId) {
        CoordinateId = coordinateId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getPeoplePhone() {
        return peoplePhone;
    }

    public void setPeoplePhone(String peoplePhone) {
        this.peoplePhone = peoplePhone;
    }
}
