package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.*;
import com.IMeeting.service.MeetingService;
import com.IMeeting.service.UserinfoService;
import com.IMeeting.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private MeetroomRoleRepository meetroomRoleRepository;
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

    @Override
    public MeetroomParameter selectParameter(Integer tenantId) {
        MeetroomParameter meetroomParameter = meetroomParameterRepository.findByTenantId(tenantId);
        return meetroomParameter;
    }

    @Override
    public List<Meetroom> getEffectiveMeetroom(Integer tenantId, HttpServletRequest request) {
        Integer roleId = (Integer) request.getSession().getAttribute("roleId");
        Integer departId = (Integer) request.getSession().getAttribute("departId");
        List<Meetroom> meetrooms = new ArrayList<>();
        List<Meetroom> lists = meetroomRepository.findByTenantIdAndAvailStatus(tenantId, 1);
        for (int i = 0; i < lists.size(); i++) {
            int bol1 = 0, bol2 = 0;
            Integer meetroomId = lists.get(i).getId();
            List<MeetroomRole> meetroomRoles = meetroomRoleRepository.findByMeetroomId(meetroomId);
            if (meetroomRoles.size() != 0) {
                for (int j = 0; j < meetroomRoles.size(); j++) {
                    if (roleId.equals(meetroomRoles.get(j).getRoleId())) {
                        bol1 = 1;
                        break;
                    }
                }
            } else {
                bol1 = 1;
            }
            List<MeetroomDepart> meetroomDeparts = meetroomDepartRepository.findByMeetroomId(meetroomId);
            if (meetroomDeparts.size() == 0) {
                bol2 = 1;
            } else {
                for (int m = 0; m < meetroomDeparts.size(); m++) {
                    if (meetroomDeparts.get(m).getDepartId().equals(departId)) {
                        if (meetroomDeparts.get(m).getSatus().equals(1))
                            bol2 = 1;
                        else if (meetroomDeparts.get(m).getSatus().equals(0))
                            bol2 = 0;
                        break;
                    }
                }
            }
            if (bol1 == 1 && bol2 == 1) {
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
        List<Meetroom> meetrooms = getEffectiveMeetroom(tenantId, request);
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

    //输入参数为某一天，格式如2019-01-13，会议室编号，前端需提前判断改天是否是限定天数之内
    //输出结果为会议开始、结束时间、主题、预定人电话、预定人名字、预定人部门、会议创建时间、id用于抢会议、协调会议参数、实际按1、2、3显示
    @Override
    public ServerResult getOneRoomReserver(String reserverDate, Integer roomId) {
        ServerResult serverResult = new ServerResult();
        List<Meeting> meetings = meetingRepository.findByMeetroomIdAndMeetDateAndStatusOrderByBegin(roomId, reserverDate, 1);
        List<ReserverRecord> reserverRecords = new ArrayList<>();
        for (int j = 0; j < meetings.size(); j++) {
            ReserverRecord reserverRecord = new ReserverRecord();
            Meeting meeting = meetings.get(j);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            reserverRecord.setBegin(sdf.format(new Date(meeting.getBegin())));
            reserverRecord.setCreateTime(sdf.format(meeting.getCreateTime()));
            reserverRecord.setOver(sdf.format(new Date(meeting.getOver())));
            reserverRecord.setMeetDate(meeting.getMeetDate());
            reserverRecord.setTopic(meeting.getTopic());
            Userinfo userinfo = userinfoService.getUserinfo(meeting.getUserId());
            reserverRecord.setPeopleName(userinfo.getName());
            reserverRecord.setPhone(userinfo.getPhone());
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
            List<Meeting> meetings = meetingRepository.findByMeetroomIdAndMeetDateAndStatusOrderByBegin(oneDayReservation.getMeetRooms().get(i),
                    oneDayReservation.getDayReservation(), 1);
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
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        long begin = 0;
        long nowTime = 0;
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Userinfo userinfo = userinfoService.getUserinfo(userId);
        Integer tenantId = userinfo.getTenantId();
        MeetroomParameter meetroomParameter = meetroomParameterRepository.findByTenantId(tenantId);
        String beginTime = meetroomParameter.getBegin();
        String overTime = meetroomParameter.getOver();
        String reserveBeginTime = reserveParameter.getBeginTime();
        begin = (sdf.parse(reserveParameter.getReserveDate() + " " + reserveBeginTime)).getTime();
        nowTime = sdf.parse(sdf.format(new java.util.Date())).getTime();
        long over = begin + reserveParameter.getLastTime() * 60 * 1000;
        String reserveOverTime = sdf1.format(new Date(over));
        TimeUtil timeUtil = new TimeUtil();
        int bol1 = 2, bol2 = 2;
        bol1 = timeUtil.DateCompare(reserveBeginTime, beginTime, "HH:mm");
        bol2 = timeUtil.DateCompare(reserveOverTime, overTime, "HH:mm");
        if (bol1 == -1) {
            serverResult.setMessage("预定时间不能早于" + beginTime);
        } else if (bol2 == 1) {
            serverResult.setMessage("结束时间不能晚于" + overTime);
        } else if (begin < nowTime) {
            serverResult.setMessage("预定会议时间不能在当前时间之前");
        } else {
            List<Meeting> meetings = meetingRepository.findIntersectMeeting(begin, over);
            if (meetings.size() == 0) {
                Meeting meeting = new Meeting();
                meeting.setMeetDate(reserveParameter.getReserveDate());
                meeting.setBegin(begin);
                meeting.setContent(reserveParameter.getContent());
                meeting.setMeetroomId(reserveParameter.getMeetRoomId());
                meeting.setOver(over);
                meeting.setStatus(1);
                meeting.setTopic(reserveParameter.getTopic());
                meeting.setTenantId(tenantId);
                meeting.setUserId(userId);
                meeting.setMeetDate(reserveParameter.getReserveDate());
                meeting.setPrepareTime(reserveParameter.getPrepareTime());
                meeting.setCreateTime(nowTime);
                meetingRepository.saveAndFlush(meeting);
                Integer meetringId = meeting.getId();
                List<Integer> list = reserveParameter.getJoinPeopleId();
                for (int i = 0; i < list.size(); i++) {
                    JoinPerson joinPerson = new JoinPerson();
                    joinPerson.setMeetingId(meetringId);
                    joinPerson.setUserId(list.get(i));
                    joinPersonRepository.saveAndFlush(joinPerson);
                }
                List<OutsideJoinPerson> outsideJoinPersons = reserveParameter.getOutsideJoinPersons();
                for (int i = 0; i < outsideJoinPersons.size(); i++) {
                    OutsideJoinPerson outsideJoinPerson = new OutsideJoinPerson();
                    outsideJoinPerson.setName(outsideJoinPersons.get(i).getName());
                    outsideJoinPerson.setPhone(outsideJoinPersons.get(i).getPhone());
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
        Meeting meeting = new Meeting();
        meeting.setMeetDate(reserveParameter.getReserveDate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long begin = 0;
        try {
            begin = (sdf.parse(reserveParameter.getReserveDate() + " " + reserveParameter.getBeginTime())).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Userinfo userinfo = userinfoService.getUserinfo(userId);
        meeting.setTenantId(userinfo.getTenantId());
        meeting.setBegin(begin);
        meeting.setTopic(reserveParameter.getTopic());
        meeting.setContent(reserveParameter.getContent());
        meeting.setMeetroomId(reserveParameter.getMeetRoomId());
        meeting.setOver(begin + reserveParameter.getLastTime() * 60 * 1000);
        meeting.setStatus(2);
        meeting.setUserId((Integer) request.getSession().getAttribute("userId"));
        meeting.setMeetDate(reserveParameter.getReserveDate());
        meeting.setPrepareTime(reserveParameter.getPrepareTime());
        try {
            meeting.setCreateTime(sdf.parse(sdf.format(new java.util.Date())).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        meetingRepository.saveAndFlush(meeting);
        Integer meetringId = meeting.getId();
        List<Integer> list = reserveParameter.getJoinPeopleId();
        for (int i = 0; i < list.size(); i++) {
            JoinPerson joinPerson = new JoinPerson();
            joinPerson.setMeetingId(meetringId);
            joinPerson.setUserId(list.get(i));
            joinPersonRepository.saveAndFlush(joinPerson);
        }
        List<OutsideJoinPerson> outsideJoinPersons = reserveParameter.getOutsideJoinPersons();
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

    //传入参数除和预定会议一样，还包括调用原因(可无)，原来会议的id
    @Override
    public ServerResult coordinateMeeting(CoordinateParameter coordinateParameter, HttpServletRequest request) {
        Meeting meeting = new Meeting();
        meeting.setMeetDate(coordinateParameter.getReserveDate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long begin = 0;
        try {
            begin = (sdf.parse(coordinateParameter.getReserveDate() + " " + coordinateParameter.getBeginTime())).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Userinfo userinfo = userinfoService.getUserinfo(userId);
        meeting.setTenantId(userinfo.getTenantId());
        meeting.setBegin(begin);
        meeting.setTopic(coordinateParameter.getTopic());
        meeting.setContent(coordinateParameter.getContent());
        meeting.setMeetroomId(coordinateParameter.getMeetRoomId());
        meeting.setOver(begin + coordinateParameter.getLastTime() * 60 * 1000);
        meeting.setStatus(5);
        meeting.setUserId((Integer) request.getSession().getAttribute("userId"));
        meeting.setMeetDate(coordinateParameter.getReserveDate());
        meeting.setPrepareTime(coordinateParameter.getPrepareTime());
        try {
            meeting.setCreateTime(sdf.parse(sdf.format(new java.util.Date())).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Meeting m = meetingRepository.saveAndFlush(meeting);
        Integer meetringId = meeting.getId();
        List<Integer> list = coordinateParameter.getJoinPeopleId();
        for (int i = 0; i < list.size(); i++) {
            JoinPerson joinPerson = new JoinPerson();
            joinPerson.setMeetingId(meetringId);
            joinPerson.setUserId(list.get(i));
            joinPersonRepository.saveAndFlush(joinPerson);
        }
        CoordinateInfo coordinateInfo = new CoordinateInfo();
        coordinateInfo.setNote(coordinateParameter.getNote());
        coordinateInfo.setMeetingId(m.getId());
        coordinateInfo.setBeforeMeetingId(coordinateParameter.getBeforeMeetingId());
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
    public ServerResult cancelMeeting(Integer meentingId) {
        meetingRepository.updateStatus(meentingId, 5);
        List<CoordinateInfo> coordinateInfos = coordinateInfoRepository.findByBeforeMeetingIdAndStatus(meentingId, 1);
        if (coordinateInfos.size() == 0) {
            Meeting meeting = findByMeetingId(meentingId);
            List<Meeting> meetings = meetingRepository.findByBeginAndOverAndMeetroomIdOrderByCreateTimeAsc(meeting.getBegin(), meeting.getOver(), meeting.getMeetroomId());
            Meeting meeting1 = meetings.get(0);
            meetingRepository.updateStatus(meeting1.getId(), 1);
        }
        ServerResult serverResult = new ServerResult();
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
            reserverRecord.setBegin(sdf.format(new java.util.Date(meeting.getBegin())));
            reserverRecord.setOver(sdf.format(new java.util.Date(meeting.getOver())));
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

    //显示一个我预定的会议室的细节
    @Override
    public ServerResult OneReserveDetail(Integer meetingId) {
        Meeting meeting = findByMeetingId(meetingId);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        ReserveParameter reserveParameter = new ReserveParameter();
        reserveParameter.setTopic(meeting.getTopic());
        reserveParameter.setContent(meeting.getContent());
        reserveParameter.setMeetRoomId(meeting.getMeetroomId());
        Meetroom meetroom = finByMeetRoomId(meeting.getMeetroomId());
        if (meetroom!=null)
            reserveParameter.setMeetroom(meetroom.getName());
        long beginTime=meeting.getBegin();
        long overTime=meeting.getOver();
        reserveParameter.setReserveDate(meeting.getMeetDate());
        reserveParameter.setBeginTime(sdf.format(new java.util.Date(beginTime)));
        reserveParameter.setLastTime((int) ((overTime-beginTime)/1000));
        reserveParameter.setOverTime(sdf.format(new java.util.Date(overTime)));
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
        }
        reserveParameter.setStatus(status);
        List<OutsideJoinPerson>outsideJoinPersons=outsideJoinPersonRepository.findByMeetingId(meetingId);
        reserveParameter.setOutsideJoinPersons(outsideJoinPersons);
        List<JoinPerson>joinPersons=joinPersonRepository.findByMeetingId(meetingId);
        List<Integer>userIds=new ArrayList<>();
        for (int i=0;i<joinPersons.size();i++){
            userIds.add(joinPersons.get(i).getUserId());
        }
        reserveParameter.setJoinPeopleId(userIds);
        List<CoordinateInfo> coordinateInfos=coordinateInfoRepository.findByBeforeMeetingIdAndStatus(meetingId,0);
        List<CoordinateResult>coordinateResults=new ArrayList<>();
        CoordinateResult coordinateResult;
        for (int i=0;i<coordinateInfos.size();i++){
            coordinateResult=new CoordinateResult();
            Meeting meeting1=findByMeetingId(coordinateInfos.get(i).getMeetingId());
            coordinateResult.setBeginTime(sdf.format(new java.util.Date(meeting1.getBegin())));
            coordinateResult.setOverTime(sdf.format(new java.util.Date(meeting1.getOver())));
            coordinateResult.setNote(coordinateInfos.get(i).getNote());
            Userinfo userinfo=userinfoService.getUserinfo(meeting1.getUserId());
            coordinateResult.setPeopleName(userinfo.getName());
            coordinateResult.setPeoplePhone(userinfo.getPhone());
            coordinateResult.setCoordinateId(coordinateInfos.get(i).getId());
            coordinateResults.add(coordinateResult);
        }
        ServerResult serverResult=new ServerResult();
        List<Object>result=new ArrayList<>();
        result.add(reserveParameter);
        result.add(coordinateResults);
        serverResult.setData(result);
        serverResult.setStatus(true);
        return serverResult;
    }

    //显示某一天我的预定记录,格式如2019-01-20
    @Override
    public ServerResult OneDayMyReserve(String reserveDate, HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Meeting> todayMeeting = meetingRepository.findMyReserve(userId, reserveDate);
        List<ReserverRecord> oneDayMeetingResult = new ArrayList<>();
        ReserverRecord reserverRecord;
        for (int i = 0; i < todayMeeting.size(); i++) {
            Meeting meeting = todayMeeting.get(i);
            reserverRecord = new ReserverRecord();
            reserverRecord.setId(meeting.getId());
            reserverRecord.setBegin(sdf.format(new java.util.Date(meeting.getBegin())));
            reserverRecord.setOver(sdf.format(new java.util.Date(meeting.getOver())));
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
    public Meetroom finByMeetRoomId(Integer meetRoomId) {
        Optional<Meetroom> meetroom = meetroomRepository.findById(meetRoomId);
        if (meetroom.isPresent())
            return meetroom.get();
        return null;
    }

//    @Override
//    public List<Meeting> selectBydate(Date date, Integer meetroomId) {
//        List<Meeting> resultList = null;
//        Specification<Meeting> querySpecifi = new Specification<Meeting>() {
//            @Override
//            public Predicate toPredicate(Root<Meeting> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
//                List<Predicate> predicates = new ArrayList<>();
//                if (StringUtils.(meetroomId)) {
//                    //大于或等于传入时间
//                    predicates.add(cb.greaterThanOrEqualTo(root.get("commitTime").as(String.class), stime));
//                }
//                if (StringUtils.isNotBlank(etime)) {
//                    //小于或等于传入时间
//                    predicates.add(cb.lessThanOrEqualTo(root.get("commitTime").as(String.class), etime));
//                }
//                if (StringUtils.isNotBlank(serach)) {
//                    //模糊查找
//                    predicates.add(cb.like(root.get("name").as(String.class), "%" + serach + "%"));
//                }
//                // and到一起的话所有条件就是且关系，or就是或关系
//                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        };
//        resultList = this.meetingRepository.findAll(querySpecifi);
//        return resultList;
//    }
}
