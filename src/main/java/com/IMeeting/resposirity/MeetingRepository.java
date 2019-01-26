package com.IMeeting.resposirity;

import com.IMeeting.entity.Meeting;
import com.IMeeting.entity.MyReserveCount;
import com.IMeeting.entity.ServerResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by gjw on 2018/12/16.
 */
@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Integer>{
    List<Meeting> findAll(Specification<Meeting> specification);
    List<Meeting> findByMeetroomIdAndMeetDateAndStatusOrderByBegin(Integer meetRoomId, String meetDate,Integer status);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meeting m set m.status=?2 where m.id=?1")
    int updateStatus(Integer meetingId,Integer status);
    List<Meeting>findByBeginAndOverAndMeetroomIdOrderByCreateTimeAsc(String begin,String over,Integer meetroomId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "select m from Meeting m where m.begin<?2 and m.over>?1 and m.meetroomId=?3 and(m.status=1 or m.status=3)")
    List<Meeting>findIntersectMeeting(String beginTime,String overTime,Integer meetroomId);
    @Query(value = "select m from Meeting m where m.userId=?1 and m.meetDate like?2 group by meetDate")
    List<Meeting> groupBymeetDate(Integer userId, String yearMonth);
    @Query(value = "select count (m) from Meeting m where m.userId=?1 and m.meetDate=?2")
    Long countMyReserve(Integer userId, String meetDate);
    @Query(value = "select m from Meeting m where m.userId=?1 and m.meetDate=?2 order by status ,begin")
    List<Meeting>findMyReserve(Integer userId,String meetDate);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meeting m set m.begin=?2 where m.id=?1")
    int updateBegin(Integer meetingId,String begin);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meeting m set m.over=?2 where m.id=?1")
    int updateOver(Integer meetingId,String over);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meeting m set m.topic=?2,m.content=?3,m.prepareTime=?4 where m.id=?1")
    int updateTCP(Integer meetingId,String topic,String content,Integer prepareTime);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meeting m set m.over=?2,m.status=?3 where m.id=?1")
    int advanceOver(Integer meetingId,String over,Integer status);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meeting m set m.status=?3 where m.begin=?1 and m.status=?2")
    int updateMeetingStatus(String beginTime,Integer beforeStatus,Integer afterStatus);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meeting m set m.status=?3 where m.over=?1 and m.status=?2")
    int updateMeetingOverStatus(String overTime,Integer beforeStatus,Integer afterStatus);
    @Query(value = "select m from Meeting m ,JoinPerson n where n.userId=?1 and m.id=n.meetingId and m.meetDate like ?2 and (m.status=1 or m.status=3 or m.status=4)")
    List<Meeting> selectMyJoinMeeting(Integer userId,String yearMonth);
}
