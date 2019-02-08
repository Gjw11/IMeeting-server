package com.IMeeting.controller;

import com.IMeeting.entity.Meetroom;
import com.IMeeting.resposirity.MeetingRepository;
import com.IMeeting.resposirity.MeetroomRepository;
import com.IMeeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gjw on 2019/1/22.
 */
@Component
public class TimerTask {
    public final static long oneTime = 60 * 1000;
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private MeetroomRepository meetroomRepository;
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

    @Scheduled(fixedRate = oneTime)
    public void updateMeetRoomBegin() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime=sdf.format(new Date());
        List<Meetroom> meetroomList=meetroomRepository.findMeetRoomRun(nowTime);
        for (Meetroom meetroom:meetroomList){
            meetroomRepository.updateMeetRoomRun(meetroom.getId());
        }
    }
    @Scheduled(fixedRate = oneTime)
    public void updateMeetRoomOver() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime=sdf.format(new Date());
        List<Meetroom> meetrooms=meetroomRepository.findMeetRoomOver(nowTime);
        for (Meetroom meetroom:meetrooms){
            meetroomRepository.updateMeetRoomRun(meetroom.getId());
        }
    }

}
