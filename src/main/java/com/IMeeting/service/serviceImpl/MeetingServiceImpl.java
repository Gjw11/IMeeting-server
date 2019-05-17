package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.*;
import com.IMeeting.service.DepartService;
import com.IMeeting.service.MeetingService;
import com.IMeeting.service.UserinfoService;
import com.IMeeting.util.MeetUtil;
import com.IMeeting.util.NumUtil;
import com.IMeeting.util.TimeUtil;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by gjw on 2018/12/12.
 */
@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private MeetroomParameterRepository meetroomParameterRepository;
    @Autowired
    private MeetroomRepository meetroomRepository;
    @Autowired
    private MeetroomDepartRepository meetroomDepartRepository;
    @Autowired
    private EquipRepositpry equipRepositpry;
    @Autowired
    private OutsideJoinPersonRepository outsideJoinPersonRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MeetroomEquipRepository meetroomEquipRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private UserinfoService userinfoService;
    @Autowired
    private JoinPersonRepository joinPersonRepository;
    @Autowired
    private CoordinateInfoRepository coordinateInfoRepository;
    @Autowired
    private LeaveInformationRepository leaveInformationRepository;
    @Autowired
    private DepartService departService;
    @Autowired
    private PushMessageRepository pushMessageRepository;

    @Override
    public MeetroomParameter selectParameter(Integer tenantId) {
        MeetroomParameter meetroomParameter = meetroomParameterRepository.findByTenantId(tenantId);
        return meetroomParameter;
    }

    @Override
    public List<Meetroom> getEffectiveMeetroom(HttpServletRequest request) {
//        Integer roleId = (Integer) request.getSession().getAttribute("roleId");
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        Integer departId = (Integer) request.getSession().getAttribute("departId");
        List<Meetroom> meetrooms = new ArrayList<>();
        List<Meetroom> lists = meetroomRepository.findByTenantIdAndAvailStatus(tenantId, 1);
        for (int i = 0; i < lists.size(); i++) {
            int bol = 0;
            Integer meetroomId = lists.get(i).getId();
//            List<MeetroomRole> meetroomRoles = meetroomRoleRepository.findByMeetroomId(meetroomId);
//            if (meetroomRoles.size() != 0) {
//                for (int j = 0; j < meetroomRoles.size(); j++) {
//                    if (roleId.equals(meetroomRoles.get(j).getRoleId())) {
//                        bol1 = 1;
//                        break;
//                    }
//                }
//            } else {
//                bol1 = 1;
//            }
            List<MeetroomDepart> meetroomDeparts = meetroomDepartRepository.findByMeetroomId(meetroomId);
            if (meetroomDeparts.size() == 0) {
                bol = 1;
            } else {
                for (int m = 0; m < meetroomDeparts.size(); m++) {

                    if (meetroomDeparts.get(m).getDepartId().equals(departId)) {
                        if (meetroomDeparts.get(m).getStatus().equals(1))
                            bol = 1;
                        else if (meetroomDeparts.get(m).getStatus().equals(0))
                            bol = 0;
                        break;
                    }
                }
            }
            if (bol == 1) {
                meetrooms.add(lists.get(i));
            }
        }
        return meetrooms;
    }

    @Override
    public List<Equip> selectEquips(Integer tenantId) {
        List<Equip> equips = equipRepositpry.findByTenantId(tenantId);
        return equips;
    }

    @Override
    public List<MeetroomEquip> selectOneMeetroomEquip(Integer meetroomId) {
        List<MeetroomEquip> meetroomEquips = meetroomEquipRepository.findByMeetroomId(meetroomId);
        return meetroomEquips;
    }

    @Override
    public ServerResult toReserveIndex(HttpServletRequest request) {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        //获取预定会议参数，需要前端存储
        MeetroomParameter meetroomParameter = selectParameter(tenantId);
        //获取可预定的会议室集合
        List<Meetroom> meetrooms = getEffectiveMeetroom(request);
//        request.getSession().setAttribute("effectiveMeetroom",meetrooms);
        //获取每个会议室对应的设备功能集合，需要前端存储
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new java.util.Date());//当前时间
        List<Object> meetroomEquipResult = new ArrayList<>();
        List<List> todayMeeting = new ArrayList<>();
        for (int i = 0; i < meetrooms.size(); i++) {
            Integer meetroomId = meetrooms.get(i).getId();
            List<MeetroomEquip> meetroomEquips = selectOneMeetroomEquip(meetroomId);
            List<Meeting> meetings = meetingRepository.findByMeetroomIdAndMeetDateAndStatusOrderByBegin(meetrooms.get(i).getId(), now, 1);
            List<Meeting> meetings2 = meetingRepository.findByMeetroomIdAndMeetDateAndStatusOrderByBegin(meetrooms.get(i).getId(), now, 3);
            List<Meeting> meetings3 = meetingRepository.findByMeetroomIdAndMeetDateAndStatusOrderByBegin(meetrooms.get(i).getId(), now, 4);
            meetings.addAll(meetings2);
            meetings.addAll(meetings3);
            todayMeeting.add(meetings);
            meetroomEquipResult.add(meetroomEquips);
        }
        List<Equip> equips = selectEquips(tenantId);//获取该租户的设备功能集合,需要前端存储
        List<Object> datas = new ArrayList<>();
        datas.add(meetroomParameter);//会议室预定参数，需要前端存储
        datas.add(equips);//该租户的设备功能，需要前端存储
        datas.add(meetrooms);//该用户可预定的会议室
        datas.add(todayMeeting);//显示今天该用户能够预定的所有会议室预定情况
        datas.add(meetroomEquipResult);//会议室设备集合
        ServerResult serverResult = new ServerResult();
        serverResult.setData(datas);
        serverResult.setStatus(true);
        return serverResult;
    }

    //输入参数为某一天，格式如2019-01-13，会议室编号，前端需提前判断该天是否是限定天数之内
    //输出结果为会议开始、结束时间、主题、预定人电话、预定人名字、预定人部门、会议创建时间、id用于抢会议、协调会议参数、实际按1、2、3显示
    @Override
    public ServerResult getOneRoomReserver(String reserverDate, Integer roomId) {
        ServerResult serverResult = new ServerResult();
        List<ReserverRecord> reserverRecords = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.findByMeetroomIdAndMeetDateAndStatusOrderByBegin(roomId, reserverDate, 3);
        for (int j = 0; j < meetings.size(); j++) {
            ReserverRecord reserverRecord = new ReserverRecord();
            Meeting meeting = meetings.get(j);
            reserverRecord.setBegin(meeting.getBegin());
            reserverRecord.setCreateTime(meeting.getCreateTime());
            reserverRecord.setOver(meeting.getOver());
            reserverRecord.setMeetDate(meeting.getMeetDate());
            reserverRecord.setTopic(meeting.getTopic());
            reserverRecord.setLastTime(meeting.getLastTime());
            Userinfo userinfo = userinfoService.getUserinfo(meeting.getUserId());
            reserverRecord.setPeopleName(userinfo.getName());
            reserverRecord.setPhone(userinfo.getPhone());
            reserverRecord.setPrepareTime(meeting.getPrepareTime());
            reserverRecord.setContent(meeting.getContent());
            reserverRecord.setStatus("进行中");
            Depart depart = userinfoService.getDepart(userinfo.getDepartId());
            reserverRecord.setDepartName(depart.getName());
            reserverRecord.setId(meeting.getId());
            reserverRecords.add(reserverRecord);
        }
        List<Meeting> meetings1 = meetingRepository.findByMeetroomIdAndMeetDateAndStatusOrderByBegin(roomId, reserverDate, 1);
        for (int j = 0; j < meetings1.size(); j++) {
            ReserverRecord reserverRecord = new ReserverRecord();
            Meeting meeting = meetings1.get(j);
            reserverRecord.setBegin(meeting.getBegin());
            reserverRecord.setCreateTime(meeting.getCreateTime());
            reserverRecord.setOver(meeting.getOver());
            reserverRecord.setMeetDate(meeting.getMeetDate());
            reserverRecord.setTopic(meeting.getTopic());
            reserverRecord.setLastTime(meeting.getLastTime());
            Userinfo userinfo = userinfoService.getUserinfo(meeting.getUserId());
            reserverRecord.setPeopleName(userinfo.getName());
            reserverRecord.setPhone(userinfo.getPhone());
            reserverRecord.setPrepareTime(meeting.getPrepareTime());
            reserverRecord.setContent(meeting.getContent());
            reserverRecord.setStatus("未开始");
            Depart depart = userinfoService.getDepart(userinfo.getDepartId());
            reserverRecord.setDepartName(depart.getName());
            reserverRecord.setId(meeting.getId());
            reserverRecords.add(reserverRecord);
        }
        List<Meeting> meetings2 = meetingRepository.findByMeetroomIdAndMeetDateAndStatusOrderByBegin(roomId, reserverDate, 4);
        for (int j = 0; j < meetings2.size(); j++) {
            ReserverRecord reserverRecord = new ReserverRecord();
            Meeting meeting = meetings2.get(j);
            reserverRecord.setBegin(meeting.getBegin());
            reserverRecord.setCreateTime(meeting.getCreateTime());
            reserverRecord.setOver(meeting.getOver());
            reserverRecord.setMeetDate(meeting.getMeetDate());
            reserverRecord.setPrepareTime(meeting.getPrepareTime());
            reserverRecord.setContent(meeting.getContent());
            reserverRecord.setTopic(meeting.getTopic());
            reserverRecord.setLastTime(meeting.getLastTime());
            Userinfo userinfo = userinfoService.getUserinfo(meeting.getUserId());
            reserverRecord.setPeopleName(userinfo.getName());
            reserverRecord.setPhone(userinfo.getPhone());
            reserverRecord.setStatus("已结束");
            Depart depart = userinfoService.getDepart(userinfo.getDepartId());
            reserverRecord.setDepartName(depart.getName());
            reserverRecord.setId(meeting.getId());
            reserverRecords.add(reserverRecord);
        }
        serverResult.setData(reserverRecords);
        serverResult.setStatus(true);
        return serverResult;
    }

    //传入参数为具体的日期,格式如2019-01-13以及要查询的会议室id集合，输出结果为相应会议室某天的预定安排
    @Override
    public ServerResult getOneDayReserve(OneDayReservation oneDayReservation) {
        ServerResult serverResult = new ServerResult();
        List<List> Meetings = new ArrayList<>();
        for (int i = 0; i < oneDayReservation.getMeetRooms().size(); i++) {
            List<Meeting> meetings = meetingRepository.findByMeetroomIdAndMeetDateAndStatusOrderByBegin(oneDayReservation.getMeetRooms().get(i), oneDayReservation.getDayReservation(), 1);
            List<Meeting> meetings2 = meetingRepository.findByMeetroomIdAndMeetDateAndStatusOrderByBegin(oneDayReservation.getMeetRooms().get(i), oneDayReservation.getDayReservation(), 3);
            List<Meeting> meetings3 = meetingRepository.findByMeetroomIdAndMeetDateAndStatusOrderByBegin(oneDayReservation.getMeetRooms().get(i), oneDayReservation.getDayReservation(), 4);
            meetings.addAll(meetings2);
            meetings.addAll(meetings3);
            Meetings.add(meetings);
        }
        serverResult.setData(Meetings);
        serverResult.setStatus(true);
        return serverResult;
    }

    //传入参数为会议主题、会议内容、会议室id、会议室日期、开始时间、持续时间、准备时间、参会人员(不包括发起人自己)、外来人员（集合形式）名字、电话（可省略)
    @Override
    public ServerResult reserveMeeting(ReserveParameter reserveParameter, HttpServletRequest request) throws Exception {
        ServerResult serverResult = new ServerResult();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        MeetroomParameter meetroomParameter = meetroomParameterRepository.findByTenantId(tenantId);
        String beginTime = meetroomParameter.getBegin();
        String overTime = meetroomParameter.getOver();
        int lastTime = reserveParameter.getLastTime();
        int prepareTime = reserveParameter.getPrepareTime();
        String reserveBeginTime = reserveParameter.getBeginTime();
        String reserveDate = reserveParameter.getReserveDate();
        String afterBeginTime = reserveDate + " " + reserveParameter.getBeginTime();
        String afterOverTime = TimeUtil.addMinute(afterBeginTime, lastTime);
        String nowTime = sdf.format(new java.util.Date());
        TimeUtil timeUtil = new TimeUtil();
        int bol1 = 2, bol2 = 2, bol3 = 2, bol4 = 2;
        bol1 = timeUtil.DateCompare(reserveBeginTime, beginTime, "HH:mm");
        bol2 = timeUtil.DateCompare(afterOverTime.substring(11, 16), overTime, "HH:mm");
        bol3 = timeUtil.DateCompare(reserveBeginTime, afterOverTime.substring(11, 16), "HH:mm");
        bol4 = timeUtil.DateCompare(afterBeginTime, nowTime, "yyyy-MM-dd HH:mm");
        if (prepareTime > lastTime) {
            serverResult.setMessage("准备时间不能大于持续时间");
        } else if (bol3 == 0) {
            serverResult.setMessage("预定时间不能为0分钟");
        } else if (bol1 == -1) {
            serverResult.setMessage("预定时间不能早于" + beginTime);
        } else if (bol2 == 1) {
            serverResult.setMessage("结束时间不能晚于" + overTime);
        } else if (bol4 == -1) {
            serverResult.setMessage("预定会议时间不能在当前时间之前");
        } else {
            Integer meetroomId = reserveParameter.getMeetRoomId();
            List<Meeting> meetings = meetingRepository.findIntersectMeeting(afterBeginTime, afterOverTime, meetroomId);
            if (meetings.size() == 0) {
                Meeting meeting = new Meeting();
                meeting.setMeetDate(reserveParameter.getReserveDate());
                meeting.setBegin(afterBeginTime);
                meeting.setContent(reserveParameter.getContent());
                meeting.setMeetroomId(meetroomId);
                meeting.setOver(afterOverTime);
                meeting.setStatus(1);
                meeting.setLastTime(lastTime);
                meeting.setTopic(reserveParameter.getTopic());
                meeting.setTenantId(tenantId);
                meeting.setUserId(userId);
                meeting.setMeetDate(reserveDate);
                meeting.setPrepareTime(prepareTime);
                Userinfo userinfo = userinfoService.getUserinfo(userId);
                meeting.setDepartId(userinfo.getDepartId());
                meeting.setCreateTime(nowTime);
                Meeting meeting1 = meetingRepository.saveAndFlush(meeting);
                Integer meetingId = meeting1.getId();
                JoinPerson joinPerson;
                PushMessage pushMessage;
//                Meetroom meetroom=findByMeetRoomId(meetroomId);
                String message = "您有一个新的会议，点击查看详情";
                int b = 0;
                List<Integer> list = reserveParameter.getJoinPeopleId();
                for (int i = 0; i < list.size(); i++) {
                    Integer id = list.get(i);
                    if (id.equals(userId))
                        b = 1;
                    joinPerson = new JoinPerson();
                    joinPerson.setMeetingId(meetingId);
                    joinPerson.setUserId(id);
                    joinPerson.setStatus(0);
                    joinPersonRepository.saveAndFlush(joinPerson);
                    pushMessage = new PushMessage();
                    pushMessage.setReceiveId(id);
                    pushMessage.setStatus(0);
                    pushMessage.setMessage(message);
                    pushMessage.setMeetingId(meetingId);
                    pushMessage.setTime(nowTime);
                    pushMessageRepository.saveAndFlush(pushMessage);
                }
                if (b == 0) {
                    joinPerson = new JoinPerson();
                    joinPerson.setMeetingId(meetingId);
                    joinPerson.setUserId(userId);
                    joinPerson.setStatus(0);
                    joinPersonRepository.saveAndFlush(joinPerson);
                    pushMessage = new PushMessage();
                    pushMessage.setReceiveId(userId);
                    pushMessage.setStatus(0);
                    pushMessage.setMessage(message);
                    pushMessageRepository.saveAndFlush(pushMessage);
                }
                List<OutsideJoinPerson> outsideJoinPersons = reserveParameter.getOutsideJoinPersons();
                for (int i = 0; i < outsideJoinPersons.size(); i++) {
                    OutsideJoinPerson outsideJoinPerson = new OutsideJoinPerson();
                    outsideJoinPerson.setName(outsideJoinPersons.get(i).getName());
                    outsideJoinPerson.setPhone(outsideJoinPersons.get(i).getPhone());
                    outsideJoinPerson.setMeetingId(meetingId);
                    outsideJoinPersonRepository.saveAndFlush(outsideJoinPerson);
                }
                serverResult.setMessage("会议预定成功");
                serverResult.setStatus(true);
            } else {
                serverResult.setMessage("预定时间段有冲突");
            }
        }
        return serverResult;
    }

    //传入参数和预定会议一样,时间、会议室无法选择，只能是那一段
    @Override
    public ServerResult robMeeting(ReserveParameter reserveParameter, HttpServletRequest request) {
        ServerResult serverResult = new ServerResult();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String reserveDate = reserveParameter.getReserveDate();
        String reserveBeginTime = reserveParameter.getBeginTime();
        int lastTime = reserveParameter.getLastTime();
        String afterBeginTime = reserveDate + " " + reserveBeginTime;
        String afterOverTime = TimeUtil.addMinute(afterBeginTime, lastTime);
        String nowTime = sdf.format(new java.util.Date());
        Meeting meeting = new Meeting();
        meeting.setMeetDate(reserveParameter.getReserveDate());
        meeting.setBegin(afterBeginTime);
        meeting.setContent(reserveParameter.getContent());
        meeting.setMeetroomId(reserveParameter.getMeetRoomId());
        meeting.setOver(afterOverTime);
        meeting.setStatus(2);
        meeting.setTopic(reserveParameter.getTopic());
        meeting.setTenantId(tenantId);
        meeting.setUserId(userId);
        Userinfo userinfo = userinfoService.getUserinfo(userId);
        meeting.setUserId(userinfo.getId());
        meeting.setLastTime(lastTime);
        meeting.setMeetDate(reserveDate);
        meeting.setPrepareTime(reserveParameter.getPrepareTime());
        meeting.setCreateTime(nowTime);
        meetingRepository.saveAndFlush(meeting);
        Integer meetingId = meeting.getId();
        JoinPerson joinPerson;
        int b = 0;
        List<Integer> list = reserveParameter.getJoinPeopleId();
        for (int i = 0; i < list.size(); i++) {
            Integer id = list.get(i);
            if (id.equals(userId))
                b = 1;
            joinPerson = new JoinPerson();
            joinPerson.setMeetingId(meetingId);
            joinPerson.setUserId(id);
            joinPerson.setStatus(0);
            joinPersonRepository.saveAndFlush(joinPerson);
        }
        if (b == 0) {
            joinPerson = new JoinPerson();
            joinPerson.setMeetingId(meetingId);
            joinPerson.setUserId(userId);
            joinPerson.setStatus(0);
            joinPersonRepository.saveAndFlush(joinPerson);
        }
        List<OutsideJoinPerson> outsideJoinPersons = reserveParameter.getOutsideJoinPersons();
        for (int i = 0; i < outsideJoinPersons.size(); i++) {
            OutsideJoinPerson outsideJoinPerson = new OutsideJoinPerson();
            outsideJoinPerson.setName(outsideJoinPersons.get(i).getName());
            outsideJoinPerson.setPhone(outsideJoinPersons.get(i).getPhone());
            outsideJoinPerson.setMeetingId(meetingId);
            outsideJoinPersonRepository.saveAndFlush(outsideJoinPerson);
        }
        serverResult.setMessage("会议预定成功");
        serverResult.setStatus(true);
        return serverResult;
    }

    //传入参数除和预定会议一样，还包括调用原因(可无)，原来会议的id
    @Override
    public ServerResult coordinateMeeting(CoordinateParameter coordinateParameter, HttpServletRequest request) {
        Meeting meeting = new Meeting();
        meeting.setMeetDate(coordinateParameter.getReserveDate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String reserveDate = coordinateParameter.getReserveDate();
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
//        String reserveBeginTime = coordinateParameter.getBeginTime();
//        String afterBeginTime=reserveDate+" "+reserveBeginTime;
        int lastTime = coordinateParameter.getLastTime();
//        String afterOverTime = TimeUtil.addMinute(,lastTime);
        Integer beforeMeetingId = coordinateParameter.getBeforeMeetingId();
        Meeting meeting1 = findByMeetingId(beforeMeetingId);
        int bol = coordinateParameter.getBeforeOrLast();
        if (bol == 1) {
            String begin = meeting1.getBegin();
            meeting.setBegin(begin);
            meeting.setOver(TimeUtil.addMinute(begin, lastTime));
        } else if (bol == 2) {
            String over = meeting1.getOver();
            meeting.setOver(over);
            meeting.setBegin(TimeUtil.addMinute(over, -lastTime));
        }
        meeting.setTenantId(tenantId);
        meeting.setTopic(coordinateParameter.getTopic());
        meeting.setContent(coordinateParameter.getContent());
        meeting.setMeetroomId(meeting1.getMeetroomId());
        meeting.setLastTime(lastTime);
        meeting.setStatus(8);
        meeting.setUserId(userId);
        Userinfo userinfo = userinfoService.getUserinfo(userId);
        meeting.setDepartId(userinfo.getDepartId());
        meeting.setMeetDate(meeting1.getMeetDate());
        meeting.setPrepareTime(coordinateParameter.getPrepareTime());
        meeting.setCreateTime(sdf.format(new java.util.Date()));
        Meeting m = meetingRepository.saveAndFlush(meeting);
        Integer meetingId = meeting.getId();
        JoinPerson joinPerson;
        int b = 0;
        List<Integer> list = coordinateParameter.getJoinPeopleId();
        for (int i = 0; i < list.size(); i++) {
            Integer id = list.get(i);
            if (id.equals(userId))
                b = 1;
            joinPerson = new JoinPerson();
            joinPerson.setMeetingId(meetingId);
            joinPerson.setUserId(id);
            joinPerson.setStatus(0);
            joinPersonRepository.saveAndFlush(joinPerson);
        }
        if (b == 0) {
            joinPerson = new JoinPerson();
            joinPerson.setMeetingId(meetingId);
            joinPerson.setUserId(userId);
            joinPerson.setStatus(0);
            joinPersonRepository.saveAndFlush(joinPerson);
        }
        CoordinateInfo coordinateInfo = new CoordinateInfo();
        coordinateInfo.setNote(coordinateParameter.getNote());
        coordinateInfo.setMeetingId(m.getId());
        coordinateInfo.setBeforeMeetingId(beforeMeetingId);
        coordinateInfo.setStatus(0);
        coordinateInfoRepository.saveAndFlush(coordinateInfo);
        List<OutsideJoinPerson> outsideJoinPersons = coordinateParameter.getOutsideJoinPersons();
        for (int i = 0; i < outsideJoinPersons.size(); i++) {
            OutsideJoinPerson outsideJoinPerson = new OutsideJoinPerson();
            outsideJoinPerson.setName(outsideJoinPersons.get(i).getName());
            outsideJoinPerson.setPhone(outsideJoinPersons.get(i).getPhone());
            outsideJoinPersonRepository.saveAndFlush(outsideJoinPerson);
        }
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    //传入参数要取消的会议id
    @Override
    public ServerResult cancelMeeting(Integer meetingId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = sdf.format(new Date());
        ServerResult serverResult = new ServerResult();
        Meeting meeting2 = findByMeetingId(meetingId);
        Integer status = meeting2.getStatus();
        PushMessage pushMessage;
        if (status == 1) {
            meetingRepository.updateStatus(meetingId, 5);
            List<CoordinateInfo> coordinateInfos = coordinateInfoRepository.findByBeforeMeetingIdAndStatus(meetingId, 1);
            if (coordinateInfos.size() == 0) {
                Meeting meeting = findByMeetingId(meetingId);
                List<Meeting> meetings = meetingRepository.findByBeginAndOverAndMeetroomIdAndStatusOrderByCreateTimeAsc(meeting.getBegin(), meeting.getOver(), meeting.getMeetroomId(), 2);
                if (meetings.size() != 0) {
                    Meeting meeting1 = meetings.get(0);
                    meetingRepository.updateStatus(meeting1.getId(), 1);
                    List<JoinPerson> joinPersonList = joinPersonRepository.findByMeetingId(meeting1.getId());
//                    Userinfo userinfo=userinfoService.getUserinfo(meeting1.getUserId());
//                    Meetroom meetroom=findByMeetRoomId(meeting1.getMeetroomId());
                    String message2 = "您有一个新的会议，点击查看详情";
                    for (JoinPerson joinPerson : joinPersonList) {
                        pushMessage = new PushMessage();
                        pushMessage.setReceiveId(joinPerson.getUserId());
                        pushMessage.setStatus(0);
                        pushMessage.setMessage(message2);
                        pushMessage.setMeetingId(meeting1.getId());
                        pushMessage.setTime(nowTime);
                        pushMessageRepository.saveAndFlush(pushMessage);
                    }
                }
            }
//            Userinfo userinfo=userinfoService.getUserinfo(meeting2.getUserId());
            String message = "您有一个会议已被取消，点击查看详情";
            List<JoinPerson> joinPersons = joinPersonRepository.findByMeetingId(meetingId);
            for (JoinPerson joinPerson : joinPersons) {
                pushMessage = new PushMessage();
                pushMessage.setReceiveId(joinPerson.getUserId());
                pushMessage.setStatus(0);
                pushMessage.setMessage(message);
                pushMessage.setMeetingId(meetingId);
                pushMessage.setTime(nowTime);
                pushMessageRepository.saveAndFlush(pushMessage);
            }
            serverResult.setMessage("会议取消成功");
        } else if (status == 8) {
            meetingRepository.updateStatus(meetingId, 5);
            coordinateInfoRepository.updateStatusByMeetingId(meetingId, 3);
            serverResult.setMessage("调用取消成功");
        } else {
            meetingRepository.updateStatus(meetingId, 5);
            serverResult.setMessage("预约取消成功");
        }
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public Meeting findByMeetingId(Integer meetingId) {
        Optional<Meeting> meeting = meetingRepository.findById(meetingId);
        if (meeting.isPresent())
            return meeting.get();
        return null;
    }

    //输出结果为本月预定的会议，默认在日历下方显示今天预定的会议，未处理的请求调用记录，显示有未处理的请求调用记录，要保存起来，点击查看详情的时候要显示
    @Override
    public ServerResult showMyReserve(HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String yearMonth = sdf.format(new java.util.Date()).substring(0, 7);
        List<Meeting> groupMeet = meetingRepository.groupBymeetDate(userId, yearMonth + "%");
        List<MyReserveCount> myReserveCounts = new ArrayList<>();
        MyReserveCount count;
        for (int i = 0; i < groupMeet.size(); i++) {
            count = new MyReserveCount();
            String meetDate = groupMeet.get(i).getMeetDate();
            count.setMeetDate(meetDate);
            count.setCount(meetingRepository.countMyReserve(userId, meetDate));
            myReserveCounts.add(count);

        }
        String today = sdf.format(new java.util.Date()).substring(0, 10);
        List<Meeting> todayMeeting = meetingRepository.findMyReserve(userId, today);
        List<ReserverRecord> todayMeetingResult = new ArrayList<>();
        ReserverRecord reserverRecord;
        for (int i = 0; i < todayMeeting.size(); i++) {
            Meeting meeting = todayMeeting.get(i);
            reserverRecord = new ReserverRecord();
            reserverRecord.setId(meeting.getId());
            reserverRecord.setBegin(meeting.getBegin());
            reserverRecord.setOver(meeting.getOver());
            reserverRecord.setContent(meeting.getContent());
            reserverRecord.setTopic(meeting.getTopic());
            reserverRecord.setMeetDate(meeting.getMeetDate());
            Userinfo userinfo = userinfoService.getUserinfo(meeting.getUserId());
            reserverRecord.setPeopleName(userinfo.getName());
            reserverRecord.setPhone(userinfo.getPhone());
            reserverRecord.setPrepareTime(meeting.getPrepareTime());
            String status = "";
            switch (meeting.getStatus()) {
                case 6:
                    status = "预约失败";
                    break;
                case 1:
                    status = "预约成功";
                    break;
                case 2:
                    status = "预约中";
                    break;
                case 3:
                    status = "会议进行中";
                    break;
                case 4:
                    status = "会议结束";
                    break;
                case 5:
                    status = "取消会议";
                    break;
                case 7:
                    status = "调用失败";
                    break;
                case 8:
                    status = "调用中";
                    break;
            }
            reserverRecord.setStatus(status);
            todayMeetingResult.add(reserverRecord);
        }
        ServerResult serverResult = new ServerResult();
        List<Object> result = new ArrayList<>();
        result.add(myReserveCounts);
        result.add(todayMeetingResult);
        serverResult.setStatus(true);
        serverResult.setData(result);
        return serverResult;
    }

    //传入参数为显示的月份,格式如2019-01，格式必须保持一致
    @Override
    public ServerResult specifiedMyReserve(HttpServletRequest request, String yearMonth) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        List<Meeting> groupMeet = meetingRepository.groupBymeetDate(userId, yearMonth + "%");
        List<MyReserveCount> myReserveCounts = new ArrayList<>();
        MyReserveCount count;
        for (int i = 0; i < groupMeet.size(); i++) {
            count = new MyReserveCount();
            String meetDate = groupMeet.get(i).getMeetDate();
            count.setMeetDate(meetDate);
            count.setCount(meetingRepository.countMyReserve(userId, meetDate));
            myReserveCounts.add(count);
        }
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        serverResult.setData(myReserveCounts);
        return serverResult;
    }

    //显示一个我预定的会议的细节
    @Override
    public ServerResult oneReserveDetail(Integer meetingId) {
        Meeting meeting = findByMeetingId(meetingId);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        ReserveParameter reserveParameter = new ReserveParameter();
        reserveParameter.setTopic(meeting.getTopic());
        reserveParameter.setContent(meeting.getContent());
        reserveParameter.setMeetRoomId(meeting.getMeetroomId());
        Meetroom meetroom = findByMeetRoomId(meeting.getMeetroomId());
        if (meetroom != null) {
            reserveParameter.setMeetroom(meetroom.getName());
            reserveParameter.setMeetroomPlace(meetroom.getPlace());
        }
        reserveParameter.setReserveDate(meeting.getMeetDate());
        reserveParameter.setBeginTime(meeting.getBegin());
        reserveParameter.setLastTime(meeting.getLastTime());
        reserveParameter.setOverTime(meeting.getOver());
        reserveParameter.setPrepareTime(meeting.getPrepareTime());
        String status = "";
        switch (meeting.getStatus()) {
            case 6:
                status = "预约失败";
                break;
            case 1:
                status = "预约成功";
                break;
            case 2:
                status = "预约中";
                break;
            case 3:
                status = "会议进行中";
                break;
            case 4:
                status = "会议结束";
                break;
            case 5:
                status = "取消会议";
                break;
            case 7:
                status = "调用失败";
                break;
            case 8:
                status = "调用中";
                break;
        }
        reserveParameter.setStatus(status);
        List<OutsideJoinPerson> outsideJoinPersons = outsideJoinPersonRepository.findByMeetingId(meetingId);
        reserveParameter.setOutsideJoinPersons(outsideJoinPersons);
        List<JoinPerson> joinPersons = joinPersonRepository.findByMeetingId(meetingId);
        List<Integer> userIds = new ArrayList<>();
        for (int i = 0; i < joinPersons.size(); i++) {
            userIds.add(joinPersons.get(i).getUserId());
        }
        reserveParameter.setJoinPeopleId(userIds);
        List<CoordinateInfo> coordinateInfos = coordinateInfoRepository.findByBeforeMeetingIdAndStatus(meetingId, 0);
        List<CoordinateResult> coordinateResults = new ArrayList<>();
        CoordinateResult coordinateResult;
        for (int i = 0; i < coordinateInfos.size(); i++) {
            coordinateResult = new CoordinateResult();
            Meeting meeting1 = findByMeetingId(coordinateInfos.get(i).getMeetingId());
            coordinateResult.setBeginTime(meeting1.getBegin());
            coordinateResult.setOverTime(meeting1.getOver());
            coordinateResult.setNote(coordinateInfos.get(i).getNote());
            Userinfo userinfo = userinfoService.getUserinfo(meeting1.getUserId());
            coordinateResult.setPeopleName(userinfo.getName());
            coordinateResult.setPeoplePhone(userinfo.getPhone());
            coordinateResult.setCoordinateId(coordinateInfos.get(i).getId());
            coordinateResults.add(coordinateResult);
        }
        ServerResult serverResult = new ServerResult();
        List<Object> result = new ArrayList<>();
        result.add(reserveParameter);
        result.add(coordinateResults);
        serverResult.setData(result);
        serverResult.setStatus(true);
        return serverResult;
    }

    //显示某一天我的预定记录,格式如2019-01-20
    @Override
    public ServerResult oneDayMyReserve(String reserveDate, HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        List<Meeting> todayMeeting = meetingRepository.findMyReserve(userId, reserveDate);
        List<ReserverRecord> oneDayMeetingResult = new ArrayList<>();
        ReserverRecord reserverRecord;
        for (int i = 0; i < todayMeeting.size(); i++) {
            Meeting meeting = todayMeeting.get(i);
            reserverRecord = new ReserverRecord();
            reserverRecord.setId(meeting.getId());
            reserverRecord.setBegin(meeting.getBegin());
            reserverRecord.setOver(meeting.getOver());
            reserverRecord.setContent(meeting.getContent());
            reserverRecord.setTopic(meeting.getTopic());
            reserverRecord.setMeetDate(meeting.getMeetDate());
            Userinfo userinfo = userinfoService.getUserinfo(meeting.getUserId());
            reserverRecord.setPeopleName(userinfo.getName());
            reserverRecord.setPhone(userinfo.getPhone());
            reserverRecord.setPrepareTime(meeting.getPrepareTime());
            String status = "";
            switch (meeting.getStatus()) {
                case 6:
                    status = "预约失败";
                    break;
                case 1:
                    status = "预约成功";
                    break;
                case 2:
                    status = "预约中";
                    break;
                case 3:
                    status = "会议进行中";
                    break;
                case 4:
                    status = "会议结束";
                    break;
                case 5:
                    status = "取消会议";
                    break;
                case 7:
                    status = "调用失败";
                    break;
                case 8:
                    status = "调用中";
                    break;
            }
            reserverRecord.setStatus(status);
            oneDayMeetingResult.add(reserverRecord);
        }
        ServerResult serverResult = new ServerResult();
        serverResult.setData(oneDayMeetingResult);
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public Meetroom findByMeetRoomId(Integer meetRoomId) {
        Optional<Meetroom> meetroom = meetroomRepository.findById(meetRoomId);
        if (meetroom.isPresent())
            return meetroom.get();
        return null;
    }

    //拒绝调用会议
    @Override
    public ServerResult disagreeCoordinate(Integer coordinateId) {
        CoordinateInfo coordinateInfo = findByCoordinateId(coordinateId);
        int bol1 = 0, bol2 = 0;
        if (coordinateInfo != null) {
            bol1 = coordinateInfoRepository.updateCoordinateStatus(coordinateId, 2);
            bol2 = meetingRepository.updateStatus(coordinateInfo.getMeetingId(), 7);
        }
        ServerResult serverResult = new ServerResult();
        if (bol1 != 0 && bol2 != 0)
            serverResult.setStatus(true);
        return serverResult;
    }

    //同意调用会议
    @Override
    public ServerResult agreeCoordinate(Integer coordinateId) {
        CoordinateInfo coordinateInfo = findByCoordinateId(coordinateId);
        if (coordinateInfo != null) {
            Integer meetingId = coordinateInfo.getMeetingId();
            Meeting meeting = findByMeetingId(meetingId);
            String beginTime = meeting.getBegin();
            String overTime = meeting.getOver();
            meetingRepository.updateStatus(meetingId, 1);
            List<JoinPerson> list = joinPersonRepository.findByMeetingId(meetingId);
            JoinPerson joinPerson;
            PushMessage pushMessage;
//            Meetroom meetroom=findByMeetRoomId(meeting.getMeetroomId());
//            Userinfo userinfo=userinfoService.getUserinfo(meeting.getUserId());
            String message = "您有一个新的会议，点击查看详情";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = sdf.format(new Date());
            for (int i = 0; i < list.size(); i++) {
                Integer id = list.get(i).getUserId();
                pushMessage = new PushMessage();
                pushMessage.setReceiveId(id);
                pushMessage.setStatus(0);
                pushMessage.setMessage(message);
                pushMessage.setMeetingId(meetingId);
                pushMessage.setTime(nowTime);
                pushMessageRepository.saveAndFlush(pushMessage);
            }
            Meeting beforeMeeting = findByMeetingId(coordinateInfo.getBeforeMeetingId());
            String beforeBeginTime = beforeMeeting.getBegin();
            String beforeOverTime = beforeMeeting.getOver();
            if (beforeBeginTime == beginTime) {
                meetingRepository.updateBegin(beforeMeeting.getId(), overTime);
            } else if (overTime == beforeOverTime) {
                meetingRepository.updateOver(beforeMeeting.getId(), beginTime);
            }
            coordinateInfoRepository.updateCoordinateStatus(coordinateId, 1);
            List<CoordinateInfo> coordinateInfos = coordinateInfoRepository.findByBeforeMeetingIdAndStatus(coordinateInfo.getBeforeMeetingId(), 0);
            for (int i = 0; i < coordinateInfos.size(); i++) {
                CoordinateInfo coordinateInfo1 = coordinateInfos.get(i);
                coordinateInfoRepository.updateCoordinateStatus(coordinateInfo1.getId(), 2);
                meetingRepository.updateStatus(coordinateInfo1.getMeetingId(), 0);
            }
        }
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public CoordinateInfo findByCoordinateId(Integer coordinateId) {
        Optional<CoordinateInfo> coordinateInfo = coordinateInfoRepository.findById(coordinateId);
        if (coordinateInfo.isPresent())
            return coordinateInfo.get();
        return null;
    }

    //修改了会议室，会议时间
    @Override
    public ServerResult oneEditMyServer(ReserveParameter reserveParameter, HttpServletRequest request) throws Exception {
        cancelMeeting(reserveParameter.getMeetingId());
        ServerResult serverResult = reserveMeeting(reserveParameter, request);
        return serverResult;
    }

    //修改除会议室、时间之外的其他内容
    @Override
    public ServerResult twoEditMyServer(ReserveParameter reserveParameter, HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String topic = reserveParameter.getTopic();
        Integer meetingId = reserveParameter.getMeetingId();
        meetingRepository.updateTCP(meetingId, topic, reserveParameter.getContent(), reserveParameter.getPrepareTime());
        List<OutsideJoinPerson> outsideJoinPersons = reserveParameter.getOutsideJoinPersons();
        outsideJoinPersonRepository.deleteByMeetingId(meetingId);
        OutsideJoinPerson outsideJoinPerson;
        for (int i = 0; i < outsideJoinPersons.size(); i++) {
            outsideJoinPerson = new OutsideJoinPerson();
            outsideJoinPerson.setMeetingId(meetingId);
            outsideJoinPerson.setName(outsideJoinPersons.get(i).getName());
            outsideJoinPerson.setPhone(outsideJoinPersons.get(i).getPhone());
            outsideJoinPersonRepository.saveAndFlush(outsideJoinPerson);
        }
        joinPersonRepository.deleteByMeetingId(meetingId);
        int b = 0;
        List<Integer> list = reserveParameter.getJoinPeopleId();
        JoinPerson joinPerson;
        PushMessage pushMessage;
        Meeting meeting = findByMeetingId(meetingId);
//        Meetroom meetroom=findByMeetRoomId(meeting.getMeetroomId());
//        Userinfo userinfo=userinfoService.getUserinfo(userId);
        String message = "您有一个会议信息修改，点击查看详情";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = sdf.format(new Date());
        for (int i = 0; i < list.size(); i++) {
            Integer id = list.get(i);
            if (id.equals(userId))
                b = 1;
            joinPerson = new JoinPerson();
            joinPerson.setMeetingId(meetingId);
            joinPerson.setUserId(id);
            joinPerson.setStatus(0);
            joinPersonRepository.saveAndFlush(joinPerson);
            pushMessage = new PushMessage();
            pushMessage.setReceiveId(id);
            pushMessage.setStatus(0);
            pushMessage.setMessage(message);
            pushMessage.setMeetingId(meetingId);
            pushMessage.setTime(nowTime);
            pushMessageRepository.saveAndFlush(pushMessage);
        }
        if (b == 0) {
            joinPerson = new JoinPerson();
            joinPerson.setMeetingId(meetingId);
            joinPerson.setUserId(userId);
            joinPerson.setStatus(0);
            joinPersonRepository.saveAndFlush(joinPerson);
            pushMessage = new PushMessage();
            pushMessage.setReceiveId(userId);
            pushMessage.setStatus(0);
            pushMessage.setMessage(message);
            pushMessageRepository.saveAndFlush(pushMessage);
        }
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        serverResult.setMessage("预定信息修改成功");
        return serverResult;
    }

    //提前结束会议
    @Override
    public ServerResult advanceOver(Integer meetingId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime = sdf.format(new java.util.Date());
        Meeting meeting = findByMeetingId(meetingId);
        int lastTime = 0;
        try {
            lastTime = (int) TimeUtil.reduceMinute(meeting.getBegin(), nowTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int bol = meetingRepository.advanceOver(meetingId, nowTime, 4, lastTime);
        ServerResult serverResult = new ServerResult();
        if (bol != 0) {
            serverResult.setStatus(true);
            serverResult.setMessage("会议已经提前结束");
        }
        serverResult.setMessage("操作失败");
        return serverResult;
    }

    //显示我参加的会议
    @Override
    public ServerResult selectMyJoinMeeting(HttpServletRequest request, String yearMonth) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        List<Meeting> meetings = meetingRepository.MyJoinMeetingGroupByDate(userId, yearMonth + "%");
        List<MyJoinCount> myJoinCounts = new ArrayList<>();
        MyJoinCount myJoinCount;
        for (int i = 0; i < meetings.size(); i++) {
            myJoinCount = new MyJoinCount();
            Meeting meeting = meetings.get(i);
            String meetDate = meeting.getMeetDate();
            myJoinCount.setMeetDate(meetDate);
            myJoinCount.setNotStartCount(meetingRepository.countNotStartMeeting(userId, meetDate, 1));
            myJoinCount.setOverCount(meetingRepository.countOverMeeting(userId, meetDate, 4));
            myJoinCounts.add(myJoinCount);
        }
        ServerResult serverResult = new ServerResult();
        serverResult.setData(myJoinCounts);
        serverResult.setStatus(true);
        return serverResult;
    }

    //到会议预约开始时间将预约成功状态变为进行中，预约中变为预约失败，调用中变为调用失败
    @Override
    public void updateMeetingStatus(String nowTime, Integer beforeStatus, Integer afterStatus) {
        meetingRepository.updateMeetingStatus(nowTime, beforeStatus, afterStatus);
    }

    //到会议结束时间将进行中状态变为会议结束
    @Override
    public void updateMeetingOverStatus(String nowTime, Integer beforeStatus, Integer afterStatus) {
        meetingRepository.updateMeetingOverStatus(nowTime, beforeStatus, afterStatus);
    }

    //显示某一天我参加的会议
    @Override
    public ServerResult selectMyJoinMeetingByDate(String meetDate, HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        List<Meeting> meetings = meetingRepository.MyJoinMeetingByDate(userId, meetDate);
        List<ReserverRecord> reserverRecords = new ArrayList<>();
        for (int j = 0; j < meetings.size(); j++) {
            ReserverRecord reserverRecord = new ReserverRecord();
            Meeting meeting = meetings.get(j);
            reserverRecord.setBegin(meeting.getBegin());
            reserverRecord.setCreateTime(meeting.getCreateTime());
            reserverRecord.setOver(meeting.getOver());
            reserverRecord.setTopic(meeting.getTopic());
            reserverRecord.setLastTime(meeting.getLastTime());
            Userinfo userinfo = userinfoService.getUserinfo(meeting.getUserId());
            reserverRecord.setPeopleName(userinfo.getName());
            reserverRecord.setPhone(userinfo.getPhone());
            Depart depart = userinfoService.getDepart(userinfo.getDepartId());
            reserverRecord.setDepartName(depart.getName());
            reserverRecord.setId(meeting.getId());
            String status = "";
            switch (meeting.getStatus()) {
                case 1:
                    status = "未开始";
                    break;
                case 3:
                    status = "进行中";
                    break;
                case 4:
                    status = "已结束";
                    break;
            }
            reserverRecord.setStatus(status);
            reserverRecords.add(reserverRecord);
        }
        ServerResult serverResult = new ServerResult();
        serverResult.setData(reserverRecords);
        serverResult.setStatus(true);
        return serverResult;
    }

    //请假，请假原因可略,参数为请假原因，会议id
    @Override
    public ServerResult sendLeaveInformation(LeaveInformation leaveInformation, HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        leaveInformation.setStatus(0);
        leaveInformation.setUserId(userId);
        leaveInformationRepository.saveAndFlush(leaveInformation);
        joinPersonRepository.updateStatus(2, leaveInformation.getMeetingId(), leaveInformation.getUserId());
        ServerResult serverResult = new ServerResult();
        serverResult.setMessage("请假已提交");
        serverResult.setStatus(true);
        return serverResult;
    }

    //根据日期显示未开始和进行中会议的请假请求总数和未处理请假数量
    @Override
    public ServerResult countLeaveInformation(HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        List<Meeting> meetings = meetingRepository.selectByUserIdAndStatus(userId);
        List<LeaveInformationCount> leaveInformationCounts = new ArrayList<>();
        Meeting meeting;
        LeaveInformationCount leaveInformationCount;
        for (int i = 0; i < meetings.size(); i++) {
            meeting = meetings.get(i);
            Integer meetingId = meeting.getId();
            leaveInformationCount = new LeaveInformationCount();
            leaveInformationCount.setMeetingId(meetingId);
            leaveInformationCount.setMeetTime(meeting.getBegin() + "-" + meeting.getOver());
            leaveInformationCount.setTopic(meeting.getTopic());
            leaveInformationCount.setAllCount(leaveInformationRepository.countAll(meetingId));
            leaveInformationCount.setNotDealCount(leaveInformationRepository.notDealCount(meetingId));
            leaveInformationCounts.add(leaveInformationCount);
        }
        ServerResult serverResult = new ServerResult();
        serverResult.setData(leaveInformationCounts);
        serverResult.setStatus(true);
        return serverResult;
    }

    //查找某场会议的请假所有请假信息
    @Override
    public ServerResult showOneMeetingLeaveInfo(Integer meetingId) {
        List<LeaveInformation> leaveInformations = leaveInformationRepository.findByMeetingIdOrderByStatus(meetingId);
        LeaveInformation leaveInformation;
        List<LeaveInfoResult> leaveInfoResults = new ArrayList<>();
        LeaveInfoResult leaveInfoResult;
        for (int i = 0; i < leaveInformations.size(); i++) {
            leaveInformation = leaveInformations.get(i);
            leaveInfoResult = new LeaveInfoResult();
            leaveInfoResult.setLeaveInfoId(leaveInformation.getId());
            Userinfo userinfo = userinfoService.getUserinfo(leaveInformation.getUserId());
            leaveInfoResult.setPeopleName(userinfo.getName());
            leaveInfoResult.setPeoplePhone(userinfo.getPhone());
            leaveInfoResult.setNote(leaveInformation.getNote());
            leaveInfoResult.setStatus(leaveInformation.getStatus());
            leaveInfoResults.add(leaveInfoResult);
        }
        ServerResult serverResult = new ServerResult();
        serverResult.setData(leaveInfoResults);
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public ServerResult findPushMessage(HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        List<PushMessage> list = pushMessageRepository.findByReceiveIdAndStatus(userId, 0);
        for (int i = 0; i < list.size(); i++) {
            pushMessageRepository.updateStatus(list.get(i).getId());
        }
        ServerResult serverResult = new ServerResult();
        serverResult.setData(list);
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public LeaveInformation findById(Integer id) {
        Optional<LeaveInformation> leaveInformation = leaveInformationRepository.findById(id);
        if (leaveInformation.isPresent())
            return leaveInformation.get();
        return null;
    }

    @Override
    public double countSimilar(double[] source, double[] target, double[] weight) {
        int room_property_num = source.length;//会议室属性的数量
        System.out.println(source[room_property_num-1]+"   aaa  " + target[room_property_num-1]);

        double sum_vec = 0;//两个想向量的点积
        double sum_source = 0;//源会议室向量的模长
        double sum_target = 0;//目标会议室向量的模长
        for (int i = 0; i < room_property_num; i++) {
            source[i] *= weight[i];
            target[i] *= weight[i];
        }
        System.out.println(source[room_property_num-1]+"   xxx  " + target[room_property_num-1]);
        source[room_property_num-1] = source[room_property_num-1] / target[room_property_num-1];
        target[room_property_num-1] = 1;
        System.out.println(source[room_property_num-1]+"   zzz  " + target[room_property_num-1]);
        //计算点积与模长
        for (int i = 0; i < room_property_num; i++) {
            sum_vec += source[i] * target[i];
            sum_source += source[i] * source[i];
            sum_target += target[i] * target[i];
        }
        return sum_vec / (Math.sqrt(sum_source * sum_target));
    }

    @Override
    public List<String> findFreeTime(Integer meetRoomId, HttpServletRequest request) {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        MeetroomParameter meetroomParameter = meetroomParameterRepository.findByTenantId(tenantId);
        String beginTime = meetroomParameter.getBegin();
        String overTime = meetroomParameter.getOver();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String today = sdf.format(new Date()).substring(0, 10);
        List<String> result;
        String nowTime = sdf.format(new java.util.Date()).substring(11, 16);
        List<Meeting> meetings = meetingRepository.selectByMeetDateAndStatusEQLOneAndThree(today, meetRoomId);
        result = MeetUtil.returnFreeTime(nowTime, overTime, meetings);
        return result;
    }


    /*-------------华丽分割线-------------*/
    @Override
    public List findBySpecification(SelectMeetingParameter sp, HttpServletRequest request) {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        Specification<Meeting> specification = new Specification<Meeting>() {
            @Override
            public Predicate toPredicate(Root<Meeting> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicatesList = new ArrayList<>();
                Predicate tenantPredicate = cb.equal(root.get("tenantId"), tenantId);
                predicatesList.add(tenantPredicate);
                if (!StringUtils.isEmpty(sp.getTopic())) {
                    Predicate topicPredicate = cb.like(root.get("topic"), '%' + sp.getTopic() + '%');
                    predicatesList.add(topicPredicate);
                }
                if (!StringUtils.isEmpty(sp.getReserveName())) {
                    Join<Meeting, Userinfo> userinfoJoin = root.join("userinfo", JoinType.LEFT);
                    Predicate reserveNamePredicate = cb.like(userinfoJoin.get("name"), '%' + sp.getReserveName() + '%');
                    predicatesList.add(reserveNamePredicate);
                }
                if (!StringUtils.isEmpty(sp.getDepartmentId())) {
                    Predicate departmentIdPredicate = cb.equal(root.get("departmentId"), sp.getDepartmentId());
                    predicatesList.add(departmentIdPredicate);
                }
                if (!StringUtils.isEmpty(sp.getMeetRoomId())) {
                    Predicate meetroomIdPredicate = cb.equal(root.get("meetroomId"), sp.getMeetRoomId());
                    predicatesList.add(meetroomIdPredicate);
                }
                if (!StringUtils.isEmpty(sp.getSelectBegin()) && !StringUtils.isEmpty(sp.getSelectOver())) {
                    Predicate meetDatePredicate = cb.between(root.get("meetDate"), sp.getSelectBegin(), sp.getSelectOver());
                    predicatesList.add(meetDatePredicate);
                }
                if (!StringUtils.isEmpty(sp.getStatus())) {
                    Integer status = null;
                    switch (sp.getStatus()) {
                        case "预约失败":
                            status = 6;
                            break;
                        case "预约成功":
                            status = 1;
                            break;
                        case "预约中":
                            status = 2;
                            break;
                        case "会议进行中":
                            status = 3;
                            break;
                        case "会议结束":
                            status = 4;
                            break;
                        case "取消会议":
                            status = 5;
                            break;
                        case "调用失败":
                            status = 7;
                            break;
                        case "调用中":
                            status = 8;
                            break;
                    }
                    Predicate statusPredicate = cb.equal(root.get("status"), sp.getStatus());
                    predicatesList.add(statusPredicate);
                }
                query.orderBy(cb.asc(root.get("begin")), cb.asc(root.get("status")));
                Predicate[] predicates = new Predicate[predicatesList.size()];
                return cb.and(predicatesList.toArray(predicates));
            }
        };
        return meetingRepository.findAll(specification);
    }

    @Override
    public void exportMeetingRecord(List<Meeting> meetings, HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("会议记录");

        String fileName = "meetingRecord" + ".xls";//设置要导出的文件的名字
        //新增数据行，并且设置单元格数据

        int rowNum = 1;

        String[] headers = {"主题", "预定人", "会议室", "开始时间", "结束时间", "准备时间", "部门"};
        //headers表示excel表中第一行的表头

        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        Integer meetRoomId;
        Integer departId;
//        String joinPersonStr = "";
//        List<JoinPerson> joinPersons;
        Depart depart;
        //在表中存放查询到的数据放入对应的列
        for (Meeting meeting : meetings) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(meeting.getTopic());
            row1.createCell(1).setCellValue(meeting.getUserinfo().getName());
            meetRoomId = meeting.getMeetroomId();
            Meetroom meetroom = findByMeetRoomId(meetRoomId);
            row1.createCell(2).setCellValue(meetroom.getName());
            row1.createCell(3).setCellValue(meeting.getBegin());
            row1.createCell(4).setCellValue(meeting.getOver());
            row1.createCell(5).setCellValue(meeting.getPrepareTime());
            departId = meeting.getDepartId();
            if (departId != null) {
                depart = departService.findByDepartId(departId);
                row1.createCell(6).setCellValue(depart.getName());
            } else {
                row1.createCell(6).setCellValue("");
            }
//            joinPersons = joinPersonRepository.findByMeetingId(meeting.getId());
//            for (JoinPerson joinPerson : joinPersons) {
//                Userinfo userinfo = userinfoService.getUserinfo(joinPerson.getUserId());
//                joinPersonStr += userinfo.getName() + " ";
//            }
//            row1.createCell(7).setCellValue(joinPersonStr);
            rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }

    @Override
    public List countTimeByDepart(Integer tenantId, String begin, String over) {
        List<DepartTime> departTimes = new ArrayList<>();
        Depart depart;
        Meeting meeting;
        DepartTime departTime;
        int place = 0;
        int num = 0;
        List<Object> result = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.selectGroupByDepart(tenantId, begin, over);
        for (int i = 0; i < meetings.size(); i++) {
            meeting = meetings.get(i);
            Integer departId = meeting.getDepartId();
            depart = departService.findByDepartId(departId);
            departTime = new DepartTime();
            departTime.setDepartName(depart.getName());
            int time = meetingRepository.countTimeByDepart(departId, begin, over);
            departTime.setId(i + 1);
            departTime.setTime(time);
            if (time > num) {
                num = time;
                place = i;
            }
            departTimes.add(departTime);
        }
        result.add(departTimes);
        result.add(place);
        return result;
    }

    @Override
    public List countTimeByPeople(Integer tenantId, String begin, String over) {
        List<UserTime> userTimes = new ArrayList<>();
        Userinfo userinfo;
        Meeting meeting;
        UserTime userTime;
        int place = 0;
        int num = 0;
        List<Object> result = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.selectGroupByUser(tenantId, begin, over);
        for (int i = 0; i < meetings.size(); i++) {
            meeting = meetings.get(i);
            Integer userId = meeting.getUserId();
            userinfo = userinfoService.getUserinfo(userId);
            userTime = new UserTime();
            userTime.setUserName(userinfo.getName());
            int time = meetingRepository.countTimeByUser(userId, begin, over);
            userTime.setTime(time);
            userTime.setId(i + 1);
            if (time > num) {
                num = time;
                place = i;
            }
            userTimes.add(userTime);
        }
        result.add(userTimes);
        result.add(place);
        return result;
    }

    @Override
    public List countTimeByMeetRoom(Integer tenantId, String begin, String over) {
        List<MeetRoomTime> meetRoomTimes = new ArrayList<>();
        Meetroom meetroom;
        Meeting meeting;
        MeetRoomTime meetRoomTime;
        int place = 0;
        int num = 0;
        List<Object> result = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.selectGroupByMeetRoom(tenantId, begin, over);
        for (int i = 0; i < meetings.size(); i++) {
            meeting = meetings.get(i);
            Integer meetroomId = meeting.getMeetroomId();
            meetroom = findByMeetRoomId(meeting.getMeetroomId());
            meetRoomTime = new MeetRoomTime();
            meetRoomTime.setMeetRoomName(meetroom.getName());
            int time = meetingRepository.countTimeByMeetRoom(meetroomId, begin, over);
            meetRoomTime.setId(i + 1);
            meetRoomTime.setTime(time);
            if (time > num) {
                num = time;
                place = i;
            }
            meetRoomTimes.add(meetRoomTime);
        }
        result.add(meetRoomTimes);
        result.add(place);
        return result;
    }

    @Override
    public List countHourByDepart(Integer tenantId, String begin, String over) {
        List<DepartHour> departHours = new ArrayList<>();
        Depart depart;
        Meeting meeting;
        DepartHour departHour;
        int place = 0;
        double num = 0;
        List<Object> result = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.selectGroupByDepart(tenantId, begin, over);
        for (int i = 0; i < meetings.size(); i++) {
            meeting = meetings.get(i);
            Integer departId = meeting.getDepartId();
            depart = departService.findByDepartId(departId);
            departHour = new DepartHour();
            departHour.setDepartName(depart.getName());
            double hour = NumUtil.hold2((meetingRepository.countHourByDepart(departId, begin, over)) * 0.0166667);
            departHour.setHour(hour);
            departHour.setId(i + 1);
            if (hour > num) {
                num = hour;
                place = i;
            }
            departHours.add(departHour);
        }
        result.add(departHours);
        result.add(place);
        return result;
    }


    @Override
    public List countHourByPeople(Integer tenantId, String begin, String over) {
        List<UserHour> userHours = new ArrayList<>();
        Userinfo userinfo;
        Meeting meeting;
        UserHour userHour;
        int place = 0;
        double num = 0;
        List<Object> result = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.selectGroupByUser(tenantId, begin, over);
        for (int i = 0; i < meetings.size(); i++) {
            meeting = meetings.get(i);
            Integer userId = meeting.getUserId();
            userinfo = userinfoService.getUserinfo(userId);
            userHour = new UserHour();
            userHour.setUserName(userinfo.getName());
            double hour = NumUtil.hold2((meetingRepository.countHourByUser(userId, begin, over)) * 0.0166667);
            userHour.setHour(hour);
            userHour.setId(i + 1);
            if (hour > num) {
                num = hour;
                place = i;
            }
            userHours.add(userHour);
        }
        result.add(userHours);
        result.add(place);
        return result;
    }

    @Override
    public List countHourByMeetRoom(Integer tenantId, String begin, String over) {
        List<MeetRoomHour> meetRoomHours = new ArrayList<>();
        Meetroom meetroom;
        Meeting meeting;
        MeetRoomHour meetRoomHour;
        List<Meeting> meetings = meetingRepository.selectGroupByMeetRoom(tenantId, begin, over);
        int place = 0;
        double num = 0;
        for (int i = 0; i < meetings.size(); i++) {
            meeting = meetings.get(i);
            Integer meetroomId = meeting.getMeetroomId();
            meetroom = findByMeetRoomId(meeting.getMeetroomId());
            meetRoomHour = new MeetRoomHour();
            meetRoomHour.setMeetRoomName(meetroom.getName());
            double hour = NumUtil.hold2((meetingRepository.countHourByMeetRoom(meetroomId, begin, over)) * 0.0166667);
            if (hour > num) {
                num = hour;
                place = i;
            }
            meetRoomHour.setHour(hour);
            meetRoomHour.setId(i + 1);
            meetRoomHours.add(meetRoomHour);
        }
        List<Object> list = new ArrayList<>();
        list.add(meetRoomHours);
        list.add(place);
        return list;
    }


}
