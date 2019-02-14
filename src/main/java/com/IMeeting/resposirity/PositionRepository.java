package com.IMeeting.resposirity;

import com.IMeeting.entity.Depart;
import com.IMeeting.entity.Position;
import com.IMeeting.entity.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by gjw on 2018/11/23.
 */
@Repository
public interface PositionRepository extends JpaRepository<Position,Integer> {
    Optional<Position> findById(Integer id);
    List<Position>findByDepartId(Integer departId);
    List<Position>findByTenantId(Integer tenantId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Position m set m.departId=?2 ,m.name=3 where m.id=?1")
    int editOne(Integer positionId,Integer departId,String positionName);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from Position m where m.id=?1")
    int deleteOne(Integer positionId);
}
