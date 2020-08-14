package org.jeecg.modules.business.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.AirqHour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface AirqHourMapper extends BaseMapper<AirqHour> {

    List<AirSiteInfo> queryInfoByCompanyId(@Param("companyIds")List<String> companyIds);
    List<AirqHourQualityVo> queryHourAirQuality(@Param("companyIds")List<String> companyIds, @Param("datatime")Timestamp datatime,@Param("datatime2")Timestamp datatime2,@Param("area")String area, @Param("mn")String mn);
    List<AirHourPlayVo> queryPollutionCloud(@Param("companyIds")List<String> companyIds,  @Param("datatime")Timestamp datatime, @Param("datatime2")Timestamp datatime2);

    List<AirqHourMonitorVO> queryAirqHourMonitor(String[] companyIds,Page page,String area,String mn, Date dateBegin, Date dateEnd);

    List<AirqHourInputVO> queryAirqHourInput(String[] companyIds,Page page, String area, String mn, Date dateBegin, Date dateEnd);
    List<AirqHourInputVO> queryManInputExport(String[] companyIds, String area, String mn, Date dateBegin, Date dateEnd);

    List<AirqHourManInsertVO> queryAirqHourManInsert(String[] companyIds,Page page, String area, String mn, Integer state,Date dateBegin, Date dateEnd);

    List<AirqHourAuditVO> queryAirqHourAudit(String[] companyIds, Page page, String area, String mn, Integer state, Date dateBegin, Date dateEnd);

    List<SiteQualityEvaluateVO> querySiteQualityEvaluate(String[] companyIds, Page page, String area, String mn,String level, Integer state, Date dateBegin, Date dateEnd);

    List<Map<String,Object>> queryAirSiteInfo(@Param("companyIds")List<String> companyIds,@Param("mn")String mn);

    List<AirHourPlayVo> queryAirAvgInfo(@Param("companyIds")List<String> companyIds,@Param("dataTime") DateTime nowDate);
}
