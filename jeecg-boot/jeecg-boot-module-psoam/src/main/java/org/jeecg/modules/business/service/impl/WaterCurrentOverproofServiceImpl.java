package org.jeecg.modules.business.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.WaterCurrentOverproof;
import org.jeecg.modules.business.mapper.WaterCurrentOverproofMapper;
import org.jeecg.modules.business.service.IWaterCurrentOverproofService;
import org.jeecg.modules.business.vo.OverEntry;
import org.jeecg.modules.business.vo.OverEntryReport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: water_current_overproof
 * @Author: jeecg-boot
 * @Date:   2020-09-08
 * @Version: V1.0
 */
@Service
public class WaterCurrentOverproofServiceImpl extends ServiceImpl<WaterCurrentOverproofMapper, WaterCurrentOverproof> implements IWaterCurrentOverproofService {

    @Resource
    WaterCurrentOverproofMapper waterCurrentOverproofMapper;

    @Override
    public List<OverEntry> queryOverWater(List<String> companyIds, String area, String code, String mn, Timestamp end, Timestamp begin) {
        return waterCurrentOverproofMapper.queryOverWater(companyIds ,area ,code ,mn ,end ,begin);
    }

    @Override
    public Page<OverEntry> queryOverWater(Page<OverEntry> page, List<String> companyIds, String area, String code, String mn, Timestamp end, Timestamp begin) {
        return page.setRecords(waterCurrentOverproofMapper.queryOverWater(page,companyIds ,area ,code ,mn ,end ,begin));
    }

    @Override
    public List<OverEntryReport> queryOverWaterReport(List<String> companyIds, String area, String code, String mn, Timestamp end, Timestamp begin) {
        return waterCurrentOverproofMapper.queryOverWaterReport(companyIds, area, code, mn, end, begin);
    }

    @Override
    public Page<OverEntryReport> queryOverWaterReport(Page<OverEntryReport> page, List<String> companyIds, String area, String code, String mn, Timestamp end, Timestamp begin) {
        return page.setRecords(waterCurrentOverproofMapper.queryOverWaterReport(page, companyIds, area, code, mn, end, begin));
    }
}
