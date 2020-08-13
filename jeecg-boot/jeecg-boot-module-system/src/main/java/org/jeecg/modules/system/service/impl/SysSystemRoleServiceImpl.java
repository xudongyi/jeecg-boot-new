package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.SysSystem;
import org.jeecg.modules.system.entity.SysSystemRole;
import org.jeecg.modules.system.mapper.SysSystemMapper;
import org.jeecg.modules.system.mapper.SysSystemRoleMapper;
import org.jeecg.modules.system.service.ISysSystemRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysSystemRoleServiceImpl extends ServiceImpl<SysSystemRoleMapper, SysSystemRole> implements ISysSystemRoleService {
    @Resource
    SysSystemRoleMapper sysSystemRoleMapper;

    @Override
    public List<SysSystemRole> querySysRoleByUserId(String userId) {
        return sysSystemRoleMapper.querySysRoleByUserId(userId);
    }
}
