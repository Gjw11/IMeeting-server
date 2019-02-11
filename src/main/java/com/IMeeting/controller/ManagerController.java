package com.IMeeting.controller;

import com.IMeeting.entity.MenuInfo;
import com.IMeeting.entity.RoleMenu;
import com.IMeeting.entity.RoleMenuPara;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.resposirity.RoleMenuRepository;
import com.IMeeting.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gjw on 2019/2/2.
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private RoleMenuRepository roleMenuRepository;
    @Autowired
    private ManagerService managerService;
    //查询该租户所有的角色和菜单列表，需存储，菜单列表下面操作需要用到
    @RequestMapping("/selectAll")
    public ServerResult selectAll(HttpServletRequest request){
        ServerResult serverResult=managerService.selectAll(request);
        return  serverResult;
    }
    //查询一个角色有的权限
    @RequestMapping("/selectOneRole")
    public ServerResult selectOneRole(@RequestParam("roleId")Integer roleId){
        List<RoleMenu>roleMenus=roleMenuRepository.findByRoleId(roleId);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        serverResult.setData(roleMenus);
        return  serverResult;
    }
    //删除一个角色
    @RequestMapping("/deleteOneRole")
    public ServerResult deleteOneRole(@RequestParam("roleId")Integer roleId){
        ServerResult serverResult=managerService.deleteOne(roleId);
        return  serverResult;
    }
    //增加一个角色 传入参数角色名字和角色的权限菜单id集合
    @RequestMapping("/insertOneRole")
    public ServerResult insertOneRole(@RequestBody RoleMenuPara roleMenuPara,HttpServletRequest request){
        ServerResult serverResult=managerService.insertOne(roleMenuPara,request);
        return  serverResult;
    }
    //更新一个角色 传入参数角色id、角色名字和角色的权限菜单id集合
    @RequestMapping("/updateOneRole")
    public ServerResult updateOneRole(@RequestBody RoleMenuPara roleMenuPara,HttpServletRequest request){
        ServerResult serverResult=managerService.updateOne(roleMenuPara);
        return  serverResult;
    }
    //点击用户界面跳转到管理员界面时显示该管理员所具有的权限即可操作的菜单
    @RequestMapping("/toManager")
    public ServerResult toManager(HttpServletRequest request){
        ServerResult serverResult=managerService.toManager(request);
        return  serverResult;
    }
}
