package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.entity.VocHour;
import org.jeecg.modules.business.mapper.VocHourMapper;
import org.jeecg.modules.business.service.IVocHourService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: voc_hour
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
@Service
public class VocHourServiceImpl extends ServiceImpl<VocHourMapper, VocHour> implements IVocHourService {

    @Resource
    private VocHourMapper vocHourMapper;

    @Override
    public Page<List<Map<String, Object>>> queryHour(Page<List<Map<String, Object>>> page, String field, List<String> companyIds, String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return page.setRecords(vocHourMapper.queryMaxHour(page,field,companyIds,area,mn));

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.HOUR).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.HOUR).toTimestamp();
            return page.setRecords(vocHourMapper.queryHour(page,field,companyIds,area,mn,begin,end));
        }
    }

    @Override
    public List<Map<String, Object>> queryHour(String field, List<String> companyIds, String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return vocHourMapper.queryMaxHour(field,companyIds,area,mn);

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.HOUR).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.HOUR).toTimestamp();
            return vocHourMapper.queryHour(field,companyIds,area,mn,begin,end);
        }
    }
}
