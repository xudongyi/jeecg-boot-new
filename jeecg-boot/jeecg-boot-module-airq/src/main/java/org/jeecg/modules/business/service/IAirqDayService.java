package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.AirqDay;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.AirqDayVO;

import java.util.List;

/**
 * @Description: airq_day
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface IAirqDayService extends IService<AirqDay> {

    List<AirqDayVO> findEvaluate(String searchTime, List<String> mns);
}
