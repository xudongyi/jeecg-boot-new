package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.VocMinute_2009;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: voc_minute_2009
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
public interface IVocMinuteService extends IService<VocMinute_2009> {
    Page<List<Map<String,Object>>> queryMinute(Page<List<Map<String,Object>>> page, String field, String tableName, List<String> companyIds,
                                               String area, String mn, String dataTime_begin, String dataTime_end);
    List<Map<String,Object>> queryMinute( String field, String tableName, List<String> companyIds,
                                          String area, String mn, String dataTime_begin, String dataTime_end);
}
