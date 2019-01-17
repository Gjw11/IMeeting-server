package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.DepartRepository;
import com.IMeeting.resposirity.GroupRecordRepository;
import com.IMeeting.resposirity.GroupRepository;
import com.IMeeting.resposirity.UserinfoRepository;
import com.IMeeting.service.GroupService;
import com.IMeeting.service.UserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by gjw on 2018/11/24.
 */
@Service
public class GroupServiceImpl implements GroupService{
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupRecordRepository groupRecordRepository;
    @Autowired
    private UserinfoRepository userinfoRepository;
    @Autowired
    private UserinfoService userinfoService;
    @Autowired
    private DepartRepository departRepository;
    @Override
    @Transactional
    public ServerResult deleteGroup(Integer id) {
        ServerResult serverResult=new ServerResult();
        groupRepository.delete(id);
        groupRecordRepository.delete(id);
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public Group selectGroup(Integer id) {
        Optional<Group> group=groupRepository.findById(id);
        if (group.isPresent()) {
            return group.get();
        }
        return null;
    }

    @Override
    public ServerResult showOneGroup(Integer id) {
        ServerResult serverResult=new ServerResult();
        Group group=selectGroup(id);
        List<GroupRecord>groupRecords=groupRecordRepository.findByGroupId(id);
        HashMap<Integer,String>u=new HashMap<>();
        String name = null;
        int departId = -1;
        for (int i=0;i<groupRecords.size();i++){
            Integer userId=groupRecords.get(i).getUserId();
            Userinfo userinfo=userinfoService.getUserinfo(userId);
            if (userinfo!=null) {
                name = userinfo.getName();
                departId=userinfo.getDepartId();
                groupRecords.get(i).setGroupId(departId);
            }
            u.put(userId,name);
        }
        List<Object>list=new ArrayList<>();
        list.add(group);
        list.add(groupRecords);
        list.add(u);
        serverResult.setData(list);
        serverResult.setStatus(true);
        return serverResult;
    }
    @Transactional
    @Override
    public ServerResult updateOneGroup(Integer groupId,List<Integer> userIds,String name) {
        int bol=groupRepository.update(groupId,name);
        groupRecordRepository.delete(groupId);
        for (int i=0;i<userIds.size();i++){
            GroupRecord groupRecord=new GroupRecord();
            groupRecord.setGroupId(groupId);
            groupRecord.setUserId(userIds.get(i));
            GroupRecord bol1=groupRecordRepository.saveAndFlush(groupRecord);
        }
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
    //获取该用户所有的群组
    @Override
    public ServerResult getGroupList(Integer userId) {
        List<Group> groups=groupRepository.findByUserId(userId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(groups);
        serverResult.setStatus(true);
        return serverResult;
    }
    //显示该租户下的所有人员
    @Override
    public ServerResult showUser(HttpServletRequest request) {
        ServerResult serverResult=new ServerResult();
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        List<Depart>departs=departRepository.findByTenantId(tenantId);
        List<Object>result=new ArrayList<>();
        List<Object>resultUser=new ArrayList<>();
        for(int i=0;i<departs.size();i++){
            Integer departId=departs.get(i).getId();
            List<Userinfo> userInfos=userinfoRepository.findByDepartId(departId);
            resultUser.add(userInfos);
        }
        result.add(departs);
        result.add(resultUser);
        serverResult.setData(result);
        serverResult.setStatus(true);
        return serverResult;
    }
}
