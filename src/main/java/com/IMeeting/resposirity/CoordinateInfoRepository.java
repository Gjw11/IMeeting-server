package com.IMeeting.resposirity;

import com.IMeeting.entity.CoordinateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2019/1/14.
 */
@Repository
public interface CoordinateInfoRepository extends JpaRepository<CoordinateInfo,Integer>{
    List<CoordinateInfo> findByBeforeMeetingIdAndStatus(Integer beforeMeetingId, Integer status);
}
