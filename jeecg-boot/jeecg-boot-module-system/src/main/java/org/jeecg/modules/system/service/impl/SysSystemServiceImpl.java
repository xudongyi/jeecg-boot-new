package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.SysSystem;
import org.jeecg.modules.system.mapper.SysRoleMapper;
import org.jeecg.modules.system.mapper.SysSystemMapper;
import org.jeecg.modules.system.service.ISysSystemService;
import org.springframework.stereotype.Service;

@Service
public class SysSystemServiceImpl extends ServiceImpl<SysSystemMapper, SysSystem> implements ISysSystemService {
}
