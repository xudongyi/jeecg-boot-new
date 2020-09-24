package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.WarnRule;
import org.jeecg.modules.business.mapper.WarnRuleMapper;
import org.jeecg.modules.business.service.IWarnRuleService;
import org.jeecg.modules.business.vo.WarnRuleVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: warn_rule
 * @Author: jeecg-boot
 * @Date:   2020-09-15
 * @Version: V1.0
 */
@Service
public class WarnRuleServiceImpl extends ServiceImpl<WarnRuleMapper, WarnRule> implements IWarnRuleService {
    @Resource
    WarnRuleMapper warnRuleMapper;

    @Override
    public List<Map<String, Object>> queryMnInfo(List<String> companyIds, String area, String siteType) {
        return warnRuleMapper.queryMnInfo(companyIds, area, siteType);
    }

    @Override
    public List<WarnRuleVO> queryWarnRuleVO(List<String> mns) {
        return warnRuleMapper.queryWarnRuleVO(mns);
    }

    @Override
    public List<Map<String, Object>> querySingleSiteInfo(String mn) {
        return warnRuleMapper.querySingleSiteInfo(mn);
    }

    @Override
    public List<Map<String, Object>> queryDeleteIds(List<String> mns, List<String> types) {
        return warnRuleMapper.queryDeleteIds(mns, types);
    }

    @Override
    public List<Map<String, Object>> queryDeleteIdsBeforeAdd(List<String> mns, String ruleType, String siteType, String code) {
        return warnRuleMapper.queryDeleteIdsBeforeAdd(mns, ruleType, siteType, code);
    }

    @Override
    public List<Map<String, Object>> queryTreeData(List<String> companyIds) {
        return warnRuleMapper.queryTreeData(companyIds);
    }
}
