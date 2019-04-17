package com.IMeeting.dao.impl;

import com.IMeeting.dao.MeetingDao;
import com.IMeeting.entity.Meeting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gjw on 2019/4/14.
 */
@Transactional
@Repository
public class MeetingDaoImpl extends BaseDaoImpl<Meeting,Integer>implements MeetingDao{
}
