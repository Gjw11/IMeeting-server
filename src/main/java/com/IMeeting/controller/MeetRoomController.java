package com.IMeeting.controller;

import com.IMeeting.entity.MeetroomPara;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.resposirity.MeetroomRepository;
import com.IMeeting.service.MeetRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gjw on 2019/2/4.
 */
@RestController
@RequestMapping("/meetRoom")
public class MeetRoomController {
    @Autowired
    private MeetRoomService meetRoomService;
    @Autowired
    private MeetroomRepository meetroomRepository;
    //查询该租户所有的会议室,前端根据数字显示会议室状态，nowStatus使用状态0表示未使用，1表示使用中,availstatus表示是否可用1表示可用0表示禁用
    //查询该租户的设备集合equips和部门集合departs需存储 insert方法插入一个部门时需要使用
    @RequestMapping("/selectAll")
    public ServerResult selectAll(HttpServletRequest request){
        ServerResult serverResult=meetRoomService.selectAll(request);
        return  serverResult;
    }
    //禁用某个会议室
    @RequestMapping("/banOne")
    public ServerResult banOne(@RequestParam("MeetRoomId")Integer meetRoomId){
        meetroomRepository.updateMeetRoomAvailStatus(meetRoomId,0);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return  serverResult;
    }
    //启用某个会议室
    @RequestMapping("/enableOne")
    public ServerResult enableOne(@RequestParam("MeetRoomId")Integer meetRoomId){
        meetroomRepository.updateMeetRoomAvailStatus(meetRoomId,1);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return  serverResult;
    }
    //删除某个会议室
    @RequestMapping("/deleteOne")
    public ServerResult deleteOne(@RequestParam("MeetRoomId")Integer meetRoomId){
        meetroomRepository.updateMeetRoomAvailStatus(meetRoomId,2);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return  serverResult;
    }
    //显示某个会议室详情,具体显示的内容见MeetRoomServiceImpl
    @RequestMapping("/showOne")
    public ServerResult showOne(@RequestParam("MeetRoomId")Integer meetRoomId,HttpServletRequest request){
        ServerResult serverResult=meetRoomService.showOne(meetRoomId,request);
        return  serverResult;
    }
    //修改一个会议室,传入参数equips表示会议室设备，enables表示允许使用会议室的部门,bans表示禁止使用会议室的部门
    @RequestMapping("/editOne")
    public ServerResult editOne(@RequestBody MeetroomPara meetroomPara, HttpServletRequest request){
        ServerResult serverResult=meetRoomService.editOne(meetroomPara,request);
        return  serverResult;
    }
    //添加一个会议室,传入参数equips表示会议室设备，enables表示允许使用会议室的部门,bans表示禁止使用会议室的部门
    @RequestMapping("/insertOne")
    public ServerResult insertOne(@RequestBody MeetroomPara meetroomPara, HttpServletRequest request){
        ServerResult serverResult=meetRoomService.insertOne(meetroomPara,request);
        return  serverResult;
    }
}
