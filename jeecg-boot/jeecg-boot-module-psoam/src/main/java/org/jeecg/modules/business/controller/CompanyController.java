package org.jeecg.modules.business.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.PsoamConfig;
import org.jeecg.modules.business.entity.SysPollutionCode;
import org.jeecg.modules.business.service.ICompanyService;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.business.service.ISysPollutionCodeService;
import org.jeecg.modules.business.utils.RedisCacheUtil;
import org.jeecg.modules.business.utils.Util;
import org.jeecg.modules.business.view.SelfEntityExcelView;
import org.jeecg.modules.business.vo.Column;
import org.jeecg.modules.business.vo.OverEntry;
import org.jeecg.modules.business.vo.OverStandardRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Api(tags = "company_acceptance")
@RestController
@RequestMapping("/psoam/companyInfo")
@Slf4j
public class CompanyController {

    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ISysDictService sysDictService;
    @Resource
    private RedisCacheUtil redisCacheUtil;
    @AutoLog(value = "gisMap查询企业信息")
    @ApiOperation(value = "gisMap查询企业信息", notes = "gisMap查询企业信息")
    @GetMapping(value = "/queryGisInfos")
    public Result<?> queryGisInfos(@RequestParam(name="companyIds",required=true) String companyIds) {
        return Result.ok(companyService.queryCompanyInfos(Arrays.asList(companyIds.split(","))));
    }


    @AutoLog(value = "gisMap查询站点信息")
    @ApiOperation(value = "站点污染因子信息", notes = "站点污染因子信息")
    @GetMapping(value = "/querySiteInfos")
    public Result<?> querySiteInfos(@RequestParam(name="siteType",required=true) String siteType,
                                    @RequestParam(name="mn",required=true) String mn) {

        Map<String,String> siteTypeTable = new HashMap<>();
        siteTypeTable.put("0","water_current_tr_");
        siteTypeTable.put("1","air_current_tr_");
        siteTypeTable.put("2","air_current_tr_");
        siteTypeTable.put("3","air_current_tr_");

        //查询启用的污染因子   污水
        List<Map<String,Object>> sysPollutionCodes = companyService.queryCodeAndStatus(mn);
        String tableName = Util.getTableName(siteTypeTable.get(siteType),"");
        String field = "";
        Map<String,Map<String,Object>> codes = new HashMap<>();
        for(Map<String,Object> sysPollutionCode:sysPollutionCodes){
            field+= sysPollutionCode.get("code")+"_rtd,";
            if ("Y".equalsIgnoreCase(sysPollutionCode.get("is_zs").toString())) {
                field+= sysPollutionCode.get("code")+"_zsrtd,";
            }
            codes.put(sysPollutionCode.get("code").toString(),sysPollutionCode);
        }
        if(field.length()==0){
            return Result.error(500,"该站点未配置");
        }
        field = field.substring(0,field.length()-1);

        Map<String, Object> realData = companyService.queryRealTime(mn,tableName,field);
        Map<String,Object> result = new HashMap<>();
        List<Map<String,String>> list = new ArrayList<>();
        Boolean has_zs = false;
        for(Map.Entry<String,Object> entry:realData.entrySet()){
            if("data_time".equals(entry.getKey())){
                result.put(entry.getKey(), DateUtil.format((Timestamp)entry.getValue(),"yyyy-MM-dd HH:mm:ss"));
                continue;
            }
            //折算值
            if(entry.getKey().endsWith("_zsrtd")){
                continue;
            }
            Map<String,String> columData = new HashMap<>();

            String key = entry.getKey().split("_")[0];
            columData.put("pollutionName",redisCacheUtil.transformCode(key));
            columData.put("value",entry.getValue().toString());
            String chromaUnit = sysDictService.queryDictTextByKey("allUnit", codes.get(key).get("chroma_unit").toString());
            columData.put("unit",chromaUnit);

            if(realData.get(key+"_zsrtd")!=null){
                columData.put("zsValue",realData.get(key+"_zsrtd").toString());
                has_zs=true;
            }
            double value = Double.parseDouble(entry.getValue().toString());
//            if(value>Double.parseDouble(codes.get(key).get("error_max").toString()))
//                columData.put("dataStatus","超量程异常") ;
//            else if(value>Double.parseDouble(codes.get(key).get("error_min").toString()))
//                columData.put("dataStatus","超标") ;
//            else{
                columData.put("dataStatus","正常") ;
//            }

            columData.put("deviceStatus",codes.get(key).get("device_state").toString());
            list.add(columData);
        }
        result.put("hasZs",has_zs);
        result.put("dataList",list);
        //查询相关联的因子
        return Result.ok(result);
    }
    @AutoLog(value = "企业超标率统计")
    @ApiOperation(value = "企业超标率统计", notes = "企业超标率统计")
    @GetMapping(value = "/overStandardRate")
    public Result<?> overStandardRate(HttpServletRequest req) {
        return Result.ok(companyService.queryoverStandardRate(req));
    }
    @AutoLog(value = "企业超标率统计")
    @ApiOperation(value = "企业超标率统计", notes = "企业超标率统计")
    @GetMapping(value = "/exportOverStandardRate")
    public ModelAndView exportOverStandardRate(HttpServletRequest req) {

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new SelfEntityExcelView(null,null));
        mv.addObject(SelfExcelConstants.TITLE, "企业超标率统计"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(SelfExcelConstants.SHEET_NAME, "企业超标率统计");
        mv.addObject(SelfExcelConstants.CLAZZ, OverStandardRate.class);
        mv.addObject(SelfExcelConstants.DATA_LIST, companyService.queryoverStandardRate(req));
//        mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");
        return mv;
    }


    @AutoLog(value = "企业站点查询")
    @ApiOperation(value = "企业站点状态查询", notes = "企业站点查询")
    @GetMapping(value = "/queryCompanySite")
    public Result<?> queryCompanySite(HttpServletRequest req) {


        return Result.ok(companyService.queryCompanySite(req));
    }

}
