package com.IMeeting.resposirity;

import com.IMeeting.entity.Depart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by gjw on 2018/11/23.
 */
@Repository
public interface DepartRepository extends JpaRepository<Depart,Integer> {
   Optional<Depart> findById(Integer id);
   List<Depart> findByTenantId(Integer tenantId);
}
