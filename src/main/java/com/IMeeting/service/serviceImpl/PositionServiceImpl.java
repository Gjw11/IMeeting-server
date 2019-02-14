package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.Depart;
import com.IMeeting.entity.Position;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.entity.Userinfo;
import com.IMeeting.resposirity.DepartRepository;
import com.IMeeting.resposirity.PositionRepository;
import com.IMeeting.resposirity.UserinfoRepository;
import com.IMeeting.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjw on 2019/2/1.
 */
@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private DepartRepository departRepository;
    @Autowired
    private UserinfoRepository userinfoRepository;
    @Override
    public ServerResult selectAll(HttpServletRequest request) {
        Integer tenantId= (Integer) request.getSession().getAttribute("tenantId");
        List<Position> positions=positionRepository.findByTenantId(tenantId);
        List<Depart>departs=departRepository.findByTenantId(tenantId);
        List<List>result=new ArrayList<>();
        result.add(positions);
        result.add(departs);
        ServerResult serverResult=new ServerResult();
        serverResult.setStatus(true);
        serverResult.setData(result);
        return serverResult;
    }

    @Override
    public ServerResult deleteOne(Integer positionId) {
        List<Userinfo>userinfos=userinfoRepository.findByPositionId(positionId);
        ServerResult serverResult=new ServerResult();
        if (userinfos.size()!=0){
            serverResult.setStatus(true);
            serverResult.setMessage("有员工属于该职位，该职位不能删除");
        }else {
            int bol=positionRepository.deleteOne(positionId);
            serverResult.setStatus(true);
        }
        return serverResult;
    }
}
