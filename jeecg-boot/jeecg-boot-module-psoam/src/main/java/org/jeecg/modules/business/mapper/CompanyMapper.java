package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.PsoamConfig;

import java.util.List;
import java.util.Map;

/**
 * @Description: 监测类型配置
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
public interface CompanyMapper {

    List<Map<String,Object>> queryCompanyInfos(@Param("companyIds") List<String> companyIds);

}
