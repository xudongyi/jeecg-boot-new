package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyBase;
import org.jeecg.modules.business.entity.SiteMonitorDevice;
import org.jeecg.modules.business.mapper.SiteMonitorDeviceMapper;
import org.jeecg.modules.business.service.ISiteMonitorDeviceService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 监测点检测设备
 * @Author: jeecg-boot
 * @Date:   2020-07-08
 * @Version: V1.0
 */
@Service
public class SiteMonitorDeviceServiceImpl extends ServiceImpl<SiteMonitorDeviceMapper, SiteMonitorDevice> implements ISiteMonitorDeviceService {

    @Override
    public Integer findCount(String monitorId) {
        return this.count(new QueryWrapper<SiteMonitorDevice>().lambda().eq(SiteMonitorDevice::getMonitorId,monitorId));
    }
}
