package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.CompanyBaseinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.CompanyAdminPenaltiesVO;
import org.jeecg.modules.business.vo.CompanyBaseInfoVo;

import java.util.Date;
import java.util.List;

/**
 * @Description: company_baseinfo
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
public interface CompanyBaseinfoMapper extends BaseMapper<CompanyBaseinfo> {


    CompanyBaseInfoVo getCompanyBaseInfo(String id);
    CompanyBaseInfoVo getInfoByCompanyId(String companyId,String status);

}
