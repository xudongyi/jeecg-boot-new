package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.SysPollutionCode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 污染因子表
 * @Author: jeecg-boot
 * @Date:   2020-07-01
 * @Version: V1.0
 */
public interface ISysPollutionCodeService extends IService<SysPollutionCode> {

    List<SysPollutionCode> queryCode(String area, String companyId, String mn, String type);
}
