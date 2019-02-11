package com.IMeeting.service;

import com.IMeeting.entity.Depart;
import com.IMeeting.entity.ServerResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gjw on 2019/2/1.
 */
public interface DepartService {
    ServerResult selectAll(HttpServletRequest request);
    ServerResult deleteOne(Integer departId);
    Depart findByDepartId(Integer departId);
}
