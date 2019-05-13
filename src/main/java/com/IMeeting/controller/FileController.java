package com.IMeeting.controller;

import com.IMeeting.dao.FaceDao;
import com.IMeeting.dao.FileUploadDao;
import com.IMeeting.entity.FaceInfo;
import com.IMeeting.entity.FileUpload;
import com.IMeeting.entity.Meeting;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.resposirity.FaceInfoRepository;
import com.IMeeting.resposirity.FileUploadRepository;
import com.IMeeting.service.MeetingService;
import com.IMeeting.util.FileUtil;
import com.IMeeting.util.SFTPUtil;
import com.IMeeting.util.TimeUtil;
import com.jcraft.jsch.SftpException;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gjw on 2019/4/30.
 */
@RestController
@RequestMapping("/file")
@Transactional
public class FileController {
    @Autowired
    private FileUploadDao fileUploadDao;
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private FileUploadRepository fileUploadRepository;
    @Autowired
    private FaceInfoRepository faceInfoRepository;
    @Autowired
    private FaceDao faceDao;
    //会议文件上传
    @RequestMapping("/upload")
    public ServerResult upload(@RequestParam("file")MultipartFile file, @RequestParam("meetingId")Integer meetingId, @RequestParam("status")Integer status, HttpServletRequest request) throws IOException, SftpException {
        SFTPUtil sftp = new SFTPUtil("root", "Jgn990206", "39.106.56.132", 22);
        sftp.login();
        InputStream is = new FileInputStream(FileUtil.multoFile(file));
        String fileName=file.getOriginalFilename();
        sftp.upload("/usr/share/nginx/image/", "Files", fileName, is);
        sftp.logout();
        FileUpload fileUpload=new FileUpload();
        fileUpload.setTenantId((Integer) request.getSession().getAttribute("tenantId"));
        fileUpload.setFileName(fileName);
        fileUpload.setMeetingId(meetingId);
        fileUpload.setStatus(status);
        Meeting meeting=meetingService.findByMeetingId(meetingId);
        fileUpload.setMeetRoomId(meeting.getMeetroomId());
        fileUpload.setFileUrl("http://www.jglo.top:8091/Files");
        fileUploadDao.save(fileUpload);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        serverResult.setMessage("会议文件上传成功");

        //删除文件
        File targetFile = new File(fileName);
        targetFile.delete();

        return serverResult;
    }
    //会议预定界面查看会议文件
    @RequestMapping("/fineOneMeetingFileOnReserve")
    public ServerResult fineOneMeetingFileOnReserve(@RequestParam("meetingId") Integer meetingId){
        List<FileUpload> fileUploads= (List<FileUpload>) fileUploadDao.findEqualField("meetingId",meetingId);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        serverResult.setData(fileUploads);
        return serverResult;
    }
    //会议参加界面查看会议文件
    @RequestMapping("/fineOneMeetingFileOnJoin")
    public ServerResult fineOneMeetingFileOnJoin(@RequestParam("meetingId") Integer meetingId){
        List<FileUpload> fileUploads= (List<FileUpload>) fileUploadRepository.findByMeetingIdAndStatus(meetingId,1);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        serverResult.setData(fileUploads);
        return serverResult;
    }
    //会议预定者修改文件是否可以下载 1可以下载 2不可以下载
    @RequestMapping("/editOne")
    public ServerResult editOne(@RequestParam("fileId") Integer fileId,@RequestParam("status") Integer status){
        int bol=fileUploadDao.executeSql("update m_file_upload m set m.status=? where m.id=?",status,fileId);
        ServerResult serverResult=new ServerResult();
        if (bol!=0){
            serverResult.setCode(1);
            serverResult.setMessage("文件下载权限变更成功");
        }else{
            serverResult.setCode(0);
            serverResult.setMessage("文件下载权限变更失败");
        }
        serverResult.setStatus(true);
        return serverResult;
    }
    //会议预定者删除文件
    @RequestMapping("/deleteOne")
    public ServerResult deleteOne(@RequestParam("fileId") Integer fileId) throws SftpException {
        FileUpload fileUpload=fileUploadDao.findOne(fileId);
       fileUploadDao.delete(fileId);
        SFTPUtil sftp = new SFTPUtil("root", "Jgn990206", "39.106.56.132", 22);
        sftp.login();
        sftp.delete(fileUpload.getFileUrl(),fileUpload.getFileName());
        sftp.logout();
        ServerResult serverResult=new ServerResult();
        serverResult.setMessage("会议文件删除成功");
        serverResult.setStatus(true);
        return serverResult;
    }
    //人脸识别验证下载会议文件
    @RequestMapping("/downLoad")
    public ServerResult downLoad(@RequestParam(value = "fileId") Integer  fileId,HttpServletRequest request) throws SftpException, ParseException, IOException {
        ServerResult serverResult=new ServerResult();
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        FaceInfo faceInfo= faceInfoRepository.findByUserId(userId);
        if (faceInfo==null){
            serverResult.setCode(-1);
            serverResult.setMessage("对不起，您还没有有效的面部信息");
        }else{
            String lastTime=faceInfo.getLastTime();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String nowTime=simpleDateFormat.format(new Date());
//            System.out.println(TimeUtil.reduceMinute(nowTime,lastTime));
            if (TimeUtil.reduceMinute(lastTime,nowTime)>5){
                serverResult.setCode(2);
                serverResult.setMessage("请先进行面部识别验证身份");
            }else{
                FileUpload fileUpload=fileUploadDao.findOne(fileId);
                SFTPUtil sftp = new SFTPUtil("root", "Jgn990206", "39.106.56.132", 22);
                sftp.login();
                File file=sftp.downloadFile(fileUpload.getFileUrl(),fileUpload.getFileName(),"/src/main/java/com.i");
                file.delete();
                sftp.logout();
            }
        }
        serverResult.setStatus(true);
        return serverResult;
    }
    //管理端查看会议文件
    @RequestMapping("/findAllOnManage")
    public ServerResult findAllOnManage(HttpServletRequest request){
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        List<FileUpload>fileUploads=fileUploadDao.findEqualField("tenantId",tenantId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(fileUploads);
        serverResult.setStatus(true);
        return serverResult;
    }
}
