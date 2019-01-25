package com.IMeeting.entity;

import java.util.List;

/**
 * Created by gjw on 2019/1/14.
 */
public class CoordinateParameter {
    private String topic;
    private String content;
    private Integer meetRoomId;
    private String reserveDate;
    private String beginTime;
    private String overTime;
    private int prepareTime;
    private int lastTime;
    private int beforeOrLast;//1表示开始后 2表示结束前
    private List<Integer> joinPeopleId;
    private String note;
    private Integer beforeMeetingId;
    private List<OutsideJoinPerson> outsideJoinPersons;

    public List<OutsideJoinPerson> getOutsideJoinPersons() {
        return outsideJoinPersons;
    }

    public void setOutsideJoinPersons(List<OutsideJoinPerson> outsideJoinPersons) {
        this.outsideJoinPersons = outsideJoinPersons;
    }

    public int getBeforeOrLast() {
        return beforeOrLast;
    }

    public void setBeforeOrLast(int beforeOrLast) {
        this.beforeOrLast = beforeOrLast;
    }

    public int getLastTime() {
        return lastTime;
    }

    public void setLastTime(int lastTime) {
        this.lastTime = lastTime;
    }

    public Integer getBeforeMeetingId() {
        return beforeMeetingId;
    }

    public void setBeforeMeetingId(Integer beforeMeetingId) {
        this.beforeMeetingId = beforeMeetingId;
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

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
