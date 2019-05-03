package com.IMeeting.controller;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.MeetingRepository;
import com.IMeeting.resposirity.MeetroomEquipRepository;
import com.IMeeting.resposirity.MeetroomRepository;
import com.IMeeting.service.MeetRoomService;
import com.IMeeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gjw on 2019/2/4.
 */
@RestController
@RequestMapping("/meetRoom")
public class MeetRoomController {
    @Autowired
    private MeetRoomService meetRoomService;
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private MeetroomRepository meetroomRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private MeetroomEquipRepository meetroomEquipRepository;
    //查询该租户所有的会议室,前端根据数字显示会议室状态，nowStatus使用状态0表示未使用，1表示使用中,availstatus表示是否可用1表示可用0表示禁用
    //查询该租户的设备集合equips和部门集合departs需存储 insert方法插入一个部门时需要使用
    @RequestMapping("/selectAll")
    public ServerResult selectAll(HttpServletRequest request){
        ServerResult serverResult=new ServerResult();
        List<List> list= (List<List>) meetRoomService.selectAll(request);
        if (list!=null) {
            serverResult.setData(list);
            serverResult.setStatus(true);
        }
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
    //用户端获取用户有权限可预定的会议室
    @RequestMapping("/getEffectiveMeetroom")
    public ServerResult getEffectiveMeetroom(HttpServletRequest request){
        List<Meetroom>meetrooms=meetingService.getEffectiveMeetroom(request);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(meetrooms);
        serverResult.setStatus(true);
        return serverResult;
    }
    //扫描二维码验证该用户是否有权限预定该会议室
    @RequestMapping("/swapCode")
    public ServerResult swepCode(@RequestParam("meetRoomId")Integer meetRoomId,HttpServletRequest request){
        ServerResult serverResult=new ServerResult();
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        List<Meetroom>meetrooms=meetingService.getEffectiveMeetroom(request);
        int bol=0;
        if (meetrooms.size()!=0){
            for (Meetroom meetroom:meetrooms){
                if (meetroom.getId()==meetRoomId){
                    bol=1;
                    break;
                }
            }
        }
        if (bol==0){
            serverResult.setMessage("对不起，您没有权限预定该会议室");
            serverResult.setCode(-1);
        }else{
            List<String>result=meetingService.findFreeTime(meetRoomId,request);
            serverResult.setCode(1);
            serverResult.setData(result);
        }
        serverResult.setStatus(true);
        return serverResult;
    }

    //会议室会议管理
    @RequestMapping("/slectByDateAndMeetRoom")
    public ServerResult slectByDateAndMeetRoom(@RequestParam("meetRoomId")Integer meetRoomId,@RequestParam("selectDate")String selectDate){
        ServerResult serverResult=new ServerResult();
        List<Meeting>meetings=meetingRepository.findByMeetroomIdAndMeetDateOrderByBegin(meetRoomId,selectDate);
        serverResult.setData(meetings);
        serverResult.setStatus(true);
        return serverResult;
    }
    //获取某个会议室的设备
    @RequestMapping("/getOneRoomEquip")
    public ServerResult getOneRoomEquip(@RequestParam("meetRoomId") Integer meetRoomId){
        List<MeetroomEquip>meetroomEquips=meetroomEquipRepository.findByMeetroomId(meetRoomId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(meetroomEquips);
        serverResult.setStatus(true);
        return serverResult;
    }

}
