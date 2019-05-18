package com.IMeeting.resposirity;

import com.IMeeting.entity.PushMessage;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = "pushMassage")
public interface PushMessageRepository extends JpaRepository<PushMessage, Integer> {
    @Cacheable(key = "#p0")
    List<PushMessage> findByReceiveIdAndStatus(Integer receiveId, Integer status);

    @CacheEvict(key = "#p1")
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update  PushMessage m set m.status=1 where m.id=?1")
    int updateStatus(Integer id, Integer userId);
}
