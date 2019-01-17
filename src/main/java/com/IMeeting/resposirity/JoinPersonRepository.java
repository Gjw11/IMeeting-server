package com.IMeeting.resposirity;

import com.IMeeting.entity.JoinPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gjw on 2019/1/13.
 */
@Repository
public interface JoinPersonRepository extends JpaRepository<JoinPerson,Integer>{
}
