package org.jeecg.modules.business.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.WarnRule;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.WarnRuleVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: warn_rule
 * @Author: jeecg-boot
 * @Date:   2020-09-15
 * @Version: V1.0
 */
public interface IWarnRuleService extends IService<WarnRule> {
    List<Map<String,Object>> queryMnInfo(List<String> companyIds, String area, String siteType);
    List<WarnRuleVO> queryWarnRuleVO(List<String> mns);
    List<Map<String,Object>> querySingleSiteInfo(String mn);
    List<Map<String,Object>> queryDeleteIds(List<String> mns,List<String> types);
    List<Map<String,Object>> queryDeleteIdsBeforeAdd(List<String> mns,String ruleType,String siteType,String code);
    List<Map<String,Object>> queryTreeData(List<String> companyIds);
    List<Map<String,Object>> queryMsgRuleInfo(List<String> companyIds,String name,String mobile,String mn,String zrCompanyId);
}
