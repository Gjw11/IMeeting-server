package com.IMeeting.controller;

import com.IMeeting.dao.MeetingVideoDao;
import com.IMeeting.dao.TenantDao;
import com.IMeeting.dao.VideoRightDao;
import com.IMeeting.entity.*;
import com.IMeeting.resposirity.MeetingVideoRepository;
import com.IMeeting.resposirity.TenantRepository;
import com.IMeeting.service.UserinfoService;
import com.IMeeting.util.tls_sigature;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gjw on 2019/5/13.
 */
@RestController
@RequestMapping("/video")
@Transactional
public class VideoController {
    String privStr = "-----BEGIN PRIVATE KEY-----\n" +
            "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgD5PeS6Qtywn8mo0Q\n" +
            "UHdvweAnZbqP8WbQVSnFJmGpm+yhRANCAAQdjpZQaB1JNU/GGIk0zLKulhNviqHC\n" +
            "/wMDdiPhUCyeP1PvXPdyCNwrIiFUMZYWBRHf0LJ/PRlMSH8Y2siE0iFy\n" +
            "-----END PRIVATE KEY-----\n";

    @Autowired
    private VideoRightDao videoRightDao;
    @Autowired
    private MeetingVideoDao meetingVideoDao;
    @Autowired
    private MeetingVideoRepository meetingVideoRepository;

    //查找我参加的视频会议
    @RequestMapping("/selectMyVideoRoom")
    public ServerResult selectMyVideoRoom(HttpServletRequest request){
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        List<MeetingVideo> meetingVideoList=meetingVideoRepository.findMyMeeting(userId);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        serverResult.setData(meetingVideoList);
        return serverResult;
    }
    //创建视频会议
    @RequestMapping("/createMeetRoom")
    public ServerResult createMeetRoom(HttpServletRequest request, @RequestBody MeetingVideoPara meetingVideoPara){
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        MeetingVideo meetingVideo=new MeetingVideo();
        meetingVideo.setCreateUserId(userId);
        meetingVideo.setTenantId(tenantId);
        meetingVideo.setVideoRoomName(meetingVideoPara.getVideoRoomName());
        meetingVideo.setStatus(1);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        meetingVideo.setCreateTime(simpleDateFormat.format(new Date()));
        MeetingVideo meetingVideo1=meetingVideoRepository.save(meetingVideo);
        List<Integer> list=meetingVideoPara.getUserId();
        int bol=0;
        VideoRight videoRight;
        for (int i=0;i<list.size();i++){
            videoRight =new VideoRight();
            videoRight.setVideoId(meetingVideo1.getId());
            videoRight.setUserId(list.get(i));
            videoRightDao.save(videoRight);
            if (list.get(i).equals(userId))
                bol=1;
        }
        if (bol==0) {
            videoRight = new VideoRight();
            videoRight.setVideoId(meetingVideo1.getId());
            videoRight.setUserId(userId);
            videoRightDao.save(videoRight);
        }
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
    //结束视频会议
    @RequestMapping("/overMeeting")
    public ServerResult overMeeting(@RequestParam("id")Integer id,HttpServletRequest request){
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        List<MeetingVideo>meetingVideos=meetingVideoRepository.findByCreateUserIdAndId(userId,id);
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        if (meetingVideos.size()==0){
            serverResult.setMessage("您没有权限结束视频会议");
        }else {
            int bol = meetingVideoDao.executeSql("update m_meeting_video m set m.status=2 where m.id=?", id);
            if (bol != 0) {
                serverResult.setCode(1);
                serverResult.setMessage("视频会议已结束");
            }
            else
                serverResult.setCode(0);
        }
        return serverResult;
    }
    //加入视频会议
    @RequestMapping("/joinMeeting")
    public ServerResult joinMeeting(HttpServletRequest request){
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        tls_sigature.GenTLSSignatureResult result = tls_sigature.GenTLSSignatureEx(1400208454, userId.toString(), privStr);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        List<Object>list=new ArrayList<>();
        list.add(userId);
        list.add(request.getSession().getAttribute("name"));
        list.add(result.urlSig);
        serverResult.setData(list);
        return serverResult;
    }
}
