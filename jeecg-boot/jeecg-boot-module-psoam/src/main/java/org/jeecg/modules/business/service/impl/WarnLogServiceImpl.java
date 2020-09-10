package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.WarnLog;
import org.jeecg.modules.business.mapper.WarnLogMapper;
import org.jeecg.modules.business.service.IWarnLogService;
import org.jeecg.modules.business.vo.RealTimeWarn;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: warn_log
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Service
public class WarnLogServiceImpl extends ServiceImpl<WarnLogMapper, WarnLog> implements IWarnLogService {
    @Resource
    WarnLogMapper warnLogMapper;

    @Override
    public Page<RealTimeWarn> queryWarn(Page<RealTimeWarn> page, List<String> companyIds, String area, String type, String warnType, String mn, Timestamp end, Timestamp begin) {
        return page.setRecords(warnLogMapper.queryWarn(page, companyIds, area, type, warnType, mn, end, begin));
    }

    @Override
    public List<RealTimeWarn> queryWarn(List<String> companyIds, String area, String type, String warnType, String mn, Timestamp end, Timestamp begin) {
        return warnLogMapper.queryWarn(companyIds, area, type, warnType, mn, end, begin);
    }
}
