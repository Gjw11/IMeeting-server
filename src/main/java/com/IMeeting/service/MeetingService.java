package com.IMeeting.service;


import com.IMeeting.entity.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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
    ServerResult OneReserveDetail(Integer meetingId);
    ServerResult OneDayMyReserve(String yearMonth,HttpServletRequest request);
    Meetroom finByMeetRoomId(Integer meetRoomId);
}
