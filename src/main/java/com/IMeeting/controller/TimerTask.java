package com.IMeeting.controller;

import com.IMeeting.resposirity.MeetingRepository;
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
    private MeetingRepository meetingRepository;
    @Scheduled(fixedRate = oneTime)
    public void startMeeting() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime=sdf.format(new Date());
        meetingRepository.updateMeetingStatus(nowTime,1,3);
        meetingRepository.updateMeetingStatus(nowTime,2,6);
        meetingRepository.updateMeetingOverStatus(nowTime,3,4);

    }
}
