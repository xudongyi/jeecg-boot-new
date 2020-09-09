package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.WaterCurrentOverproof;
import org.jeecg.modules.business.vo.OverEntry;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: water_current_overproof
 * @Author: jeecg-boot
 * @Date:   2020-09-08
 * @Version: V1.0
 */
public interface WaterCurrentOverproofMapper extends BaseMapper<WaterCurrentOverproof> {

    List<OverEntry>  queryOverWater(@Param("companyIds") List<String>companyIds, String area, String code,
                                    String mn, Timestamp end, Timestamp begin);
    List<OverEntry> queryOverWater(Page<OverEntry> page, @Param("companyIds") List<String>companyIds, String area, String code,
                                   String mn, Timestamp end, Timestamp begin);

}
