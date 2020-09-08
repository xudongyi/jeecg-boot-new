package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.entity.AirMinute;
import org.jeecg.modules.business.mapper.AirMinuteMapper;
import org.jeecg.modules.business.mapper.WaterMinuteMapper;
import org.jeecg.modules.business.service.IAirMinuteService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: air_minute
 * @Author: jeecg-boot
 * @Date:   2020-09-07
 * @Version: V1.0
 */
@Service
public class AirMinuteServiceImpl extends ServiceImpl<AirMinuteMapper, AirMinute> implements IAirMinuteService {
    @Resource
    AirMinuteMapper airMinuteMapper;

    @Override
    public Page<Map<String, Object>> queryMinute(Page<Map<String, Object>> page, String field, String tableName, List<String> companyIds,
                                                 String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return page.setRecords(airMinuteMapper.queryMaxMinute(page,field,tableName,companyIds,area,mn));

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.MINUTE).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.MINUTE).toTimestamp();
            return page.setRecords(airMinuteMapper.queryMinute(page,field,tableName,companyIds,area,mn,begin,end));
        }
    }
    @Override
    public List<Map<String, Object>> queryMinute(String field, String tableName, List<String> companyIds,
                                                 String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return airMinuteMapper.queryMaxMinute(field,tableName,companyIds,area,mn);

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.MINUTE).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.MINUTE).toTimestamp();
            return airMinuteMapper.queryMinute(field,tableName,companyIds,area,mn,begin,end);
        }
    }
}
