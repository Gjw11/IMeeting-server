package com.IMeeting.resposirity;

import com.IMeeting.entity.FaceInfo;
import com.IMeeting.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2019/5/1.
 */
@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload,Integer> {
    List<FileUpload>findByMeetingIdAndStatus(Integer meetingId,Integer status);
}
