package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.VocCurrentTr_2009;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: voc_current_tr_2009
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
public interface IVocCurrentTrService extends IService<VocCurrentTr_2009> {
    Page<List<Map<String,Object>>> queryRealTime(Page<List<Map<String,Object>>> page, String field, String tableName, List<String> companyIds,
                                                 String area, String mn, String dataTime_begin, String dataTime_end);
    List<Map<String,Object>> queryRealTime(String field,String tableName, List<String> companyIds,
                                           String area,String mn,String dataTime_begin,String dataTime_end);
}
