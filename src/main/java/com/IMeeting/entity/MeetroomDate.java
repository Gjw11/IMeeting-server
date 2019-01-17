package com.IMeeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.util.List;

/**
 * Created by gjw on 2018/12/16.
 */
public class MeetroomDate {
    List<Integer>MeetroomIds;
    String MeetroomDate;

    public List<Integer> getMeetroomIds() {
        return MeetroomIds;
    }

    public void setMeetroomIds(List<Integer> meetroomIds) {
        MeetroomIds = meetroomIds;
    }

    public String getMeetroomDate() {
        return MeetroomDate;
    }

    public void setMeetroomDate(String meetroomDate) {
        MeetroomDate = meetroomDate;
    }
}
