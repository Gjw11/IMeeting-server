package com.IMeeting.resposirity;
import com.IMeeting.entity.GroupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2018/11/24.
 */
@Repository
public interface GroupRecordRepository extends JpaRepository<GroupRecord,Integer> {
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from GroupRecord m where m.groupId=?1")
    void delete(Integer groupId);
    List<GroupRecord>findByGroupId(Integer groupId);
}
