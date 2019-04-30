package com.IMeeting.dao.impl;

import com.IMeeting.dao.BaseDao;
import com.IMeeting.dao.OpenApplyDao;
import com.IMeeting.entity.OpenApply;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gjw on 2019/4/29.
 */
@Transactional
@Repository
public class OpenApplyDaoImpl extends BaseDaoImpl<OpenApply,Integer>implements OpenApplyDao{
}
