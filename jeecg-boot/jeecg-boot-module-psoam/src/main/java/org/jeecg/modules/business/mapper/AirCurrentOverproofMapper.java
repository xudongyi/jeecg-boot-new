package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.AirCurrentOverproof;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.OverEntry;
import org.jeecg.modules.business.vo.OverEntryReport;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: air_current_overproof
 * @Author: jeecg-boot
 * @Date:   2020-09-14
 * @Version: V1.0
 */
public interface AirCurrentOverproofMapper extends BaseMapper<AirCurrentOverproof> {

    List<OverEntry> queryOverAir(List<String> companyIds, String area, String code, String mn, Timestamp end, Timestamp begin);
    List<OverEntry> queryOverAir(Page<OverEntry> page, @Param("companyIds") List<String>companyIds, String area, String code,
                                   String mn, Timestamp end, Timestamp begin);

    List<OverEntryReport>  queryOverAirReport(@Param("companyIds") List<String>companyIds, String area, String code,
                                              String mn, Timestamp end, Timestamp begin);
    List<OverEntryReport> queryOverAirReport(Page<OverEntryReport> page, @Param("companyIds") List<String>companyIds, String area, String code,
                                             String mn, Timestamp end, Timestamp begin);
}
