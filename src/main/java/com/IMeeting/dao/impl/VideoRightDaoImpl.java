package com.IMeeting.dao.impl;

import com.IMeeting.dao.TaskDao;
import com.IMeeting.dao.VideoRightDao;
import com.IMeeting.entity.Task;
import com.IMeeting.entity.VideoRight;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gjw on 2019/5/13.
 */

@Transactional
@Repository
public class VideoRightDaoImpl extends BaseDaoImpl<VideoRight,Integer>implements VideoRightDao {
}
