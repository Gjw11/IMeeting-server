package com.IMeeting.controller;

import com.IMeeting.entity.AbnormalInfo;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.resposirity.AbnormalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gjw on 2019/5/18.
 */
@RestController
@RequestMapping("/abnormal")
public class AbnormalController {
    @Autowired
    private AbnormalRepository abnormalRepository;
    //查找登录用户所有的异常人员进入信息
    @RequestMapping("/selctMyAbnormal")
    public ServerResult selectMyAbnormal(HttpServletRequest request){
        int userId= (int) request.getSession().getAttribute("userId");
        List<AbnormalInfo> abnormalInfoList=abnormalRepository.selectMyAbnormal(userId);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        serverResult.setData(abnormalInfoList);
        return serverResult;
    }
    //状态变更 未读改为已读
    @RequestMapping("/isRead")
    public ServerResult isRead(@RequestParam("id") int id){
        int bol=abnormalRepository.isRead(id);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        if (bol!=0)
            serverResult.setCode(1);
        else
            serverResult.setCode(-1);
        return serverResult;
    }
}
