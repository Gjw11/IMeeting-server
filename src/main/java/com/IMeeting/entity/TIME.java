package com.IMeeting.entity;

/**
 * Created by gjw on 2019/4/10.
 */

public class TIME {
    int hour;
    int minute;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public TIME(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Boolean compare(TIME t) {
        if(hour !=t.hour)
            return hour < t.hour;
        return minute < t.minute;
    }

    @Override
    public String toString() {
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }

}
