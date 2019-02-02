package com.IMeeting.controller;

import com.IMeeting.entity.ServerResult;
import com.IMeeting.service.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gjw on 2019/2/2.
 */
@RestController
@RequestMapping("/equip")
public class EquipController {
    @Autowired
    private EquipService equipService;
    //查询该租户所有的会议室设备
    @RequestMapping("/selectAll")
    public ServerResult selectAll(HttpServletRequest request){
        ServerResult serverResult=equipService.selectAll(request);
        return  serverResult;
    }
}
