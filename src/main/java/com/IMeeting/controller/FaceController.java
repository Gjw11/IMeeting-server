package com.IMeeting.controller;

import com.IMeeting.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by gjw on 2019/1/18.
 */
@Controller
@RequestMapping("/face")
public class FaceController {
    @RequestMapping("/toupload")
    public String toupload(){
        return "/toupload";
    }
    @RequestMapping("/fileUpload")
    public String upload(@RequestParam("file")MultipartFile file ){
        //1定义要上传文件 的存放路径
        String localPath="39.106.56.132/faceInfo";
        //2获得文件名字
        String fileName=file.getOriginalFilename();
        //2上传失败提示
        String warning="";
        if(FileUtil.upload(file, localPath, fileName)){
            //上传成功
            warning="上传成功";

        }else{
            warning="上传失败";
        }
        System.out.println(warning);
        return "上传成功";
    }

}
