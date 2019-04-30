package com.IMeeting.controller;

import com.IMeeting.dao.WeekMeetingDao;
import com.IMeeting.entity.Meeting;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.entity.WeekMeeting;
import com.IMeeting.resposirity.MeetingRepository;
import com.IMeeting.util.TimeUtil;
import com.IMeeting.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gjw on 2019/4/15.
 */
@RestController
@RequestMapping("/weekMeeting")
public class WeekMeetingController {
    @Autowired
    private WeekMeetingDao weekMeetingDao;
    @Autowired
    private MeetingRepository meetingRepository;

    //用户端申请每周例会
    @RequestMapping("/insertOne")
    public ServerResult insertOne(@RequestBody WeekMeeting weekMeeting, HttpServletRequest request) {
        List<String> days = WebUtils.getDayOfWeekWithinDateInterval(weekMeeting.getBeginTime(), weekMeeting.getOverTime(), weekMeeting.getWeek());
        String beginTime = weekMeeting.getBeginTime();
        String overTime = weekMeeting.getOverTime();
        Integer meetroomId = weekMeeting.getMeetRoomId();
        int bol = 0;
        for (int i = 0; i < days.size(); i++) {
            String date = days.get(i);
            List<Meeting> meetings = meetingRepository.findIntersectMeeting(date + " " + beginTime, date + " " + overTime, meetroomId);
            if (meetings.size() != 0) {
                bol = 1;
                break;
            }
        }
        ServerResult serverResult = new ServerResult();
        if (bol == 1) {
            serverResult.setMessage("对不起！您选择时间段有会议冲突，无法申请！");
            serverResult.setCode(0);
        } else {
            Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            Integer departId = (Integer) request.getSession().getAttribute("departId");
            weekMeeting.setTenantId(tenantId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = sdf.format(new Date());
            weekMeeting.setUserId(userId);
            weekMeeting.setStatus(0);
            weekMeeting.setDepartId(departId);
            weekMeeting.setCreateTime(nowTime);
            weekMeetingDao.save(weekMeeting);
            serverResult.setMessage("申请每周例会成功，等待审核");
            serverResult.setCode(1);

        }
        serverResult.setStatus(true);
        return serverResult;
    }

    //管理端拒绝每周例会申请
    @RequestMapping("/disagreeOne")
    public ServerResult disagreeOne(@RequestParam("id") Integer id) {
        int bol = weekMeetingDao.executeSql("update m_week_meeting m set m.status=2 where m.id=?", id);
        ServerResult serverResult = new ServerResult();
        if (bol != 0) {
            serverResult.setStatus(true);
            serverResult.setMessage("审批成功");
            serverResult.setCode(1);
        }
        return serverResult;
    }

    //管理端同意每周例会申请
    @RequestMapping("/agreeOne")
    public ServerResult agreeOne(@RequestParam("id") Integer id) throws ParseException {
        WeekMeeting weekMeeting = weekMeetingDao.findOne(id);
        List<String> days = WebUtils.getDayOfWeekWithinDateInterval(weekMeeting.getBeginTime(), weekMeeting.getOverTime(), weekMeeting.getWeek());
        String beginTime = weekMeeting.getBeginTime();
        String overTime = weekMeeting.getOverTime();
        Integer meetroomId = weekMeeting.getMeetRoomId();
        int bol = 0;
        for (int i = 0; i < days.size(); i++) {
            String date = days.get(i);
            List<Meeting> meetings = meetingRepository.findIntersectMeeting(date + " " + beginTime, date + " " + overTime, meetroomId);
            if (meetings.size() != 0) {
                bol = 1;
                break;
            }
        }
        ServerResult serverResult = new ServerResult();
        if (bol == 1) {
            serverResult.setMessage("您审批的每周会议与已被预定会议有冲突，审批无法通过");
            serverResult.setCode(-1);
        } else {
            int bol1 = weekMeetingDao.executeSql("update m_week_meeting m set m.status=1 where m.id=?", id);
//            int bol2=0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = sdf.format(new Date());
            Integer departId = weekMeeting.getDepartId();
            for (int j = 0; j < days.size(); j++) {
                Meeting meeting = new Meeting();
                meeting.setCreateTime(nowTime);
                meeting.setStatus(1);
                meeting.setDepartId(departId);
                meeting.setMeetroomId(weekMeeting.getMeetRoomId());
                meeting.setUserId(weekMeeting.getUserId());
                meeting.setBegin(weekMeeting.getBeginTime());
                meeting.setOver(weekMeeting.getOverTime());
                meeting.setLastTime((int) TimeUtil.reduceMinute(weekMeeting.getBeginTime(), weekMeeting.getOverTime()));
                meeting.setMeetDate(days.get(j));
                meetingRepository.saveAndFlush(meeting);
            }
            if (bol1 != 0) {
                serverResult.setCode(1);
                serverResult.setMessage("审批成功");
            } else {
                serverResult.setCode(2);
            }
        }
        serverResult.setStatus(true);
        return serverResult;
    }

    //管理端查看每周例会申请
    @RequestMapping("/manageFindAll")
    public ServerResult manageFindAll(HttpServletRequest request) {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        List<WeekMeeting> weekMeetings = weekMeetingDao.findEqualField("tenantId", tenantId);
        ServerResult serverResult = new ServerResult();
        serverResult.setData(weekMeetings);
        return serverResult;
    }

    //用户端查看每周例会申请
    @RequestMapping("/userFindAll")
    public ServerResult userFindAll(HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        List<WeekMeeting> weekMeetings = weekMeetingDao.findEqualField("user_id", userId);
        ServerResult serverResult = new ServerResult();
        serverResult.setData(weekMeetings);
        return serverResult;
    }

    //用户端取消每周例会申请
    @RequestMapping("/cancelOne")
    public ServerResult cancelOne(@RequestParam("id") Integer id) {
        int bol = weekMeetingDao.executeSql("update m_week_meeting m set m.status=3 where m.id=?", id);
        ServerResult serverResul = new ServerResult();
        if (bol != 0) {
            serverResul.setCode(1);
        } else {
            serverResul.setCode(0);
        }
        serverResul.setStatus(true);
        return serverResul;

    }
}
