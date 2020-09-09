package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.WaterCurrentOverproof;
import org.jeecg.modules.business.vo.OverEntry;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: water_current_overproof
 * @Author: jeecg-boot
 * @Date:   2020-09-08
 * @Version: V1.0
 */
public interface IWaterCurrentOverproofService extends IService<WaterCurrentOverproof> {

    List<OverEntry> queryOverWater(List<String>companyIds, String area, String code,
                                   String mn, Timestamp end, Timestamp begin);
    Page<OverEntry> queryOverWater(Page<OverEntry> page, List<String>companyIds, String area, String code,
                                   String mn, Timestamp end, Timestamp begin);
}
