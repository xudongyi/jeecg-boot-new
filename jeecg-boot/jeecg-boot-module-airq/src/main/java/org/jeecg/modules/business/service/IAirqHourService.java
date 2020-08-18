package org.jeecg.modules.business.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirqHour;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface IAirqHourService extends IService<AirqHour> {

    List<AirSiteInfo> queryInfoByCompanyId(List<String> companyIds);
    List<AirqHourQualityVo> queryHourAirQuality(List<String> companyIds, String datatime,String datatime2,String area,String mn);
    LinkedHashMap<String,List<AirHourPlayVo>> queryPollutionCloud(List<String> companyIds, String datatime, String datatime2);
    Page<AirqHourMonitorVO> queryAirqHourMonitor(String companyIds,Page page, String area, String mn, Date dateBegin, Date dateEnd);

    Page<AirqHourInputVO> queryAirqHourInput(String companyIds,Page page, String area, String mn, Date dateBegin, Date dateEnd);
    List<AirqHourInputVO> queryManInputExport(String companyIds, String area, String mn, Date dateBegin, Date dateEnd);

    Page<AirqHourManInsertVO> queryAirqHourManInsert(String companyIds,Page page, String area, String mn,Integer state, Date dateBegin, Date dateEnd);

    Page<AirqHourAuditVO> queryAirqHourAudit(String companyIds, Page page, String area, String mn, Integer state, Date dateBegin, Date dateEnd);

    Page<SiteQualityEvaluateVO> querySiteQualityEvaluate(String companyIds, Page page, String area, String mn,String level, Integer state, Date dateBegin, Date dateEnd);

    List<Map<String,Object>> queryAirSiteInfo(List<String> companyIds, String mn);

    List<Map<String,Object>> queryHourChartInfo(String mn);

    List<AirHourPlayVo> queryAirAvgInfo(List<String> asList, DateTime nowDate);

    List<AirqAppLineVO> queryAppLine(List<String> asList, DateTime startTime, DateTime endTime);

    List<Map<String,Object>> queryAirMoreInfo(String mn,String dateBegin);
}
