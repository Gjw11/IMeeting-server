package com.IMeeting.resposirity;

/**
 * Created by gjw on 2019/1/30.
 */

import com.IMeeting.entity.LeaveInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LeaveInformationRepository extends JpaRepository<LeaveInformation,Integer>{
    @Query(value = "select count (m) from LeaveInformation m where m.meetingId=?1 ")
    int countAll(Integer meetingId);
    @Query(value = "select count (m) from LeaveInformation m where m.meetingId=?1 and m.status=0 ")
    int notDealCount(Integer meetingId);
    List<LeaveInformation> findByMeetingIdOrderByStatus(Integer meetingId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update LeaveInformation m set m.status=1 where m.id=?1")
    int agreeLeave(Integer leaveInfoId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update LeaveInformation m set m.status=1 where m.id=?2")
    int disagreeLeave(Integer leaveInfoId);
}
