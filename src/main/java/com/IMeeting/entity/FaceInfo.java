package com.IMeeting.entity;

import javax.persistence.*;

/**
 * Created by gjw on 2019/1/18.
 */
@Entity
@Table(name = "u_face")
public class FaceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private String faceAddress;
    private byte[] faceDetail;
    private Integer status;
    private Integer tenantId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFaceAddress() {
        return faceAddress;
    }

    public void setFaceAddress(String faceAddress) {
        this.faceAddress = faceAddress;
    }

    public byte[] getFaceDetail() {
        return faceDetail;
    }

    public void setFaceDetail(byte[] faceDetail) {
        this.faceDetail = faceDetail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
}
