package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.AirqHour;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.AirSiteInfo;

import java.util.List;

/**
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface IAirqHourService extends IService<AirqHour> {

    List<AirSiteInfo> queryInfoByCompanyId(List<String> companyIds);

}
