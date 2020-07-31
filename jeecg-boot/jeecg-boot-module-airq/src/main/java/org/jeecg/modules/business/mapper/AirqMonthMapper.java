package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirqMonth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface AirqMonthMapper extends BaseMapper<AirqMonth> {

    List<AirqVO> findEvaluate(String mns, String timeStart, String timeEnd);

    List<AirqMonthQualityVO> queryAirqMonthQuality(String[] companyIds, Page page, String area, String mn, String searchTime,String startTime, String endTime);

    List<SiteQualityRankMonthVO> querySiteMonth(String[] companyIds, Page page, String area, String mn, String queryDate);
}
