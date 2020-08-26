package org.jeecg.modules.business.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.SysWarnLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.AirqHourInputVO;
import org.jeecg.modules.business.vo.WarnLogVO;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 报警日志表
 * @Author: jeecg-boot
 * @Date:   2020-07-30
 * @Version: V1.0
 */
public interface ISysWarnLogService extends IService<SysWarnLog> {
    Page<WarnLogVO> queryWarnLogInfo(String companyIds, Page page, String area, String monitorId, String dateBegin, String dateEnd,String flag);
    List<WarnLogVO> exportWarnLogInfo(String companyIds, String area, String monitorId, String dateBegin, String dateEnd,String flag);

    List<Map<String,Object>> queryWarnInfo(List<String> companyIds, String monitorId,String dateBegin, String dateEnd,String flag);

    List<Map<String, Object>> queryAppPie(List<String> companyIds);

    Integer queryAppColumn(List<String> companyIds, DateTime startTime, DateTime endTime);

    List<Map<String, Object>> queryAlarmInfo(List<String> companyIds);

    List<Map<String, Object>> querySiteState(List<String> companyIds, String startTime, String endTime);

    List<Map<String, Object>> querySiteNum(List<String> companyIds, String startTime, String endTime);
}
