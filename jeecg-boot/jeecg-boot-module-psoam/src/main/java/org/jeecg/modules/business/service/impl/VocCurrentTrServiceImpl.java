package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.entity.VocCurrentTr_2009;
import org.jeecg.modules.business.mapper.VocCurrentTrMapper;
import org.jeecg.modules.business.service.IVocCurrentTrService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: voc_current_tr_2009
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
@Service
public class VocCurrentTrServiceImpl extends ServiceImpl<VocCurrentTrMapper, VocCurrentTr_2009> implements IVocCurrentTrService {
    @Resource
    private VocCurrentTrMapper vocCurrentTrMapper;

    @Override
    public Page<List<Map<String, Object>>> queryRealTime(Page<List<Map<String, Object>>> page, String field, String tableName,
                                                         List<String> companyIds, String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return page.setRecords(vocCurrentTrMapper.queryMaxRealTime(page,field,tableName,companyIds,area,mn));

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.REALTIME).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.REALTIME).toTimestamp();
            return page.setRecords(vocCurrentTrMapper.queryRealTime(page,field,tableName,companyIds,area,mn,begin,end));
        }
    }

    @Override
    public List<Map<String, Object>> queryRealTime(String field, String tableName, List<String> companyIds, String area, String mn, String dataTime_begin, String dataTime_end) {
        if(StrUtil.isEmpty(dataTime_begin))
        {
            return vocCurrentTrMapper.queryMaxRealTime(field,tableName,companyIds,area,mn);

        }
        else
        {
            Timestamp begin = DateUtil.parse(dataTime_begin, PollutionSource.DataFormat.REALTIME).toTimestamp();
            Timestamp end = DateUtil.parse(dataTime_end, PollutionSource.DataFormat.REALTIME).toTimestamp();
            return vocCurrentTrMapper.queryRealTime(field,tableName,companyIds,area,mn,begin,end);
        }
    }
}
