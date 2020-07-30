package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirqQuarter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.AirqQuarterQualityVO;

import java.util.List;

/**
 * @Description: airq_quarter
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface AirqQuarterMapper extends BaseMapper<AirqQuarter> {

    List<AirqQuarterQualityVO> queryAirqQuarterQuality(String[] companyIds, Page page, String area, String mn, String year, String quarter);
}
