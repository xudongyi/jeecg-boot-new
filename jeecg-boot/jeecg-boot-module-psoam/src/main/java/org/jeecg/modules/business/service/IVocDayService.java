package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.VocDay;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: voc_day
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
public interface IVocDayService extends IService<VocDay> {
    List<Map<String,Object>> queryCompanyName(List<String> companyIds);

    Page<List<Map<String,Object>>> queryDay(Page<List<Map<String,Object>>> page, String field, List<String> companyIds,
                                            String area, String mn, String dataTime_begin, String dataTime_end);
}
