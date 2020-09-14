package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirCurrentOverproof;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface IAirCurrentOverproofService extends IService<AirCurrentOverproof> {
    List<OverEntry> queryOverAir(List<String>companyIds, String area, String code,
                                   String mn, Timestamp end, Timestamp begin);
    Page<OverEntry> queryOverAir(Page<OverEntry> page, List<String>companyIds, String area, String code,
                                   String mn, Timestamp end, Timestamp begin);
    List<OverEntryReport> queryOverAirReport(List<String>companyIds, String area, String code,
                                             String mn, Timestamp end, Timestamp begin);
    Page<OverEntryReport> queryOverAirReport(Page<OverEntryReport> page, List<String>companyIds, String area, String code,
                                             String mn, Timestamp end, Timestamp begin);
}
