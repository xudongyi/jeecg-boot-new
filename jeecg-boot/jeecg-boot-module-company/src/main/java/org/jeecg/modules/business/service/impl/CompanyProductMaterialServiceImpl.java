package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyProductMaterial;
import org.jeecg.modules.business.mapper.CompanyProductMaterialMapper;
import org.jeecg.modules.business.service.ICompanyProductMaterialService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: company_product_material
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Service
public class CompanyProductMaterialServiceImpl extends ServiceImpl<CompanyProductMaterialMapper, CompanyProductMaterial> implements ICompanyProductMaterialService {

    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyProductMaterial> queryWrapper = new QueryWrapper<>();
        return this.count(queryWrapper.lambda().eq(CompanyProductMaterial::getCompanyId, companyId)
                .eq(CompanyProductMaterial::getStatus, Constant.status.NORMAL));
    }
}
