package com.IMeeting.service;

import com.IMeeting.entity.ServerResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gjw on 2019/2/3.
 */
public interface FaceService {
    ServerResult selectAll(HttpServletRequest request);
}
