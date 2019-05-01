package com.IMeeting.controller;

import com.IMeeting.dao.OpenApplyDao;
import com.IMeeting.entity.OpenApply;
import com.IMeeting.entity.ServerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    //显示用户端查看自己提交的开门申请
    @RequestMapping("/userShow")
    public ServerResult userShow( HttpServletRequest request){
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        List<OpenApply>openApplies=openApplyDao.findEqualField("userId",userId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(openApplies);
        serverResult.setStatus(true);
        return serverResult;
    }
    //用户端取消门禁权限申请
    @RequestMapping("/cancelOne")
    public ServerResult cancelOne(@RequestParam("id")Integer id){
        int bol = openApplyDao.executeSql("update m_open_apply m set m.status=3 where m.id=?", id);
        ServerResult serverResult=new ServerResult();
        if (bol!=0){
            serverResult.setCode(1);
            serverResult.setMessage("取消成功");
        }else{
            serverResult.setCode(0);
            serverResult.setMessage("取消失败");
        }
       serverResult.setStatus(true);
        return serverResult;
    }
    //管理端查看用户端提交的门禁权限申请
    @RequestMapping("/manageShow")
    public ServerResult manageShow( HttpServletRequest request){
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        List<OpenApply>openApplies=openApplyDao.findEqualField("tenantId",tenantId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(openApplies);
        serverResult.setStatus(true);
        return serverResult;
    }
    //管理端审核通过用户端提交的门禁权限申请
    @RequestMapping("/agreeOne")
    public ServerResult agreeOne( @RequestParam("id")Integer id){
        int bol = openApplyDao.executeSql("update m_open_apply m set m.status=1 where m.id=?", id);
        ServerResult serverResult=new ServerResult();
        if (bol!=0){
            serverResult.setCode(1);
            serverResult.setMessage("审核成功");
        }else{
            serverResult.setCode(0);
            serverResult.setMessage("审核失败");
        }
        serverResult.setStatus(true);
        return serverResult;
    }
    //管理端审核不通过用户端提交的门禁权限申请
    @RequestMapping("/disagreeOne")
    public ServerResult disagreeOne( @RequestParam("id")Integer id){
        int bol = openApplyDao.executeSql("update m_open_apply m set m.status=2 where m.id=?", id);
        ServerResult serverResult=new ServerResult();
        if (bol!=0){
            serverResult.setCode(1);
            serverResult.setMessage("审核成功");
        }else{
            serverResult.setCode(0);
            serverResult.setMessage("审核失败");
        }
        serverResult.setStatus(true);
        return serverResult;
    }
    //管理端添加门禁权限
    @RequestMapping("/insertByManage")
    public ServerResult insertByManage(@RequestBody OpenApply openApply, HttpServletRequest request){
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime=sdf.format(new Date());
        openApply.setTenantId(tenantId);
        openApply.setCreateTime(nowTime);
        openApply.setStatus(1);
        openApplyDao.save(openApply);
        ServerResult serverResult=new ServerResult();
        serverResult.setMessage("添加成功");
        serverResult.setStatus(true);
        return serverResult;
    }
}
