package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirqDay;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.AirqDayQualityVo;
import org.jeecg.modules.business.vo.AirqHourQualityVo;
import org.jeecg.modules.business.vo.AirqVO;
import org.jeecg.modules.business.vo.SiteQualityRankDayVO;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: airq_day
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface IAirqDayService extends IService<AirqDay> {

    List<AirqDayQualityVo> queryDayAirQuality(List<String> companyIds, String datatime, String datatime2, String area, String mn);
    List<AirqVO> findEvaluate(String searchTime, List<String> mns);
    List<Map<String,Object>>  queryCalendarAirQuality(String datatime, String datatime2,String area,List<String> checkedKeys);
    Page<SiteQualityRankDayVO> querySiteDay(String companyIds,Page page, String area, String mn, Date queryDate);
    List<SiteQualityRankDayVO> querySiteDayExport(String companyIds, String area, String mn, Date queryDate);

    List<Map<String,Object>> queryDayChartInfo(String mn);
}
