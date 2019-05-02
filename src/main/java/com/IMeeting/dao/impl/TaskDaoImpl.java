package com.IMeeting.dao.impl;

import com.IMeeting.dao.OpenApplyDao;
import com.IMeeting.dao.TaskDao;
import com.IMeeting.entity.OpenApply;
import com.IMeeting.entity.Task;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gjw on 2019/5/2.
 */
@Transactional
@Repository
public class TaskDaoImpl extends BaseDaoImpl<Task,Integer>implements TaskDao {
}
