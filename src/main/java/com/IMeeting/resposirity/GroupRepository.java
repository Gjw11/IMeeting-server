package com.IMeeting.resposirity;

import com.IMeeting.entity.Group;
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
public interface GroupRepository  extends JpaRepository<Group,Integer> {
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from Group m where m.id=?1")
    void delete(Integer id);
    List<Group> findByUserId(Integer userId);
    @Modifying(clearAutomatically = true)
    @Query(value = "update Group m set m.name =?2 where m.id=?1")
    int update(Integer id,String name);
}
