package com.IMeeting.service;

import com.IMeeting.entity.Equip;
import com.IMeeting.entity.ServerResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gjw on 2019/2/2.
 */
public interface EquipService {
    ServerResult selectAll(HttpServletRequest request);
    ServerResult insertOne(String equipName,HttpServletRequest request);
    ServerResult updateOne(String equipName,Integer equipId,HttpServletRequest request);
    ServerResult deleteOne(Integer equipId);
}
