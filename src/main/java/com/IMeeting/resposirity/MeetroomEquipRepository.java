package com.IMeeting.resposirity;

import com.IMeeting.entity.MeetroomEquip;
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
public interface MeetroomEquipRepository extends JpaRepository<MeetroomEquip,Integer>{
    List<MeetroomEquip> findByMeetroomId(Integer meetroomId);
    List<MeetroomEquip> findByEquipId(Integer equipId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from MeetroomEquip m where m.equipId=?1")
    int deleteByEquipId(Integer equipId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from MeetroomEquip m where m.meetroomId=?1")
    int deleteByMeetRoomId(Integer meetRoomId);
}
