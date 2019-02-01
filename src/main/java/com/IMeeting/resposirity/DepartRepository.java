package com.IMeeting.resposirity;

import com.IMeeting.entity.Depart;
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
public interface DepartRepository extends JpaRepository<Depart,Integer> {
   Optional<Depart> findById(Integer id);
   List<Depart> findByTenantId(Integer tenantId);
   @Transactional
   @Modifying(clearAutomatically = true)
   @Query(value = "update Depart m set m.name=?2 where m.id=?1")
   int editOne(Integer departId,String name);
   @Query(value = "delete from Depart m where m.id=?1")
   int deleteOne(Integer departId);
}
