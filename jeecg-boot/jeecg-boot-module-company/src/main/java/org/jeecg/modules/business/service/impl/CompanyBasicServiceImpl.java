package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 企业人员信息
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */



@Service
public class CompanyBasicServiceImpl implements ICompanyBasicService {
    @Autowired
    private ICompanyAcceptanceService companyAcceptanceService;

    @Autowired
    private ICompanyPreventionService companyPreventionService;

    @Autowired
    private ICompanyDirtyAllowService companyDirtyAllowService;

    @Autowired
    private ICompanyAdminPenaltiesService companyAdminPenaltiesService;

    @Autowired
    private ICompanyDynamicSupervisionService companyDynamicSupervisionService;

    @Autowired
    private ICompanySupervisoryMonitorService companySupervisoryMonitorService;

    @Autowired
    private ICompanyComplaintLetterService companyComplaintLetterService;

    @Autowired
    private ICompanyEnvTaxService companyEnvTaxService;

    @Autowired
    private ICompanyCleanProductService companyCleanProductService;

    @Autowired
    private ICompanyOnlineInfoService companyOnlineInfoService;

    @Autowired
    private ICompanyRiskWasteService companyRiskWasteService;

    @Autowired
    private ICompanySolidWasteService companySolidWasteService;

    @Autowired
    private ICompanyRadiateWasteService companyRadiateWasteService;
    @Autowired
    private ICompanyQualificationService companyQualificationService;
    @Autowired
    private ICompanyUserinfoService companyUserinfoService;
    @Autowired
    private ICompanyProductMaterialService companyProductMaterialService;
    @Autowired
    private ICompanyEnvTrialService companyEnvTrialService;
    @Autowired
    private ICompanyBaseinfoService companyBaseinfoService;
    @Autowired
    private ISiteMonitorPointService siteMonitorPointService;
    /**
     *  根据conpanyId组装 一企一档基础信息的菜单信息
     * @param companyId
     * @return 菜单信息
     */
    public List<Map<String,String>> getbasicInfoMenus(String companyId){
        List<Map<String,String>> basicInfoMenus = new ArrayList<>();
        addElements("1"," 基本信息",companyBaseinfoService.findCountByCompanyId(companyId),basicInfoMenus);

        addElements("2"," 企业资质",companyQualificationService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("3"," 员工信息",companyUserinfoService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("4"," 产品物料信息",companyProductMaterialService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("5"," 环评审批信息",companyEnvTrialService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("6"," 竣工验收信息",companyAcceptanceService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("7"," 污染防治信息",companyPreventionService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("8"," 排污许可证信息",companyDirtyAllowService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("9"," 危废经营许可信息",companyRiskWasteService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("10"," 固废许可证信息",companySolidWasteService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("11"," 辐射许可证信息",companyRadiateWasteService.findCountByCompanyId(companyId),basicInfoMenus);

        addElements("12"," 环保税信息",companyEnvTaxService.findCountByCompanyId(companyId),basicInfoMenus);

        addElements("13"," 清洁生产信息",companyCleanProductService.findCountByCompanyId(companyId),basicInfoMenus);

        addElements("14"," 在线监控验收信息",companyOnlineInfoService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("15"," 污染源信息",0,basicInfoMenus);
        List<String> siteTypes = new ArrayList<String>() {
            {
                add("0");
                add("1");
                add("2");
            }
        };
        addElements("16"," 监测站点信息",siteMonitorPointService.count(new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getCompanyId,companyId).in(SiteMonitorPoint::getSiteType,siteTypes)),basicInfoMenus);

        return basicInfoMenus;
    }
    /**
     *  根据conpanyId组装 一企一档监督检查的菜单信息
     * @param companyId
     * @return 菜单信息
     */
    public List<Map<String,String>> getSuperviseMenus(String companyId){
        List<Map<String,String>> basicInfoMenus = new ArrayList<>();
        addElements("1"," 年度动态监管",companyDynamicSupervisionService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("2"," 行政处罚信息",companyAdminPenaltiesService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("3"," 监督性监测信息",companySupervisoryMonitorService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("4"," 信访投诉信息",companyComplaintLetterService.findCountByCompanyId(companyId),basicInfoMenus);

        return basicInfoMenus;
    }

    private void addElements(String key,String value,Integer counts,List<Map<String,String>> basicInfoMenus){
        Map<String,String> param = new HashMap<>(2);
        param.put("key",key);
        param.put("text",value);
        param.put("point",Integer.toString(counts));
        basicInfoMenus.add(param);
    }

}
