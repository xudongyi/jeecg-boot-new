package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirqYear;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.AirqVO;
import org.jeecg.modules.business.vo.AirqYearQualityVO;
import org.jeecg.modules.business.vo.SiteQualityRankMonthVO;
import org.jeecg.modules.business.vo.SiteQualityRankYearVO;

import java.util.List;

/**
 * @Description: airq_year
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface IAirqYearService extends IService<AirqYear> {

    List<AirqVO> findEvaluate(String searchTime, List<String> mns);

    Page<AirqYearQualityVO> queryAirqYearQuality(String companyIds, Page page, String area, String mn, String yearBegin,String yearEnd);
    List<AirqYearQualityVO> exportAirqYearQuality(String companyIds, String area, String mn, String yearBegin,String yearEnd);

    Page<SiteQualityRankYearVO> querySiteYear(String companyIds, Page page, String area, String mn, String queryDate);
    List<SiteQualityRankYearVO> exportSiteYear(String companyIds, String area, String mn, String queryDate);
}
