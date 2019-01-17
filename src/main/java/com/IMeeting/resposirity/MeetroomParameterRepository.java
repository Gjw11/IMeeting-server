package com.IMeeting.resposirity;

/**
 * Created by gjw on 2018/12/12.
 */

import com.IMeeting.entity.MeetroomParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetroomParameterRepository extends JpaRepository<MeetroomParameter,Integer>{
    MeetroomParameter findByTenantId(Integer tenantId);
}
