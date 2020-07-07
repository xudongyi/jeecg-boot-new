package org.jeecg.modules.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
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
import org.jeecg.modules.business.entity.CompanyAcceptance;
import org.jeecg.modules.business.entity.CompanyApply;
import org.jeecg.modules.business.entity.CompanyFile;
import org.jeecg.modules.business.service.ICompanyAcceptanceService;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.business.utils.Constant;
import org.jeecg.modules.business.utils.Constant.status;
import org.jeecg.modules.business.utils.Constant.tables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description: company_acceptance
 * @Author: jeecg-boot
 * @Date: 2020-05-27
 * @Version: V1.0
 */
@Api(tags = "company_acceptance")
@RestController
@RequestMapping("/business/companyAcceptance")
@Slf4j
public class CompanyAcceptanceController extends JeecgController<CompanyAcceptance, ICompanyAcceptanceService> {
    @Autowired
    private ICompanyAcceptanceService companyAcceptanceService;

    @Autowired
    private ICompanyApplyService companyApplyService;

    @Autowired
    private ICompanyFileService companyFileService;

    /**
     * 分页列表查询
     *
     * @param companyAcceptance
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "company_acceptance-分页列表查询")
    @ApiOperation(value = "company_acceptance-分页列表查询", notes = "company_acceptance-分页列表查询")
    @GetMapping(value = "/list/{listType}")
    public Result<?> queryPageList(CompanyAcceptance companyAcceptance,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, @PathVariable int listType) {
        QueryWrapper<CompanyAcceptance> queryWrapper = QueryGenerator.initQueryWrapper(companyAcceptance, req.getParameterMap());
        if (listType == 2) {
            queryWrapper.eq("status", status.NORMAL);
        } else if (listType == 1) {
            queryWrapper.and(wrapper -> wrapper.eq("status", status.PEND).or().eq("status", status.NORMAL));
        } else if (listType == 0) {
            queryWrapper.ne("status",status.EXPIRED).orderByDesc("create_time");
        }
        Page<CompanyAcceptance> page = new Page<CompanyAcceptance>(pageNo, pageSize);
        IPage<CompanyAcceptance> pageList = companyAcceptanceService.page(page, queryWrapper);
        return Result.ok(pageList);
    }


    /**
     * 添加
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "company_acceptance-添加")
    @ApiOperation(value = "company_acceptance-添加", notes = "company_acceptance-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody JSONObject jsonObject) {
        CompanyAcceptance companyAcceptance = this.getCompanyAcceptance(jsonObject);
        companyAcceptance.setStatus(status.TEMPORARY);
        companyAcceptanceService.save(companyAcceptance);
        //新增申报记录（暂存）
        companyApplyService.saveByBase(companyAcceptance.getCompanyId(), companyAcceptance.getId(), companyAcceptance.getStatus(), "", tables.ACCEPTANCE);
        companyFileService.saveFiles(jsonObject.getString("fileList"), Constant.fileType.FILE, Constant.tables.ACCEPTANCE, companyAcceptance.getId());
        return Result.ok("添加成功！");
    }

    /**
     * 申报
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "company_acceptance-申报")
    @ApiOperation(value = "company_acceptance-申报", notes = "company_acceptance-申报")
    @PutMapping(value = "/declare")
    public Result<?> declare(@RequestBody JSONObject jsonObject) {
        CompanyAcceptance companyAcceptance = this.getCompanyAcceptance(jsonObject);
        companyAcceptance.setStatus(status.PEND);
        //判断是新增还是编辑
        if (!StrUtil.isEmpty(companyAcceptance.getId())) {
            //编辑
            //查询修改之前的对象
            CompanyAcceptance oldCompanyAcceptance = companyAcceptanceService.getById(companyAcceptance.getId());
            //状态为正常
            if (status.NORMAL.equals(oldCompanyAcceptance.getStatus())) {
                /*CompanyApply companyApply = companyApplyService.findByNewId(oldCompanyAcceptance.getId(), Constant.tables.ACCEPTANCE);
                //修改老数据状态为过期
                oldCompanyAcceptance.setStatus(status.EXPIRED);
                companyAcceptanceService.updateById(oldCompanyAcceptance);
                //修改老申报记录为过期
                companyApply.setStatus(Constant.status.EXPIRED);
                companyApplyService.updateById(companyApply);*/
                //新增修改后的为新数据
                companyAcceptance.setId("");
                companyAcceptanceService.save(companyAcceptance);
                //新增申报记录
                companyApplyService.saveByBase(companyAcceptance.getCompanyId(), companyAcceptance.getId(), companyAcceptance.getStatus(), oldCompanyAcceptance.getId(), tables.ACCEPTANCE);
            } else if (status.NOPASS.equals(oldCompanyAcceptance.getStatus()) || status.TEMPORARY.equals(oldCompanyAcceptance.getStatus())) {
                //状态为审核未通过、暂存（直接修改）
                companyAcceptanceService.updateById(companyAcceptance);
                //修改申报记录状态为待审核
                CompanyApply companyApply = companyApplyService.findByNewId(companyAcceptance.getId(), tables.ACCEPTANCE);
                companyApply.setStatus(status.PEND);
                companyApplyService.updateById(companyApply);
                //删除原有的
                companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable, Constant.tables.ACCEPTANCE)
                        .eq(CompanyFile::getTableId, companyAcceptance.getId()));
            }
        } else {
            //新增
            companyAcceptanceService.save(companyAcceptance);
            //新增申报记录
            companyApplyService.saveByBase(companyAcceptance.getCompanyId(), companyAcceptance.getId(), companyAcceptance.getStatus(), "", tables.ACCEPTANCE);
        }
        companyFileService.saveFiles(jsonObject.getString("fileList"), Constant.fileType.FILE, Constant.tables.ACCEPTANCE, companyAcceptance.getId());
        return Result.ok("申报成功!");
    }

    /**
     * 编辑
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "company_acceptance-编辑")
    @ApiOperation(value = "company_acceptance-编辑", notes = "company_acceptance-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody JSONObject jsonObject) {
        CompanyAcceptance companyAcceptance = this.getCompanyAcceptance(jsonObject);
        CompanyAcceptance oldCompanyAcceptance = companyAcceptanceService.getById(companyAcceptance.getId());
        //查询数据状态
        if (status.NORMAL.equals(companyAcceptance.getStatus())) {
            companyAcceptance.setStatus(status.TEMPORARY);
            //正常
           /* oldCompanyAcceptance.setStatus(status.EXPIRED);
            companyAcceptanceService.updateById(oldCompanyAcceptance);*/
            //新增修改后的为新数据
            companyAcceptance.setId("");
            companyAcceptanceService.save(companyAcceptance);
            //新增申报记录
            companyApplyService.saveByBase(companyAcceptance.getCompanyId(), companyAcceptance.getId(), companyAcceptance.getStatus(), oldCompanyAcceptance.getId(), tables.ACCEPTANCE);
        } else if (status.NOPASS.equals(oldCompanyAcceptance.getStatus()) || status.TEMPORARY.equals(oldCompanyAcceptance.getStatus())) {
            //状态为未通过和暂存的
            companyAcceptance.setStatus(status.TEMPORARY);
            companyAcceptanceService.updateById(companyAcceptance);
            //修改申报记录状态为暂存
            CompanyApply companyApply = companyApplyService.findByNewId(companyAcceptance.getId(), tables.ACCEPTANCE);
            companyApply.setStatus(status.TEMPORARY);
            companyApplyService.updateById(companyApply);
            //删除原有的
            companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable, Constant.tables.ACCEPTANCE)
                    .eq(CompanyFile::getTableId, companyAcceptance.getId()));
        }
        //保存文件
        companyFileService.saveFiles(jsonObject.getString("fileList"), Constant.fileType.FILE, Constant.tables.ACCEPTANCE, companyAcceptance.getId());
        return Result.ok("编辑成功!");
    }

    private CompanyAcceptance getCompanyAcceptance(JSONObject jsonObject) {
        CompanyAcceptance companyAcceptance = new CompanyAcceptance();
        companyAcceptance.setId(jsonObject.getString("id"));
        companyAcceptance.setCompanyId(jsonObject.getString("companyId"));
        companyAcceptance.setCreateBy(jsonObject.getString("createBy"));
        companyAcceptance.setCreateTime(jsonObject.getDate("createTime"));
        companyAcceptance.setUpdateBy(jsonObject.getString("updateBy"));
        companyAcceptance.setUpdateTime(jsonObject.getDate("updateTime"));
        companyAcceptance.setStatus(jsonObject.getString("status"));
        companyAcceptance.setProjectName(jsonObject.getString("projectName"));
        companyAcceptance.setExamineUnit(jsonObject.getString("examineUnit"));
        companyAcceptance.setExamineNum(jsonObject.getString("examineNum"));
        companyAcceptance.setExamineTime(jsonObject.getDate("examineTime"));
        return companyAcceptance;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "company_acceptance-通过id删除")
    @ApiOperation(value = "company_acceptance-通过id删除", notes = "company_acceptance-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        companyAcceptanceService.removeById(id);
        //删除申报记录
        companyApplyService.remove(new QueryWrapper<CompanyApply>().lambda()
                .eq(CompanyApply::getStatus, Constant.status.TEMPORARY)
                .in(CompanyApply::getNewId, id));
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "company_acceptance-批量删除")
    @ApiOperation(value = "company_acceptance-批量删除", notes = "company_acceptance-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companyAcceptanceService.removeByIds(Arrays.asList(ids.split(",")));
        //删除申报记录
        companyApplyService.remove(new QueryWrapper<CompanyApply>().lambda()
                .eq(CompanyApply::getStatus, Constant.status.TEMPORARY)
                .in(CompanyApply::getNewId, Arrays.asList(ids.split(","))));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "company_acceptance-通过id查询")
    @ApiOperation(value = "company_acceptance-通过id查询", notes = "company_acceptance-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CompanyAcceptance companyAcceptance = companyAcceptanceService.getById(id);
        if (companyAcceptance == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(companyAcceptance);
    }

    /**
     * @Description: 批量申报
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/4
     */
    @AutoLog(value = "company_acceptance-批量申报")
    @ApiOperation(value = "company_acceptance-批量申报", notes = "company_acceptance-批量申报")
    @GetMapping(value = "/batchDeclare")
    public Result<?> batchDeclare(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        if (CollectionUtil.isNotEmpty(idList)) {
            for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
                String id = iterator.next();
                //查询
                CompanyAcceptance companyAcceptance = companyAcceptanceService.getById(id);
                //判断申报的是否是暂存
                if (!status.TEMPORARY.equals(companyAcceptance.getStatus())) {
                    return Result.error("请选择暂存的竣工验收信息申报！");
                }
                //修改状态为1：待审核状态
                companyAcceptance.setStatus(status.PEND);
                companyAcceptanceService.updateById(companyAcceptance);
                //查询申报记录
                CompanyApply companyApply = companyApplyService.findByNewId(companyAcceptance.getId(), tables.ACCEPTANCE);
                companyApply.setStatus(status.PEND);
                companyApplyService.updateById(companyApply);
            }
        }
        return Result.ok("申报成功!");
    }


    /**
     * 导出excel
     *
     * @param request
     * @param companyAcceptance
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyAcceptance companyAcceptance) {
        return super.exportXls(request, companyAcceptance, CompanyAcceptance.class, tables.ACCEPTANCE);
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CompanyAcceptance.class);
    }

    /**
     * 查询附件
     *
     * @param id
     * @return
     */
    @AutoLog(value = "查询附件")
    @ApiOperation(value = "查询附件", notes = "查询附件")
    @GetMapping(value = "/queryFiles")
    public Result<?> queryFiles(@RequestParam(name = "id", required = true) String id) {
        List<Map<String, String>> result = companyFileService.getFileMaps(id, Constant.tables.ACCEPTANCE);
        return Result.ok(result);
    }
}
