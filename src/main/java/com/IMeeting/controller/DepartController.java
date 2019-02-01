package com.IMeeting.controller;

import com.IMeeting.entity.Depart;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.resposirity.DepartRepository;
import com.IMeeting.service.DepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gjw on 2019/2/1.
 */
@RestController
@RequestMapping("/depart")
public class DepartController {
    @Autowired
    private DepartService departService;
    @Autowired
    private DepartRepository departRepository;
    //查询该租户所有的部门
    @RequestMapping("/selectAll")
    public ServerResult selectAll(HttpServletRequest request){
        ServerResult serverResult=departService.selectAll(request);
        return  serverResult;
    }
    //修改一个部门的名字
    @RequestMapping("/editOne")
    public ServerResult selectAll(@RequestParam("departId") Integer departId,@RequestParam("departName")String departName){
        int bol=departRepository.editOne(departId,departName);
        ServerResult serverResult=new ServerResult();
        if (bol!=0){
            serverResult.setStatus(true);
        }
        return  serverResult;
    }
    //删除一个部门,显示各种情况的相应内容Message
    @RequestMapping("/deleteOne")
    public ServerResult deleteOne(@RequestParam("departId") Integer departId){
        ServerResult serverResult=departService.deleteOne(departId);
        return  serverResult;
    }
    //增加一个部门
    @RequestMapping("/insertOne")
    public ServerResult insertOne(@RequestParam("departName") String departName,HttpServletRequest request){
        Depart depart=new Depart();
        depart.setTenantId((Integer)request.getSession().getAttribute("tenantId"));
        depart.setName(departName);
        departRepository.saveAndFlush(depart);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return  serverResult;
    }
}
