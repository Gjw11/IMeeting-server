package com.IMeeting.controller;

import com.IMeeting.entity.Meeting;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.resposirity.MeetingRepository;
import com.IMeeting.service.JoinPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/joinPerson")
public class JoinPersonController {
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private JoinPersonService joinPersonService;
    //前往签到记录，显示序号、topic(主题)、begin（开始时间）、over（结束时间）三个参数、后面一个查看按钮，查看某场会议具体签到情况
    @RequestMapping("/toJoinPersonIndex")
    public ServerResult toJoinPersonIndex(HttpServletRequest request){
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        List<Meeting>meetings=meetingRepository.selectByUserIdAndStatusJoin(userId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(meetings);
        serverResult.setStatus(true);
        return serverResult;
    }
    //显示某场会议的签到情况
    //传入参数 显示序号、参会人员名字、电话、状态(如果状态是已签到，显示签到时间，如果状态是未签到的有个提醒按钮）
    @RequestMapping("/showOneMeeting")
    public ServerResult showOneMeeting(@RequestParam("meetingId") Integer meetingId){
        ServerResult serverResult=joinPersonService.showOneMeeting(meetingId);
        return serverResult;
    }
    //提醒状态是未签到的人
    @RequestMapping("/remindOne")
    public ServerResult remindOne(@RequestParam("meetingId") Integer meetingId,@RequestParam("userId")Integer userId){
        ServerResult serverResult=joinPersonService.remindOne(meetingId,userId);
        return serverResult;
    }
}
