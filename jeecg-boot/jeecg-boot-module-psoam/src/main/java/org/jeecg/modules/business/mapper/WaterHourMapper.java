package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.WaterHour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: water_hour
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
public interface WaterHourMapper extends BaseMapper<WaterHour> {
    List<Map<String, Object>> queryHour(Page<Map<String, Object>> page, String field,
                                                @Param("companyIds") List<String> companyIds, String area, String mn, Timestamp begin, Timestamp end);
    List<Map<String, Object>> queryMaxHour(Page<Map<String, Object>> page, String field,
                                                   @Param("companyIds") List<String> companyIds, String area, String mn);
    List<Map<String, Object>> queryHour(String field,
                                              @Param("companyIds") List<String> companyIds, String area, String mn, Timestamp begin, Timestamp end);
    List<Map<String, Object>> queryMaxHour( String field,
                                                 @Param("companyIds") List<String> companyIds, String area, String mn);
}
