package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.SourceDirectory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 污染源名录库
 * @Author: jeecg-boot
 * @Date:   2020-09-15
 * @Version: V1.0
 */
public interface ISourceDirectoryService extends IService<SourceDirectory> {

    IPage<Map<String, Object>> getSourceDirectoryList(Page<Map<String, Object>> page, String area, String companyId,String companyType, String industry, String intensiveUnit, String intensiveCompany, String siteType, String siteLevel, String siteState);

    IPage<Map<String, Object>> getUnSelectCompany(Page<Map<String, Object>> page, String companyId);

    IPage<Map<String, Object>> getSelectCompany(Page<Map<String, Object>> page, String companyId);
}
