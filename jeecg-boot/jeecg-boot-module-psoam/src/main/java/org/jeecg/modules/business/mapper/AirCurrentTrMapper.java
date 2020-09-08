package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.AirCurrentTr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: air_current_tr
 * @Author: jeecg-boot
 * @Date:   2020-09-07
 * @Version: V1.0
 */
public interface AirCurrentTrMapper extends BaseMapper<AirCurrentTr> {
    List<Map<String, Object>> queryRealTime(Page<Map<String, Object>> page, String field, String tableName,
                                            @Param("companyIds") List<String> companyIds, String area, String mn, Timestamp begin, Timestamp end);
    List<Map<String, Object>> queryMaxRealTime(Page<Map<String, Object>> page, String field, String tableName,
                                               @Param("companyIds") List<String> companyIds, String area, String mn);
    List<Map<String, Object>> queryRealTime( String field, String tableName,
                                             @Param("companyIds") List<String> companyIds, String area, String mn, Timestamp begin, Timestamp end);
    List<Map<String, Object>> queryMaxRealTime(String field, String tableName,
                                               @Param("companyIds") List<String> companyIds, String area, String mn);


    List<Map<String, Object>> getAirCurrentTrList(Page<Map<String, Object>> page, String area, String companyId, String mn, String tableName,List<Integer> dataStatus,Integer offLine);

}
