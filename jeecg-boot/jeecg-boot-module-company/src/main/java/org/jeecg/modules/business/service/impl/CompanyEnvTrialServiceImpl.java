package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyEnvTrial;
import org.jeecg.modules.business.mapper.CompanyEnvTrialMapper;
import org.jeecg.modules.business.service.ICompanyEnvTrialService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 环评审批信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Service
public class CompanyEnvTrialServiceImpl extends ServiceImpl<CompanyEnvTrialMapper, CompanyEnvTrial> implements ICompanyEnvTrialService {
    /**
     * @Description:根据companyId查询数量
     */
    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyEnvTrial> queryWrapper = new QueryWrapper<>();
        return this.count(queryWrapper.lambda().eq(CompanyEnvTrial::getCompanyId, companyId)
                .eq(CompanyEnvTrial::getStatus, Constant.status.NORMAL));
    }
}
