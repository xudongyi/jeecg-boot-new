package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.WarnLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.OverEntry;
import org.jeecg.modules.business.vo.RealTimeWarn;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: warn_log
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface IWarnLogService extends IService<WarnLog> {
    Page<RealTimeWarn> queryWarn(Page<RealTimeWarn> page, List<String> companyIds, String area, String type,String warnType,
                              String mn, Timestamp end, Timestamp begin);
    List<RealTimeWarn> queryWarn(List<String> companyIds, String area, String type,String warnType,
                                 String mn, Timestamp end, Timestamp begin);
}
