package com.IMeeting.controller;

import com.IMeeting.entity.FaceInfo;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.resposirity.FaceInfoRepository;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gjw on 2019/1/18.
 */
@RestController
@RequestMapping("/face")
public class FaceController {
    @Autowired
    private FaceInfoRepository faceInfoRepository;
    public String getUrl(MultipartFile fileupload) throws OSSException, ClientException, IOException {
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        String accessKeyId = "LTAI8bzC3TvwnYNZ";
        String accessKeySecret = "OPbUtvrPLs1zme45RHMcjf7jINWqpR";

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 文件桶
        String bucketName = "jgn";
        // 文件名格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        // 该桶中的文件key
        String dateString = sdf.format(new Date()) + ".jpg";// 20180322010634.jpg
        // 上传文件
        ossClient.putObject("jgn", dateString, new ByteArrayInputStream(fileupload.getBytes()));

        // 设置URL过期时间为100年，默认这里是int型，转换为long型即可
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 100);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, dateString, expiration);
        return url.toString();
    }
    @RequestMapping("/insert")
    public ServerResult insertPicture(@RequestParam("fileupload") MultipartFile fileupload, @RequestParam("faceDetail") String faceDetail, HttpServletRequest request) throws OSSException, ClientException, IOException {
        FaceInfo faceInfo=new FaceInfo();
        faceInfo.setTenantId((Integer) request.getSession().getAttribute("tenantId"));
        faceInfo.setFaceDetail(faceDetail);
        faceInfo.setStatus(0);
        faceInfo.setUserId((Integer) request.getSession().getAttribute("userId"));
        faceInfo.setFaceAddress(getUrl(fileupload));
        FaceInfo bol=faceInfoRepository.saveAndFlush(faceInfo);
        ServerResult serverResult=new ServerResult();
        if (bol!=null)
            serverResult.setStatus(true);
        return serverResult;
    }
    //查询该用户数据库中是否有人脸数据记录，如果有及相应状态
    //查询结构返回code -1表示没有人脸数据 0表示未审核 1表示已通过 2表示未通过
    @RequestMapping("/selectStatus")
    public ServerResult insertPicture(HttpServletRequest request){
        Integer userId= (Integer) request.getSession().getAttribute("userId");
        FaceInfo faceInfo=faceInfoRepository.findByUserId(userId);
        ServerResult serverResult=new ServerResult();
        if (faceInfo==null)
            serverResult.setCode(-1);//没有该用户人脸数据
        else{
            Integer status=faceInfo.getStatus();
            serverResult.setCode(status);
        }
        serverResult.setStatus(true);
        return serverResult;
    }

}
