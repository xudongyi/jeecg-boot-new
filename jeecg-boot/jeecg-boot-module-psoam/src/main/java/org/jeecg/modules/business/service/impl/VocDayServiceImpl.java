package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.entity.VocDay;
import org.jeecg.modules.business.mapper.VocDayMapper;
import org.jeecg.modules.business.service.IVocDayService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: voc_day
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
@Service
public class VocDayServiceImpl extends ServiceImpl<VocDayMapper, VocDay> implements IVocDayService {

    @Resource
    private VocDayMapper vocDayMapper;

    @Override
    public List<Map<String, Object>> queryCompanyName(List<String> companyIds) {
        return vocDayMapper.queryCompanyName(companyIds);
    }

    @Override
    public Page<List<Map<String, Object>>> queryDay(Page<List<Map<String, Object>>> page, String field, List<String> companyIds, String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return page.setRecords(vocDayMapper.queryMaxDay(page,field,companyIds,area,mn));

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.DAY).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.DAY).toTimestamp();
            return page.setRecords(vocDayMapper.queryDay(page,field,companyIds,area,mn,begin,end));
        }
    }
}
