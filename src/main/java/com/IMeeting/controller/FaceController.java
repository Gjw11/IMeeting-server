package com.IMeeting.controller;

import com.IMeeting.entity.FaceInfo;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.entity.Userinfo;
import com.IMeeting.resposirity.FaceInfoRepository;
import com.IMeeting.resposirity.UserinfoRepository;
import com.IMeeting.service.FaceService;
import com.IMeeting.util.BinaryConversion;
import com.IMeeting.util.FaceRecognition;
import com.IMeeting.util.FileUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gjw on 2019/1/18.
 */
@RestController
@RequestMapping("/face")
public class FaceController {
    @Autowired
    private FaceInfoRepository faceInfoRepository;
    @Autowired
    private FaceService faceService;
    @Autowired
    private UserinfoRepository userinfoRepository;

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
        FaceInfo faceInfo = new FaceInfo();
        faceInfo.setTenantId((Integer) request.getSession().getAttribute("tenantId"));
        faceInfo.setFaceDetail(BinaryConversion.parseHexStr2Byte(faceDetail));
        faceInfo.setStatus(0);
        faceInfo.setUserId((Integer) request.getSession().getAttribute("userId"));
        faceInfo.setFaceAddress(getUrl(fileupload));
        FaceInfo bol = faceInfoRepository.saveAndFlush(faceInfo);
        ServerResult serverResult = new ServerResult();
        if (bol != null)
            serverResult.setStatus(true);
        return serverResult;
    }

    //审核失败重新上传面部信息
    @RequestMapping("/update")
    public ServerResult update(@RequestParam("fileupload") MultipartFile fileupload, @RequestParam("faceDetail") String faceDetail, HttpServletRequest request) throws OSSException, ClientException, IOException {
        String faceAddress = getUrl(fileupload);
        byte[] realFaceDetail = BinaryConversion.parseHexStr2Byte(faceDetail);
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        int bol = faceInfoRepository.updateFaceInfo(userId, 0, faceAddress, realFaceDetail);
        ServerResult serverResult = new ServerResult();
        if (bol != 0) {
            serverResult.setStatus(true);
        }
        return serverResult;
    }

    //查询该用户数据库中是否有人脸数据记录，如果有及相应状态
    //查询结构返回code -1表示没有人脸数据 0表示未审核 1表示已通过 2表示未通过
    @RequestMapping("/selectStatus")
    public ServerResult insertPicture(HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        FaceInfo faceInfo = faceInfoRepository.findByUserId(userId);
        ServerResult serverResult = new ServerResult();
        if (faceInfo == null)
            serverResult.setCode(-1);//没有该用户人脸数据
        else {
            Integer status = faceInfo.getStatus();
            serverResult.setCode(status);
        }
        serverResult.setStatus(true);
        return serverResult;
    }

    /*-------------华丽分割线-------------*/
    //查询该租户所有员工的面部信息
    @RequestMapping("/selectAll")
    public ServerResult selectAll(HttpServletRequest request) {
        ServerResult serverResult = faceService.selectAll(request);
        return serverResult;
    }

    //审核通过
    @RequestMapping("/pass")
    public ServerResult pass(@RequestParam("faceId") Integer faceId) {
        faceInfoRepository.updateFaceStatus(faceId, 1);
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    //审核未通过
    @RequestMapping("/dispass")
    public ServerResult dispass(@RequestParam("faceId") Integer faceId) {
        faceInfoRepository.updateFaceStatus(faceId, 2);
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    //删除某一员工的人脸数据
    @RequestMapping("/deleteOne")
    public ServerResult deleteOne(@RequestParam("faceId") Integer faceId) {
        faceInfoRepository.deleteOne(faceId);
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    //管理端上传员工人脸数据
    @RequestMapping("/insertByManager")
    public ServerResult insertByManager(@RequestParam("fileupload") MultipartFile fileupload, @RequestParam("worknum") String worknum, HttpServletRequest request) throws IOException {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        Userinfo userinfo = userinfoRepository.findByWorknumAndTenantId(worknum, tenantId);
        ServerResult serverResult = new ServerResult();
        if (userinfo == null) {
            serverResult.setMessage("该工号的员工不存在");
        } else {
            Integer userId = userinfo.getId();
            FaceInfo faceinfo = faceInfoRepository.findByUserId(userId);
            if (faceinfo != null) {
                serverResult.setMessage("该工号的员工面部信息已存在，请勿重复录入");
            } else {
                FaceInfo faceInfo = new FaceInfo();
                faceInfo.setTenantId(tenantId);
                FaceRecognition faceRecognition = new FaceRecognition();
                File f=FileUtil.multoFile(fileupload);
                faceInfo.setFaceDetail(faceRecognition.getFeatureData(f));
                File del = new File(f.toURI());
                del.delete();
                faceInfo.setStatus(1);
                faceInfo.setUserId(userId);
                faceInfo.setFaceAddress(getUrl(fileupload));
                faceInfoRepository.saveAndFlush(faceInfo);
                serverResult.setStatus(true);
            }
        }
        return serverResult;
    }

    //删除某一员工的人脸数据
    @RequestMapping("/compare")
    public ServerResult compare(@RequestParam("fileupload") MultipartFile fileupload,HttpServletRequest request) throws IOException {
        File f=FileUtil.multoFile(fileupload);
        FaceRecognition faceRecognition=new FaceRecognition();
        byte[]source=faceRecognition.getFeatureData(f);
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        List<FaceInfo> faceInfoList=faceInfoRepository.findByTenantIdAndStatus(tenantId,1);
        FaceInfo faceInfo=new FaceInfo();
        for (int i=0;i<faceInfoList.size();i++){
            faceInfo=faceInfoList.get(i);
            byte[]target=faceInfo.getFaceDetail();
            double similarResult=faceRecognition.faceCompare(source,target);
            System.out.println(similarResult);
        }
        File del = new File(f.toURI());
        del.delete();
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }
}
