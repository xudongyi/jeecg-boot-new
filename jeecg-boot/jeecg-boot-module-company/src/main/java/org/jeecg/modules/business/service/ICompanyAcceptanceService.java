package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.CompanyAcceptance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 竣工验收信息
 * @Author: jeecg-boot
 * @Date: 2020-05-30
 * @Version: V1.0
 */
public interface ICompanyAcceptanceService extends IService<CompanyAcceptance> {

    /**
     * @Description:根据companyId查询数量
     * @Param: companyId
     * @return: Integer
     * @Author: 周志远
     * @Date: 2020/6/1
     */
    Integer findCountByCompanyId(String companyId);

    /**
    * @Description:  根据companyId查询实体集合
    * @Param:  
    * @return:  
    * @Author: 周志远
    * @Date: 2020/6/5
    */
    List<CompanyAcceptance> findByCompanyId(String companyId);

}
