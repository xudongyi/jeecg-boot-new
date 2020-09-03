package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.WaterCurrentTr;
import org.jeecg.modules.business.mapper.WaterCurrentTrMapper;
import org.jeecg.modules.business.service.IWaterCurrentTrService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: water_current_tr
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
@Service
public class WaterCurrentTrServiceImpl extends ServiceImpl<WaterCurrentTrMapper, WaterCurrentTr> implements IWaterCurrentTrService {
    @Resource
    private WaterCurrentTrMapper waterCurrentTrMapper;

    @Override
    public IPage<Map<String, Object>> getWaterCurrentTrList(Page<Map<String, Object>> page, String area, String companyId, String mn, String type,String tableName) {
        return page.setRecords(waterCurrentTrMapper.getWaterCurrentTrList(page,area,companyId,mn,type,tableName));
    }
}
