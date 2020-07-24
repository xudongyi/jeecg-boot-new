package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.AirqMonth;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.AirqVO;

import java.util.List;

/**
 * @Description: airq_month
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface IAirqMonthService extends IService<AirqMonth> {

    List<AirqVO> findEvaluate(String searchTime, List<String> mns);
}
