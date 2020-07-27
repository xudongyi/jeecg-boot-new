package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.AirqHour;
import org.jeecg.modules.business.mapper.AirqHourMapper;
import org.jeecg.modules.business.service.IAirqHourService;
import org.jeecg.modules.business.utils.RedisCacheUtil;
import org.jeecg.modules.business.vo.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Service
public class AirqHourServiceImpl extends ServiceImpl<AirqHourMapper, AirqHour> implements IAirqHourService {

    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private RedisCacheUtil redisCacheUtil;
    @Resource
    private AirqHourMapper airqHourMapper;

    @Override
    public List<AirSiteInfo> queryInfoByCompanyId(List<String> companyIds) {
        List<AirSiteInfo> airSiteInfos =  airqHourMapper.queryInfoByCompanyId(companyIds);
        airSiteInfos.forEach(airSiteInfo -> {
            airSiteInfo.setMeaning(redisCacheUtil.transformCode(airSiteInfo.getFirstCode()));
        });
        return airSiteInfos;
    }

    @Override
    public List<AirqHourQualityVo> queryHourAirQuality(List<String> companyIds, String datatime,String datatime2,String area,String mn) {
        Timestamp ts = DateUtil.parse(datatime, "yyyy-MM-dd HH").toTimestamp();
        Timestamp ts2 = DateUtil.parse(datatime2, "yyyy-MM-dd HH").toTimestamp();
        List<AirqHourQualityVo> airqHourQualityVos = airqHourMapper.queryHourAirQuality(companyIds,ts,ts2,area,mn);
        airqHourQualityVos.forEach(airqHourQualityVo -> {
            airqHourQualityVo.setMeaning(redisCacheUtil.transformCode(airqHourQualityVo.getFirstCode()));
        });
        return airqHourQualityVos;
    }

    @Override
    public Page<AirqHourMonitorVO> queryAirqHourMonitor(String companyIds,Page page, String area, String mn, Date dateBegin, Date dateEnd) {
        List<AirqHourMonitorVO> airqHourMonitorVOS = airqHourMapper.queryAirqHourMonitor(companyIds.split(","),page, area, mn, dateBegin, dateEnd);
        airqHourMonitorVOS.forEach(airqHourMonitorVO -> {
            airqHourMonitorVO.setMeaning(redisCacheUtil.transformCode(airqHourMonitorVO.getFirstCode()));
        });
        return page.setRecords(airqHourMonitorVOS);
    }

    @Override
    public Page<AirqHourInputVO> queryAirqHourInput(String companyIds,Page page, String area, String mn, Date dateBegin, Date dateEnd) {
        List<AirqHourInputVO> airqHourInputVOS = airqHourMapper.queryAirqHourInput(companyIds.split(","),page, area, mn, dateBegin, dateEnd);
        airqHourInputVOS.forEach(airqHourInputVO -> {
            airqHourInputVO.setMeaning(redisCacheUtil.transformCode(airqHourInputVO.getFirstCode()));
        });
        return page.setRecords(airqHourInputVOS);
    }

    @Override
    public Page<AirqHourManInsertVO> queryAirqHourManInsert(String companyIds,Page page, String area, String mn, Integer state, Date dateBegin, Date dateEnd) {
        return page.setRecords(airqHourMapper.queryAirqHourManInsert(companyIds.split(","),page, area, mn, state,dateBegin, dateEnd));
    }

    @Override
    public Page<AirqHourAuditVO> queryAirqHourAudit(String companyIds, Page page, String area, String mn, Integer state, Date dateBegin, Date dateEnd) {
        return page.setRecords(airqHourMapper.queryAirqHourAudit(companyIds.split(","),page, area, mn, state,dateBegin, dateEnd));
    }

    @Override
    public Page<SiteQualityEvaluateVO> querySiteQualityEvaluate(String companyIds, Page page, String area, String mn, String level, Integer state, Date dateBegin, Date dateEnd) {
        List<SiteQualityEvaluateVO> siteQualityEvaluateVOS = airqHourMapper.querySiteQualityEvaluate(companyIds.split(","),page,area,mn,level,state,dateBegin,dateEnd);
        siteQualityEvaluateVOS.forEach(siteQualityEvaluateVO -> {
            siteQualityEvaluateVO.setMeaning(redisCacheUtil.transformCode(siteQualityEvaluateVO.getFirstCode()));
        });
        return page.setRecords(siteQualityEvaluateVOS);
    }


}
