package com.IMeeting.controller;

import com.IMeeting.resposirity.MeetingRepository;
import com.IMeeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private MeetingService meetingService;
    @Scheduled(fixedRate = oneTime)
    public void startMeeting() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime=sdf.format(new Date());
        meetingService.updateMeetingStatus(nowTime,1,3);
    }
    @Scheduled(fixedRate = oneTime)
    public void startMeeting1() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime=sdf.format(new Date());
        meetingService.updateMeetingStatus(nowTime,2,6);
    }
    @Scheduled(fixedRate = oneTime)
    public void startMeeting2() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime=sdf.format(new Date());
        meetingService.updateMeetingStatus(nowTime,8,7);
    }
    @Scheduled(fixedRate = oneTime)
    public void overMeeting() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime=sdf.format(new Date());
        meetingService.updateMeetingOverStatus(nowTime,3,4);

    }
}
