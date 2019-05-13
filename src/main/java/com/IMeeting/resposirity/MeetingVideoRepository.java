package com.IMeeting.resposirity;

import com.IMeeting.entity.LeaveInformation;
import com.IMeeting.entity.MeetingVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2019/5/13.
 */
@Repository
public interface MeetingVideoRepository extends JpaRepository<MeetingVideo,Integer> {
    @Query(value = "select m from MeetingVideo m,VideoRight n where n.userId=?1 and n.videoId=m.id and m.status=1")
    List<MeetingVideo>findMyMeeting(Integer userId);
    List<MeetingVideo>findByCreateUserIdAndId(Integer userId,Integer id);
}
