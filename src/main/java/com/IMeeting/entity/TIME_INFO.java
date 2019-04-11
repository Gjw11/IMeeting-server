package com.IMeeting.entity;

/**
 * Created by gjw on 2019/4/10.
 */

public class TIME_INFO {
    TIME start_time;
    TIME end_time;


    public TIME getStart_time() {
        return start_time;
    }


    public void setStart_time(TIME start_time) {
        this.start_time = start_time;
    }


    public TIME getEnd_time() {
        return end_time;
    }


    public void setEnd_time(TIME end_time) {
        this.end_time = end_time;
    }


    public TIME_INFO(TIME start_time, TIME end_time) {
        super();
        this.start_time = start_time;
        this.end_time = end_time;
    }

}
