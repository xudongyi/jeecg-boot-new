package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.entity.AirDay;
import org.jeecg.modules.business.mapper.AirDayMapper;
import org.jeecg.modules.business.mapper.WaterDayMapper;
import org.jeecg.modules.business.service.IAirDayService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: air_day
 * @Author: jeecg-boot
 * @Date:   2020-09-07
 * @Version: V1.0
 */
@Service
public class AirDayServiceImpl extends ServiceImpl<AirDayMapper, AirDay> implements IAirDayService {

    @Resource
    AirDayMapper airDayMapper;
    @Override
    public Page<Map<String, Object>> queryDay(Page<Map<String, Object>> page, String field, List<String> companyIds,
                                              String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return page.setRecords(airDayMapper.queryMaxDay(page,field,companyIds,area,mn));

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.DAY).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.DAY).toTimestamp();
            return page.setRecords(airDayMapper.queryDay(page,field,companyIds,area,mn,begin,end));
        }
    }
    @Override
    public List<Map<String, Object>> queryDay( String field, List<String> companyIds,
                                               String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return airDayMapper.queryMaxDay(field,companyIds,area,mn);

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.DAY).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.DAY).toTimestamp();
            return airDayMapper.queryDay(field,companyIds,area,mn,begin,end);
        }
    }
}
