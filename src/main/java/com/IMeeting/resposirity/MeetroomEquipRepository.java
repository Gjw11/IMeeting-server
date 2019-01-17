package com.IMeeting.resposirity;

import com.IMeeting.entity.MeetroomEquip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2018/12/12.
 */
@Repository
public interface MeetroomEquipRepository extends JpaRepository<MeetroomEquip,Integer>{
    List<MeetroomEquip> findByMeetroomId(Integer meetroomId);
}
