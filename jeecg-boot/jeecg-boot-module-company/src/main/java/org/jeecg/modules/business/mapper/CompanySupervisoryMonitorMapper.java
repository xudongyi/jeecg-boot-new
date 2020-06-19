package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanySupervisoryMonitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.CompanySupervisoryMonitorVO;

import java.util.Date;
import java.util.List;

/**
 * @Description: 监督性监测信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
public interface CompanySupervisoryMonitorMapper extends BaseMapper<CompanySupervisoryMonitor> {
    List<CompanySupervisoryMonitorVO> getCompanySupervisoryMonitor(Page page, String[] companyIds, String status, Date dateBegin, Date dateEnd,Integer listType);
}
