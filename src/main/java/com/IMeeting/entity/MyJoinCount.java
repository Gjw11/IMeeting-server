package com.IMeeting.entity;

/**
 * Created by gjw on 2019/1/27.
 */
public class MyJoinCount {
    private String meetDate;
    private int notStartCount;
    private int overCount;

    public String getMeetDate() {
        return meetDate;
    }

    public void setMeetDate(String meetDate) {
        this.meetDate = meetDate;
    }

    public int getNotStartCount() {
        return notStartCount;
    }

    public void setNotStartCount(int notStartCount) {
        this.notStartCount = notStartCount;
    }

    public int getOverCount() {
        return overCount;
    }

    public void setOverCount(int overCount) {
        this.overCount = overCount;
    }
}
