package org.jeecg.modules.business.controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.CompanyAcceptance;
import org.jeecg.modules.business.entity.CompanyApply;
import org.jeecg.modules.business.entity.CompanyDirtyAllow;
import org.jeecg.modules.business.entity.CompanyFile;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyDirtyAllowService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.business.utils.Constant;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 排污许可证信息
 * @Author: jeecg-boot
 * @Date: 2020-06-01
 * @Version: V1.0
 */
@Api(tags = "排污许可证信息")
@RestController
@RequestMapping("/dirty/companyDirtyAllow")
@Slf4j
public class CompanyDirtyAllowController extends JeecgController<CompanyDirtyAllow, ICompanyDirtyAllowService> {
    @Autowired
    private ICompanyDirtyAllowService companyDirtyAllowService;
    @Autowired
    private ICompanyApplyService companyApplyService;
    @Autowired
    private ICompanyFileService companyFileService;


    /**
     * 分页列表查询
     *
     * @param companyDirtyAllow
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "排污许可证信息-分页列表查询")
    @ApiOperation(value = "排污许可证信息-分页列表查询", notes = "排污许可证信息-分页列表查询")
    @GetMapping(value = "/list/{listType}")
    public Result<?> queryPageList(CompanyDirtyAllow companyDirtyAllow,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, @PathVariable int listType) {
        QueryWrapper<CompanyDirtyAllow> queryWrapper = QueryGenerator.initQueryWrapper(companyDirtyAllow, req.getParameterMap());
        if (listType == 2) {
            queryWrapper.eq("status", Constant.status.NORMAL);
        } else if (listType == 1) {
            queryWrapper.and(wrapper -> wrapper.eq("status", Constant.status.PEND).or().eq("status", Constant.status.NORMAL));
        } else if (listType == 0) {
            queryWrapper.ne("status", Constant.status.EXPIRED).orderByDesc("create_time");
        }
        Page<CompanyDirtyAllow> page = new Page<CompanyDirtyAllow>(pageNo, pageSize);
        IPage<CompanyDirtyAllow> pageList = companyDirtyAllowService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "排污许可证信息-添加")
    @ApiOperation(value = "排污许可证信息-添加", notes = "排污许可证信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody JSONObject jsonObject) {
        CompanyDirtyAllow companyDirtyAllow = this.getCompanyDirtyAllow(jsonObject);
        companyDirtyAllow.setStatus(Constant.status.TEMPORARY);
        companyDirtyAllowService.save(companyDirtyAllow);
        //新增申报记录（暂存）
        companyApplyService.saveByBase(companyDirtyAllow.getCompanyId(), companyDirtyAllow.getId(), companyDirtyAllow.getStatus(), "", Constant.tables.DIRTYALLOW);
        companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.DIRTYALLOW,companyDirtyAllow.getId());
        return Result.ok("添加成功！");
    }

    private CompanyDirtyAllow getCompanyDirtyAllow(JSONObject jsonObject) {
        CompanyDirtyAllow companyDirtyAllow = new CompanyDirtyAllow();
        companyDirtyAllow.setId(jsonObject.getString("id"));
        companyDirtyAllow.setCompanyId(jsonObject.getString("companyId"));
        companyDirtyAllow.setCreateBy(jsonObject.getString("createBy"));
        companyDirtyAllow.setCreateTime(jsonObject.getDate("createTime"));
        companyDirtyAllow.setUpdateBy(jsonObject.getString("updateBy"));
        companyDirtyAllow.setUpdateTime(jsonObject.getDate("updateTime"));
        companyDirtyAllow.setStatus(jsonObject.getString("status"));
        companyDirtyAllow.setLicenceCode(jsonObject.getString("licenceCode"));
        companyDirtyAllow.setValidStarttime(jsonObject.getDate("validStarttime"));
        companyDirtyAllow.setValidEndtime(jsonObject.getDate("validEndtime"));
        companyDirtyAllow.setCertificateTime(jsonObject.getDate("certificateTime"));
        companyDirtyAllow.setCertificateOffice(jsonObject.getString("certificateOffice"));
        companyDirtyAllow.setDirtyType(jsonObject.getString("dirtyType"));
        return companyDirtyAllow;
    }

    /**
     * 申报
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "排污许可证-申报")
    @ApiOperation(value = "排污许可证-申报", notes = "排污许可证-申报")
    @PutMapping(value = "/declare")
    public Result<?> declare(@RequestBody JSONObject jsonObject) {
        CompanyDirtyAllow companyDirtyAllow = this.getCompanyDirtyAllow(jsonObject);
        companyDirtyAllow.setStatus(Constant.status.PEND);
        //判断是新增还是编辑
        if (!StrUtil.isEmpty(companyDirtyAllow.getId())) {
            //编辑
            //查询修改之前的对象
            CompanyDirtyAllow oldCompanyDirtyAllow = companyDirtyAllowService.getById(companyDirtyAllow.getId());
            //状态为正常
            if (Constant.status.NORMAL.equals(oldCompanyDirtyAllow.getStatus())) {
                //修改老数据状态为过期
                /*oldCompanyDirtyAllow.setStatus(Constant.status.EXPIRED);
                companyDirtyAllowService.updateById(oldCompanyDirtyAllow);*/
                //修改老申报记录为过期
                //新增修改后的为新数据
                companyDirtyAllow.setId("");
                companyDirtyAllowService.save(companyDirtyAllow);
                //新增申报记录
                companyApplyService.saveByBase(companyDirtyAllow.getCompanyId(), companyDirtyAllow.getId(), companyDirtyAllow.getStatus(), oldCompanyDirtyAllow.getId(), Constant.tables.DIRTYALLOW);
            } else if (Constant.status.NOPASS.equals(oldCompanyDirtyAllow.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyDirtyAllow.getStatus())) {
                //状态为审核未通过、暂存（直接修改）
                companyDirtyAllowService.updateById(companyDirtyAllow);
                //修改申报记录状态为待审核
                CompanyApply companyApply = companyApplyService.findByNewId(companyDirtyAllow.getId(), Constant.tables.DIRTYALLOW);
                companyApply.setStatus(Constant.status.PEND);
                companyApplyService.updateById(companyApply);
                //删除原有的
                companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.DIRTYALLOW)
                        .eq(CompanyFile::getTableId,companyDirtyAllow.getId()));
            }
        } else {
            //新增
            companyDirtyAllowService.save(companyDirtyAllow);
            //新增申报记录
            companyApplyService.saveByBase(companyDirtyAllow.getCompanyId(), companyDirtyAllow.getId(), companyDirtyAllow.getStatus(), "", Constant.tables.DIRTYALLOW);
        }
        companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.DIRTYALLOW,companyDirtyAllow.getId());
        return Result.ok("申报成功!");
    }

    /**
     * @Description: 批量申报
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/4
     */
    @AutoLog(value = "排污许可证-批量申报")
    @ApiOperation(value = "排污许可证-批量申报", notes = "排污许可证-批量申报")
    @GetMapping(value = "/batchDeclare")
    public Result<?> batchDeclare(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        if (CollectionUtil.isNotEmpty(idList)) {
            for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
                String id = iterator.next();
                //查询
                CompanyDirtyAllow companyDirtyAllow = companyDirtyAllowService.getById(id);
                //判断申报的是否是暂存
                if (!Constant.status.TEMPORARY.equals(companyDirtyAllow.getStatus())) {
                    return Result.error("请选择暂存的信息申报！");
                }
                //修改状态为1：待审核状态
                companyDirtyAllow.setStatus(Constant.status.PEND);
                companyDirtyAllowService.updateById(companyDirtyAllow);
                //查询申报记录
                CompanyApply companyApply = companyApplyService.findByNewId(companyDirtyAllow.getId(), Constant.tables.DIRTYALLOW);
                companyApply.setStatus(Constant.status.PEND);
                companyApplyService.updateById(companyApply);
            }
        }
        return Result.ok("申报成功!");
    }

    /**
     * 编辑
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "排污许可证信息-编辑")
    @ApiOperation(value = "排污许可证信息-编辑", notes = "排污许可证信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody JSONObject jsonObject) {
        CompanyDirtyAllow companyDirtyAllow = this.getCompanyDirtyAllow(jsonObject);
        CompanyDirtyAllow oldcompanyDirtyAllow = companyDirtyAllowService.getById(companyDirtyAllow.getId());
        //查询数据状态
        if (Constant.status.NORMAL.equals(companyDirtyAllow.getStatus())) {
            companyDirtyAllow.setStatus(Constant.status.TEMPORARY);
            //正常
           /* oldcompanyDirtyAllow.setStatus(Constant.status.EXPIRED);
            companyDirtyAllowService.updateById(oldcompanyDirtyAllow);*/
            //新增修改后的为新数据
            companyDirtyAllow.setId("");
            companyDirtyAllowService.save(companyDirtyAllow);
            //新增申报记录
            companyApplyService.saveByBase(companyDirtyAllow.getCompanyId(), companyDirtyAllow.getId(), companyDirtyAllow.getStatus(), oldcompanyDirtyAllow.getId(), Constant.tables.DIRTYALLOW);
        } else if (Constant.status.NOPASS.equals(oldcompanyDirtyAllow.getStatus()) || Constant.status.TEMPORARY.equals(oldcompanyDirtyAllow.getStatus())) {
            companyDirtyAllow.setStatus(Constant.status.TEMPORARY);
            //状态为未通过和暂存的
            companyDirtyAllowService.updateById(companyDirtyAllow);
            //修改申报记录状态为暂存
            CompanyApply companyApply = companyApplyService.findByNewId(companyDirtyAllow.getId(), Constant.tables.DIRTYALLOW);
            companyApply.setStatus(Constant.status.TEMPORARY);
            companyApplyService.updateById(companyApply);
            //删除原有的
            companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.DIRTYALLOW)
                    .eq(CompanyFile::getTableId,companyDirtyAllow.getId()));
        }
        companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.DIRTYALLOW,companyDirtyAllow.getId());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "排污许可证信息-通过id删除")
    @ApiOperation(value = "排污许可证信息-通过id删除", notes = "排污许可证信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        companyDirtyAllowService.removeById(id);
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
    @AutoLog(value = "排污许可证信息-批量删除")
    @ApiOperation(value = "排污许可证信息-批量删除", notes = "排污许可证信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companyDirtyAllowService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "排污许可证信息-通过id查询")
    @ApiOperation(value = "排污许可证信息-通过id查询", notes = "排污许可证信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CompanyDirtyAllow companyDirtyAllow = companyDirtyAllowService.getById(id);
        if (companyDirtyAllow == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(companyDirtyAllow);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param companyDirtyAllow
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyDirtyAllow companyDirtyAllow) {
        return super.exportXls(request, companyDirtyAllow, CompanyDirtyAllow.class, "排污许可证信息");
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
        return super.importExcel(request, response, CompanyDirtyAllow.class);
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
        List<Map<String, String>> result = companyFileService.getFileMaps(id, Constant.tables.DIRTYALLOW);
        return Result.ok(result);
    }

}
