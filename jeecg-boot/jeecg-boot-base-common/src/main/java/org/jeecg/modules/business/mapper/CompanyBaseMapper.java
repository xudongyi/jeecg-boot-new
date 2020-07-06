package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanyBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.CompanyBaseInfoSimple;

import java.util.List;

/**
 * @Description: 企业基础表
 * @Author: jeecg-boot
 * @Date:   2020-06-09
 * @Version: V1.0
 */
public interface CompanyBaseMapper extends BaseMapper<CompanyBase> {
    /**
     * 获取所有公司的简单信息
     * @return
     */
    public List<CompanyBaseInfoSimple> baseInfolist(Page<CompanyBaseInfoSimple> page);
}
