package com.IMeeting.service;

import com.IMeeting.entity.ServerResult;

/**
 * Created by gjw on 2019/2/13.
 */
public interface JoinPersonService {
    ServerResult showOneMeeting(Integer meetingId);
    ServerResult remindOne(Integer meetingId,Integer userId);
}
