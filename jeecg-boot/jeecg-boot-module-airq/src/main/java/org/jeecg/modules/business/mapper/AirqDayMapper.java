package org.jeecg.modules.business.mapper;

import org.jeecg.modules.business.entity.AirqDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.AirqVO;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.AirqDay;
import org.jeecg.modules.business.vo.AirqDayQualityVo;
import org.jeecg.modules.business.vo.AirqDayVO;

import java.util.List;

/**
 * @Description: airq_day
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface AirqDayMapper extends BaseMapper<AirqDay> {

    List<AirqVO> findEvaluate(String mns, String timeStart, String timeEnd);

    List<AirqDayQualityVo> queryDayAirQuality(@Param("companyIds")List<String> companyIds, @Param("datatime") Timestamp datatime, @Param("datatime2")Timestamp datatime2, @Param("area")String area, @Param("mn")String mn);

}
