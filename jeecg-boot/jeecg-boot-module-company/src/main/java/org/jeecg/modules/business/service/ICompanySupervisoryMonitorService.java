package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanySupervisoryMonitor;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.CompanyAdminPenaltiesVO;
import org.jeecg.modules.business.vo.CompanySupervisoryMonitorVO;

import java.util.Date;

/**
 * @Description: 监督性监测信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
public interface ICompanySupervisoryMonitorService extends IService<CompanySupervisoryMonitor> {
    Integer findCountByCompanyId(String companyId);
    Page<CompanySupervisoryMonitorVO> getCompanySupervisoryMonitor(Page<CompanySupervisoryMonitorVO> page, String companyId, String status, String companyName, Date dateBegin, Date dateEnd);
}
