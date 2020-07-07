package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanyDynamicSupervision;
import org.jeecg.modules.business.mapper.CompanyDynamicSupervisionMapper;
import org.jeecg.modules.business.service.ICompanyDynamicSupervisionService;
import org.jeecg.modules.business.utils.Constant;
import org.jeecg.modules.business.vo.CompanyDynamicSupervisionVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 企业年度动态监管
 * @Author: jeecg-boot
 * @Date: 2020-05-27
 * @Version: V1.0
 */
@Service
public class CompanyDynamicSupervisionServiceImpl extends ServiceImpl<CompanyDynamicSupervisionMapper, CompanyDynamicSupervision> implements ICompanyDynamicSupervisionService {
    @Resource
    CompanyDynamicSupervisionMapper companyDynamicSupervisionMapper;

    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyDynamicSupervision> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyDynamicSupervision::getCompanyId, companyId).eq(CompanyDynamicSupervision::getStatus, Constant.status.NORMAL);
        return this.count(queryWrapper);
    }

    @Override
    public Page<CompanyDynamicSupervisionVO> getCompanyDynamicSupervision(Page<CompanyDynamicSupervisionVO> page, String companyIds, String status,String reportYear,Integer listType) {

        return page.setRecords(companyDynamicSupervisionMapper.getCompanyDynamicSupervision(page, companyIds.split(","), status,reportYear,listType));
    }

//    @Override
//    public Page<CompanyDynamicSupervisionVO> getCompanyDynamicSupervision(Page<CompanyDynamicSupervisionVO> page, String companyId) {
//        return page.setRecords(companyDynamicSupervisionMapper.getCompanyDynamicSupervision(page, companyId));
//    }
}
