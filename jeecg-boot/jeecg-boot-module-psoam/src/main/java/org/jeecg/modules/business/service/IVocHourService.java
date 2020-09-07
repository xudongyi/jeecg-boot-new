package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.VocHour;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: voc_hour
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
public interface IVocHourService extends IService<VocHour> {
    Page<List<Map<String,Object>>> queryHour(Page<List<Map<String,Object>>> page, String field, List<String> companyIds,
                                             String area, String mn, String dataTime_begin, String dataTime_end);
}
