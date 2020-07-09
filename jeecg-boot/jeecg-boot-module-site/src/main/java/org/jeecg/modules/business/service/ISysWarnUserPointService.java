package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.SysWarnUserPoint;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.SysWarnPointListVO;
import org.jeecg.modules.business.vo.SysWarnUserPointVO;

/**
 * @Description: 站点报警短信接收人配置
 * @Author: jeecg-boot
 * @Date:   2020-07-08
 * @Version: V1.0
 */
public interface ISysWarnUserPointService extends IService<SysWarnUserPoint> {
    Page<SysWarnUserPointVO> getSysWarnUserPoint(Page<SysWarnUserPointVO> page,String name,String mobile);
}
