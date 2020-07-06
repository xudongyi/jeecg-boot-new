package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanyBase;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.CompanyBaseInfoSimple;

/**
 * @Description: 企业基础表
 * @Author: jeecg-boot
 * @Date:   2020-06-09
 * @Version: V1.0
 */
public interface ICompanyBaseService extends IService<CompanyBase> {


    /**
     * 获取所有公司的简单信息
     * @return
     */

     Page<CompanyBaseInfoSimple> baseInfolist(Page<CompanyBaseInfoSimple> page);
}
