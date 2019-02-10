package com.IMeeting.entity;

/**
 * Created by gjw on 2019/2/10.
 */
public class SelectMeetingParameter {
    private String topic;   //会议主题
    private String selectBegin;//查找开始时间
    private String selectOver;//查找结束时间
    private Integer meetRoomId;//会议室id
    private Integer departmentId;//部门id
    private String reserveName;//预定人名字

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSelectBegin() {
        return selectBegin;
    }

    public void setSelectBegin(String selectBegin) {
        this.selectBegin = selectBegin;
    }

    public String getSelectOver() {
        return selectOver;
    }

    public void setSelectOver(String selectOver) {
        this.selectOver = selectOver;
    }

    public Integer getMeetRoomId() {
        return meetRoomId;
    }

    public void setMeetRoomId(Integer meetRoomId) {
        this.meetRoomId = meetRoomId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getReserveName() {
        return reserveName;
    }

    public void setReserveName(String reserveName) {
        this.reserveName = reserveName;
    }
}
