package org.jeecg.modules.business.mapper;

import org.jeecg.modules.business.entity.SysPollutionCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description: 污染因子表
 * @Author: jeecg-boot
 * @Date:   2020-07-01
 * @Version: V1.0
 */
public interface SysPollutionCodeMapper extends BaseMapper<SysPollutionCode> {

    List<SysPollutionCode> queryCode(String area, String companyId, String mn, String type);
}
