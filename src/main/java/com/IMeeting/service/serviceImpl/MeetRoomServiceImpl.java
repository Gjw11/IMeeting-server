package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.*;
import com.IMeeting.service.MeetRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by gjw on 2019/2/4.
 */
@Service
public class MeetRoomServiceImpl implements MeetRoomService{
    @Autowired
    private MeetroomRepository meetroomRepository;
    @Autowired
    private EquipRepositpry equipRepositpry;
    @Autowired
    private DepartRepository departRepository;
    @Autowired
    private MeetroomEquipRepository meetroomEquipRepository;
    @Autowired
    private MeetroomDepartRepository meetroomDepartRepository;
    @Override
    public ServerResult selectAll(HttpServletRequest request) {
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        List<Meetroom> meetrooms=meetroomRepository.selectByTenantId(tenantId);
        List<Equip>equips=equipRepositpry.findByTenantId(tenantId);
        List<Depart>departs=departRepository.findByTenantId(tenantId);
        ServerResult serverResult=new ServerResult();
        List<List>lists=new ArrayList<>();
        lists.add(meetrooms);
        lists.add(equips);
        lists.add(departs);
        serverResult.setData(lists);
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public Meetroom getMeetRoom(Integer meetRoomId) {
        Optional<Meetroom>meetroom=meetroomRepository.findById(meetRoomId);
        if (meetroom.isPresent())
            return meetroom.get();
        return null;
    }

    @Override
    public ServerResult showOne(Integer meetRoomId, HttpServletRequest request) {
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        Meetroom meetroom=getMeetRoom(meetRoomId);
        List<Equip>equips=equipRepositpry.findByTenantId(tenantId);
        List<MeetroomEquip>meetroomEquips=meetroomEquipRepository.findByMeetroomId(meetRoomId);
        List<Depart>departs=departRepository.findByTenantId(tenantId);
        List<MeetroomDepart>enables=meetroomDepartRepository.findByMeetroomIdAndStatus(meetRoomId,1);
        List<MeetroomDepart>bans=meetroomDepartRepository.findByMeetroomIdAndStatus(meetRoomId,0);
        List<Object>result=new ArrayList<>();
        ServerResult serverResult=new ServerResult();
        result.add(meetroom);   //该会议室的数据
        result.add(equips);     //该租户的设备
        result.add(meetroomEquips);//该会议室对应的设备
        result.add(departs);    //该租户的部门
        result.add(enables);    //该会议室允许使用的部门
        result.add(bans);       //该会议室禁止使用的部门
        serverResult.setStatus(true);
        serverResult.setData(result);
        return serverResult;
    }

    @Override
    public ServerResult editOne(MeetroomPara meetroomPara, HttpServletRequest request) {
        Integer meetRoomId=meetroomPara.getId();
        meetroomRepository.updateMeetRoom(meetRoomId,meetroomPara.getName(),meetroomPara.getNum(),meetroomPara.getPlace(),meetroomPara.getContain());
        meetroomEquipRepository.deleteByMeetRoomId(meetRoomId);
        if (meetroomPara.getEquips()!=null) {
            List<Integer> equips = meetroomPara.getEquips();
            MeetroomEquip meetroomEquip;
            for (int i = 0; i < equips.size(); i++) {
                meetroomEquip = new MeetroomEquip();
                meetroomEquip.setMeetroomId(meetRoomId);
                meetroomEquip.setEquipId(equips.get(i));
                meetroomEquipRepository.saveAndFlush(meetroomEquip);
            }
        }
        meetroomDepartRepository.deleteByMeetRoomId(meetRoomId);
        MeetroomDepart meetroomDepart;
        if (meetroomPara.getEnables()!=null) {
            List<Integer> enables = meetroomPara.getEnables();
            for (int i = 0; i < enables.size(); i++) {
                meetroomDepart = new MeetroomDepart();
                meetroomDepart.setMeetroomId(meetRoomId);
                meetroomDepart.setDepartId(enables.get(i));
                meetroomDepart.setStatus(1);
                meetroomDepartRepository.saveAndFlush(meetroomDepart);
            }
        }
        if (meetroomPara.getBans()!=null) {
            List<Integer> bans = meetroomPara.getBans();
            for (int i = 0; i < bans.size(); i++) {
                meetroomDepart = new MeetroomDepart();
                meetroomDepart.setMeetroomId(meetRoomId);
                meetroomDepart.setDepartId(bans.get(i));
                meetroomDepart.setStatus(0);
                meetroomDepartRepository.saveAndFlush(meetroomDepart);
            }
        }
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public ServerResult insertOne(MeetroomPara meetroomPara, HttpServletRequest request) {
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        Meetroom meetroom=new Meetroom();
        meetroom.setTenantId(tenantId);
        meetroom.setAvailStatus(1);
        meetroom.setNowStatus(0);
        meetroom.setContain(meetroomPara.getContain());
        meetroom.setName(meetroomPara.getName());
        meetroom.setNum(meetroomPara.getNum());
        meetroom.setPlace(meetroomPara.getPlace());
        Meetroom meetroom1=meetroomRepository.saveAndFlush(meetroom);
        Integer meetRoomId=meetroom1.getId();
        if (meetroomPara.getEquips()!=null) {
            List<Integer> equips = meetroomPara.getEquips();
            MeetroomEquip meetroomEquip;
            for (int i = 0; i < equips.size(); i++) {
                meetroomEquip = new MeetroomEquip();
                meetroomEquip.setMeetroomId(meetRoomId);
                meetroomEquip.setEquipId(equips.get(i));
                meetroomEquipRepository.saveAndFlush(meetroomEquip);
            }
        }
        MeetroomDepart meetroomDepart;
        if (meetroomPara.getEnables()!=null) {
            List<Integer> enables = meetroomPara.getEnables();
            for (int i = 0; i < enables.size(); i++) {
                meetroomDepart = new MeetroomDepart();
                meetroomDepart.setMeetroomId(meetRoomId);
                meetroomDepart.setDepartId(enables.get(i));
                meetroomDepart.setStatus(1);
                meetroomDepartRepository.saveAndFlush(meetroomDepart);
            }
        }
        if (meetroomPara.getBans()!=null) {
            List<Integer> bans = meetroomPara.getBans();
            for (int i = 0; i < bans.size(); i++) {
                meetroomDepart = new MeetroomDepart();
                meetroomDepart.setMeetroomId(meetRoomId);
                meetroomDepart.setDepartId(bans.get(i));
                meetroomDepart.setStatus(0);
                meetroomDepartRepository.saveAndFlush(meetroomDepart);
            }
        }
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
}
