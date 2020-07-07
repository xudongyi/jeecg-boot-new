package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.SysWarnPointRule;
import org.jeecg.modules.business.mapper.SysWarnPointRuleMapper;
import org.jeecg.modules.business.service.ISysWarnPointRuleService;
import org.jeecg.modules.business.vo.SysWarnPointListVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 站点报警策略表
 * @Author: jeecg-boot
 * @Date:   2020-07-06
 * @Version: V1.0
 */
@Service
public class SysWarnPointRuleServiceImpl extends ServiceImpl<SysWarnPointRuleMapper, SysWarnPointRule> implements ISysWarnPointRuleService {
    @Resource
    SysWarnPointRuleMapper sysWarnPointRuleMapper;

    @Override
    public Page<SysWarnPointListVO> getSysWarnPointList(Page<SysWarnPointListVO> page) {
        return page.setRecords(sysWarnPointRuleMapper.getSysWarnPointList(page));
    }
}
