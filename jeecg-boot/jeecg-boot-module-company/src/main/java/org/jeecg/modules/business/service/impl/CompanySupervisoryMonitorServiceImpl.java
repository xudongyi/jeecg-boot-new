package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanySupervisoryMonitor;
import org.jeecg.modules.business.mapper.CompanySupervisoryMonitorMapper;
import org.jeecg.modules.business.service.ICompanySupervisoryMonitorService;
import org.jeecg.modules.business.utils.Constant;
import org.jeecg.modules.business.vo.CompanySupervisoryMonitorVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description: 监督性监测信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Service
public class CompanySupervisoryMonitorServiceImpl extends ServiceImpl<CompanySupervisoryMonitorMapper, CompanySupervisoryMonitor> implements ICompanySupervisoryMonitorService {

    @Resource
    CompanySupervisoryMonitorMapper companySupervisoryMonitorMapper;

    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanySupervisoryMonitor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanySupervisoryMonitor::getCompanyId,companyId).eq(CompanySupervisoryMonitor::getStatus, Constant.status.NORMAL);
        return this.count(queryWrapper);
    }

    @Override
    public Page<CompanySupervisoryMonitorVO> getCompanySupervisoryMonitor(Page<CompanySupervisoryMonitorVO> page, String companyIds, String status, Date dateBegin, Date dateEnd,Integer listType) {
        return page.setRecords(companySupervisoryMonitorMapper.getCompanySupervisoryMonitor(page,companyIds.split(","),status,dateBegin,dateEnd,listType));
    }
}
