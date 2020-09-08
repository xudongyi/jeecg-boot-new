package org.jeecg.modules.business.mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.VocCurrentTr_2009;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: voc_current_tr_2009
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
public interface VocCurrentTrMapper extends BaseMapper<VocCurrentTr_2009> {
    List<List<Map<String, Object>>> queryRealTime(Page<List<Map<String, Object>>> page, String field, String tableName,
                                                  @Param("companyIds") List<String> companyIds, String area, String mn, Timestamp begin, Timestamp end);
    List<List<Map<String, Object>>> queryMaxRealTime(Page<List<Map<String, Object>>> page, String field, String tableName,
                                                     @Param("companyIds") List<String> companyIds, String area, String mn);
    List<Map<String, Object>> queryRealTime( String field, String tableName,
                                             @Param("companyIds") List<String> companyIds, String area, String mn, Timestamp begin, Timestamp end);
    List<Map<String, Object>> queryMaxRealTime(String field, String tableName,
                                               @Param("companyIds") List<String> companyIds, String area, String mn);
}
