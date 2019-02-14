package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.JoinPersonRepository;
import com.IMeeting.resposirity.PushMessageRepository;
import com.IMeeting.service.JoinPersonService;
import com.IMeeting.service.UserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gjw on 2019/2/13.
 */
@Service
public class JoinPersonServiceImpl implements JoinPersonService {
    @Autowired
    private JoinPersonRepository joinPersonRepository;
    @Autowired
    private UserinfoService userinfoService;
    @Autowired
    private PushMessageRepository pushMessageRepository;

    @Override
    public ServerResult showOneMeeting(Integer meetingId) {
        List<JoinPerson> joinPersonList = joinPersonRepository.findByMeetingIdOrderByStatus(meetingId);
        JoinPersonInfo joinPersonInfo;
        List<JoinPersonInfo> joinPersonInfos = new ArrayList<>();
        for (JoinPerson joinPerson : joinPersonList) {
            joinPersonInfo = new JoinPersonInfo();
            joinPersonInfo.setRecordId(joinPerson.getId());
            joinPersonInfo.setSignTime(joinPerson.getSignTime());
            String status = null;
            switch (joinPerson.getStatus()) {
                case 0:
                    status = "未签到";
                    break;
                case 1:
                    status = "已签到";
                    break;
                case 2:
                    status = "请假中";
                    break;
                case 3:
                    status = "同意请假";
                    break;
                case 4:
                    status = "拒绝请假";
                    break;
            }
            joinPersonInfo.setStatus(status);
            if (joinPerson.getUserId() != null) {
                Integer userId = joinPerson.getUserId();
                joinPersonInfo.setUserId(userId);
                Userinfo userinfo = userinfoService.getUserinfo(joinPerson.getUserId());
                if (userinfo != null) {
                    joinPersonInfo.setUserName(userinfo.getName());
                    joinPersonInfo.setUserPhone(userinfo.getPhone());
                }
            }
            joinPersonInfos.add(joinPersonInfo);
        }
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        serverResult.setData(joinPersonInfos);
        return serverResult;
    }

    @Override
    public ServerResult remindOne(Integer meetingId, Integer userId) {
        PushMessage pushMessage=new PushMessage();
        pushMessage.setMeetingId(meetingId);
        pushMessage.setStatus(0);
        pushMessage.setReceiveId(userId);
        pushMessage.setMessage("您有一场未签到的会议，请及时签到");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime=sdf.format(new Date());
        pushMessage.setTime(nowTime);
        pushMessageRepository.saveAndFlush(pushMessage);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
}
