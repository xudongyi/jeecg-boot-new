package org.jeecg.modules.business.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.IViewCompanyBaseService;
import org.jeecg.modules.business.vo.ViewCompanyBase;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
