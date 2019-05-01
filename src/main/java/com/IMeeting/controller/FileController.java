package com.IMeeting.controller;

import com.IMeeting.dao.FileUploadDao;
import com.IMeeting.entity.FileUpload;
import com.IMeeting.entity.Meeting;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.service.MeetingService;
import com.IMeeting.util.FileUtil;
import com.IMeeting.util.SFTPUtil;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

/**
 * Created by gjw on 2019/4/30.
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileUploadDao fileUploadDao;
    @Autowired
    private MeetingService meetingService;
    //会议文件上传
    @RequestMapping("/upload")
    public ServerResult upload(@RequestParam("file")MultipartFile file, @RequestParam("meetingId")Integer meetingId, @RequestParam("status")Integer status, HttpServletRequest request) throws IOException, SftpException {
        SFTPUtil sftp = new SFTPUtil("root", "Jgn990206", "39.106.56.132", 22);
        sftp.login();
        InputStream is = new FileInputStream(FileUtil.multoFile(file));
        String fileName=file.getOriginalFilename();
        sftp.upload("/root", "MeetFile", fileName, is);
        sftp.logout();
        FileUpload fileUpload=new FileUpload();
        fileUpload.setTenantId((Integer) request.getSession().getAttribute("tenantId"));
        fileUpload.setFileName(fileName);
        fileUpload.setMeetingId(meetingId);
        fileUpload.setStatus(status);
        Meeting meeting=meetingService.findByMeetingId(meetingId);
        fileUpload.setMeetRoomId(meeting.getMeetroomId());
        fileUpload.setFileUrl("/root/MeetFile");
        fileUploadDao.save(fileUpload);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        serverResult.setMessage("会议文件上传成功");
        return serverResult;
    }
    //会议预定/参加会议界面查看会议文件
    @RequestMapping("/fineOneMeetingFile")
    public ServerResult findOnReserve(@RequestParam("meetingId") Integer meetingId){
        List<FileUpload> fileUploads= (List<FileUpload>) fileUploadDao.findEqualField("meetingId",meetingId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(fileUploads);
        return serverResult;
    }
    //会议预定者修改文件信息
    @RequestMapping("/editOne")
    public ServerResult editOne(@RequestParam("editOne") Integer editOne){
        List<FileUpload> fileUploads= (List<FileUpload>) fileUploadDao.findEqualField("meetingId",meetingId);
        ServerResult serverResult=new ServerResult();
        return serverResult;
    }
    //会议预定者删除文件
    @RequestMapping("/deleteOne")
    public ServerResult deleteOne(@RequestParam("fileId") Integer fileId){
       fileUploadDao.delete(fileId);
        
        ServerResult serverResult=new ServerResult();
        return serverResult;
    }
}
}
