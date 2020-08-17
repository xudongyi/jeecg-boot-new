package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.business.entity.AirqMonth;
import org.jeecg.modules.business.mapper.AirqMonthMapper;
import org.jeecg.modules.business.service.IAirqMonthService;
import org.jeecg.modules.business.utils.RedisCacheUtil;
import org.jeecg.modules.business.vo.AirqMonthQualityVO;
import org.jeecg.modules.business.vo.AirqVO;
import org.jeecg.modules.business.vo.AirqYearQualityVO;
import org.jeecg.modules.business.vo.SiteQualityRankMonthVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: airq_month
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Service
public class AirqMonthServiceImpl extends ServiceImpl<AirqMonthMapper, AirqMonth> implements IAirqMonthService {
    @Resource
    private AirqMonthMapper airqMonthMapper;

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
        return airqMonthMapper.findEvaluate(StringUtils.join(mnsTemp.toArray(), ","),timeStart,timeEnd);
    }

    @Override
    public IPage<AirqMonthQualityVO> queryAirqMonthQuality(String companyIds, Page<AirqMonthQualityVO> page, String area, String mn, String searchTime,String startTime, String endTime) {
        List<AirqMonthQualityVO> airqMonthQualityVOS = airqMonthMapper.queryAirqMonthQuality(companyIds.split(","), page,area,mn,searchTime,startTime,endTime);
        airqMonthQualityVOS.forEach(airqMonthQualityVO -> {
            airqMonthQualityVO.setMeaning(redisCacheUtil.transformCode(airqMonthQualityVO.getFirstCode()));
        });
        return page.setRecords(airqMonthQualityVOS);
    }

    @Override
    public List<AirqMonthQualityVO> exportAirqMonthQuality(String companyIds, String area, String mn, String searchTime, String startTime, String endTime) {
        List<AirqMonthQualityVO> airqMonthQualityVOS = airqMonthMapper.queryAirqMonthQuality(companyIds.split(","),null,area,mn,searchTime,startTime,endTime);
        airqMonthQualityVOS.forEach(airqMonthQualityVO -> {
            airqMonthQualityVO.setMeaning(redisCacheUtil.transformCode(airqMonthQualityVO.getFirstCode()));
        });
        return airqMonthQualityVOS;
    }

    @Override
    public IPage<SiteQualityRankMonthVO> querySiteMonth(String companyIds, Page<SiteQualityRankMonthVO> page, String area, String mn, String queryDate) {
        List<SiteQualityRankMonthVO> airqMonthQualityVOS = airqMonthMapper.querySiteMonth(companyIds.split(","), page,area,mn,queryDate);
        airqMonthQualityVOS.forEach(airqMonthQualityVO -> {
            airqMonthQualityVO.setMeaning(redisCacheUtil.transformCode(airqMonthQualityVO.getFirstCode()));
        });
        return page.setRecords(airqMonthQualityVOS);
    }

    @Override
    public List<SiteQualityRankMonthVO> exportSiteMonth(String companyIds, String area, String mn, String queryDate) {
        List<SiteQualityRankMonthVO> airqMonthQualityVOS = airqMonthMapper.querySiteMonth(companyIds.split(","), null,area,mn,queryDate);
        airqMonthQualityVOS.forEach(airqMonthQualityVO -> {
            airqMonthQualityVO.setMeaning(redisCacheUtil.transformCode(airqMonthQualityVO.getFirstCode()));
        });
        return airqMonthQualityVOS;
    }

    @Override
    public List<Map<String, Object>> queryMonthChartInfo(String mn) {
        //近12月
        String end = DateUtil.format(new Date(), "yyyy-MM");
        Date timeEnd = DateUtil.parse(end,"yyyy-MM");
        Date timeBegin = DateUtil.offsetMonth(timeEnd, -12);
        Timestamp dateBegin = new Timestamp(timeBegin.getTime());
        Timestamp dateEnd = new Timestamp(timeEnd.getTime());
        return airqMonthMapper.queryMonthChartInfo(mn,dateBegin,dateEnd);
    }

}
