package com.IMeeting.resposirity;

import com.IMeeting.entity.OutsideJoinPerson;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by gjw on 2019/1/15.
 */
@Repository
public interface OutsideJoinPersonRepository extends JpaRepository<OutsideJoinPerson,Integer>{
    List<OutsideJoinPerson>findByMeetingId(Integer meetingId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from OutsideJoinPerson m where m.meetingId=?1")
    int deleteByMeetingId(Integer meetingId);
}
