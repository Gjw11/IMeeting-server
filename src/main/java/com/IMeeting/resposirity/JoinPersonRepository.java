package com.IMeeting.resposirity;

import com.IMeeting.entity.JoinPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by gjw on 2019/1/13.
 */
@Repository
public interface JoinPersonRepository extends JpaRepository<JoinPerson,Integer>{
    List<JoinPerson>findByMeetingId(Integer meetingId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from JoinPerson m where m.meetingId=?1")
    int deleteByMeetingId(Integer meetingId);
}
