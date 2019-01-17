package com.IMeeting.entity;

import javax.persistence.*;

/**
 * Created by gjw on 2019/1/14.
 */
@Entity
@Table(name = "m_coordinateInfo")
public class CoordinateInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer meetingId;
    private String note;
    private Integer status;
    private Integer beforeMeetingId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBeforeMeetingId() {
        return beforeMeetingId;
    }

    public void setBeforeMeetingId(Integer beforeMeetingId) {
        this.beforeMeetingId = beforeMeetingId;
    }
}
