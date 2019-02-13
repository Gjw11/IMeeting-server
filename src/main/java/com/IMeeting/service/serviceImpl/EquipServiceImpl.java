package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.Equip;
import com.IMeeting.entity.MeetroomEquip;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.resposirity.EquipRepositpry;
import com.IMeeting.resposirity.MeetroomEquipRepository;
import com.IMeeting.service.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gjw on 2019/2/2.
 */
@Service
public class EquipServiceImpl implements EquipService{
    @Autowired
    private EquipRepositpry equipRepositpry;
    @Autowired
    private MeetroomEquipRepository meetroomEquipRepository;
    @Override
    public ServerResult selectAll(HttpServletRequest request) {
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        List<Equip> equips=equipRepositpry.findByTenantId(tenantId);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        serverResult.setData(equips);
        return serverResult;
    }

    @Override
    public ServerResult insertOne(String equipName,HttpServletRequest request) {
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        Equip equip=new Equip();
        equip.setTenantId(tenantId);
        equip.setName(equipName);
        equipRepositpry.saveAndFlush(equip);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public ServerResult updateOne(String equipName, Integer equipId, HttpServletRequest request) {
        equipRepositpry.updateOne(equipId,equipName);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public ServerResult deleteOne(Integer equipId) {
        ServerResult serverResult=new ServerResult();
        meetroomEquipRepository.deleteByEquipId(equipId);
        equipRepositpry.deleteOne(equipId);
        serverResult.setStatus(true);
        return serverResult;
    }
}
