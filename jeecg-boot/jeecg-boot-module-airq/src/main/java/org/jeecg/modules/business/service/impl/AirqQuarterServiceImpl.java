package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirqQuarter;
import org.jeecg.modules.business.mapper.AirqQuarterMapper;
import org.jeecg.modules.business.service.IAirqQuarterService;
import org.jeecg.modules.business.utils.RedisCacheUtil;
import org.jeecg.modules.business.vo.AirqMonthQualityVO;
import org.jeecg.modules.business.vo.AirqQuarterQualityVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: airq_quarter
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Service
public class AirqQuarterServiceImpl extends ServiceImpl<AirqQuarterMapper, AirqQuarter> implements IAirqQuarterService {

    @Resource
    private AirqQuarterMapper airqQuarterMapper;

    @Resource
    private RedisCacheUtil redisCacheUtil;

    @Override
    public IPage<AirqQuarterQualityVO> queryAirqQuarterQuality(String companyIds, Page<AirqQuarterQualityVO> page, String area, String mn, String year, String quarter) {
        List<AirqQuarterQualityVO> airqQuarterQualityVOS = airqQuarterMapper.queryAirqQuarterQuality(companyIds.split(","),page,area,mn,year,quarter);
        airqQuarterQualityVOS.forEach(airqQuarterQualityVO -> {
            airqQuarterQualityVO.setMeaning(redisCacheUtil.transformCode(airqQuarterQualityVO.getFirstCode()));
        });
        return page.setRecords(airqQuarterQualityVOS);
    }

    @Override
    public List<AirqQuarterQualityVO> exportAirqQuarterQuality(String companyIds, String area, String mn, String year, String quarter) {
        List<AirqQuarterQualityVO> airqQuarterQualityVOS = airqQuarterMapper.queryAirqQuarterQuality(companyIds.split(","),null,area,mn,year,quarter);
        airqQuarterQualityVOS.forEach(airqQuarterQualityVO -> {
            airqQuarterQualityVO.setMeaning(redisCacheUtil.transformCode(airqQuarterQualityVO.getFirstCode()));
        });
        return airqQuarterQualityVOS;
    }
}
