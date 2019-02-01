package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.*;
import com.IMeeting.resposirity.DepartRepository;
import com.IMeeting.resposirity.PositionRepository;
import com.IMeeting.resposirity.RoleInfoRepository;
import com.IMeeting.resposirity.UserinfoRepository;
import com.IMeeting.service.TenantService;
import com.IMeeting.service.UserinfoService;
import com.IMeeting.util.MD5;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
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
    @Autowired
    private TenantService tenantService;

    @Override
    public Userinfo login(String username, String password) {
        MD5 m = new MD5();
        String newPassword = m.MD5(password);
        Userinfo u1 = userinfoRepository.findByUsernameAndPasswordAndStatus(username, newPassword, 1);
        if (u1 != null)
            return u1;
        else {
            Userinfo u2 = userinfoRepository.findByPhoneAndPasswordAndStatus(username, newPassword, 1);
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
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        List<List> result = new ArrayList<>();
        List<Depart> departs = departRepository.findByTenantId(tenantId);
        List<Position> positions = positionRepository.findByDepartId(tenantId);
        List<RoleInfo> roleInfos = roleInfoRepository.findByTenantId(tenantId);
        result.add(departs);
        result.add(positions);
        result.add(roleInfos);
        List<Userinfo> userinfos = userinfoRepository.findByTenantIdAndStatus(tenantId, 1);
        result.add(userinfos);
        ServerResult serverResult = new ServerResult();
        serverResult.setData(result);
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public ServerResult updateOne(Userinfo userinfo) {
        Integer userId = userinfo.getId();
        Userinfo userinfo1 = getUserinfo(userId);
        if (userinfo1.getWorknum().equals(userinfo.getWorknum())) {
        } else {
            Tenant tenant = tenantService.findById(userinfo1.getTenantId());
            userinfoRepository.updateUsername(userId, tenant.getNum() + userinfo.getWorknum());
        }
        Integer departId = null, positionId = null, roleId = null;
        if (userinfo.getPositionId() != null)
            positionId = userinfo.getPositionId();
        if (userinfo.getDepartId() != null)
            departId = userinfo.getDepartId();
        if (userinfo.getRoleId() != null)
            roleId = userinfo.getRoleId();
        userinfoRepository.updateUserInfo(userId, userinfo.getWorknum(), userinfo.getName(), userinfo.getPhone(), departId, positionId, roleId);
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public ServerResult insertOne(Userinfo userinfo, HttpServletRequest request) {
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        Tenant tenant = tenantService.findById(tenantId);
        userinfo.setUsername(tenant.getNum() + userinfo.getWorknum());
        userinfo.setTenantId(tenantId);
        userinfo.setStatus(1);
        userinfoRepository.saveAndFlush(userinfo);
        ServerResult serverResult = new ServerResult();
        serverResult.setStatus(true);
        return serverResult;
    }

    @Override
    public ServerResult batchImport(String fileName, MultipartFile file, HttpServletRequest request) throws Exception {
        List<Userinfo> userinfos = new ArrayList<Userinfo>();
        ServerResult serverResult = new ServerResult();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            serverResult.setMessage("文件格式不正确,必须为xls或者xlsx格式");
            serverResult.setStatus(true);
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        boolean flag = false;
        Userinfo userinfo;
        Integer tenantId = (Integer) request.getSession().getAttribute("tenantId");
        Tenant tenant = tenantService.findById(tenantId);
        String tenantNum = tenant.getNum();
        MD5 m = new MD5();
        String password = m.MD5("123456");
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            userinfo = new Userinfo();
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String workNum = row.getCell(0).getStringCellValue();
            if (workNum == null || workNum.isEmpty()) {
                serverResult.setMessage("导入失败(第" + (r + 1) + "行,工号未填写)");
                serverResult.setStatus(true);
                flag = true;
                break;
            }
            if (row.getCell(1).getCellType() != 1) {
                serverResult.setMessage("导入失败(第" + (r + 1) + "行,姓名请设为文本格式)");
                serverResult.setStatus(true);
                flag = true;
                break;
            }
            String name = row.getCell(1).getStringCellValue();
            if (name == null || name.isEmpty()) {
                serverResult.setMessage("导入失败(第" + (r + 1) + "行,姓名未填写)");
                serverResult.setStatus(true);
                flag = true;
                break;
            }
            row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
            String phone = row.getCell(2).getStringCellValue();
            Userinfo u = userinfoRepository.findByWorknumAndTenantId(workNum, tenantId);
            if (u != null) {
                serverResult.setMessage("导入失败(第" + (r + 1) + "行工号为" + workNum + "的员工已存在)");
                serverResult.setStatus(true);
                flag = true;
                break;
            }
            userinfo.setName(name);
            userinfo.setWorknum(workNum);
            userinfo.setUsername(tenantNum + workNum);
            userinfo.setPassword(password);
            userinfo.setPhone(phone);
            userinfo.setTenantId(tenantId);
            userinfo.setStatus(1);
            userinfos.add(userinfo);
        }
        if (flag == false) {
            for (Userinfo userinfoRecord : userinfos) {
                userinfoRepository.saveAndFlush(userinfoRecord);
            }
            serverResult.setStatus(true);
            serverResult.setMessage("导入成功");
        }
        return serverResult;
    }
}
