package com.IMeeting.controller;

import com.IMeeting.entity.Meeting;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.resposirity.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gjw on 2019/2/13.
 */
@RestController
@RequestMapping("/joinPerson")
public class JoinPersonController {
    @Autowired
    private MeetingRepository meetingRepository;
    @RequestMapping("/toJoinPersonIndex")
    public ServerResult toJoinPersonIndex(HttpServletRequest request){
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        List<Meeting>meetings=meetingRepository.selectByUserIdAndStatusJoin(userId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(meetings);
        serverResult.setStatus(true);
        return serverResult;
    }

}
