package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.SysPollutionCode;
import org.jeecg.modules.business.mapper.SysPollutionCodeMapper;
import org.jeecg.modules.business.service.ISysPollutionCodeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 污染因子表
 * @Author: jeecg-boot
 * @Date:   2020-07-01
 * @Version: V1.0
 */
@Service
public class SysPollutionCodeServiceImpl extends ServiceImpl<SysPollutionCodeMapper, SysPollutionCode> implements ISysPollutionCodeService {
    @Resource
    private  SysPollutionCodeMapper sysPollutionCodeMapper;

    @Override
    public List<SysPollutionCode> queryCode(String area, String companyId, String mn, String type) {
        return sysPollutionCodeMapper.queryCode(area,companyId,mn,type);
    }
}
