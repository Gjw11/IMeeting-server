package com.IMeeting.entity;

import javax.persistence.*;

/**
 * Created by gjw on 2019/3/5.
 */
@Entity
@Table(name = "m_open_apply")
public class OpenApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String beginDate;
    private String overDate;
    @Column(name = "meet_room_id")
    private int meetRoomId;
    @Column(name = "user_id")
    private int userId;
    private String note;
    private String beginTime;
    private String overTime;
    private int status;
    private String createTime;
    private int tenantId;

    @OneToOne
    @JoinColumn(name = "user_id",insertable = false,updatable = false,nullable = false)
    private Userinfo userinfo;
    @OneToOne
    @JoinColumn(name = "meet_room_id",insertable = false,updatable = false,nullable = false)
    private Meetroom meetroom;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getOverDate() {
        return overDate;
    }

    public void setOverDate(String overDate) {
        this.overDate = overDate;
    }

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public Meetroom getMeetroom() {
        return meetroom;
    }

    public void setMeetroom(Meetroom meetroom) {
        this.meetroom = meetroom;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeetRoomId() {
        return meetRoomId;
    }

    public void setMeetRoomId(int meetRoomId) {
        this.meetRoomId = meetRoomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
