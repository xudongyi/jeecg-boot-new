package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.AirDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: air_day
 * @Author: jeecg-boot
 * @Date:   2020-09-07
 * @Version: V1.0
 */
public interface AirDayMapper extends BaseMapper<AirDay> {
    List<Map<String, Object>> queryDay(Page<Map<String, Object>> page, String field,
                                       @Param("companyIds") List<String> companyIds, String area, String mn, Timestamp begin, Timestamp end);
    List<Map<String, Object>> queryMaxDay(Page<Map<String, Object>> page, String field,
                                          @Param("companyIds") List<String> companyIds, String area, String mn);
    List<Map<String, Object>> queryDay( String field,
                                        @Param("companyIds") List<String> companyIds, String area, String mn, Timestamp begin, Timestamp end);
    List<Map<String, Object>> queryMaxDay( String field,
                                           @Param("companyIds") List<String> companyIds, String area, String mn);
}
