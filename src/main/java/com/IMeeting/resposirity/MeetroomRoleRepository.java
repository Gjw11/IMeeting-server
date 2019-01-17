package com.IMeeting.resposirity;

import com.IMeeting.entity.MeetroomRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2018/12/12.
 */
@Repository
public interface MeetroomRoleRepository extends JpaRepository<MeetroomRole,Integer>{
    List<MeetroomRole>findByMeetroomId(Integer meetroomId);
}
