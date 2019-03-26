package com.IMeeting.controller;

import com.IMeeting.entity.Equip;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.service.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


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
    //增加会议室设备,传入参数会设备名字
    @RequestMapping("/insertOne")
    public ServerResult insertOne(@RequestParam("equipName") String equipName, HttpServletRequest request){
        ServerResult serverResult=equipService.insertOne(equipName,request);
        return  serverResult;
    }
    //修改一个会议室设备的名字,传入参数会设备名字和id
    @RequestMapping("/updateOne")
    public ServerResult updateOne(@RequestParam("equipName") String equipName, @RequestParam("equipId") Integer equipId,HttpServletRequest request){
        ServerResult serverResult=equipService.updateOne(equipName,equipId,request);
        return  serverResult;
    }
    //删除一个会议室设备,传入参数会设备id
    @RequestMapping("/deleteOne")
    public ServerResult deleteOne(@RequestParam("equipId") Integer equipId){
        ServerResult serverResult=equipService.deleteOne(equipId);
        return  serverResult;
    }
}
