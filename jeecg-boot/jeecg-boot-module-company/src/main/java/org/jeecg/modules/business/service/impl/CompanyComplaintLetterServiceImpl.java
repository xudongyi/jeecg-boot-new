package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanyComplaintLetter;
import org.jeecg.modules.business.mapper.CompanyComplaintLetterMapper;
import org.jeecg.modules.business.service.ICompanyComplaintLetterService;
import org.jeecg.modules.business.vo.CompanyComplaintLetterVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description: 信访投诉信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Service
public class CompanyComplaintLetterServiceImpl extends ServiceImpl<CompanyComplaintLetterMapper, CompanyComplaintLetter> implements ICompanyComplaintLetterService {

    @Resource
    CompanyComplaintLetterMapper companyComplaintLetterMapper;

    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanyComplaintLetter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanyComplaintLetter::getCompanyId,companyId);
        return this.count(queryWrapper);
    }

    @Override
    public Page<CompanyComplaintLetterVO> getCompanyComplaintLetter(Page<CompanyComplaintLetterVO> page, String companyIds, String status, Date dateBegin, Date dateEnd) {
        return page.setRecords(companyComplaintLetterMapper.getCompanyComplaintLetter(page,companyIds.split(","),status,dateBegin,dateEnd));
    }
}
