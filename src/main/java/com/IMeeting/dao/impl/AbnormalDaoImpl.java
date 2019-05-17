package com.IMeeting.dao.impl;

import com.IMeeting.dao.AbnormalInfoDao;
import com.IMeeting.dao.FileUploadDao;
import com.IMeeting.entity.AbnormalInfo;
import com.IMeeting.entity.FileUpload;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gjw on 2019/5/16.
 */

@Transactional
@Repository
public class AbnormalDaoImpl  extends BaseDaoImpl<AbnormalInfo,Integer>implements AbnormalInfoDao {
}
