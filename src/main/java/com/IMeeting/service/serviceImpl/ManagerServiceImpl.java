package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.MenuInfoRepository;
import com.IMeeting.resposirity.RoleInfoRepository;
import com.IMeeting.resposirity.RoleMenuRepository;
import com.IMeeting.resposirity.UserinfoRepository;
import com.IMeeting.service.ManagerService;
import com.IMeeting.service.UserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by gjw on 2019/2/2.
 */
@Service
public class ManagerServiceImpl implements ManagerService{
    @Autowired
    private RoleInfoRepository roleInfoRepository;
    @Autowired
    private MenuInfoRepository menuInfoRepository;
    @Autowired
    private UserinfoRepository userinfoRepository;
    @Autowired
    private RoleMenuRepository roleMenuRepository;
    @Autowired
    private UserinfoService userinfoService;
    @Override
    public ServerResult selectAll(HttpServletRequest request) {
        List<List>result=new ArrayList<>();
        List<MenuInfo> menuInfos=menuInfoRepository.findAll();
        result.add(menuInfos);
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        List<RoleInfo>roleInfos=roleInfoRepository.findByTenantId(tenantId);
        result.add(roleInfos);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        serverResult.setData(result);
        return serverResult;
    }

    @Override
    public ServerResult deleteOne(Integer roleId) {
        List<Userinfo>userinfos=userinfoRepository.findByRoleId(roleId);
        ServerResult serverResult=new ServerResult();
        if (userinfos!=null){
            serverResult.setStatus(true);
            serverResult.setMessage("无法删除该角色，有用户为该角色");
        }else{
            roleMenuRepository.deleteOne(roleId);
            roleInfoRepository.deleteOne(roleId);
            serverResult.setStatus(true);
            serverResult.setMessage("角色删除成功");
        }
        return serverResult;
    }

    @Override
    public ServerResult insertOne(RoleMenuPara roleMenuPara,HttpServletRequest request) {
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        RoleInfo roleInfo=new RoleInfo();
        roleInfo.setName(roleMenuPara.getRoleName());
        roleInfo.setTenantId(tenantId);
        RoleInfo roleInfo1=roleInfoRepository.saveAndFlush(roleInfo);
        List<Integer>list=roleMenuPara.getMenus();
        Integer roleId=roleInfo1.getId();
        RoleMenu roleMenu;
        for (int i=0;i<list.size();i++){
            roleMenu=new RoleMenu();
            roleMenu.setMenuId(list.get(i));
            roleMenu.setRoleId(roleId);
            roleMenuRepository.saveAndFlush(roleMenu);
        }
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public ServerResult updateOne(RoleMenuPara roleMenuPara) {
        Integer roleId=roleMenuPara.getRoleId();
        String roleName=roleMenuPara.getRoleName();
        roleInfoRepository.updateOne(roleId,roleName);
        List<Integer>list=roleMenuPara.getMenus();
        roleMenuRepository.deleteOne(roleId);
        RoleMenu roleMenu;
        for (int i=0;i<list.size();i++){
            roleMenu=new RoleMenu();
            roleMenu.setMenuId(list.get(i));
            roleMenu.setRoleId(roleId);
            roleMenuRepository.saveAndFlush(roleMenu);
        }
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public ServerResult toManager(HttpServletRequest request) {
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        Userinfo userinfo=userinfoService.getUserinfo(userId);
        Integer roleId=null;
        if (userinfo.getRoleId()!=null){
            roleId=userinfo.getRoleId();
        }
        List<RoleMenu> roleMenus=roleMenuRepository.findByRoleId(roleId);
        List<MenuInfo>menuInfos=new ArrayList<>();
        MenuInfo menuInfo;
        for (int i=0;i<roleMenus.size();i++){
            menuInfo=findById(roleMenus.get(i).getMenuId());
            menuInfos.add(menuInfo);
        }
        ServerResult serverResult=new ServerResult();
        serverResult.setData(menuInfos);
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public MenuInfo findById(Integer menuId) {
        Optional<MenuInfo>menuInfo=menuInfoRepository.findById(menuId);
        if (menuInfo.isPresent())
            return menuInfo.get();
        else
            return null;
    }
}
