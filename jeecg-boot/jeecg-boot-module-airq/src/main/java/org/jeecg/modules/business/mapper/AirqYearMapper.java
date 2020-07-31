package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirqYear;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.AirqVO;
import org.jeecg.modules.business.vo.AirqYearQualityVO;
import org.jeecg.modules.business.vo.SiteQualityRankYearVO;

import java.util.List;

/**
 * @Description: airq_year
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface AirqYearMapper extends BaseMapper<AirqYear> {

    List<AirqVO> findEvaluate(String mns, String timeStart, String timeEnd);

    List<AirqYearQualityVO> queryAirqYearQuality(String[] companyIds, Page page, String area, String mn, String yearBegin,String yearEnd);
    List<AirqYearQualityVO> exportAirqYearQuality(String[] companyIds, String area, String mn, String yearBegin,String yearEnd);

    List<SiteQualityRankYearVO> querySiteYear(String[] companyIds, Page page, String area, String mn, String queryDate);
 }
