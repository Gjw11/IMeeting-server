package com.IMeeting.entity;

import javax.persistence.*;

/**
 * Created by gjw on 2018/11/18.
 */
@Entity
@Table(name = "m_meetroom_depart")
public class MeetroomDepart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer meetroomId;
    private Integer departId;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMeetroomId() {
        return meetroomId;
    }

    public void setMeetroomId(Integer meetroomId) {
        this.meetroomId = meetroomId;
    }

    public Integer getDepartId() {
        return departId;
    }

    public void setDepartId(Integer departId) {
        this.departId = departId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
