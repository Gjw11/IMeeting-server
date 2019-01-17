package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.Depart;
import com.IMeeting.entity.Position;
import com.IMeeting.entity.Userinfo;
import com.IMeeting.resposirity.DepartRepository;
import com.IMeeting.resposirity.PositionRepository;
import com.IMeeting.resposirity.UserinfoRepository;
import com.IMeeting.service.UserinfoService;
import com.IMeeting.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by gjw on 2018/11/19.
 */
@Service
public class UserserviceImpl implements UserinfoService {
    @Autowired
    private UserinfoRepository userinfoRepository;
    @Autowired
    private DepartRepository departRepository;
    @Autowired
    private PositionRepository positionRepository;

    @Override
    public Userinfo login(String username, String password) {
        MD5 m = new MD5();
        String newPassword = m.MD5(password);
        Userinfo u1 = userinfoRepository.findByUsernameAndPassword(username, newPassword);
        if (u1 != null)
            return u1;
        else {
            Userinfo u2 = userinfoRepository.findByPhoneAndPassword(username, newPassword);
            if (u2 != null)
                return u2;
        }
        return null;
    }

    @Override
    public Userinfo getUserinfo(Integer id) {
        Optional<Userinfo> userinfo = userinfoRepository.findById(id);
        if (userinfo.isPresent()) {
            return userinfo.get();
        }
        return null;
    }

    @Override
    public Depart getDepart(Integer id) {
        Optional<Depart> depart = departRepository.findById(id);
        if (depart.isPresent()) {
            return depart.get();
        }
        return null;
    }

    @Override
    public Position getPosition(Integer id) {
        Optional<Position> position = positionRepository.findById(id);
        if (position.isPresent()) {
            return position.get();
        }
        return null;
    }
}
