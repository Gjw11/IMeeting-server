package com.IMeeting.service;

import com.IMeeting.entity.ServerResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gjw on 2019/2/1.
 */
public interface PositionService {
    ServerResult selectAll(HttpServletRequest request);
    ServerResult deleteOne(Integer positionId);
}
