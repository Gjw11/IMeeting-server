package com.IMeeting.entity;

import java.util.List;

/**
 * Created by gjw on 2019/4/8.
 */
public class RecommandResult {
    private int meetRoomId;
    private String meetRoomName;
    private String similar;
    private double calSimilar;
    private int contain;
    private String num;
//    private List<String> equips;
    private List<MeetroomEquip>meetroomEquips;

    public List<MeetroomEquip> getMeetroomEquips() {
        return meetroomEquips;
    }

    public void setMeetroomEquips(List<MeetroomEquip> meetroomEquips) {
        this.meetroomEquips = meetroomEquips;
    }

    public int getMeetRoomId() {
        return meetRoomId;
    }

    public void setMeetRoomId(int meetRoomId) {
        this.meetRoomId = meetRoomId;
    }


    public String getSimilar() {
        return similar;
    }

    public void setSimilar(String similar) {
        this.similar = similar;
    }

    public double getCalSimilar() {
        return calSimilar;
    }

    public void setCalSimilar(double calSimilar) {
        this.calSimilar = calSimilar;
    }

    public String getMeetRoomName() {
        return meetRoomName;
    }

    public void setMeetRoomName(String meetRoomName) {
        this.meetRoomName = meetRoomName;
    }

    public int getContain() {
        return contain;
    }

    public void setContain(int contain) {
        this.contain = contain;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

//    public List<String> getEquips() {
//        return equips;
//    }
//
//    public void setEquips(List<String> equips) {
//        this.equips = equips;
//    }
}
