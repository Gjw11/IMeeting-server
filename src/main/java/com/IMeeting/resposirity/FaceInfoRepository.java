package com.IMeeting.resposirity;

import com.IMeeting.entity.FaceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gjw on 2019/1/19.
 */
@Repository
public interface FaceInfoRepository extends JpaRepository<FaceInfo,Integer>{
}
