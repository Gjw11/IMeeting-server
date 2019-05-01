package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.EquipRepairInfoRepository;
import com.IMeeting.resposirity.EquipRepositpry;
import com.IMeeting.resposirity.MeetroomEquipRepository;
import com.IMeeting.service.EquipService;
import com.IMeeting.service.MeetRoomService;
import com.IMeeting.service.UserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by gjw on 2019/2/2.
 */
@Service
public class EquipServiceImpl implements EquipService {
    @Autowired
    private EquipRepositpry equipRepositpry;
    @Autowired
    private MeetroomEquipRepository meetroomEquipRepository;
    @Autowired
    private EquipRepairInfoRepository equipRepairInfoRepository;
    @Autowired
    private UserinfoService userinfoService;
    @Autowired
    private MeetRoomService meetRoomService;
    @Override
    public ServerResult selectAll(HttpServletRequest request) {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        List<Equip> equips = equipRepositpry.findByTenantId(tenantId);
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        serverResult.setData(equips);
        return serverResult;
    }

    @Override
    public ServerResult insertOne(String equipName, HttpServletRequest request) {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        Equip equip = new Equip();
        equip.setTenantId(tenantId);
        equip.setName(equipName);
        equipRepositpry.saveAndFlush(equip);
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public ServerResult updateOne(String equipName, Integer equipId, HttpServletRequest request) {
        equipRepositpry.updateOne(equipId, equipName);
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public ServerResult deleteOne(Integer equipId) {
        ServerResult serverResult = new ServerResult();
        meetroomEquipRepository.deleteByEquipId(equipId);
        equipRepositpry.deleteOne(equipId);
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public int reportDemage(HttpServletRequest request, @RequestBody EquipRepairInfo equipRepairInfo) {
        equipRepairInfo.setUserId((Integer) request.getSession().getAttribute("userId"));
        equipRepairInfo.setStatus(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new java.util.Date());
        equipRepairInfo.setReportTime(now);
        equipRepairInfo.setTenantId((Integer) request.getSession().getAttribute("tenantId"));
        EquipRepairInfo equipRepairInfo1 = equipRepairInfoRepository.saveAndFlush(equipRepairInfo);
        if (equipRepairInfo1 != null)
            return 1;
        else
            return 0;
    }

    @Override
    public List getEquipRequairInfo(HttpServletRequest request) {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        List<EquipRepairInfo> equipRepairInfos = new ArrayList<>();
        if (tenantId != null) {
            equipRepairInfos = equipRepairInfoRepository.findByTenantId(tenantId);
        }
        List<EquipRepairInfoResult>equipRepairInfoResults=new ArrayList<>();
        EquipRepairInfoResult equipRepairInfoResult;
        String status="";
        for (EquipRepairInfo equipRepairInfo:equipRepairInfos){
            equipRepairInfoResult=new EquipRepairInfoResult();
            equipRepairInfoResult.setId(equipRepairInfo.getId());
            equipRepairInfoResult.setDamageInfo(equipRepairInfo.getDamageInfo());
            switch (equipRepairInfo.getStatus()){
                case 0:
                    status="未处理";
                    break;
                case 1:
                    status="已修复";
                    break;
                case 2:
                    status="修复失败";
                    break;
            }
            equipRepairInfoResult.setStatus(status);
            equipRepairInfoResult.setRepairName(equipRepairInfo.getRepairName());
            equipRepairInfoResult.setRepairTime(equipRepairInfo.getRepairTime());
            equipRepairInfoResult.setReportTime(equipRepairInfo.getReportTime());
            Userinfo userinfo=userinfoService.getUserinfo(equipRepairInfo.getUserId());
            equipRepairInfoResult.setUserName(userinfo.getName());
            Meetroom meetroom=meetRoomService.getMeetRoom(equipRepairInfo.getMeetRoomId());
            System.out.println(meetroom);
            System.out.println(meetroom.getName());
            equipRepairInfoResult.setMeetRoomName(meetroom.getName());
            Equip equip=getOne(equipRepairInfo.getEquipId());
            equipRepairInfoResult.setEquipName(equip.getName());
            equipRepairInfoResults.add(equipRepairInfoResult);
        }
        return equipRepairInfoResults;
    }

    @Override
    public Equip getOne(Integer equipId) {
        Optional<Equip>equip=equipRepositpry.findById(equipId);
        if (equip.isPresent())
            return equip.get();
        return null;
    }
}
