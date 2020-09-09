package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirMinute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: air_minute
 * @Author: jeecg-boot
 * @Date:   2020-09-07
 * @Version: V1.0
 */
public interface IAirMinuteService extends IService<AirMinute> {
    Page<Map<String,Object>> queryMinute(Page<Map<String,Object>> page, String field, String tableName, List<String> companyIds,
                                         String area, String mn, String dataTime_begin, String dataTime_end);
    List<Map<String,Object>> queryMinute( String field, String tableName, List<String> companyIds,
                                          String area, String mn, String dataTime_begin, String dataTime_end);
}
