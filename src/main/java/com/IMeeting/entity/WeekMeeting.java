package com.IMeeting.entity;

import javax.persistence.*;

/**
 * Created by gjw on 2019/3/5.
 */
@Entity
@Table(name = "m_week_meeting")
public class WeekMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String beginTime;
    private String overTime;
    private int week;
    @Column(name = "user_id")
    private int userId;
    private String createTime;
    private int status;
    @Column(name = "meet_room_id")
    private int meetRoomId;
    private String meetBegin;
    private String meetOver;
    private int tenantId;
    @Column(name = "depart_id")
    private int departId;
    private String note;

    @OneToOne
    @JoinColumn(name = "user_id",insertable = false,updatable = false,nullable = false)
    private Userinfo userinfo;
    @OneToOne
    @JoinColumn(name = "depart_id",insertable = false,updatable = false,nullable = false)
    private Depart depart;
    @OneToOne
    @JoinColumn(name = "meet_room_id",insertable = false,updatable = false,nullable = false)
    private Meetroom meetroom;

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public Depart getDepart() {
        return depart;
    }

    public void setDepart(Depart depart) {
        this.depart = depart;
    }

    public Meetroom getMeetroom() {
        return meetroom;
    }

    public void setMeetroom(Meetroom meetroom) {
        this.meetroom = meetroom;
    }

    public int getDepartId() {
        return departId;
    }

    public void setDepartId(int departId) {
        this.departId = departId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getMeetRoomId() {
        return meetRoomId;
    }

    public void setMeetRoomId(int meetRoomId) {
        this.meetRoomId = meetRoomId;
    }

    public String getMeetBegin() {
        return meetBegin;
    }

    public void setMeetBegin(String meetBegin) {
        this.meetBegin = meetBegin;
    }

    public String getMeetOver() {
        return meetOver;
    }

    public void setMeetOver(String meetOver) {
        this.meetOver = meetOver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
