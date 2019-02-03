package com.IMeeting.resposirity;

import com.IMeeting.entity.FaceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by gjw on 2019/1/19.
 */
@Repository
public interface FaceInfoRepository extends JpaRepository<FaceInfo,Integer>{
    FaceInfo findByUserId(Integer userId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update FaceInfo m set m.status=?2 ,m.faceAddress=?3,m.faceDetail=?4 where m.userId=?1")
    int updateFaceInfo(Integer userId,Integer status,String faceAddress,String faceDetail);
    List<FaceInfo>findByTenantIdOrderByStatus(Integer tenantId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update FaceInfo m set m.status=?2 where m.id=?1")
    int updateFaceStatus(Integer faceId,Integer status);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from FaceInfo m where m.id=?1")
    int deleteOne(Integer faceId);

}
