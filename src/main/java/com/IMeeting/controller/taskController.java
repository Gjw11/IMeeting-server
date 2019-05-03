package com.IMeeting.controller;

import com.IMeeting.dao.OutlineDao;
import com.IMeeting.dao.TaskDao;
import com.IMeeting.entity.Outline;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.entity.Task;
import com.IMeeting.resposirity.OutlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by gjw on 2019/5/2.
 */
@RestController
@RequestMapping("/task")
@Transactional
public class taskController {
    @Autowired
    private TaskDao taskDao;

    //会议预定者和会议参与者查看某个会议的任务
    @RequestMapping("/findByMeeting")
    public ServerResult findOne(@RequestParam("meetingId")Integer meetingId){
        List<Task> tasks=taskDao.findEqualField("meetingId",meetingId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(tasks);
        serverResult.setStatus(true);
        return serverResult;
    }
    //会议预定者查看插入某个会议的任务
    @RequestMapping("/insertOne")
    public ServerResult insertOne(@RequestBody Task task){
        taskDao.save(task);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
    //会议预定者删除某条会议任务
    @RequestMapping("/deleteOne")
    public ServerResult insertOne(@RequestParam("taskId")Integer taskId){
        taskDao.delete(taskId);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
    //会议预定者修改某条会议任务
    @RequestMapping("/updateOne")
    public ServerResult updateOne(@RequestBody Task task){
        taskDao.update(task);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
}
