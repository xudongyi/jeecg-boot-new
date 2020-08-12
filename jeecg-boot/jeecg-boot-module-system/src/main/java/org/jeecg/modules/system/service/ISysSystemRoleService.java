package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysSystemRole;

import java.util.List;

public interface ISysSystemRoleService extends IService<SysSystemRole> {
     List<SysSystemRole> querySysRoleByUserId(String userId);
}
