package com.IMeeting.entity;

import java.sql.Time;
import java.util.List;

/**
 * Created by gjw on 2019/1/13.
 */
public class ReserveParameter {
    private String topic;
    private String content;
    private Integer meetRoomId;
    private String reserveDate;
    private String beginTime;
    private int lastTime;
    private int prepareTime;
    private List<Integer>joinPeopleId;
    private String status;
    private String meetroom;
    private List<OutsideJoinPerson>outsideJoinPersons;

    public List<OutsideJoinPerson> getOutsideJoinPersons() {
        return outsideJoinPersons;
    }

    public void setOutsideJoinPersons(List<OutsideJoinPerson> outsideJoinPersons) {
        this.outsideJoinPersons = outsideJoinPersons;
    }

    public String getStatus() {
        return status;
    }

    public String getMeetroom() {
        return meetroom;
    }

    public void setMeetroom(String meetroom) {
        this.meetroom = meetroom;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMeetRoomId() {
        return meetRoomId;
    }

    public void setMeetRoomId(Integer meetRoomId) {
        this.meetRoomId = meetRoomId;
    }

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public int getLastTime() {
        return lastTime;
    }

    public void setLastTime(int lastTime) {
        this.lastTime = lastTime;
    }

    public int getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(int prepareTime) {
        this.prepareTime = prepareTime;
    }

    public List<Integer> getJoinPeopleId() {
        return joinPeopleId;
    }

    public void setJoinPeopleId(List<Integer> joinPeopleId) {
        this.joinPeopleId = joinPeopleId;
    }
}
