package com.IMeeting.resposirity;

import com.IMeeting.entity.Meetroom;
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
public interface MeetroomRepository extends JpaRepository<Meetroom,Integer>{
    List<Meetroom> findByTenantIdAndAvailStatus(Integer tenantId, Integer availStatus);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "select m from Meetroom m , Meeting n where m.id=n.meetroomId and n.begin=?1")
    List<Meetroom>findMeetRoomRun(String beginTime);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meetroom m set m.nowStatus=1 where m.id=?1")
    int updateMeetRoomRun(Integer meetRoomId);
    @Query(value = "select m from Meetroom m , Meeting n where m.id=n.meetroomId and n.over=?1")
    List<Meetroom>findMeetRoomOver(String overTime);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meetroom m set m.nowStatus=0 where m.id=?1")
    int updateMeetRoomOver(Integer meetRoomId);
    @Query(value = "select m from Meetroom m where m.tenantId=?1 and (m.availStatus=0 or m.availStatus=1)")
    List<Meetroom>selectByTenantId(Integer tenantId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meetroom m set m.availStatus=?2 where m.id=?1")
    int updateMeetRoomAvailStatus(Integer meetRoomId,Integer availStatus);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meetroom m set m.name=?2,m.num=?3,m.place=?4,m.contain=?5 where m.id=?1")
    int updateMeetRoom(Integer meetRoomId,String name,String num,String place,Integer contain);
    List<Meetroom> findByTenantId(Integer tenantId);
    @Query(value = "select count (m) from Meetroom m where m.tenantId=?1")
    int countAll(Integer tenantId);
    @Query(value = "select count (m) from Meetroom m where m.tenantId=?1 and m.nowStatus=0")
    int countFree(Integer tenantId);
}
