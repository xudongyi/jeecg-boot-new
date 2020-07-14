package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.SiteDataCollection;
import org.jeecg.modules.business.mapper.SiteDataCollectionMapper;
import org.jeecg.modules.business.service.ISiteDataCollectionService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 数采仪
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
@Service
public class SiteDataCollectionServiceImpl extends ServiceImpl<SiteDataCollectionMapper, SiteDataCollection> implements ISiteDataCollectionService {

    @Override
    public SiteDataCollection findByMnCode(String mn) {
        LambdaQueryWrapper<SiteDataCollection> wrapper = new QueryWrapper<SiteDataCollection>().lambda().eq(SiteDataCollection::getMn, mn);
        SiteDataCollection siteDataCollection = this.getOne(wrapper);
        return siteDataCollection;
    }

    @Override
    public SiteDataCollection getByMonitorId(String monitorId) {
        LambdaQueryWrapper<SiteDataCollection> wrapper = new QueryWrapper<SiteDataCollection>().lambda().eq(SiteDataCollection::getMonitorId, monitorId);
        SiteDataCollection siteDataCollection = this.getOne(wrapper);
        return siteDataCollection;
    }
}
