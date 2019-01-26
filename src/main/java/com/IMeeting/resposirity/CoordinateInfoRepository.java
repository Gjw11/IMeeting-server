package com.IMeeting.resposirity;

import com.IMeeting.entity.CoordinateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by gjw on 2019/1/14.
 */
@Repository
public interface CoordinateInfoRepository extends JpaRepository<CoordinateInfo,Integer>{
    List<CoordinateInfo> findByBeforeMeetingIdAndStatus(Integer beforeMeetingId, Integer status);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update CoordinateInfo m set m.status=?2 where m.id=?1")
    int updateCoordinateStatus(Integer coordinateId,Integer status);
}
