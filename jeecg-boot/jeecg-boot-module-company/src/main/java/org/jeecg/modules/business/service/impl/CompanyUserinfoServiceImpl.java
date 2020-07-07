package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.CompanyUserinfo;
import org.jeecg.modules.business.mapper.CompanyUserinfoMapper;
import org.jeecg.modules.business.service.ICompanyUserinfoService;
import org.jeecg.modules.bussiness.utils.Constant;
import org.springframework.stereotype.Service;

/**
 * @Description: company_userinfo
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Service
public class CompanyUserinfoServiceImpl extends ServiceImpl<CompanyUserinfoMapper, CompanyUserinfo> implements ICompanyUserinfoService {

    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyUserinfo> queryWrapper = new QueryWrapper<>();
        return this.count(queryWrapper.lambda().eq(CompanyUserinfo::getCompanyId, companyId)
                .eq(CompanyUserinfo::getStatus, Constant.status.NORMAL));
    }
}
