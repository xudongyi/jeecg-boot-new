package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.entity.AirCurrentTr;
import org.jeecg.modules.business.mapper.AirCurrentTrMapper;
import org.jeecg.modules.business.mapper.WaterCurrentTrMapper;
import org.jeecg.modules.business.service.IAirCurrentTrService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: air_current_tr
 * @Author: jeecg-boot
 * @Date:   2020-09-07
 * @Version: V1.0
 */
@Service
public class AirCurrentTrServiceImpl extends ServiceImpl<AirCurrentTrMapper, AirCurrentTr> implements IAirCurrentTrService {

    @Resource
    private AirCurrentTrMapper airCurrentTrMapper;


    @Override
    public Page<Map<String, Object>> queryRealTime(Page<Map<String, Object>> page,String field,String tableName, List<String> companyIds,
                                                   String area,String mn,String dataTime_begin,String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return page.setRecords(airCurrentTrMapper.queryMaxRealTime(page,field,tableName,companyIds,area,mn));

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.REALTIME).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.REALTIME).toTimestamp();
            return page.setRecords(airCurrentTrMapper.queryRealTime(page,field,tableName,companyIds,area,mn,begin,end));
        }


    }
    @Override
    public List<Map<String, Object>> queryRealTime(String field,String tableName, List<String> companyIds,
                                                   String area,String mn,String dataTime_begin,String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return airCurrentTrMapper.queryMaxRealTime(field,tableName,companyIds,area,mn);

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.REALTIME).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.REALTIME).toTimestamp();
            return airCurrentTrMapper.queryRealTime(field,tableName,companyIds,area,mn,begin,end);
        }


    }
    @Override
    public IPage<Map<String, Object>> getAirCurrentTrList(Page<Map<String, Object>> page, String area, String companyId, String mn, String tableName, List<Integer> dataStatus, Integer offLine) {
        return page.setRecords(airCurrentTrMapper.getAirCurrentTrList(page,area,companyId,mn,tableName,dataStatus,offLine));
    }
}
