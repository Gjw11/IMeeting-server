package com.IMeeting.resposirity;

import com.IMeeting.entity.RoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2019/2/1.
 */
@Repository
public interface RoleInfoRepository extends JpaRepository<RoleInfo,Integer>{
    List<RoleInfo>findByTenantId(Integer tenantId);
}
