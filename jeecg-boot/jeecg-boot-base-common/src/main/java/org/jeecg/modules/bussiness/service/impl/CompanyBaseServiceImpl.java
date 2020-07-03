package org.jeecg.modules.bussiness.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.bussiness.entity.CompanyBase;
import org.jeecg.modules.bussiness.mapper.CompanyBaseMapper;
import org.jeecg.modules.bussiness.service.ICompanyBaseService;
import org.jeecg.modules.bussiness.vo.CompanyBaseInfoSimple;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 企业基础表
 * @Author: jeecg-boot
 * @Date:   2020-06-09
 * @Version: V1.0
 */
@Service
public class CompanyBaseServiceImpl extends ServiceImpl<CompanyBaseMapper, CompanyBase> implements ICompanyBaseService {

    @Resource
    CompanyBaseMapper companyBaseMapper;

    @Override
    public Page<CompanyBaseInfoSimple> baseInfolist(Page<CompanyBaseInfoSimple> page) {
        return page.setRecords(companyBaseMapper.baseInfolist(page));
    }
}
