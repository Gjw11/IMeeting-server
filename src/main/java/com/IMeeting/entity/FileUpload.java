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
    @Column(name = "meet_room_id")
    private int meetRoomId;
    @Column(name = "meeting_id")
    private int meetingId;
    private String fileName;
    private String fileUrl;
    private int status;
    private int tenantId;
    @OneToOne
    @JoinColumn(name = "meet_room_id",insertable = false,updatable = false,nullable = false)
    private Meetroom meetroom;
    @OneToOne
    @JoinColumn(name = "meeting_id",insertable = false,updatable = false,nullable = false)
    private Meeting meeting;

    public Meetroom getMeetroom() {
        return meetroom;
    }

    public void setMeetroom(Meetroom meetroom) {
        this.meetroom = meetroom;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

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
