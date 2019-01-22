package com.IMeeting.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gjw on 2019/1/22.
 */
@Component
public class TimerTask {
    public final static long oneTime = 60 * 1000;

//    @Scheduled(fixedRate = oneTime)
//    public void startMeeting() throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        long nowTime = sdf.parse(sdf.format(new java.util.Date())).getTime();
//
//        System.out.println(new Date());
//
//    }
}
