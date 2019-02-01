package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.DepartRepository;
import com.IMeeting.resposirity.PositionRepository;
import com.IMeeting.resposirity.RoleInfoRepository;
import com.IMeeting.resposirity.UserinfoRepository;
import com.IMeeting.service.UserinfoService;
import com.IMeeting.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private RoleInfoRepository roleInfoRepository;
    @Override
    public Userinfo login(String username, String password) {
        MD5 m = new MD5();
        String newPassword = m.MD5(password);
        Userinfo u1 = userinfoRepository.findByUsernameAndPasswordAndStatus(username, newPassword,1);
        if (u1 != null)
            return u1;
        else {
            Userinfo u2 = userinfoRepository.findByPhoneAndPasswordAndStatus(username, newPassword,1);
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
    /*-------------华丽分割线-------------*/
    @Override
    public ServerResult selectAllPeople(HttpServletRequest request) {
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        List<List>result=new ArrayList<>();
        List<Depart>departs=departRepository.findByTenantId(tenantId);
        List<Position>positions=positionRepository.findByDepartId(tenantId);
        List<RoleInfo>roleInfos=roleInfoRepository.findByTenantId(tenantId);
        result.add(departs);
        result.add(positions);
        result.add(roleInfos);
        List<Userinfo>userinfos=userinfoRepository.findByTenantIdAndStatus(tenantId,1);
        
        return null;
    }

}
