package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.entity.VocMinute_2009;
import org.jeecg.modules.business.mapper.VocMinuteMapper;
import org.jeecg.modules.business.service.IVocMinuteService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: voc_minute_2009
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
@Service
public class VocMinuteServiceImpl extends ServiceImpl<VocMinuteMapper, VocMinute_2009> implements IVocMinuteService {

    @Resource
    private VocMinuteMapper vocMinuteMapper;

    @Override
    public Page<List<Map<String, Object>>> queryMinute(Page<List<Map<String, Object>>> page, String field, String tableName, List<String> companyIds, String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return page.setRecords(vocMinuteMapper.queryMaxMinute(page,field,tableName,companyIds,area,mn));

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.MINUTE).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.MINUTE).toTimestamp();
            return page.setRecords(vocMinuteMapper.queryMinute(page,field,tableName,companyIds,area,mn,begin,end));
        }
    }

    @Override
    public List<Map<String, Object>> queryMinute(String field, String tableName, List<String> companyIds, String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return vocMinuteMapper.queryMaxMinute(field,tableName,companyIds,area,mn);

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.MINUTE).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.MINUTE).toTimestamp();
            return vocMinuteMapper.queryMinute(field,tableName,companyIds,area,mn,begin,end);
        }
    }
}
