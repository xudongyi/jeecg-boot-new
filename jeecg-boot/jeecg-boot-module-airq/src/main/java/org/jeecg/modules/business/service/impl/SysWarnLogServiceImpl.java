package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.SysWarnLog;
import org.jeecg.modules.business.mapper.SysWarnLogMapper;
import org.jeecg.modules.business.service.ISysWarnLogService;
import org.jeecg.modules.business.vo.WarnLogVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 报警日志表
 * @Author: jeecg-boot
 * @Date:   2020-07-30
 * @Version: V1.0
 */
@Service
public class SysWarnLogServiceImpl extends ServiceImpl<SysWarnLogMapper, SysWarnLog> implements ISysWarnLogService {

    @Resource
    private SysWarnLogMapper sysWarnLogMapper;

    @Override
    public Page<WarnLogVO> queryWarnLogInfo(String companyIds, Page page, String area, String monitorId, String timeBegin, String timeEnd,String flag) {
        Timestamp dateBegin = null;
        Timestamp dateEnd = null;
        if(StrUtil.isNotEmpty(timeBegin) && StrUtil.isNotEmpty(timeEnd)) {
            dateBegin = DateUtil.parse(timeBegin, "yyyy-MM-dd").toTimestamp();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtil.parse(timeEnd, "yyyy-MM-dd"));
            calendar.set(Calendar.HOUR,23);
            dateEnd = new Timestamp(calendar.getTimeInMillis());
        }

        return page.setRecords(sysWarnLogMapper.queryWarnLogInfo(companyIds.split(","),page,area,monitorId,dateBegin,dateEnd,flag));
    }

    @Override
    public List<WarnLogVO> exportWarnLogInfo(String companyIds, String area, String monitorId, String timeBegin, String timeEnd, String flag) {
        Timestamp dateBegin = null;
        Timestamp dateEnd = null;
        if(StrUtil.isNotEmpty(timeBegin) && StrUtil.isNotEmpty(timeEnd)) {
            dateBegin = DateUtil.parse(timeBegin, "yyyy-MM-dd").toTimestamp();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtil.parse(timeEnd, "yyyy-MM-dd"));
            calendar.set(Calendar.HOUR,23);
            dateEnd = new Timestamp(calendar.getTimeInMillis());
        }
        return sysWarnLogMapper.exportWarnLogInfo(companyIds.split(","),area,monitorId,dateBegin,dateEnd,flag);
    }

    @Override
    public List<Map<String, Object>> queryWarnInfo(List<String> companyIds, String monitorId, String timeBegin, String timeEnd, String flag) {
        Timestamp dateBegin = null;
        Timestamp dateEnd = null;
        if(StrUtil.isNotEmpty(timeBegin) && StrUtil.isNotEmpty(timeEnd)) {
            dateBegin = DateUtil.parse(timeBegin, "yyyy-MM-dd HH:mm").toTimestamp();
            dateEnd = DateUtil.parse(timeEnd, "yyyy-MM-dd HH:mm").toTimestamp();
        }
        return sysWarnLogMapper.queryWarnInfo(companyIds, monitorId, dateBegin, dateEnd, flag);
    }

    @Override
    public List<Map<String, Object>> queryAppPie(List<String> companyIds) {
        return sysWarnLogMapper.queryAppPie(companyIds);
    }

    @Override
    public Integer queryAppColumn(List<String> companyIds, DateTime startTime, DateTime endTime) {
        return sysWarnLogMapper.queryAppColumn(companyIds,startTime,endTime);
    }
}
