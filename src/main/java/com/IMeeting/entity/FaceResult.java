package com.IMeeting.entity;

/**
 * Created by gjw on 2019/2/3.
 */
public class FaceResult {
    private Integer id;
    private String name;
    private String FaceAddress;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaceAddress() {
        return FaceAddress;
    }

    public void setFaceAddress(String faceAddress) {
        FaceAddress = faceAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
