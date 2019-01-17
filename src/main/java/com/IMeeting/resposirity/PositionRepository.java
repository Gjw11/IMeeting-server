package com.IMeeting.resposirity;

import com.IMeeting.entity.Depart;
import com.IMeeting.entity.Position;
import com.IMeeting.entity.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by gjw on 2018/11/23.
 */
@Repository
public interface PositionRepository extends JpaRepository<Position,Integer> {
    Optional<Position> findById(Integer id);
}
