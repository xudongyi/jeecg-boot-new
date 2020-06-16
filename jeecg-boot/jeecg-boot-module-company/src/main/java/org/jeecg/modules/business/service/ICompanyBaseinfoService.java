package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.CompanyBaseinfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.CompanyBaseInfoVo;

/**
 * @Description: company_baseinfo
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
public interface ICompanyBaseinfoService extends IService<CompanyBaseinfo> {

    CompanyBaseinfo queryByCompanyId(String companyId);
    boolean upDateStatus(String companyId,String status);
    CompanyBaseInfoVo getCompanyBaseInfo(String id);
    CompanyBaseInfoVo getInfoByCompanyId(String companyId);
}
