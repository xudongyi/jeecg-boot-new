package org.jeecg.modules.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.annotation.Area;
import org.jeecg.modules.business.entity.SysArea;
import org.jeecg.modules.business.mapper.SysDictBussinessMapper;
import org.jeecg.modules.business.service.ISysAreaService;
import org.jeecg.modules.business.service.IViewCompanyBaseService;
import org.jeecg.modules.business.vo.ViewCompanyBase;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    IViewCompanyBaseService viewCompanyBaseService;
    @Resource
    private SysDictBussinessMapper dictService;
    @Autowired
    ISysAreaService sysAreaService;
    /**
     * 导出excel
     *
     * @param request
     * @param viewCompanyBase
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ViewCompanyBase viewCompanyBase) {
        // Step.1 组装查询条件
        QueryWrapper<ViewCompanyBase> queryWrapper = QueryGenerator.initQueryWrapper(viewCompanyBase, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<ViewCompanyBase> pageList = viewCompanyBaseService.list(queryWrapper);
        List<ViewCompanyBase> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }
        dictText(exportList);
        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "企业基本信息"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, ViewCompanyBase.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams( "企业基本信息报表", "导出人:" + sysUser.getRealname(), "企业基本信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;

    }
    /**
     * 获取对象ID
     *
     * @return
     */
    private String getId(ViewCompanyBase item) {
        try {
            return PropertyUtils.getProperty(item, "id").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //后期字典表需要缓存
    private void dictText(List<ViewCompanyBase> exportList) {

        //如果有字典
        for(ViewCompanyBase viewCompanyBase : exportList){
            // 遍历所有的字段
            for (Field field : ViewCompanyBase.class.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    //字典
                    if(field.getAnnotation(Dict.class) != null){

                        field.set(viewCompanyBase, dictService.queryDictTextByKey( field.getAnnotation(Dict.class).dicCode(),String.valueOf(field.get(viewCompanyBase))));

                    }
                    //行政所属区域
                    if(field.getAnnotation(Area.class) != null){

                        field.set(viewCompanyBase,handleArea(String.valueOf(field.get(viewCompanyBase)), ""));

                    }
                } catch (IllegalAccessException e) {
                    // 只要调用了 field.setAccessible(true) 就不会报这个异常
                    throw new IllegalStateException("获取属性发生异常: " + field.getName(), e);
                }
            }
        }
    }

    private String handleArea(String code, String area) throws IllegalAccessException {
        //查询
        SysArea sysArea = sysAreaService.getOne(new QueryWrapper<SysArea>().lambda().eq(SysArea::getCode,code));
        if(sysArea==null)
            return "";
        area=sysArea.getName()+area;
        if(sysArea.getParentCode().equals("86"))
            return area;
        return handleArea(sysArea.getParentCode(), area);
    }
}
