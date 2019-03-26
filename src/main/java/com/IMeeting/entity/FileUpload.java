package com.IMeeting.entity;

import javax.persistence.*;

/**
 * Created by gjw on 2019/3/5.
 */
@Entity
@Table(name = "m_file_upload")
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int meetRoomId;
    private int meetingId;
    private String fileName;
    private String fileUrl;
    private int status;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
