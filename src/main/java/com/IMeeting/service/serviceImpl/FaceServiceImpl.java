package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.FaceInfo;
import com.IMeeting.entity.FaceResult;
import com.IMeeting.entity.ServerResult;
import com.IMeeting.entity.Userinfo;
import com.IMeeting.resposirity.FaceInfoRepository;
import com.IMeeting.service.FaceService;
import com.IMeeting.service.UserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjw on 2019/2/3.
 */
@Service
public class FaceServiceImpl implements FaceService {
    @Autowired
    private FaceInfoRepository faceInfoRepository;
    @Autowired
    private UserinfoService userinfoService;
    @Override
    public ServerResult selectAll(HttpServletRequest request) {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        List<FaceInfo> faceInfos = faceInfoRepository.findByTenantIdOrderByStatus(tenantId);
        List<FaceResult> faceResults = new ArrayList<>();
        FaceResult faceResult;
        for (int i = 0; i < faceInfos.size(); i++) {
            faceResult = new FaceResult();
            FaceInfo faceInfo=faceInfos.get(i);
            faceResult.setId(faceInfo.getId());
            faceResult.setFaceAddress(faceInfo.getFaceAddress());
            String status="";
            switch (faceInfo.getStatus()){
                case 0:
                    status="未审核";
                    break;
                case 1:
                    status="已通过";
                    break;
                case 2:
                    status="未通过";
                    break;
            }
            faceResult.setStatus(status);
            Userinfo userinfo=userinfoService.getUserinfo(faceInfo.getUserId());
            faceResult.setName(userinfo.getName());
            faceResults.add(faceResult);
        }
        ServerResult serverResult=new ServerResult();
        serverResult.setData(faceResults);
        serverResult.setStatus(true);
        return serverResult;
    }
}
