package com.IMeeting.controller;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.MeetingRepository;
import com.IMeeting.resposirity.UserinfoRepository;
import com.IMeeting.service.GroupService;
import com.IMeeting.service.MeetingService;
import com.IMeeting.service.UserinfoService;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gjw on 2018/12/10.
 */
@RestController
@RequestMapping("/meeting")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private GroupService groupService;
    //预定会议首页
    @RequestMapping("/reserveIndex")
    public ServerResult reserveIndex(HttpServletRequest request){
       ServerResult serverResult=meetingService.toReserveIndex(request);
        return serverResult;
    }
    //查找一个会议室某一天的预定情况
    @RequestMapping("/oneRoomReserver")
    public ServerResult oneRoomReserver(@RequestParam(value = "reserverDate",required = false) String reserverDate,@RequestParam(value = "roomId",required = false)Integer roomId){
        ServerResult serverResult=meetingService.getOneRoomReserver(reserverDate,roomId);
        return serverResult;
    }
    //查询某天会议室集合的预定情况，进度条显示
    @RequestMapping("/oneDayReserver")
    public ServerResult oneDayReserver(@RequestBody OneDayReservation oneDayReservation){
        ServerResult serverResult=meetingService.getOneDayReserve(oneDayReservation);
        return serverResult;
    }
    //在预定的时候获取该用户拥有的群组列表
    @RequestMapping("/getGroupList")
    public ServerResult getGroupList(HttpServletRequest request){
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        ServerResult serverResult=groupService.getGroupList(userId);
        return serverResult;
    }
    //在预定的时候选择某个群组，将该群组的所有成员显示出来
    @RequestMapping("/showOneGroup")
    public ServerResult showOneGroup(@RequestParam Integer groupId){
        ServerResult serverResult=groupService.showOneGroup(groupId);
        return serverResult;
    }
    //除了群组人员以外选择其他人员
    @RequestMapping("/selectPeople")
    public ServerResult selectPeople(HttpServletRequest request){
        ServerResult serverResult=groupService.showUser(request);
        return serverResult;
    }
    //预定会议
    @RequestMapping("/reserveMeeting")
    public ServerResult reserveMeeting(@RequestBody ReserveParameter reserveParameter, HttpServletRequest request) throws Exception {
        ServerResult serverResult=meetingService.reserveMeeting(reserveParameter,request);
        return serverResult;
    }
    //抢会议
    @RequestMapping("/robMeeting")
    public ServerResult robMeeting(@RequestBody ReserveParameter reserveParameter, HttpServletRequest request){
        ServerResult serverResult=meetingService.robMeeting(reserveParameter,request);
        return serverResult;
    }
    //调用会议
    @RequestMapping("/coordinateMeeting")
    public ServerResult coordinateMeeting(@RequestBody CoordinateParameter coordinateParameter, HttpServletRequest request){
        ServerResult serverResult=meetingService.coordinateMeeting(coordinateParameter,request);
        return serverResult;
    }
    //取消会议
    @RequestMapping("/cancelMeeting")
    public ServerResult coordinateMeeting(@RequestParam("meetingId")Integer meetingId){
        ServerResult serverResult=meetingService.cancelMeeting(meetingId);
        return serverResult;
    }
    //显示用户当月预定情况
    @RequestMapping("/showMyReserve")
    public ServerResult showMyReserve(HttpServletRequest request){
        ServerResult serverResult=meetingService.showMyReserve(request);
        return serverResult;
    }
    //查找某个月用户会议预定情况
    @RequestMapping("/specifiedMyReserve")
    public ServerResult specifiedMyReserve(HttpServletRequest request,@RequestParam("yearMonth")String yearMonth){
        ServerResult serverResult=meetingService.specifiedMyReserve(request,yearMonth);
        return serverResult;
    }
    //显示用户某一天所有预定情况
    @RequestMapping("/showOneDayReserve")
    public ServerResult showMyReserve(@RequestParam("reserveDate")String reserveDate, HttpServletRequest request){
        ServerResult serverResult=meetingService.OneDayMyReserve(reserveDate,request);
        return serverResult;
    }
    //显示某个预定会议的细节
    @RequestMapping("/showOneReserveDetail")
    public ServerResult showOneReserveDetail(@RequestParam("meetingId")Integer meetingId){
        ServerResult serverResult=meetingService.OneReserveDetail(meetingId);
        return serverResult;
    }
}
