package com.IMeeting.resposirity;

import com.IMeeting.entity.Meeting;
import com.IMeeting.entity.Meetroom;
import com.IMeeting.entity.MyReserveCount;
import com.IMeeting.entity.ServerResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
public interface MeetingRepository extends JpaRepository<Meeting,Integer>,JpaSpecificationExecutor<Meeting> {
    List<Meeting> findByMeetroomIdAndMeetDateAndStatusOrderByBegin(Integer meetRoomId, String meetDate,Integer status);
    @Transactional
    @Modifying
    @Query(value = "update Meeting m set m.status=?2 where m.id=?1")
    int updateStatus(Integer meetingId,Integer status);
    List<Meeting>findByBeginAndOverAndMeetroomIdAndStatusOrderByCreateTimeAsc(String begin,String over,Integer meetroomId,Integer status);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "select m from Meeting m where m.begin<?2 and m.over>?1 and m.meetroomId=?3 and(m.status=1 or m.status=3)")
    List<Meeting>findIntersectMeeting(String beginTime,String overTime,Integer meetroomId);
    @Query(value = "select m from Meeting m where m.userId=?1 and m.meetDate like?2 group by m.meetDate")
    List<Meeting> groupBymeetDate(Integer userId, String yearMonth);
    @Query(value = "select count (m) from Meeting m where m.userId=?1 and m.meetDate=?2")
    Long countMyReserve(Integer userId, String meetDate);
    @Query(value = "select m from Meeting m where m.userId=?1 and m.meetDate=?2 order by m.status ,m.begin")
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
    @Query(value = "update Meeting m set m.over=?2,m.status=?3,m.lastTime=?4 where m.id=?1")
    int advanceOver(Integer meetingId,String over,Integer status,Integer lastTime);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meeting m set m.status=?3 where m.begin=?1 and m.status=?2")
    int updateMeetingStatus(String beginTime,Integer beforeStatus,Integer afterStatus);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Meeting m set m.status=?3 where m.over=?1 and m.status=?2")
    int updateMeetingOverStatus(String overTime,Integer beforeStatus,Integer afterStatus);
    @Query(value = "select m from Meeting m ,JoinPerson n where n.userId=?1 and m.id=n.meetingId and m.meetDate like?2 and (m.status=1 or m.status=4)group by m.meetDate")
    List<Meeting> MyJoinMeetingGroupByDate(Integer userId,String yearMonth);
    @Query(value = "select count(m) from Meeting m ,JoinPerson n where n.userId=?1 and m.id=n.meetingId and m.meetDate =?2 and m.status=?3")
    int countNotStartMeeting(Integer userId,String meetDate,Integer status);
    @Query(value = "select count(m) from Meeting m ,JoinPerson n where n.userId=?1 and m.id=n.meetingId and m.meetDate =?2 and m.status=?3")
    int countOverMeeting(Integer userId,String meetDate,Integer status);
    @Query(value = "select m from Meeting m ,JoinPerson n where n.userId=?1 and m.id=n.meetingId and m.meetDate =?2 and (m.status=1 or m.status=3 or m.status=4)order by m.status")
    List<Meeting> MyJoinMeetingByDate(Integer userId,String date);
    @Query(value = "select m from Meeting m where m.userId=?1 and (m.status=1 or m.status=3 )order by m.begin")
    List<Meeting> selectByUserIdAndStatus(Integer userId);
    @Query(value = "select m from Meeting m where m.userId=?1 and (m.status=3 or m.status=4)order by m.begin desc")
    List<Meeting> selectByUserIdAndStatusJoin(Integer userId);
    @Query(value = "select m from Meeting m where m.meetDate=?1 and m.status=?2 and meetroomId=?3 order by m.begin asc")
    List<Meeting> selectByMeetDateAndStatus(String meetDate,Integer status,int meetRoomId);
    @Query(value = "select m from Meeting m where m.meetDate=?1 and (m.status=1 or m.status=3) and meetroomId=?2  order by m.begin asc")
    List<Meeting> selectByMeetDateAndStatusEQLOneAndThree(String meetDate,int meetRoomId);
    @Query(value = "select  m from Meeting m , JoinPerson n where  m.meetDate>=?2 and m.meetDate<=?3 and (m.status=1 or m.status=3) and n.userId=?1 and n.meetingId=m.id order by m.begin")
    List<Meeting> findMyRecentMeeting(Integer userId,String beginDate,String overDate);
    @Query(value="select  m from Meeting m where m.userId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4 order by m.meetDate")
    List<Meeting> findMyLastTwoWeekMeeting(Integer userId, String beginDate, String overDate);
    @Query(value="select  m from Meeting m where m.userId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4 group by  m.meetroomId")
    List<Meeting> GroupMyLastTwoWeekMeetingByMeetRoom(Integer userId, String beginDate, String overDate);
    @Query(value="select  count (m) from Meeting m where m.userId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4 and m.meetroomId=?4 ")
    int countMyLastTwoWeekMeetingByRoomId(Integer userId, String beginDate, String overDate,Integer roomId);
    @Query(value="select  m from Meeting m where m.tenantId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4 group by  m.meetroomId")
    List<Meeting> GroupLastTwoWeekMeetingByTenant(Integer tenantId, String beginDate, String overDate);
    @Query(value="select  count (m) from Meeting m where m.meetDate>=?1 and m.meetDate<=?2 and m.status=4 and m.meetroomId=?3 ")
    int countLastTwoWeekMeetingByRoomId(String beginDate, String overDate,Integer roomId);
    List<Meeting>findByMeetroomIdAndMeetDateOrderByBegin(Integer meetRoomId,String meetDate);
    /*-------------华丽分割线-------------*/
    @Query(value = "select  m from Meeting m where m.tenantId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4 group by m.departId")
    List<Meeting>selectGroupByDepart(Integer tenantId,String begin,String over);
    @Query(value = "select  m from Meeting m where m.tenantId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4 group by m.userId")
    List<Meeting>selectGroupByUser(Integer tenantId,String begin,String over);
    @Query(value = "select  m from Meeting m where m.tenantId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4 group by m.meetroomId")
    List<Meeting>selectGroupByMeetRoom(Integer tenantId,String begin,String over);
    @Query(value = "select sum(m.lastTime) from Meeting m where m.userId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4")
    int countHourByUser(Integer userId,String begin,String over);
    @Query(value = "select sum(m.lastTime) from Meeting m where m.departId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4")
    int countHourByDepart(Integer departId,String begin,String over);
    @Query(value = "select sum(m.lastTime) from Meeting m where m.meetroomId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4")
    int countHourByMeetRoom(Integer meetRoomId,String begin,String over);
    @Query(value = "select count (m) from Meeting m where m.userId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4")
    int countTimeByUser(Integer userId,String begin,String over);
    @Query(value = "select count (m) from Meeting m where m.departId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4")
    int countTimeByDepart(Integer departId,String begin,String over);
    @Query(value = "select count (m) from Meeting m where m.meetroomId=?1 and m.meetDate>=?2 and m.meetDate<=?3 and m.status=4")
    int countTimeByMeetRoom(Integer meetRoomId,String begin,String over);
}
