package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.WaterCurrentTr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: water_current_tr
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
public interface WaterCurrentTrMapper extends BaseMapper<WaterCurrentTr> {

    List<List<Map<String, Object>>> queryRealTime(Page<List<Map<String, Object>>> page, String field, String tableName,
                                                  @Param("companyIds") List<String> companyIds, String area, String mn, Timestamp begin, Timestamp end);
    List<List<Map<String, Object>>> queryMaxRealTime(Page<List<Map<String, Object>>> page, String field, String tableName,
                                                  @Param("companyIds") List<String> companyIds, String area, String mn);



    List<Map<String, Object>> getWaterCurrentTrList(Page<Map<String, Object>> page, String area, String companyId, String mn, String tableName,List<Integer> dataStatus,Integer offLine);
}
