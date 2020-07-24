package org.jeecg.modules.business.mapper;

import org.jeecg.modules.business.entity.AirqMonth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.AirqVO;

import java.util.List;

/**
 * @Description: airq_month
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface AirqMonthMapper extends BaseMapper<AirqMonth> {

    List<AirqVO> findEvaluate(String mns, String timeStart, String timeEnd);
}
