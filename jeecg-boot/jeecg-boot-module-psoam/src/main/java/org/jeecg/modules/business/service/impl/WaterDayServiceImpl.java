package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.entity.WaterDay;
import org.jeecg.modules.business.mapper.WaterDayMapper;
import org.jeecg.modules.business.service.IWaterDayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: water_day
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
@Service
public class WaterDayServiceImpl extends ServiceImpl<WaterDayMapper, WaterDay> implements IWaterDayService {
    @Resource
    WaterDayMapper waterDayMapper;
    @Override
    public Page<List<Map<String, Object>>> queryDay(Page<List<Map<String, Object>>> page, String field, List<String> companyIds,
                                                     String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return page.setRecords(waterDayMapper.queryMaxDay(page,field,companyIds,area,mn));

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.DAY).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.DAY).toTimestamp();
            return page.setRecords(waterDayMapper.queryDay(page,field,companyIds,area,mn,begin,end));
        }


    }
}
