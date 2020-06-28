package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.CompanyRadiateWaste;
import org.jeecg.modules.business.entity.CompanyRiskWaste;
import org.jeecg.modules.business.mapper.CompanyRiskWasteMapper;
import org.jeecg.modules.business.service.ICompanyRiskWasteService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.stereotype.Service;

/**
 * @Description: 危废许可证信息
 * @Author: jeecg-boot
 * @Date: 2020-06-01
 * @Version: V1.0
 */
@Service
public class CompanyRiskWasteServiceImpl extends ServiceImpl<CompanyRiskWasteMapper, CompanyRiskWaste> implements ICompanyRiskWasteService {

    /**
     * @Description:根据企业id查询危废许可证信息数量
     * @Param:companyId
     * @return:Integer
     * @Author: 周志远
     * @Date: 2020/6/1
     */
    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyRiskWaste> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyRiskWaste::getCompanyId, companyId).eq(CompanyRiskWaste::getStatus, Constant.status.NORMAL);
        return this.count(queryWrapper);
    }
}
