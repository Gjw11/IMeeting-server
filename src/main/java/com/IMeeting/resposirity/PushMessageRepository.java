package com.IMeeting.resposirity;

import com.IMeeting.entity.PushMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2019/2/11.
 */
@Repository
public interface PushMessageRepository extends JpaRepository<PushMessage,Integer>{
    List<PushMessage>findByReceiveIdAndStatus(Integer receiveId,Integer status);
}
