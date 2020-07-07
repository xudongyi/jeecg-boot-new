package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.CompanySolidWaste;
import org.jeecg.modules.business.mapper.CompanySolidWasteMapper;
import org.jeecg.modules.business.service.ICompanySolidWasteService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.stereotype.Service;

/**
 * @Description: 固废许可证信息
 * @Author: jeecg-boot
 * @Date: 2020-06-01
 * @Version: V1.0
 */
@Service
public class CompanySolidWasteServiceImpl extends ServiceImpl<CompanySolidWasteMapper, CompanySolidWaste> implements ICompanySolidWasteService {

    /**
     * @Description:根据企业id查询固废许可证信息数量
     * @Param:companyId
     * @return:Integer
     * @Author: 周志远
     * @Date: 2020/6/1
     */
    @Override
    public Integer findCountByCompanyId(String companyId) {
        QueryWrapper<CompanySolidWaste> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CompanySolidWaste::getCompanyId, companyId).eq(CompanySolidWaste::getStatus, Constant.status.NORMAL);
        return this.count(queryWrapper);
    }
}
