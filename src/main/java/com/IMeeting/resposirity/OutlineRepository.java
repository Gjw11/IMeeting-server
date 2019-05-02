package com.IMeeting.resposirity;

import com.IMeeting.entity.Outline;
import com.IMeeting.entity.OutsideJoinPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2019/5/2.
 */
@Repository
public interface OutlineRepository extends JpaRepository<Outline,Integer> {
    List<Outline> findByMeetingIdOrderByLevel(Integer meetingId);
}
