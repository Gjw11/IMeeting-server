package com.IMeeting.service;

import com.IMeeting.entity.Equip;
import com.IMeeting.entity.EquipRepairInfo;
import com.IMeeting.entity.ServerResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gjw on 2019/2/2.
 */
public interface EquipService {
    ServerResult selectAll(HttpServletRequest request);
    ServerResult insertOne(String equipName,HttpServletRequest request);
    ServerResult updateOne(String equipName,Integer equipId,HttpServletRequest request);
    ServerResult deleteOne(Integer equipId);
    int reportDemage(HttpServletRequest request, @RequestBody EquipRepairInfo equipRepairInfo);
    List getEquipRequairInfo(HttpServletRequest request);
    Equip getOne(Integer equipId);
}
