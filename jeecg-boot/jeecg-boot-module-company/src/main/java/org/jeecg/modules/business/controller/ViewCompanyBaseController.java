package org.jeecg.modules.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.CompanyBaseinfo;
import org.jeecg.modules.business.entity.CompanySysuser;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyBaseinfoService;
import org.jeecg.modules.business.service.ICompanySysuserService;
import org.jeecg.modules.business.service.IViewCompanyBaseService;
import org.jeecg.modules.business.utils.Constant;
import org.jeecg.modules.business.vo.CompanyBaseInfoSimple;
import org.jeecg.modules.business.vo.ViewCompanyBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
* @Description: company_baseinfo
* @Author: jeecg-boot
* @Date:   2020-05-27
* @Version: V1.0
*/
@Api(tags="company_baseinfo")
@RestController
@RequestMapping("/company/viewCompanyBase")
@Slf4j
public class ViewCompanyBaseController extends JeecgController<ViewCompanyBase, IViewCompanyBaseService> {

    /**
     * 导出excel
     *
     * @param request
     * @param viewCompanyBase
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ViewCompanyBase viewCompanyBase) {
        return super.exportXls(request, viewCompanyBase, ViewCompanyBase.class, "企业基本信息");
    }

}
