package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirCurrentTr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: air_current_tr
 * @Author: jeecg-boot
 * @Date:   2020-09-07
 * @Version: V1.0
 */
public interface IAirCurrentTrService extends IService<AirCurrentTr> {
    Page<Map<String,Object>> queryRealTime(Page<Map<String,Object>> page,String field,String tableName, List<String> companyIds,
                                           String area,String mn,String dataTime_begin,String dataTime_end);
    List<Map<String,Object>> queryRealTime(String field,String tableName, List<String> companyIds,
                                           String area,String mn,String dataTime_begin,String dataTime_end);

    IPage<Map<String, Object>> getAirCurrentTrList(Page<Map<String, Object>> page, String area, String companyId, String mn,String tableName,List<Integer> dataStatus,Integer offLine);

    Map<String, Object> findBySiteIdAndTime(String siteId, String dataTime);
}
