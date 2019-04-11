package com.IMeeting.service;

import com.IMeeting.entity.Meetroom;
import com.IMeeting.entity.MeetroomPara;
import com.IMeeting.entity.ServerResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gjw on 2019/2/4.
 */
public interface MeetRoomService {
    List selectAll(HttpServletRequest request);
    Meetroom getMeetRoom(Integer meetRoomId);
    ServerResult showOne(Integer meetRoomId,HttpServletRequest request);
    ServerResult editOne(MeetroomPara meetroomPara, HttpServletRequest request);
    ServerResult insertOne(MeetroomPara meetroomPara, HttpServletRequest request);
}
