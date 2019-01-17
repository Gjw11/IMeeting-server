package com.IMeeting.resposirity;

import com.IMeeting.entity.Meetroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2018/12/12.
 */
@Repository
public interface MeetroomRepository extends JpaRepository<Meetroom,Integer>{
    List<Meetroom> findByTenantIdAndAvailStatus(Integer tenantId, Integer availStatus);
}
