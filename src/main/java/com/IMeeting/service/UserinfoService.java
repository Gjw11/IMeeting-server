package com.IMeeting.service;

import com.IMeeting.entity.Depart;
import com.IMeeting.entity.Position;
import com.IMeeting.entity.Userinfo;
import com.IMeeting.resposirity.UserinfoRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by gjw on 2018/11/19.
 */
public interface UserinfoService {
    Userinfo login(String username,String password);
    Userinfo getUserinfo(Integer id);
    Depart getDepart(Integer id);
    Position getPosition(Integer id);
}
