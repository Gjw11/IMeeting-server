package com.IMeeting.resposirity;

import com.IMeeting.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gjw on 2019/2/1.
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant,Integer>{

}
