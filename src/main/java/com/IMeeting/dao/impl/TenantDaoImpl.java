package com.IMeeting.dao.impl;

import com.IMeeting.dao.TenantDao;
import com.IMeeting.dao.VideoRightDao;
import com.IMeeting.entity.Tenant;
import com.IMeeting.entity.VideoRight;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gjw on 2019/5/13.
 */
@Transactional
@Repository
public class TenantDaoImpl extends BaseDaoImpl<Tenant,Integer>implements TenantDao {
}
