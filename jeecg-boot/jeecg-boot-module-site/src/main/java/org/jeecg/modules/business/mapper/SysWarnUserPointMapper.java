package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.SysWarnUserPoint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.SysWarnPointListVO;
import org.jeecg.modules.business.vo.SysWarnUserPointVO;

import java.util.List;

/**
 * @Description: 站点报警短信接收人配置
 * @Author: jeecg-boot
 * @Date:   2020-07-08
 * @Version: V1.0
 */
public interface SysWarnUserPointMapper extends BaseMapper<SysWarnUserPoint> {
    List<SysWarnUserPointVO> getSysWarnUserPoint(Page<SysWarnUserPointVO> page,String name,String mobile);
}
