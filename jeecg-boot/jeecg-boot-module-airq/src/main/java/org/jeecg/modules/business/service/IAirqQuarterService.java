package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirqQuarter;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.AirqQuarterQualityVO;

/**
 * @Description: airq_quarter
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface IAirqQuarterService extends IService<AirqQuarter> {

    IPage<AirqQuarterQualityVO> queryAirqQuarterQuality(String companyIds, Page<AirqQuarterQualityVO> page, String area, String mn, String year, String quarter);
}
