package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.CompanyApply;
import org.jeecg.modules.business.entity.CompanyQualification;

import java.util.List;
import java.util.Map;

/**
 * @Description: 企业资质
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
public interface ICompanyQualificationService extends IService<CompanyQualification> {

     Map<String, List<Map<String,String>>> getQualificationFiles(String companyId);
     Boolean updateQualificationFiles(List<String> ids,Map<String,Object> updateParams);
     Map<String, List<Map<String,String>>> compareQualification(String applyId);

     Map<String, List<Map<String, String>>> queryQualificationAudit(CompanyApply companyApply);

     /**
      * @Description:根据companyId查询数量
      */
     Integer findCountByCompanyId(String companyId) ;
}
