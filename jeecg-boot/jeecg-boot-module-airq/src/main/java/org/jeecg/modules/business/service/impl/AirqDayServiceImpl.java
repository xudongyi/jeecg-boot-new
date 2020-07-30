package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.business.entity.AirqDay;
import org.jeecg.modules.business.mapper.AirqDayMapper;
import org.jeecg.modules.business.service.IAirqDayService;
import org.jeecg.modules.business.utils.RedisCacheUtil;
import org.jeecg.modules.business.vo.AirqDayQualityVo;
import org.jeecg.modules.business.vo.AirqVO;
import org.jeecg.modules.business.vo.SiteQualityRankDayVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: airq_day
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Service
public class AirqDayServiceImpl extends ServiceImpl<AirqDayMapper, AirqDay> implements IAirqDayService {
    @Resource
    private AirqDayMapper airqDayMapper;
    @Resource
    private RedisCacheUtil redisCacheUtil;
    @Override
    public List<AirqVO> findEvaluate(String searchTime, List<String> mns) {
        String[] times = null;
        String timeStart = null;
        String timeEnd = null;
        if(StrUtil.isNotEmpty(searchTime)){
            times = searchTime.split(",");
            timeStart = times[0];
            timeEnd = times[1];
        }
        List<String> mnsTemp = new ArrayList<>();
        for(int i = 0 ;i< mns.size();i++){
            mnsTemp.add(i,"'"+mns.get(i)+"'");
        }
       return airqDayMapper.findEvaluate(StringUtils.join(mnsTemp.toArray(), ","),timeStart,timeEnd);
    }

    @Override
    public List<Map<String, Object>> queryCalendarAirQuality(String datatime, String datatime2, String area, List<String> checkedKeys) {
        Timestamp ts = DateUtil.parse(datatime, "yyyy-MM-dd").toTimestamp();
        Timestamp ts2 = DateUtil.parse(datatime2, "yyyy-MM-dd").toTimestamp();
        List<Map<String,Object>>  airQualitys = airqDayMapper.queryCalendarAirQuality(ts,ts2,area,checkedKeys);
        airQualitys.forEach(airQuality -> {
            airQuality.put("firstCode",redisCacheUtil.transformCode(String.valueOf(airQuality.get("firstCode"))));
        });
        return airQualitys;
    }

    @Override
    public List<AirqDayQualityVo> queryDayAirQuality(List<String> companyIds, String datatime, String datatime2, String area, String mn) {
        Timestamp ts = DateUtil.parse(datatime, "yyyy-MM-dd").toTimestamp();
        Timestamp ts2 = DateUtil.parse(datatime2, "yyyy-MM-dd").toTimestamp();
        List<AirqDayQualityVo>  airqDayQualityVos = airqDayMapper.queryDayAirQuality(companyIds,ts,ts2,area,mn);
        airqDayQualityVos.forEach(airqDayQualityVo -> {
            airqDayQualityVo.setMeaning(redisCacheUtil.transformCode(airqDayQualityVo.getFirstCode()));
        });
        return airqDayQualityVos;
    }

    @Override
    public Page<SiteQualityRankDayVO> querySiteDay(String companyIds, Page page, String area, String mn, Date queryDate) {
        List<SiteQualityRankDayVO> siteQualityRankDayVOS = airqDayMapper.querySiteDay(companyIds.split(","),page,area,mn,queryDate);
        siteQualityRankDayVOS.forEach(siteQualityRankDayVO -> {
            siteQualityRankDayVO.setMeaning(redisCacheUtil.transformCode(siteQualityRankDayVO.getFirstCode()));
        });
        return page.setRecords(siteQualityRankDayVOS);
    }

    @Override
    public List<SiteQualityRankDayVO> querySiteDayExport(String companyIds, String area, String mn, Date queryDate) {
        List<SiteQualityRankDayVO> siteQualityRankDayVOS = airqDayMapper.querySiteDayExport(companyIds.split(","),area,mn,queryDate);
        siteQualityRankDayVOS.forEach(siteQualityRankDayVO -> {
            siteQualityRankDayVO.setMeaning(redisCacheUtil.transformCode(siteQualityRankDayVO.getFirstCode()));
        });
        return siteQualityRankDayVOS;
    }
}
