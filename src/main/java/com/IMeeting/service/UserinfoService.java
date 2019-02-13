package com.IMeeting.service;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.UserinfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gjw on 2018/11/19.
 */
public interface UserinfoService {
    Userinfo login(String username,String password);
    Userinfo getUserinfo(Integer id);
    Depart getDepart(Integer id);
    Position getPosition(Integer id);
     /*-------------华丽分割线-------------*/
    ServerResult selectAllPeople(HttpServletRequest request);
    ServerResult updateOne(Userinfo userinfo);
    ServerResult insertOne(Userinfo userinfo,HttpServletRequest request);
    ServerResult batchImport(String fileName, MultipartFile file,HttpServletRequest request) throws Exception;
    ServerResult showOne(Integer id);
    RoleInfo getRoleInfo(Integer roleId);
}
