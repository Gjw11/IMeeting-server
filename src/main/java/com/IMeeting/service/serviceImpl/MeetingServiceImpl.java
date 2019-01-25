package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.*;
import com.IMeeting.service.MeetingService;
import com.IMeeting.service.UserinfoService;
import com.IMeeting.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
            reserverRecord.setBegin(meeting.getBegin());
            reserverRecord.setCreateTime(meeting.getCreateTime());
            reserverRecord.setOver(meeting.getOver());
            reserverRecord.setMeetDate(meeting.getMeetDate());
            reserverRecord.setTopic(meeting.getTopic());
            reserverRecord.setLastTime(meeting.getLastTime());
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
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        MeetroomParameter meetroomParameter = meetroomParameterRepository.findByTenantId(tenantId);
        String beginTime = meetroomParameter.getBegin();
        String overTime = meetroomParameter.getOver();
        int lastTime=reserveParameter.getLastTime();
        int prepareTime=reserveParameter.getPrepareTime();
        String reserveBeginTime = reserveParameter.getBeginTime();
        String reserveDate = reserveParameter.getReserveDate();
        String afterBeginTime = reserveDate+" "+reserveParameter.getBeginTime();
        String afterOverTime=TimeUtil.addMinute(afterBeginTime,lastTime);
        String nowTime = sdf.format(new java.util.Date());
        TimeUtil timeUtil = new TimeUtil();
        int bol1 = 2, bol2 = 2, bol3 = 2, bol4 = 2;
        bol1 = timeUtil.DateCompare(reserveBeginTime, beginTime, "HH:mm");
        bol2 = timeUtil.DateCompare(afterOverTime.substring(11,16), overTime, "HH:mm");
        bol3 = timeUtil.DateCompare(reserveBeginTime, afterOverTime.substring(11,16), "HH:mm");
        bol4 = timeUtil.DateCompare(afterBeginTime, nowTime, "yyyy-MM-dd HH:mm");
        if (prepareTime>lastTime){
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
            List<Meeting> meetings = meetingRepository.findIntersectMeeting(afterBeginTime,afterOverTime);
            if (meetings.size() == 0) {
                Meeting meeting = new Meeting();
                meeting.setMeetDate(reserveParameter.getReserveDate());
                meeting.setBegin(afterBeginTime);
                meeting.setContent(reserveParameter.getContent());
                meeting.setMeetroomId(reserveParameter.getMeetRoomId());
                meeting.setOver(afterOverTime);
                meeting.setStatus(1);
                meeting.setLastTime(lastTime);
                meeting.setTopic(reserveParameter.getTopic());
                meeting.setTenantId(tenantId);
                meeting.setUserId(userId);
                meeting.setMeetDate(reserveDate);
                meeting.setPrepareTime(prepareTime);
                meeting.setCreateTime(nowTime);
                meetingRepository.saveAndFlush(meeting);
                Integer meetingId = meeting.getId();
                List<Integer> list = reserveParameter.getJoinPeopleId();
                for (int i = 0; i < list.size(); i++) {
                    JoinPerson joinPerson = new JoinPerson();
                    joinPerson.setMeetingId(meetingId);
                    joinPerson.setUserId(list.get(i));
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
        int lastTime=reserveParameter.getLastTime();
        String afterBeginTime=reserveDate+" "+reserveBeginTime;
        String afterOverTime = TimeUtil.addMinute(afterBeginTime,lastTime);
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
        meeting.setLastTime(lastTime);
        meeting.setMeetDate(reserveDate);
        meeting.setPrepareTime(reserveParameter.getPrepareTime());
        meeting.setCreateTime(nowTime);
        meetingRepository.saveAndFlush(meeting);
        Integer meetingId = meeting.getId();
        List<Integer> list = reserveParameter.getJoinPeopleId();
        for (int i = 0; i < list.size(); i++) {
            JoinPerson joinPerson = new JoinPerson();
            joinPerson.setMeetingId(meetingId);
            joinPerson.setUserId(list.get(i));
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
        Integer tenantId=(Integer) request.getSession().getAttribute("tenantId");
//        String reserveBeginTime = coordinateParameter.getBeginTime();
//        String afterBeginTime=reserveDate+" "+reserveBeginTime;
        int lastTime=coordinateParameter.getLastTime();
//        String afterOverTime = TimeUtil.addMinute(,lastTime);
        Integer beforeMeetingId=coordinateParameter.getBeforeMeetingId();
        Meeting meeting1=findByMeetingId(beforeMeetingId);
        int bol=coordinateParameter.getBeforeOrLast();
        if (bol==1){
            String begin=meeting1.getBegin();
            meeting.setBegin(begin);
            meeting.setOver(TimeUtil.addMinute(begin,lastTime));
        }else if(bol==2){
            String over=meeting1.getOver();
            meeting.setOver(over);
            meeting.setBegin(TimeUtil.addMinute(over,-lastTime));
        }
        meeting.setTenantId(tenantId);
        meeting.setTopic(coordinateParameter.getTopic());
        meeting.setContent(coordinateParameter.getContent());
        meeting.setMeetroomId(meeting1.getMeetroomId());
        meeting.setLastTime(lastTime);
        meeting.setStatus(2);
        meeting.setUserId(userId);
        meeting.setMeetDate(meeting1.getMeetDate());
        meeting.setPrepareTime(coordinateParameter.getPrepareTime());
        meeting.setCreateTime(sdf.format(new java.util.Date()));
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
    public ServerResult oneReserveDetail(Integer meetingId) {
        Meeting meeting = findByMeetingId(meetingId);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        ReserveParameter reserveParameter = new ReserveParameter();
        reserveParameter.setTopic(meeting.getTopic());
        reserveParameter.setContent(meeting.getContent());
        reserveParameter.setMeetRoomId(meeting.getMeetroomId());
        Meetroom meetroom = finByMeetRoomId(meeting.getMeetroomId());
        if (meetroom != null)
            reserveParameter.setMeetroom(meetroom.getName());
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
            coordinateResult.setBeginTime(sdf.format(meeting1.getBegin()));
            coordinateResult.setOverTime(sdf.format(meeting1.getOver()));
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
            reserverRecord.setBegin(sdf.format((meeting.getBegin())));
            reserverRecord.setOver(sdf.format((meeting.getOver())));
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

    //拒绝调用会议
    @Override
    public ServerResult disagreeCoordinate(Integer coordinateId) {
        CoordinateInfo coordinateInfo = findByCoordinateId(coordinateId);
        int bol1 = 0, bol2 = 0;
        if (coordinateInfo != null) {
            bol1 = coordinateInfoRepository.updateCoordinateStatus(coordinateId, 2);
            bol2 = meetingRepository.updateStatus(coordinateInfo.getMeetingId(), 0);
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
    public ServerResult twoEditMyServer(ReserveParameter reserveParameter) {
        Integer meetingId = reserveParameter.getMeetingId();
        meetingRepository.updateTCP(meetingId, reserveParameter.getTopic(), reserveParameter.getContent(), reserveParameter.getPrepareTime());
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
        List<Integer> joinPersonId = reserveParameter.getJoinPeopleId();
        JoinPerson joinPerson;
        for (int i = 0; i < joinPersonId.size(); i++) {
            joinPerson = new JoinPerson();
            joinPerson.setMeetingId(meetingId);
            joinPerson.setUserId(joinPersonId.get(i));
            joinPersonRepository.saveAndFlush(joinPerson);
        }
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        serverResult.setMessage("预定信息修改成功");
        return serverResult;
    }

    //提前结束会议
    @Override
    public ServerResult advanceOver(Integer meetingId)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime = sdf.format(new java.util.Date());
        int bol = meetingRepository.advanceOver(meetingId, nowTime, 4);
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
        List<Meeting> meetings = meetingRepository.selectMyJoinMeeting(userId, yearMonth);
        ServerResult serverResult = new ServerResult();
        serverResult.setData(meetings);
        serverResult.setStatus(true);
        return serverResult;
    }

}
