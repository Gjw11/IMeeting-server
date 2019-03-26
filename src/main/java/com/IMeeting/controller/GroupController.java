package com.IMeeting.controller;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.DepartRepository;
import com.IMeeting.resposirity.GroupRecordRepository;
import com.IMeeting.resposirity.GroupRepository;
import com.IMeeting.resposirity.UserinfoRepository;
import com.IMeeting.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
//@CrossOrigin(allowCredentials = "true")
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private DepartRepository departRepository;
    @Autowired
    private UserinfoRepository userinfoRepository;
    @Autowired
    private GroupRecordRepository groupRecordRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupService groupService;
    //显示用户
    @RequestMapping("/showUser")
    public ServerResult showUserinfo(HttpServletRequest request) {
        ServerResult serverResult=groupService.showUser(request);
        return serverResult;
    }
    //保存单条群组记录
    @RequestMapping("/saveGroup")
    public ServerResult insertGroupRecord(HttpServletRequest request, @RequestBody GroupList group) {
        ServerResult serverResult=new ServerResult();
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        Group group1=new Group();
        group1.setName(group.getName());
        group1.setUserId(userId);
        Group bol=groupRepository.saveAndFlush(group1);
        Integer GroupId=group1.getId();
        List<Integer>ids=group.getUserIds();
        for (int i=0;i<ids.size();i++){
            GroupRecord groupRecord=new GroupRecord();
            groupRecord.setGroupId(GroupId);
            groupRecord.setUserId(ids.get(i));
            GroupRecord bol1=groupRecordRepository.saveAndFlush(groupRecord);
        }
        serverResult.setStatus(true);
        return serverResult;
    }
    //删除单条群组记录
    @RequestMapping("/deleteGroup")
    public ServerResult deleteGroup(@RequestParam("id") Integer id) {
        ServerResult serverResult=groupService.deleteGroup(id);
        return serverResult;
    }
    //显示该用户的所有群组
    @RequestMapping("/showGroup")
    public ServerResult showGroup(HttpServletRequest request){
        Integer userId=(Integer) request.getSession().getAttribute("userId");
        List<Group>groups=groupRepository.findByUserId(userId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(groups);
        serverResult.setStatus(true);
        return serverResult;
    }
    //显示单条详细群组记录
    @RequestMapping("/showOneGroup")
    public ServerResult showOneGroup(@RequestParam("id")Integer id){
        ServerResult serverResult=groupService.showOneGroup(id);
        return serverResult;
    }
    //更新单条群组记录
    @RequestMapping("/updateOneGroup")
    public ServerResult updateOneGroup(@RequestBody GroupList groupList){
        ServerResult serverResult=groupService.updateOneGroup(groupList.getGroupId(),groupList.getUserIds(),groupList.getName());
        return serverResult;
    }
}
