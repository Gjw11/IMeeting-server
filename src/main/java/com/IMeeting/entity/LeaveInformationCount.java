package com.IMeeting.entity;

/**
 * Created by gjw on 2019/1/30.
 */
public class LeaveInformationCount {
    Integer meetingId;
    String meetTime;
    String topic;
    Integer allCount;
    Integer notDealCount;

    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetTime() {
        return meetTime;
    }

    public void setMeetTime(String meetTime) {
        this.meetTime = meetTime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getNotDealCount() {
        return notDealCount;
    }

    public void setNotDealCount(Integer notDealCount) {
        this.notDealCount = notDealCount;
    }
}
