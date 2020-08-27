package org.jeecg.modules.business.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirqDay;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.*;

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

    List<Map<String,Object>>  queryAirHomeCalendar(String datatime, String datatime2);

    Page<SiteQualityRankDayVO> querySiteDay(String companyIds,Page page, String area, String mn, Date queryDate);
    List<SiteQualityRankDayVO> querySiteDayExport(String companyIds, String area, String mn, Date queryDate);

    List<AirqAppLineVO> queryAppLine(List<String> companyIds, DateTime startTime, DateTime endTime);

    List<Map<String,Object>> queryDayChartInfo(String mn);

    List<Map<String,Object>> queryAirDayMoreInfo(String mn,String dateTime);

    Integer querDays(List<String> asList,int level, String startTime, String endTime);

}
