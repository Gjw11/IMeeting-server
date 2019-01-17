package com.IMeeting.resposirity;

import com.IMeeting.entity.OutsideJoinPerson;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gjw on 2019/1/15.
 */
@Repository
public interface OutsideJoinPersonRepository extends JpaRepository<OutsideJoinPerson,Integer>{

}
