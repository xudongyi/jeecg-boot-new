package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysSystemRole;

import java.util.List;

public interface SysSystemRoleMapper extends BaseMapper<SysSystemRole> {
    List<SysSystemRole> querySysRoleByUserId(String userId);
}
