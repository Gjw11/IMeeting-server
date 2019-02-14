package com.IMeeting.entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

/**
 * Created by gjw on 2018/11/18.
 */
@Entity
@Table(name = "m_meeting")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String topic;
    private String content;
    private String begin;
    private String over;
    @Column(name = "user_id")
    private Integer userId;
    private Integer meetroomId;
    private Integer status;
    private Integer tenantId;
    private String meetDate;
    private Integer prepareTime;
    private String createTime;
    private Integer lastTime;
    @Column(name = "depart_id")
    private Integer departId;

    @OneToOne
    @JoinColumn(name = "user_id",insertable = false,updatable = false,nullable = false)
    private Userinfo userinfo;
    @OneToOne
    @JoinColumn(name = "depart_id",insertable = false,updatable = false,nullable = false)
    private Depart depart;

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

    public Integer getDepartId() {
        return departId;
    }

    public void setDepartId(Integer departId) {
        this.departId = departId;
    }

    public void setPrepareTime(Integer prepareTime) {
        this.prepareTime = prepareTime;
    }

    public Integer getLastTime() {
        return lastTime;
    }

    public void setLastTime(Integer lastTime) {
        this.lastTime = lastTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(int prepareTime) {
        this.prepareTime = prepareTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getMeetDate() {
        return meetDate;
    }

    public void setMeetDate(String meetDate) {
        this.meetDate = meetDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMeetroomId() {
        return meetroomId;
    }

    public void setMeetroomId(Integer meetroomId) {
        this.meetroomId = meetroomId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
