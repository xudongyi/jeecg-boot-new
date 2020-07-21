package org.jeecg.modules.business.mapper;

import org.jeecg.modules.business.entity.AirqDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.AirqDayVO;

import java.util.List;

/**
 * @Description: airq_day
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface AirqDayMapper extends BaseMapper<AirqDay> {

    List<AirqDayVO> findEvaluate(String mns, String timeStart, String timeEnd);
}
