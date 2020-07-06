package org.jeecg.modules.business.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.SysWarnPointRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.SysWarnPointListVO;

/**
 * @Description: 站点报警策略表
 * @Author: jeecg-boot
 * @Date:   2020-07-06
 * @Version: V1.0
 */
public interface SysWarnPointRuleMapper extends BaseMapper<SysWarnPointRule> {
    List<SysWarnPointListVO> getSysWarnPointList(Page<SysWarnPointListVO> page);
}
