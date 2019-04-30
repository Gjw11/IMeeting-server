package com.IMeeting.controller;

import com.IMeeting.dao.OpenApplyDao;
import com.IMeeting.entity.OpenApply;
import com.IMeeting.entity.ServerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gjw on 2019/4/29.
 */
@RestController
@RequestMapping("/openApply")
public class OpenApplyController {
    @Autowired
    private OpenApplyDao openApplyDao;
    //用户端提交开门申请
    @RequestMapping("/insertOne")
    public ServerResult insertOne(@RequestBody OpenApply openApply, HttpServletRequest request){
        Integer useId= (Integer) request.getSession().getAttribute("userId");
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime=sdf.format(new Date());
        openApply.setUserId(useId);
        openApply.setTenantId(tenantId);
        openApply.setCreateTime(nowTime);
        openApply.setStatus(0);
        openApplyDao.save(openApply);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
    //@Re
}
