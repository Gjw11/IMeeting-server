package com.IMeeting.controller;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.*;
import com.IMeeting.service.GroupService;
import com.IMeeting.service.MeetingService;
import com.IMeeting.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private LeaveInformationRepository leaveInformationRepository;
    @Autowired
    private MeetroomRepository meetroomRepository;
    @Autowired
    private DepartRepository departRepository;
    @Autowired
    private JoinPersonRepository joinPersonRepository;
    @Autowired
    private PushMessageRepository pushMessageRepository;

    //预定会议首页
    @RequestMapping("/reserveIndex")
    public ServerResult reserveIndex(HttpServletRequest request) {
        ServerResult serverResult = meetingService.toReserveIndex(request);
        return serverResult;
    }

    //查找一个会议室某一天的预定情况
    @RequestMapping("/oneRoomReserver")
    public ServerResult oneRoomReserver(@RequestParam(value = "reserverDate", required = false) String reserverDate, @RequestParam(value = "roomId", required = false) Integer roomId) {
        ServerResult serverResult = meetingService.getOneRoomReserver(reserverDate, roomId);
        return serverResult;
    }

    //查询某天会议室集合的预定情况，进度条显示
    @RequestMapping("/oneDayReserver")
    public ServerResult oneDayReserver(@RequestBody OneDayReservation oneDayReservation) {
        ServerResult serverResult = meetingService.getOneDayReserve(oneDayReservation);
        return serverResult;
    }

    //在预定的时候获取该用户拥有的群组列表
    @RequestMapping("/getGroupList")
    public ServerResult getGroupList(HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        ServerResult serverResult = groupService.getGroupList(userId);
        return serverResult;
    }

    //在预定的时候选择某个群组，将该群组的所有成员显示出来
    @RequestMapping("/showOneGroup")
    public ServerResult showOneGroup(@RequestParam Integer groupId) {
        ServerResult serverResult = groupService.showOneGroup(groupId);
        return serverResult;
    }

    //除了群组人员以外选择其他人员
    @RequestMapping("/selectPeople")
    public ServerResult selectPeople(HttpServletRequest request) {
        ServerResult serverResult = groupService.showUser(request);
        return serverResult;
    }

    //预定会议
    @RequestMapping("/reserveMeeting")
    public ServerResult reserveMeeting(@RequestBody ReserveParameter reserveParameter, HttpServletRequest request) throws Exception {
        ServerResult serverResult = meetingService.reserveMeeting(reserveParameter, request);
        return serverResult;
    }

    //抢会议
    @RequestMapping("/robMeeting")
    public ServerResult robMeeting(@RequestBody ReserveParameter reserveParameter, HttpServletRequest request) {
        ServerResult serverResult = meetingService.robMeeting(reserveParameter, request);
        return serverResult;
    }

    //调用会议
    @RequestMapping("/coordinateMeeting")
    public ServerResult coordinateMeeting(@RequestBody CoordinateParameter coordinateParameter, HttpServletRequest request) {
        ServerResult serverResult = meetingService.coordinateMeeting(coordinateParameter, request);
        return serverResult;
    }

    //取消会议
    @RequestMapping("/cancelMeeting")
    public ServerResult coordinateMeeting(@RequestParam("meetingId") Integer meetingId) {
        ServerResult serverResult = meetingService.cancelMeeting(meetingId);
        return serverResult;
    }

    //显示用户当月预定情况
    @RequestMapping("/showMyReserve")
    public ServerResult showMyReserve(HttpServletRequest request) {
        ServerResult serverResult = meetingService.showMyReserve(request);
        return serverResult;
    }

    //查找某个月用户会议预定情况
    @RequestMapping("/specifiedMyReserve")
    public ServerResult specifiedMyReserve(HttpServletRequest request, @RequestParam("yearMonth") String yearMonth) {
        ServerResult serverResult = meetingService.specifiedMyReserve(request, yearMonth);
        return serverResult;
    }

    //显示用户某一天所有预定情况
    @RequestMapping("/showOneDayReserve")
    public ServerResult showMyReserve(@RequestParam("reserveDate") String reserveDate, HttpServletRequest request) {
        ServerResult serverResult = meetingService.oneDayMyReserve(reserveDate, request);
        return serverResult;
    }

    //显示某个预定会议的细节
    @RequestMapping("/showOneReserveDetail")
    public ServerResult showOneReserveDetail(@RequestParam("meetingId") Integer meetingId) {
        ServerResult serverResult = meetingService.oneReserveDetail(meetingId);
        return serverResult;
    }

    //拒绝调用会议
    @RequestMapping("/disagreeCoordinate")
    public ServerResult disagreeCoordinate(@RequestParam("coordinateId") Integer coordinateId) {
        ServerResult serverResult = meetingService.disagreeCoordinate(coordinateId);
        return serverResult;
    }

    //同意调用会议
    @RequestMapping("/agreeCoordinate")
    public ServerResult agreeCoordinate(@RequestParam("coordinateId") Integer coordinateId) {
        ServerResult serverResult = meetingService.agreeCoordinate(coordinateId);
        return serverResult;
    }

    //第一种修改方式，修改了时间或者会议室地点或者都修改，相当于取消原会议重新预定
    //第二种修改方式，修改除时间和会议室地点外的其他内容
    @RequestMapping("/editOneServer")
    public ServerResult OneEditMyServer(@RequestBody ReserveParameter reserveParameter, HttpServletRequest request) throws Exception {
        Meeting meeting = meetingService.findByMeetingId(reserveParameter.getMeetingId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String reserveBegin = reserveParameter.getReserveDate() + " " + reserveParameter.getBeginTime();
        String reserveOver = TimeUtil.addMinute(reserveBegin, reserveParameter.getLastTime());
        Integer reserveMeetroomId = reserveParameter.getMeetRoomId();
        ServerResult serverResult = null;
        if (meeting.getBegin().equals(reserveBegin) && meeting.getOver().equals(reserveOver) && meeting.getMeetroomId().equals(reserveMeetroomId)) {
            serverResult = meetingService.twoEditMyServer(reserveParameter, request);
        } else {
            serverResult = meetingService.oneEditMyServer(reserveParameter, request);
        }
        return serverResult;
    }

    //提前结束会议
    @RequestMapping("/advanceOver")
    public ServerResult advanceOver(@RequestParam("meetingId") Integer meetingId) throws Exception {
        ServerResult serverResult = meetingService.advanceOver(meetingId);
        return serverResult;
    }

    //计算显示我参加的会议(本月)已结束和未开始会议的次数
    @RequestMapping("/toSelectMyJoinMeeting")
    public ServerResult toSelectMyJoinMeeting(HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String yearMonth = sdf.format(new java.util.Date()).substring(0, 7);
        ServerResult serverResult = meetingService.selectMyJoinMeeting(request, yearMonth);
        return serverResult;
    }

    //根据月份计算显示我参加的会议的已结束和未开始会议的次数参数如2019-01-20
    @RequestMapping("/specifiedMyJoinMeeting")
    public ServerResult specifiedMyJoinMeeting(HttpServletRequest request, @RequestParam("yearMonth") String yearMonth) {
        ServerResult serverResult = meetingService.selectMyJoinMeeting(request, yearMonth);
        return serverResult;
    }

    //显示某一天我参加的会议情况
    @RequestMapping("/selectMyJoinMeetingByDate")
    public ServerResult selectMyJoinMeetingByDate(HttpServletRequest request, @RequestParam("meetDate") String meetDate) {
        ServerResult serverResult = meetingService.selectMyJoinMeetingByDate(meetDate, request);
        return serverResult;
    }

    //提交请假
    @RequestMapping("/sendLeaveInformation")
    public ServerResult sendLeaveInformation(HttpServletRequest request, @RequestBody LeaveInformation leaveInformation) {
        ServerResult serverResult = meetingService.sendLeaveInformation(leaveInformation, request);
        return serverResult;
    }

    //根据日期显示未开始和进行中会议的请假请求总数和未处理请假数量
    @RequestMapping("/CountLeaveInformation")
    public ServerResult CountLeaveInformation(HttpServletRequest request) {
        ServerResult serverResult = meetingService.countLeaveInformation(request);
        return serverResult;
    }

    //显示某场会议的请假情况
    @RequestMapping("/showOneMeetingLeaveInfo")
    public ServerResult showOneMeetingLeaveInfo(@RequestParam("meetingId") Integer meetingId) {
        ServerResult serverResult = meetingService.showOneMeetingLeaveInfo(meetingId);
        return serverResult;
    }

    //同意请假
    @RequestMapping("/agreeLeave")
    public ServerResult agreeLeave(@RequestParam("leaveInfoId") Integer leaveInfoId) {
        leaveInformationRepository.agreeLeave(leaveInfoId);
        LeaveInformation leaveInformation = meetingService.findById(leaveInfoId);
        joinPersonRepository.updateStatus(3, leaveInformation.getMeetingId(), leaveInformation.getUserId());
        PushMessage pushMessage=new PushMessage();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime=sdf.format(new Date());
        pushMessage.setTime(nowTime);
        pushMessage.setStatus(0);
        pushMessage.setMessage("请假审批通过");
        pushMessage.setReceiveId(leaveInformation.getUserId());
        pushMessage.setMeetingId(leaveInformation.getMeetingId());
        pushMessageRepository.saveAndFlush(pushMessage);
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    //拒绝请假
    @RequestMapping("/disagreeLeave")
    public ServerResult disagreeLeave(@RequestParam("leaveInfoId") Integer leaveInfoId) {
        leaveInformationRepository.disagreeLeave(leaveInfoId);
        LeaveInformation leaveInformation = meetingService.findById(leaveInfoId);
        joinPersonRepository.updateStatus(4, leaveInformation.getMeetingId(), leaveInformation.getUserId());
        PushMessage pushMessage=new PushMessage();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime=sdf.format(new Date());
        pushMessage.setTime(nowTime);
        pushMessage.setStatus(0);
        pushMessage.setMessage("请假审批未通过");
        pushMessage.setReceiveId(leaveInformation.getUserId());
        pushMessage.setMeetingId(leaveInformation.getMeetingId());
        pushMessageRepository.saveAndFlush(pushMessage);
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    //消息推送
    @RequestMapping("/pushMessage")
    public ServerResult pushMessage(HttpServletRequest request) {
        ServerResult serverResult = meetingService.findPushMessage(request);
        return serverResult;
    }

    /*-------------华丽分割线-------------*/
    //跳转条件查找预定记录页面,返回显示会议室id和名字，部门id和名字
    @RequestMapping("/toFindMeetingBySpecification")
    public ServerResult toFindMeetingBySpecification(HttpServletRequest request) {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        List<Meetroom> meetrooms = meetroomRepository.findByTenantId(tenantId);
        List<Depart> departs = departRepository.findByTenantId(tenantId);
        List<List> lists = new ArrayList<>();
        lists.add(meetrooms);
        lists.add(departs);
        ServerResult serverResult = new ServerResult();
        serverResult.setData(lists);
        serverResult.setStatus(true);
        return serverResult;
    }

    //条件查找预定记录,参数备注见实体类，查询开始时间、结束时间必须同时有才可以
    @RequestMapping("/findMeetingBySpecification")
    public ServerResult findMeetingBySpecification(@RequestBody SelectMeetingParameter selectMeetingParameter, HttpServletRequest request) {
        ServerResult serverResult = new ServerResult();
        List<Meeting> meetings = meetingService.findBySpecification(selectMeetingParameter, request);
        serverResult.setData(meetings);
        serverResult.setStatus(true);
        return serverResult;
    }

    //导出会议预定情况，需要和findMeetingBySpecification一样的条件传入
    @RequestMapping("/exportMeetingRecord")
    public void exportMeetingRecord(@RequestBody SelectMeetingParameter selectMeetingParameter, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServerResult serverResult = new ServerResult();
        List<Meeting> meetings = meetingService.findBySpecification(selectMeetingParameter, request);
        meetingService.exportMeetingRecord(meetings, response);
    }

    //传入参数begin查询开始时间 over结束时间 way方面 1会议室 2部门 3预定人 type方式 1时间 2次数
    @RequestMapping("/selectDataCount")
    public ServerResult selectDataCount(HttpServletRequest request, @RequestParam("begin") String begin, @RequestParam("over") String over, @RequestParam("way") int way, @RequestParam("type") int type) {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        ServerResult serverResult = new ServerResult();
        if (way == 1 && type == 1) {
            List<Object> result = meetingService.countHourByMeetRoom(tenantId, begin, over);
            List<MeetRoomHour> meetRoomHours = (List<MeetRoomHour>) result.get(0);
            if (meetRoomHours.size() != 0) {
                serverResult.setMessage(meetRoomHours.get((int) result.get(1)).getMeetRoomName() + "会议室在查询时间内使用会议室时间长，建议增加开设该类型会议室，" +
                        "对该会议室资源进行合理分配管理");
                serverResult.setData(meetRoomHours);
                serverResult.setStatus(true);
            }else{
                serverResult.setMessage("该时间段内会议室未被使用");
            }
        } else if (way == 1 && type == 2) {
            List<Object> result = meetingService.countTimeByMeetRoom(tenantId, begin, over);
            List<MeetRoomTime> meetRoomTimes = (List<MeetRoomTime>) result.get(0);
            if (meetRoomTimes.size() != 0) {
                serverResult.setMessage(meetRoomTimes.get((int) result.get(1)).getMeetRoomName() + "在查询时间内使用频率高，建议增加开设该类型会议室，" +
                        "对该会议室资源进行合理分配管理");
                serverResult.setData(meetRoomTimes);
                serverResult.setStatus(true);
            }else {
                serverResult.setMessage("该时间段内会议室未被使用");
                serverResult.setStatus(true);
            }
        }else if (way == 2 && type == 1) {
            List<Object> result = meetingService.countHourByDepart(tenantId, begin, over);
            List<DepartHour> departHours = (List<DepartHour>) result.get(0);
            if (departHours.size() != 0) {
                serverResult.setMessage(departHours.get((int) result.get(1)).getDepartName() + "在查询时间内使用会议室时间长，建立对会议室资源进行合理调控");
                serverResult.setData(departHours);
                serverResult.setStatus(true);
            }else {
                serverResult.setMessage("该时间段内会议室未被使用");
                serverResult.setStatus(true);
            }
        }else if (way == 2 && type == 2) {
            List<Object> result = meetingService.countTimeByDepart(tenantId, begin, over);
            List<DepartTime> departTimes = (List<DepartTime>) result.get(0);
            if (departTimes.size() != 0) {
                serverResult.setMessage(departTimes.get((int) result.get(1)).getDepartName() + "在查询时间内使用会议室频率高，建立对会议室资源进行合理调控");
                serverResult.setData(departTimes);
                serverResult.setStatus(true);
            }else {
                serverResult.setMessage("该时间段内会议室未被使用");
                serverResult.setStatus(true);
            }
        }else if (way == 3 && type == 1) {
            List<Object> result = meetingService.countHourByPeople(tenantId, begin, over);
            List<UserHour> userHours = (List<UserHour>) result.get(0);
            if (userHours.size() != 0) {
                serverResult.setMessage(userHours.get((int) result.get(1)).getUserName() + "在查询时间内使用会议室时间长，建立对会议室资源进行合理调控");
                serverResult.setData(userHours);
                serverResult.setStatus(true);
            }else {
                serverResult.setMessage("该时间段内会议室未被使用");
                serverResult.setStatus(true);
            }
        }else if (way == 3 && type == 2) {
            List<Object> result = meetingService.countTimeByPeople(tenantId, begin, over);
            List<UserTime> userTimes = (List<UserTime>) result.get(0);
            if (userTimes.size() != 0) {
                serverResult.setMessage(userTimes.get((int) result.get(1)).getUserName() + "在查询时间内使用会议室频率高，建立对会议室资源进行合理调控");
                serverResult.setData(userTimes);
                serverResult.setStatus(true);
            }else {
                serverResult.setMessage("该时间段内会议室未被使用");
                serverResult.setStatus(true);
            }
        }
        return serverResult;
    }

}
