package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.VocDay;
import org.jeecg.modules.business.mapper.VocDayMapper;
import org.jeecg.modules.business.service.IVocDayService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: voc_day
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
@Service
public class VocDayServiceImpl extends ServiceImpl<VocDayMapper, VocDay> implements IVocDayService {

    @Resource
    private VocDayMapper vocDayMapper;

    @Override
    public List<Map<String, Object>> queryCompanyName(List<String> companyIds) {
        return vocDayMapper.queryCompanyName(companyIds);
    }
}
