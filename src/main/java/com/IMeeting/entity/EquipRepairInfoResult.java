package com.IMeeting.entity;

/**
 * Created by gjw on 2019/4/3.
 */
public class EquipRepairInfoResult {
    private int id;
    private String meetRoomName;
    private String userName;
    private String status;
    private String reportTime;
    private String repairTime;
    private String repairName;
    private String EquipName;
    private String damageInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeetRoomName() {
        return meetRoomName;
    }

    public void setMeetRoomName(String meetRoomName) {
        this.meetRoomName = meetRoomName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(String repairTime) {
        this.repairTime = repairTime;
    }

    public String getRepairName() {
        return repairName;
    }

    public void setRepairName(String repairName) {
        this.repairName = repairName;
    }

    public String getEquipName() {
        return EquipName;
    }

    public void setEquipName(String equipName) {
        EquipName = equipName;
    }

    public String getDamageInfo() {
        return damageInfo;
    }

    public void setDamageInfo(String damageInfo) {
        this.damageInfo = damageInfo;
    }
}
