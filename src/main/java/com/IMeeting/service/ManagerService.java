package com.IMeeting.service;

import com.IMeeting.entity.MenuInfo;
import com.IMeeting.entity.RoleMenuPara;
import com.IMeeting.entity.ServerResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gjw on 2019/2/2.
 */
public interface ManagerService {
    ServerResult selectAll(HttpServletRequest request);
    ServerResult deleteOne(Integer roleId);
    ServerResult insertOne(RoleMenuPara roleMenuPara,HttpServletRequest request);
    ServerResult updateOne(RoleMenuPara roleMenuPara);
    ServerResult toManager(HttpServletRequest request);
    MenuInfo findById(Integer menuId);
}
