package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.SysWarnUserPoint;
import org.jeecg.modules.business.mapper.SysWarnUserPointMapper;
import org.jeecg.modules.business.service.ISysWarnUserPointService;
import org.jeecg.modules.business.vo.SysWarnUserPointVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 站点报警短信接收人配置
 * @Author: jeecg-boot
 * @Date:   2020-07-08
 * @Version: V1.0
 */
@Service
public class SysWarnUserPointServiceImpl extends ServiceImpl<SysWarnUserPointMapper, SysWarnUserPoint> implements ISysWarnUserPointService {
    @Resource
    SysWarnUserPointMapper sysWarnUserPointMapper;

    @Override
    public Page<SysWarnUserPointVO> getSysWarnUserPoint(Page<SysWarnUserPointVO> page, String name, String mobile) {
        return page.setRecords(sysWarnUserPointMapper.getSysWarnUserPoint(page,name,mobile));
    }
}
