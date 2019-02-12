package com.IMeeting.service;


import com.IMeeting.entity.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;

/**
 * Created by gjw on 2018/12/12.
 */
public interface MeetingService {
    MeetroomParameter selectParameter(Integer tenantId);
    List<Meetroom> getEffectiveMeetroom(Integer tenantId,HttpServletRequest request);
    List<Equip> selectEquips(Integer tenantId);
    List<MeetroomEquip>selectOneMeetroomEquip(Integer meetroomId);
    ServerResult toReserveIndex(HttpServletRequest request);
    ServerResult getOneRoomReserver(String reserverDate,Integer roomId);
    ServerResult getOneDayReserve(OneDayReservation oneDayReservation);
    ServerResult reserveMeeting(ReserveParameter reserveParameter,HttpServletRequest request) throws Exception;
    ServerResult robMeeting(ReserveParameter reserveParameter,HttpServletRequest request);
    ServerResult coordinateMeeting(CoordinateParameter coordinateParameter,HttpServletRequest request);
    ServerResult cancelMeeting(Integer meetingId);
    Meeting findByMeetingId(Integer meetingId);
    ServerResult showMyReserve(HttpServletRequest request);
    ServerResult specifiedMyReserve(HttpServletRequest request,String yearMonth);
    ServerResult oneReserveDetail(Integer meetingId);
    ServerResult oneDayMyReserve(String yearMonth,HttpServletRequest request);
    Meetroom findByMeetRoomId(Integer meetRoomId);
    ServerResult disagreeCoordinate(Integer coordinateId);
    ServerResult agreeCoordinate(Integer coordinateId);
    CoordinateInfo findByCoordinateId(Integer coordinateId);
    ServerResult oneEditMyServer(ReserveParameter reserveParameter,HttpServletRequest request) throws Exception;
    ServerResult twoEditMyServer(ReserveParameter reserveParameter,HttpServletRequest request);
    ServerResult advanceOver(Integer meetingId);
    ServerResult selectMyJoinMeeting(HttpServletRequest request,String yearMonth);
    void updateMeetingStatus(String nowTime,Integer beforeStatus,Integer afterStatus);
    void updateMeetingOverStatus(String nowTime,Integer beforeStatus,Integer afterStatus);
    ServerResult selectMyJoinMeetingByDate(String meetDate,HttpServletRequest request);
    ServerResult sendLeaveInformation(LeaveInformation leaveInformation,HttpServletRequest request);
    ServerResult countLeaveInformation(HttpServletRequest request);
    ServerResult showOneMeetingLeaveInfo(Integer meetingId);
    ServerResult findPushMessage(HttpServletRequest request);
    LeaveInformation findById(Integer id);
    /*-------------华丽分割线-------------*/
    List findBySpecification(SelectMeetingParameter selectMeetingParameter,HttpServletRequest request);
    void exportMeetingRecord(List<Meeting> meetings,HttpServletResponse response) throws IOException;
    List countTimeByDepart(Integer tenantId,String begin,String over);
    List countTimeByPeople(Integer tenantId,String begin,String over);
    List countTimeByMeetRoom(Integer tenantId,String begin,String over);
    List countHourByDepart(Integer tenantId,String begin,String over);
    List countHourByPeople(Integer tenantId,String begin,String over);
    List countHourByMeetRoom(Integer tenantId,String begin,String over);

}
