package org.jeecg.modules.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.business.entity.PsoamConfig;
import org.jeecg.modules.business.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Api(tags = "company_acceptance")
@RestController
@RequestMapping("/psoam/companyInfo")
@Slf4j
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    @AutoLog(value = "gisMap查询企业信息")
    @ApiOperation(value = "gisMap查询企业信息", notes = "gisMap查询企业信息")
    @GetMapping(value = "/queryGisInfos")
    public Result<?> queryGisInfos(@RequestParam(name="companyIds",required=true) String companyIds) {
        return Result.ok(companyService.queryCompanyInfos(Arrays.asList(companyIds.split(","))));
    }
}
