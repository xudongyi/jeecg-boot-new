package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.SysWarnPointRule;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.SysWarnPointListVO;

/**
 * @Description: 站点报警策略表
 * @Author: jeecg-boot
 * @Date:   2020-07-06
 * @Version: V1.0
 */
public interface ISysWarnPointRuleService extends IService<SysWarnPointRule> {
    Page<SysWarnPointListVO> getSysWarnPointList(Page<SysWarnPointListVO> page);
}
