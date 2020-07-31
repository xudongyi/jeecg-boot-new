package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirqMonth;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.AirqMonthQualityVO;
import org.jeecg.modules.business.vo.AirqVO;
import org.jeecg.modules.business.vo.SiteQualityRankMonthVO;

import java.util.List;

/**
 * @Description: airq_month
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface IAirqMonthService extends IService<AirqMonth> {

    List<AirqVO> findEvaluate(String searchTime, List<String> mns);

    IPage<AirqMonthQualityVO> queryAirqMonthQuality(String companyIds, Page<AirqMonthQualityVO> page, String area, String mn,String searchTime,String startTime, String endTime);

    List<AirqMonthQualityVO> exportAirqMonthQuality(String companyIds, String area, String mn, String searchTime, String startTime, String endTime);

    IPage<SiteQualityRankMonthVO> querySiteMonth(String companyIds, Page<SiteQualityRankMonthVO> page, String area, String mn, String queryDate);
    List<SiteQualityRankMonthVO> exportSiteMonth(String companyIds, String area, String mn, String queryDate);
}
