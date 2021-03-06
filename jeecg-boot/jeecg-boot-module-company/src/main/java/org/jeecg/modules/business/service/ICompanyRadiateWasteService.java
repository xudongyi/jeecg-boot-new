package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.CompanyRadiateWaste;

/**
 * @Description: 辐射许可证信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
public interface ICompanyRadiateWasteService extends IService<CompanyRadiateWaste> {

    /**
     * @Description:
     * @Param:companyId
     * @return:Integer
     * @Author: 周志远
     * @Date: 2020/6/1
     */
    Integer findCountByCompanyId(String companyId);
}
