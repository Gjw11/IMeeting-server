package com.IMeeting.controller;

import com.IMeeting.entity.Equip;
import com.IMeeting.entity.EquipRepairInfo;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.resposirity.EquipRepairInfoRepository;
import com.IMeeting.service.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;


@RestController
@RequestMapping("/equip")
public class EquipController {
    @Autowired
    private EquipService equipService;
    @Autowired
    private EquipRepairInfoRepository equipRepairInfoRepository;
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
    //设备报修
    @RequestMapping("/reportDemage")
    public ServerResult reportDemage(HttpServletRequest request, @RequestBody EquipRepairInfo equipRepairInfo){
        int bol=equipService.reportDemage(request,equipRepairInfo);
        ServerResult serverResult=new ServerResult();
        if (bol==1){
            serverResult.setCode(1);
        }else{
            serverResult.setCode(0);
        }
        serverResult.setStatus(true);
        return  serverResult;
    }
    //管理端查看设备报修
    @RequestMapping("/getEquipRequairInfos")
    public ServerResult getEquipRequairInfos(HttpServletRequest request){
        List<EquipRepairInfo> list=equipService.getEquipRequairInfo(request);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(list);
        serverResult.setStatus(true);
        return  serverResult;
    }
    //管理端处理设备报修
    @RequestMapping("/dealEquipRequair")
    public ServerResult dealEquipRequair(@RequestParam("repairName")String repairName,@RequestParam("id")Integer id){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = sdf.format(new java.util.Date());
        int bol=equipRepairInfoRepository.updateRepairInfo(repairName,nowTime,id);
        int code;
        if (bol!=0){
            code=1;
        }else{
            code=0;
        }
        ServerResult serverResult=new ServerResult();
        serverResult.setCode(code);
        serverResult.setStatus(true);
        return  serverResult;
    }
    //用户端查看自己提交的设备报修
    @RequestMapping("/userGetEquipRequairInfos")
    public ServerResult userGetEquipRequairInfos(HttpServletRequest request){
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        List<EquipRepairInfo> list=equipRepairInfoRepository.findByUserId(userId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(list);
        serverResult.setStatus(true);
        return  serverResult;
    }
}
