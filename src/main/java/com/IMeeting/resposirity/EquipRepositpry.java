package com.IMeeting.resposirity;

import com.IMeeting.entity.Equip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by gjw on 2018/12/12.
 */
@Repository
public interface EquipRepositpry extends JpaRepository<Equip,Integer>{
    List<Equip> findByTenantId(Integer tenantId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Equip m set m.name=?2 where m.id=?1")
    int updateOne(Integer equipId,String equipName);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from Equip m where m.id=?1")
    int deleteOne(Integer equipId);
}
