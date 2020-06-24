package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanyAdminPenalties;
import org.jeecg.modules.business.mapper.CompanyAdminPenaltiesMapper;
import org.jeecg.modules.business.service.ICompanyAdminPenaltiesService;
import org.jeecg.modules.business.utils.Constant;
import org.jeecg.modules.business.vo.CompanyAdminPenaltiesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description: 行政处罚信息
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Service
public class CompanyAdminPenaltiesServiceImpl extends ServiceImpl<CompanyAdminPenaltiesMapper, CompanyAdminPenalties> implements ICompanyAdminPenaltiesService {
    @Resource
    CompanyAdminPenaltiesMapper companyAdminPenaltiesMapper;

    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyAdminPenalties> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyAdminPenalties::getCompanyId,companyId).eq(CompanyAdminPenalties::getStatus, Constant.status.NORMAL);
        return this.count(queryWrapper);
    }

    @Override
    public Page<CompanyAdminPenaltiesVO> getCompanyAdminPenalties(Page<CompanyAdminPenaltiesVO> page, String companyIds, String status,Date dateBegin,Date dateEnd,Integer listType) {
        return page.setRecords(companyAdminPenaltiesMapper.getCompanyAdminPenalties(page,companyIds.split(","),status,dateBegin,dateEnd,listType));
    }
}
