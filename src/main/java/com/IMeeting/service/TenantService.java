package com.IMeeting.service;

import com.IMeeting.entity.Tenant;

/**
 * Created by gjw on 2019/2/1.
 */
public interface TenantService {
    Tenant findById(Integer tenantId);
}
