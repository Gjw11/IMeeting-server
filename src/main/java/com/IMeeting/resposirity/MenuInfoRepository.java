package com.IMeeting.resposirity;

import com.IMeeting.entity.MenuInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gjw on 2019/2/2.
 */
@Repository
public interface MenuInfoRepository extends JpaRepository<MenuInfo,Integer>{

}
