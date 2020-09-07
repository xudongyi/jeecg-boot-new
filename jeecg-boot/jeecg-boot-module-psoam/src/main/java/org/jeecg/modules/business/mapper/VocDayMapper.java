package org.jeecg.modules.business.mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.VocDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: voc_day
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
public interface VocDayMapper extends BaseMapper<VocDay> {
    List<Map<String,Object>> queryCompanyName(@Param("companyIds")List<String> companyIds);

    List<List<Map<String, Object>>> queryDay(Page<List<Map<String, Object>>> page, String field,
                                             @Param("companyIds") List<String> companyIds, String area, String mn, Timestamp begin, Timestamp end);
    List<List<Map<String, Object>>> queryMaxDay(Page<List<Map<String, Object>>> page, String field,
                                                @Param("companyIds") List<String> companyIds, String area, String mn);
}
