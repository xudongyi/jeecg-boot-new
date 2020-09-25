package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.SourceDirectory;
import org.jeecg.modules.business.mapper.SourceDirectoryMapper;
import org.jeecg.modules.business.service.ISourceDirectoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 污染源名录库
 * @Author: jeecg-boot
 * @Date:   2020-09-15
 * @Version: V1.0
 */
@Service
public class SourceDirectoryServiceImpl extends ServiceImpl<SourceDirectoryMapper, SourceDirectory> implements ISourceDirectoryService {
    @Resource
    private SourceDirectoryMapper sourceDirectoryMapper;

    @Override
    public IPage<Map<String, Object>> getSourceDirectoryList(Page<Map<String, Object>> page, String area, String companyId,String companyType, String industry, String intensiveUnit, String intensiveCompany, String siteType, String siteLevel, String siteState) {
        return sourceDirectoryMapper.getSourceDirectoryList(page,area,companyId,companyType,industry,intensiveUnit,intensiveCompany,siteType,siteLevel,siteState);
    }

    @Override
    public IPage<Map<String, Object>> getUnSelectCompany(Page<Map<String, Object>> page, List<String> companyIds) {
        return sourceDirectoryMapper.getUnSelectCompany(page,companyIds);
    }

    @Override
    public IPage<Map<String, Object>> getSelectCompany(Page<Map<String, Object>> page,List<String> companyIds) {
        return sourceDirectoryMapper.getSelectCompany(page,companyIds);
    }
}
