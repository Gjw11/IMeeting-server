package com.IMeeting.entity;

/**
 * Created by gjw on 2019/1/30.
 */
public class LeaveInfoResult {
    Integer leaveInfoId;
    String peopleName;
    String peoplePhone;
    String note;
    Integer status;

    public Integer getLeaveInfoId() {
        return leaveInfoId;
    }

    public void setLeaveInfoId(Integer leaveInfoId) {
        this.leaveInfoId = leaveInfoId;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }
}
