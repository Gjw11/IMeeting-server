package com.IMeeting.resposirity;

import com.IMeeting.entity.MeetroomDepart;
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
public interface MeetroomDepartRepository extends JpaRepository<MeetroomDepart,Integer>{
    List<MeetroomDepart>findByMeetroomId(Integer meetroomId);
    List<MeetroomDepart>findByDepartId(Integer departId);
    List<MeetroomDepart>findByMeetroomIdAndStatus(Integer meetRoomId,Integer Status);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from MeetroomDepart m where m.meetroomId=?1")
    int deleteByMeetRoomId(Integer meetRoomId);
}
