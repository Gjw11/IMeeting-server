package com.IMeeting.resposirity;

import com.IMeeting.entity.MeetroomDepart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2018/12/12.
 */
@Repository
public interface MeetroomDepartRepository extends JpaRepository<MeetroomDepart,Integer>{
    List<MeetroomDepart>findByMeetroomId(Integer meetroomId);
}
