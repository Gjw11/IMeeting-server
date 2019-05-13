package com.IMeeting.entity;

import java.util.List;

/**
 * Created by gjw on 2019/5/13.
 */
public class MeetingVideoPara {
    private List<Integer> userId;
    private String videoRoomName;

    public List<Integer> getUserId() {
        return userId;
    }

    public void setUserId(List<Integer> userId) {
        this.userId = userId;
    }

    public String getVideoRoomName() {
        return videoRoomName;
    }

    public void setVideoRoomName(String videoRoomName) {
        this.videoRoomName = videoRoomName;
    }
}
