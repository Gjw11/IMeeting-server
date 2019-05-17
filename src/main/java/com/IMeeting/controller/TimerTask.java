package com.IMeeting.controller;

import com.IMeeting.dao.AbnormalInfoDao;
import com.IMeeting.entity.AbnormalInfo;
import com.IMeeting.entity.FaceInfo;
import com.IMeeting.entity.Meeting;
import com.IMeeting.entity.PushMessage;
import com.IMeeting.resposirity.AbnormalRepository;
import com.IMeeting.resposirity.FaceInfoRepository;
import com.IMeeting.resposirity.MeetingRepository;
import com.IMeeting.resposirity.PushMessageRepository;
import com.IMeeting.util.FaceRecognition;
import com.IMeeting.util.SFTPUtil;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by gjw on 2019/1/22.
 */
@Component
public class TimerTask {
    public final static long oneTime = 10 * 1000;
    @Autowired
    private FaceInfoRepository faceInfoRepository;
    @Autowired
    private AbnormalInfoDao abnormalInfoDao;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private PushMessageRepository pushMessageRepository;
    @Autowired
    private AbnormalRepository abnormalRepository;

    @Scheduled(fixedRate = oneTime)
    public void startMeeting() throws ParseException, SftpException, FileNotFoundException {
        List<FaceInfo> faceInfos = faceInfoRepository.selectJoinPersonFaceInfo(1, 3);
        List<Meeting> meetings = meetingRepository.findByMeetroomIdAndStatus(1, 3);
        if (meetings.size() != 0) {
            Meeting meeting = meetings.get(0);
            int meetingId = meeting.getId();
            int userId = meeting.getUserId();
            if (faceInfos.size() != 0) {
                SFTPUtil sftp = new SFTPUtil("root", "Jgn990206", "39.106.56.132", 22);
                sftp.login();
                Vector<?> sftpList = sftp.listFiles("/usr/share/nginx/image/Face");
                List<String> files = new ArrayList<>();
                for (int i = 0; i < sftpList.size(); i++) {
                    int idx = sftpList.get(i).toString().indexOf("test");
                    String str = null;
                    if (idx != -1) {
                        str = sftpList.get(i).toString().substring(idx);
                        files.add(str);
                    }
                }
                for (int j = 0; j < files.size(); j++) {
                    List<AbnormalInfo>abnormalInfos=abnormalRepository.findByMeetingIdAndImgUrl(meetingId,"https://www.jglo.top:8091/Face/"+files.get(j));
                    if (abnormalInfos.size()==0) {
                        FaceRecognition faceRecognition = new FaceRecognition();
                        File file = sftp.downloadFile("/usr/share/nginx/image/Face", files.get(j), "com");
                        byte[] faceDetail = faceRecognition.getFeatureData(file);
                        boolean flag=true;
                        for (FaceInfo faceInfo : faceInfos) {
                            double similar = faceRecognition.faceCompare(faceDetail, faceInfo.getFaceDetail());
                            if (similar > 0.8) {
                                flag=false;
                                break;
                            }
                        }
                        if (flag) {
                            AbnormalInfo abnormalInfo = new AbnormalInfo();
                            abnormalInfo.setImgUrl("https://www.jglo.top:8091/Face/" + files.get(j));
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            abnormalInfo.setTime(simpleDateFormat.format(new Date()));
                            abnormalInfo.setMeetingId(meetingId);
                            abnormalInfo.setStatus(0);
                            abnormalInfo.setUserId(userId);
                            abnormalInfoDao.save(abnormalInfo);
                        }
                    }
                }
                sftp.logout();
            }
        }
    }

    @Scheduled(fixedRate = oneTime)
    public void pushAbnormal() {
        List<AbnormalInfo> abnormalInfos = abnormalRepository.findByStatus(0);
        PushMessage pushMessage;
        for (AbnormalInfo abnormalInfo : abnormalInfos) {
            pushMessage = new PushMessage();
            pushMessage.setTime(abnormalInfo.getTime());
            pushMessage.setMeetingId(abnormalInfo.getMeetingId());
            pushMessage.setMessage(abnormalInfo.getImgUrl());
            pushMessage.setReceiveId(abnormalInfo.getUserId());
            pushMessage.setStatus(0);
            abnormalRepository.changeStatus(abnormalInfo.getId());
            pushMessageRepository.save(pushMessage);
        }
    }

//    @Autowired
//    private MeetingService meetingService;
//    @Autowired
//    private MeetroomRepository meetroomRepository;
//    @Scheduled(fixedRate = oneTime)
//    public void startMeeting() throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String nowTime=sdf.format(new Date());
//        meetingService.updateMeetingStatus(nowTime,1,3);
//    }
//    @Scheduled(fixedRate = oneTime)
//    public void startMeeting1() throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String nowTime=sdf.format(new Date());
//        meetingService.updateMeetingStatus(nowTime,2,6);
//    }
//    @Scheduled(fixedRate = oneTime)
//    public void startMeeting2() throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String nowTime=sdf.format(new Date());
//        meetingService.updateMeetingStatus(nowTime,8,7);
//    }
//    @Scheduled(fixedRate = oneTime)
//    public void overMeeting() throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String nowTime=sdf.format(new Date());
//        meetingService.updateMeetingOverStatus(nowTime,3,4);
//
//    }
//
//    @Scheduled(fixedRate = oneTime)
//    public void updateMeetRoomBegin() throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String nowTime=sdf.format(new Date());
//        List<Meetroom> meetroomList=meetroomRepository.findMeetRoomRun(nowTime);
//        for (Meetroom meetroom:meetroomList){
//            meetroomRepository.updateMeetRoomRun(meetroom.getId());
//        }
//    }
//    @Scheduled(fixedRate = oneTime)
//    public void updateMeetRoomOver() throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String nowTime=sdf.format(new Date());
//        List<Meetroom> meetrooms=meetroomRepository.findMeetRoomOver(nowTime);
//        for (Meetroom meetroom:meetrooms){
//            meetroomRepository.updateMeetRoomOver(meetroom.getId());
//        }
//    }

}
