package org.jeecg.modules.business.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.business.entity.AirqYear;
import org.jeecg.modules.business.mapper.AirqYearMapper;
import org.jeecg.modules.business.service.IAirqYearService;
import org.jeecg.modules.business.utils.RedisCacheUtil;
import org.jeecg.modules.business.vo.AirqVO;
import org.jeecg.modules.business.vo.AirqYearQualityVO;
import org.jeecg.modules.business.vo.SiteQualityRankYearVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: airq_year
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Service
public class AirqYearServiceImpl extends ServiceImpl<AirqYearMapper, AirqYear> implements IAirqYearService {

    @Resource
    private AirqYearMapper airqYearMapper;

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
        return airqYearMapper.findEvaluate(StringUtils.join(mnsTemp.toArray(), ","),timeStart,timeEnd);
    }

    @Override
    public Page<AirqYearQualityVO> queryAirqYearQuality(String companyIds, Page page, String area, String mn, String yearBegin,String yearEnd) {
        List<AirqYearQualityVO> airqYearQualityVOS = airqYearMapper.queryAirqYearQuality(companyIds.split(","), page,area,mn,yearBegin,yearEnd);
        airqYearQualityVOS.forEach(airqYearQualityVO -> {
            airqYearQualityVO.setMeaning(redisCacheUtil.transformCode(airqYearQualityVO.getFirstCode()));
        });
        return page.setRecords(airqYearQualityVOS);
    }

    @Override
    public List<AirqYearQualityVO> exportAirqYearQuality(String companyIds, String area, String mn, String yearBegin, String yearEnd) {
        List<AirqYearQualityVO> airqYearQualityVOS = airqYearMapper.exportAirqYearQuality(companyIds.split(","),area,mn,yearBegin,yearEnd);
        airqYearQualityVOS.forEach(airqYearQualityVO -> {
            airqYearQualityVO.setMeaning(redisCacheUtil.transformCode(airqYearQualityVO.getFirstCode()));
        });
        return airqYearQualityVOS;
    }

    @Override
    public Page<SiteQualityRankYearVO> querySiteYear(String companyIds, Page page, String area, String mn, String queryDate) {
        List<SiteQualityRankYearVO> airqYearQualityVOS = airqYearMapper.querySiteYear(companyIds.split(","), page,area,mn,queryDate);
        airqYearQualityVOS.forEach(airqYearQualityVO -> {
            airqYearQualityVO.setMeaning(redisCacheUtil.transformCode(airqYearQualityVO.getFirstCode()));
        });
        return page.setRecords(airqYearQualityVOS);
    }

    @Override
    public List<SiteQualityRankYearVO> exportSiteYear(String companyIds, String area, String mn, String queryDate) {
        List<SiteQualityRankYearVO> airqYearQualityVOS = airqYearMapper.querySiteYear(companyIds.split(","), null,area,mn,queryDate);
        airqYearQualityVOS.forEach(airqYearQualityVO -> {
            airqYearQualityVO.setMeaning(redisCacheUtil.transformCode(airqYearQualityVO.getFirstCode()));
        });
        return airqYearQualityVOS;
    }
}
