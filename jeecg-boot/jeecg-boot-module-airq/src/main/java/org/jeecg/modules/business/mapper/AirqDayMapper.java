package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.AirqDay;
import org.jeecg.modules.business.vo.AirqDayQualityVo;
import org.jeecg.modules.business.vo.AirqVO;
import org.jeecg.modules.business.vo.SiteQualityRankDayVO;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: airq_day
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface AirqDayMapper extends BaseMapper<AirqDay> {

    List<AirqVO> findEvaluate(String mns, String timeStart, String timeEnd);

    List<AirqDayQualityVo> queryDayAirQuality(@Param("companyIds")List<String> companyIds, @Param("datatime") Timestamp datatime, @Param("datatime2")Timestamp datatime2, @Param("area")String area, @Param("mn")String mn);

    List<SiteQualityRankDayVO> querySiteDay(String[] companyIds, Page page, String area, String mn, Date queryDate);


    List<Map<String,Object>> queryCalendarAirQuality(@Param("datatime") Timestamp datatime, @Param("datatime2")Timestamp datatime2, @Param("area")String area, @Param("checkedKeys")List<String> checkedKeys);
    List<SiteQualityRankDayVO> querySiteDayExport(String[] companyIds, String area, String mn, Date queryDate);

    List<Map<String,Object>> queryDayChartInfo(@Param("mn")String mn,@Param("dateBegin")Timestamp dateBegin,@Param("dateEnd")Timestamp dateEnd);
}
