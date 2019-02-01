package com.IMeeting.service.serviceImpl;

import com.IMeeting.entity.Tenant;
import com.IMeeting.resposirity.TenantRepository;
import com.IMeeting.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by gjw on 2019/2/1.
 */
@Service
public class TenantServiceImpl implements TenantService{
    @Autowired
    private TenantRepository tenantRepository;
    @Override
    public Tenant findById(Integer tenantId) {
        Optional<Tenant> tenant=tenantRepository.findById(tenantId);
        if (tenant.isPresent()){
            return tenant.get();
        }
        return null;
    }
}
