package com.IMeeting.controller;

import com.IMeeting.dao.OutlineDao;
import com.IMeeting.entity.Outline;
import com.IMeeting.entity.ServerResult;
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
@RequestMapping("/outline")
@Transactional
public class OutlineController {
    @Autowired
    private OutlineDao outlineDao;
    @Autowired
    private OutlineRepository outlineRepository;
    //会议预定者和会议参与者查看某个会议的会议大纲
    @RequestMapping("/findByMeeting")
    public ServerResult findOne(@RequestParam("meetingId")Integer meetingId){
        List<Outline>outlines=outlineRepository.findByMeetingIdOrderByLevel(meetingId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(outlines);
        serverResult.setStatus(true);
        return serverResult;
    }
    //会议预定者查看插入某个会议的大纲
    @RequestMapping("/insertOne")
    public ServerResult insertOne(@RequestBody Outline outline){
        outlineDao.save(outline);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
    //会议预定者删除某条会议大纲
    @RequestMapping("/deleteOne")
    public ServerResult insertOne(@RequestParam("outlineId")Integer outlineId){
        outlineDao.delete(outlineId);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
    //会议预定者修改某条会议大纲
    @RequestMapping("/updateOne")
    public ServerResult updateOne(@RequestBody Outline outline){
        outlineDao.update(outline);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
}
