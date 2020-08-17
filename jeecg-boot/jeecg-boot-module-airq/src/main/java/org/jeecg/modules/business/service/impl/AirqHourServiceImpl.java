package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.AirqHour;
import org.jeecg.modules.business.entity.AirqLevel;
import org.jeecg.modules.business.mapper.AirqHourMapper;
import org.jeecg.modules.business.service.IAirqHourService;
import org.jeecg.modules.business.utils.RedisCacheUtil;
import org.jeecg.modules.business.vo.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Service
public class AirqHourServiceImpl extends ServiceImpl<AirqHourMapper, AirqHour> implements IAirqHourService {


    @Resource
    private RedisCacheUtil<AirqLevel> redisCacheUtil;
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
            airqHourQualityVo.setLeveltype(airqHourQualityVo.getLevel());
        });
        return airqHourQualityVos;
    }

    @Override
    public LinkedHashMap<String,List<AirHourPlayVo>> queryPollutionCloud(List<String> companyIds, String datatime, String datatime2) {
        Timestamp ts = DateUtil.parse(datatime, "yyyy-MM-dd").toTimestamp();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.parse(datatime2, "yyyy-MM-dd"));
        calendar.set(Calendar.HOUR,23);
        Timestamp ts2 = new Timestamp(calendar.getTimeInMillis());
        LinkedHashMap<String,List<AirHourPlayVo>> result= new LinkedHashMap<>();
        airqHourMapper.queryPollutionCloud(companyIds,ts,ts2).forEach(airHourPlayVo->{
            String dataTime = DateUtil.format(airHourPlayVo.getDataTime(),"yyyy年MM月dd日HH时");
            result.putIfAbsent(dataTime,new ArrayList<>());
            result.get(dataTime).add(airHourPlayVo);
        });
        return result;
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
    public List<AirqHourInputVO> queryManInputExport(String companyIds, String area, String mn, Date dateBegin, Date dateEnd) {
        List<AirqHourInputVO> airqHourInputVOS = airqHourMapper.queryManInputExport(companyIds.split(","), area,mn,dateBegin,dateEnd);
        airqHourInputVOS.forEach(airqHourInputVO -> {
            airqHourInputVO.setMeaning(redisCacheUtil.transformCode(airqHourInputVO.getFirstCode()));
        });
        return airqHourInputVOS;
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
            if(!StrUtil.isEmpty(siteQualityEvaluateVO.getLevel())){
                AirqLevel airqLevel = redisCacheUtil.getAdviceAndContent(siteQualityEvaluateVO.getLevel(), AirqLevel.class);
                siteQualityEvaluateVO.setAdvice(airqLevel.getAdvice());
                siteQualityEvaluateVO.setLevelContent(airqLevel.getLevelContent());
            }
        });
        return page.setRecords(siteQualityEvaluateVOS);
    }

    @Override
    public List<Map<String, Object>> queryAirSiteInfo(List<String> companyIds, String mn) {
        return airqHourMapper.queryAirSiteInfo(companyIds, mn);
    }

    @Override
    public List<Map<String, Object>> queryHourChartInfo(String mn) {
        //近72小时
        Date timeEnd = new Date();
        Date timeBegin = DateUtil.offsetHour(timeEnd, -72);
        Timestamp dateBegin = new Timestamp(timeBegin.getTime());
        Timestamp dateEnd = new Timestamp(timeEnd.getTime());
        return airqHourMapper.queryHourChartInfo(mn,dateBegin,dateEnd);
    }

    @Override
    public List<AirHourPlayVo> queryAirAvgInfo(List<String> companyIds, DateTime nowDate) {
        return airqHourMapper.queryAirAvgInfo(companyIds,nowDate);
    }


}
