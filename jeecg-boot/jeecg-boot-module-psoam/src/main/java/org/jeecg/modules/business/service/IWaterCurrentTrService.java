package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.WaterCurrentTr;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.SysWarnPointListVO;

import java.util.List;
import java.util.Map;

import java.util.List;
import java.util.Map;

/**
 * @Description: water_current_tr
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
public interface IWaterCurrentTrService extends IService<WaterCurrentTr> {

    Page<Map<String,Object>> queryRealTime(Page<Map<String,Object>> page,String field,String tableName, List<String> companyIds,
                                                 String area,String mn,String dataTime_begin,String dataTime_end);
    List<Map<String,Object>> queryRealTime(String field,String tableName, List<String> companyIds,
                                                 String area,String mn,String dataTime_begin,String dataTime_end);
    IPage<Map<String, Object>> getWaterCurrentTrList(Page<Map<String, Object>> page, String area, String companyId, String mn,String tableName);
}
