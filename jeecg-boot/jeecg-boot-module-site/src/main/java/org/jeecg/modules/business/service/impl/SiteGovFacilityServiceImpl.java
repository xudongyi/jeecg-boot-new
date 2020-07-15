package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.SiteGovFacility;
import org.jeecg.modules.business.entity.SiteMonitorDevice;
import org.jeecg.modules.business.mapper.SiteGovFacilityMapper;
import org.jeecg.modules.business.service.ISiteGovFacilityService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 监测站治理设施
 * @Author: jeecg-boot
 * @Date:   2020-07-08
 * @Version: V1.0
 */
@Service
public class SiteGovFacilityServiceImpl extends ServiceImpl<SiteGovFacilityMapper, SiteGovFacility> implements ISiteGovFacilityService {

    @Override
    public Integer findCount(String monitorId) {
        return this.count(new QueryWrapper<SiteGovFacility>().lambda().eq(SiteGovFacility::getMonitorId,monitorId));
    }
}
