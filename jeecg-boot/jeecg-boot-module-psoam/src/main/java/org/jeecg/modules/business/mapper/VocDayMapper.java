package org.jeecg.modules.business.mapper;

import java.util.List;
import java.util.Map;

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
}
