package com.IMeeting.entity;

import java.util.List;

/**
 * Created by gjw on 2019/1/13.
 */
public class OneDayReservation {
    private String dayReservation;
    private List<Integer> meetRooms;

    public String getDayReservation() {
        return dayReservation;
    }

    public void setDayReservation(String dayReservation) {
        this.dayReservation = dayReservation;
    }

    public List<Integer> getMeetRooms() {
        return meetRooms;
    }

    public void setMeetRooms(List<Integer> meetRooms) {
        this.meetRooms = meetRooms;
    }
}
