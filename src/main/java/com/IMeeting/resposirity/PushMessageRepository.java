package com.IMeeting.resposirity;

import com.IMeeting.entity.PushMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by gjw on 2019/2/11.
 */
@Repository
public interface PushMessageRepository extends JpaRepository<PushMessage,Integer>{
    List<PushMessage>findByReceiveIdAndStatus(Integer receiveId,Integer status);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update  PushMessage m set m.status=1 where m.id=?1")
    int updateStatus(Integer id);
}
