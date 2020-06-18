package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.CompanyRadiateWaste;
import org.jeecg.modules.business.mapper.CompanyRadiateWasteMapper;
import org.jeecg.modules.business.service.ICompanyRadiateWasteService;
import org.springframework.stereotype.Service;

/**
 * @Description: 辐射许可证信息
 * @Author: jeecg-boot
 * @Date: 2020-06-01
 * @Version: V1.0
 */
@Service
public class CompanyRadiateWasteServiceImpl extends ServiceImpl<CompanyRadiateWasteMapper, CompanyRadiateWaste> implements ICompanyRadiateWasteService {

    /**
     * @Description:根据企业id查询辐射许可证信息数量
     * @Param:companyId
     * @return:Integer
     * @Author: 周志远
     * @Date: 2020/6/1
     */
    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyRadiateWaste> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyRadiateWaste::getCompanyId, companyId);
        return this.count(queryWrapper);
    }
}
