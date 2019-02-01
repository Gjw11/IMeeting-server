package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.DepartRepository;
import com.IMeeting.resposirity.MeetroomDepartRepository;
import com.IMeeting.resposirity.PositionRepository;
import com.IMeeting.resposirity.UserinfoRepository;
import com.IMeeting.service.DepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by gjw on 2019/2/1.
 */
@Service
public class DepartServiceImpl implements DepartService{
    @Autowired
    private DepartRepository departRepository;
    @Autowired
    private UserinfoRepository userinfoRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private MeetroomDepartRepository meetroomDepartRepository;
    @Override
    public ServerResult selectAll(HttpServletRequest request) {
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        List<Depart>departs=departRepository.findByTenantId(tenantId);
        ServerResult serverResult=new ServerResult();
        serverResult.setData(departs);
        serverResult.setStatus(true);
        return serverResult;
    }
    @Override
    public ServerResult deleteOne(Integer departId) {
        ServerResult serverResult=new ServerResult();
        List<Userinfo> userinfos=userinfoRepository.findByDepartId(departId);
        List<Position> positions=positionRepository.findByDepartId(departId);
        List<MeetroomDepart>meetroomDeparts=meetroomDepartRepository.findByDepartId(departId);
        if (userinfos.size()!=0){
            serverResult.setMessage("有员工属于该部门，该部门不能删除");
            serverResult.setStatus(true);
        }else if (positions.size()!=0){
            serverResult.setMessage("有职位从属于该部门，该部门不能删除");
            serverResult.setStatus(true);
        }else if (meetroomDeparts.size()!=0){
            serverResult.setMessage("有会议室绑定该部门，该部门不能删除");
            serverResult.setStatus(true);
        }else{
            int bol=departRepository.deleteOne(departId);
            if (bol!=0)
                serverResult.setStatus(true);
        }
        return serverResult;
    }
}
