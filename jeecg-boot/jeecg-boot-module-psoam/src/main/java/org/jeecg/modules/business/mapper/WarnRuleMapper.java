package org.jeecg.modules.business.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.WarnRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.WarnRuleVO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: warn_rule
 * @Author: jeecg-boot
 * @Date:   2020-09-15
 * @Version: V1.0
 */
public interface WarnRuleMapper extends BaseMapper<WarnRule> {
    List<Map<String,Object>> queryMnInfo(@Param("companyIds")List<String> companyIds,String area,String siteType);
    List<WarnRuleVO> queryWarnRuleVO(@Param("mns")List<String> mns);
    List<Map<String,Object>> querySingleSiteInfo(String mn);
    List<Map<String,Object>> queryDeleteIds(@Param("mns")List<String> mns,@Param("types")List<String> types);
    List<Map<String,Object>> queryDeleteIdsBeforeAdd(@Param("mns")List<String> mns,String ruleType,String siteType,String code);
    List<Map<String,Object>> queryTreeData(@Param("companyIds")List<String> companyIds);
}
