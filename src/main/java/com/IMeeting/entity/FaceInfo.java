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
    private String faceDetail;
    private Integer isChecked;
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

    public String getFaceDetail() {
        return faceDetail;
    }

    public void setFaceDetail(String faceDetail) {
        this.faceDetail = faceDetail;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
}
