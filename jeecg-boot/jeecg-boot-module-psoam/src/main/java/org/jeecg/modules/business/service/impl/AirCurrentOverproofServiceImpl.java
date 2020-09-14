package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirCurrentOverproof;
import org.jeecg.modules.business.mapper.AirCurrentOverproofMapper;
import org.jeecg.modules.business.service.IAirCurrentOverproofService;
import org.jeecg.modules.business.vo.OverEntry;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: air_current_overproof
 * @Author: jeecg-boot
 * @Date:   2020-09-14
 * @Version: V1.0
 */
@Service
public class AirCurrentOverproofServiceImpl extends ServiceImpl<AirCurrentOverproofMapper, AirCurrentOverproof> implements IAirCurrentOverproofService {
    @Resource
    AirCurrentOverproofMapper airCurrentOverproofMapper;

    @Override
    public List<OverEntry> queryOverAir(List<String> companyIds, String area, String code, String mn, Timestamp end, Timestamp begin) {
        return airCurrentOverproofMapper.queryOverAir(companyIds ,area ,code ,mn ,end ,begin);
    }

    @Override
    public Page<OverEntry> queryOverAir(Page<OverEntry> page, List<String> companyIds, String area, String code, String mn, Timestamp end, Timestamp begin) {
        return page.setRecords(airCurrentOverproofMapper.queryOverAir(page,companyIds ,area ,code ,mn ,end ,begin));
    }
}
