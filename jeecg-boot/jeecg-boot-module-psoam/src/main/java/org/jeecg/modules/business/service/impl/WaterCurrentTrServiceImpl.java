package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.constant.PollutionSource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.WaterCurrentTr;
import org.jeecg.modules.business.mapper.WaterCurrentTrMapper;
import org.jeecg.modules.business.service.IWaterCurrentTrService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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

    @Resource
    WaterCurrentTrMapper waterCurrentTrMapper;

    @Override
    public Page<List<Map<String, Object>>> queryRealTime(Page<List<Map<String, Object>>> page,String field,String tableName, List<String> companyIds,
                                                         String area,String mn,String dataTime_begin,String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return page.setRecords(waterCurrentTrMapper.queryMaxRealTime(page,field,tableName,companyIds,area,mn));

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.REALTIME).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.REALTIME).toTimestamp();
            return page.setRecords(waterCurrentTrMapper.queryRealTime(page,field,tableName,companyIds,area,mn,begin,end));
        }


    }
    @Override
    public IPage<Map<String, Object>> getWaterCurrentTrList(Page<Map<String, Object>> page, String area, String companyId, String mn,String tableName) {
        return page.setRecords(waterCurrentTrMapper.getWaterCurrentTrList(page,area,companyId,mn,tableName));
    }
}
