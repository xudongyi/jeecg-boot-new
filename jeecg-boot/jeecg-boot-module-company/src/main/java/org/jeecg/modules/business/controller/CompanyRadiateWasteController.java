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
import org.jeecg.modules.business.entity.CompanyApply;
import org.jeecg.modules.business.entity.CompanyFile;
import org.jeecg.modules.business.entity.CompanyRadiateWaste;
import org.jeecg.modules.business.entity.CompanyRiskWaste;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.business.service.ICompanyRadiateWasteService;
import org.jeecg.modules.business.utils.Constant;
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
 * @Description: 辐射许可证信息
 * @Author: jeecg-boot
 * @Date: 2020-06-01
 * @Version: V1.0
 */
@Api(tags = "辐射许可证信息")
@RestController
@RequestMapping("/radiate/companyRadiateWaste")
@Slf4j
public class CompanyRadiateWasteController extends JeecgController<CompanyRadiateWaste, ICompanyRadiateWasteService> {
    @Autowired
    private ICompanyRadiateWasteService companyRadiateWasteService;
    @Autowired
    private ICompanyApplyService companyApplyService;
    @Autowired
    private ICompanyFileService companyFileService;

    /**
     * 分页列表查询
     *
     * @param companyRadiateWaste
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "辐射许可证信息-分页列表查询")
    @ApiOperation(value = "辐射许可证信息-分页列表查询", notes = "辐射许可证信息-分页列表查询")
    @GetMapping(value = "/list/{listType}")
    public Result<?> queryPageList(CompanyRadiateWaste companyRadiateWaste,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, @PathVariable int listType) {
        QueryWrapper<CompanyRadiateWaste> queryWrapper = QueryGenerator.initQueryWrapper(companyRadiateWaste, req.getParameterMap());
        if (listType == 0) {
            queryWrapper.ne("status", Constant.status.EXPIRED);
        } else {
            queryWrapper.eq("status", Constant.status.PEND).or().eq("status", Constant.status.NORMAL);
        }
        Page<CompanyRadiateWaste> page = new Page<CompanyRadiateWaste>(pageNo, pageSize);
        IPage<CompanyRadiateWaste> pageList = companyRadiateWasteService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "辐射许可证信息-添加")
    @ApiOperation(value = "辐射许可证信息-添加", notes = "辐射许可证信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody JSONObject jsonObject) {
        CompanyRadiateWaste companyRadiateWaste = this.getCompanyRadiateWaste(jsonObject);
        companyRadiateWaste.setStatus(Constant.status.TEMPORARY);
        companyRadiateWasteService.save(companyRadiateWaste);
        //新增申报记录（暂存）
        companyApplyService.saveByBase(companyRadiateWaste.getCompanyId(), companyRadiateWaste.getId(), companyRadiateWaste.getStatus(), "", Constant.tables.RADIATEWASTE);
        companyFileService.saveFiles(jsonObject.getString("fileList"), Constant.fileType.FILE, Constant.tables.RADIATEWASTE, companyRadiateWaste.getId());
        return Result.ok("添加成功！");
    }

    private CompanyRadiateWaste getCompanyRadiateWaste(JSONObject jsonObject) {
        CompanyRadiateWaste companyRadiateWaste = new CompanyRadiateWaste();
        companyRadiateWaste.setId(jsonObject.getString("id"));
        companyRadiateWaste.setCompanyId(jsonObject.getString("companyId"));
        companyRadiateWaste.setCreateBy(jsonObject.getString("createBy"));
        companyRadiateWaste.setCreateTime(jsonObject.getDate("createTime"));
        companyRadiateWaste.setUpdateBy(jsonObject.getString("updateBy"));
        companyRadiateWaste.setUpdateTime(jsonObject.getDate("updateTime"));
        companyRadiateWaste.setStatus(jsonObject.getString("status"));
        companyRadiateWaste.setLicenceCode(jsonObject.getString("licenceCode"));
        companyRadiateWaste.setValidStarttime(jsonObject.getDate("validStarttime"));
        companyRadiateWaste.setValidEndtime(jsonObject.getDate("validEndtime"));
        companyRadiateWaste.setCertificateTime(jsonObject.getDate("certificateTime"));
        companyRadiateWaste.setCertificateOffice(jsonObject.getString("certificateOffice"));
        companyRadiateWaste.setDirtyType(jsonObject.getString("dirtyType"));
        return companyRadiateWaste;
    }

    /**
     * 申报
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "辐射许可证-申报")
    @ApiOperation(value = "辐射许可证-申报", notes = "辐射许可证-申报")
    @PutMapping(value = "/declare")
    public Result<?> declare(@RequestBody JSONObject jsonObject) {
        CompanyRadiateWaste companyRadiateWaste = this.getCompanyRadiateWaste(jsonObject);
        companyRadiateWaste.setStatus(Constant.status.PEND);
        //判断是新增还是编辑
        if (!StrUtil.isEmpty(companyRadiateWaste.getId())) {
            //编辑
            //查询修改之前的对象
            CompanyRadiateWaste oldCompanyRadiateWaste = companyRadiateWasteService.getById(companyRadiateWaste.getId());
            //状态为正常
            if (Constant.status.NORMAL.equals(oldCompanyRadiateWaste.getStatus())) {
                CompanyApply companyApply = companyApplyService.findByNewId(oldCompanyRadiateWaste.getId(), Constant.tables.RADIATEWASTE);
               /* //修改老数据状态为过期
                oldCompanyRadiateWaste.setStatus(Constant.status.EXPIRED);
                companyRadiateWasteService.updateById(oldCompanyRadiateWaste);*/
                //修改老申报记录为过期
                companyApply.setStatus(Constant.status.EXPIRED);
                companyApplyService.updateById(companyApply);
                //新增修改后的为新数据
                companyRadiateWaste.setId("");
                companyRadiateWasteService.save(companyRadiateWaste);
                //新增申报记录
                companyApplyService.saveByBase(companyRadiateWaste.getCompanyId(), companyRadiateWaste.getId(), companyRadiateWaste.getStatus(), oldCompanyRadiateWaste.getId(), Constant.tables.RADIATEWASTE);
            } else if (Constant.status.NOPASS.equals(oldCompanyRadiateWaste.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyRadiateWaste.getStatus())) {
                //状态为审核未通过、暂存（直接修改）
                companyRadiateWasteService.updateById(companyRadiateWaste);
                //修改申报记录状态为待审核
                CompanyApply companyApply = companyApplyService.findByNewId(companyRadiateWaste.getId(), Constant.tables.RADIATEWASTE);
                companyApply.setStatus(Constant.status.PEND);
                companyApplyService.updateById(companyApply);
                //删除原有的
                companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable, Constant.tables.RADIATEWASTE)
                        .eq(CompanyFile::getTableId, companyRadiateWaste.getId()));
            }
        } else {
            //新增
            companyRadiateWasteService.save(companyRadiateWaste);
            //新增申报记录
            companyApplyService.saveByBase(companyRadiateWaste.getCompanyId(), companyRadiateWaste.getId(), companyRadiateWaste.getStatus(), "", Constant.tables.RADIATEWASTE);
        }
        companyFileService.saveFiles(jsonObject.getString("fileList"), Constant.fileType.FILE, Constant.tables.RADIATEWASTE, companyRadiateWaste.getId());
        return Result.ok("申报成功!");
    }

    /**
     * @Description: 批量申报
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/4
     */
    @AutoLog(value = "辐射许可证-批量申报")
    @ApiOperation(value = "辐射许可证-批量申报", notes = "辐射许可证-批量申报")
    @GetMapping(value = "/batchDeclare")
    public Result<?> batchDeclare(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        if (CollectionUtil.isNotEmpty(idList)) {
            for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
                String id = iterator.next();
                //查询
                CompanyRadiateWaste companyRadiateWaste = companyRadiateWasteService.getById(id);
                //判断申报的是否是暂存
                if (!Constant.status.TEMPORARY.equals(companyRadiateWaste.getStatus())) {
                    return Result.error("请选择暂存的信息申报！");
                }
                //修改状态为1：待审核状态
                companyRadiateWaste.setStatus(Constant.status.PEND);
                companyRadiateWasteService.updateById(companyRadiateWaste);
                //查询申报记录
                CompanyApply companyApply = companyApplyService.findByNewId(companyRadiateWaste.getId(), Constant.tables.RADIATEWASTE);
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
    @AutoLog(value = "辐射许可证信息-编辑")
    @ApiOperation(value = "辐射许可证信息-编辑", notes = "辐射许可证信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody JSONObject jsonObject) {
        CompanyRadiateWaste companyRadiateWaste = this.getCompanyRadiateWaste(jsonObject);
        CompanyRadiateWaste oldcompanyRadiateWaste = companyRadiateWasteService.getById(companyRadiateWaste.getId());
        //查询数据状态
        if (Constant.status.NORMAL.equals(companyRadiateWaste.getStatus())) {
            CompanyApply companyApply = companyApplyService.findByNewId(oldcompanyRadiateWaste.getId(), Constant.tables.RADIATEWASTE);
            companyRadiateWaste.setStatus(Constant.status.TEMPORARY);
            //正常
           /* oldcompanyRadiateWaste.setStatus(Constant.status.EXPIRED);
            companyRadiateWasteService.updateById(oldcompanyRadiateWaste);*/
            //新增修改后的为新数据
            companyRadiateWaste.setId("");
            companyRadiateWasteService.save(companyRadiateWaste);
            //新增申报记录
            companyApplyService.saveByBase(companyRadiateWaste.getCompanyId(), companyRadiateWaste.getId(), companyRadiateWaste.getStatus(), oldcompanyRadiateWaste.getId(), Constant.tables.RADIATEWASTE);
        } else if (Constant.status.NOPASS.equals(oldcompanyRadiateWaste.getStatus()) || Constant.status.TEMPORARY.equals(oldcompanyRadiateWaste.getStatus())) {
            companyRadiateWaste.setStatus(Constant.status.TEMPORARY);
            //状态为未通过和暂存的
            companyRadiateWasteService.updateById(companyRadiateWaste);
            //修改申报记录状态为暂存
            CompanyApply companyApply = companyApplyService.findByNewId(companyRadiateWaste.getId(), Constant.tables.RADIATEWASTE);
            companyApply.setStatus(Constant.status.TEMPORARY);
            companyApplyService.updateById(companyApply);
            //删除原有的
            companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable, Constant.tables.RADIATEWASTE)
                    .eq(CompanyFile::getTableId, companyRadiateWaste.getId()));
        }
        companyFileService.saveFiles(jsonObject.getString("fileList"), Constant.fileType.FILE, Constant.tables.RADIATEWASTE, companyRadiateWaste.getId());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "辐射许可证信息-通过id删除")
    @ApiOperation(value = "辐射许可证信息-通过id删除", notes = "辐射许可证信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        companyRadiateWasteService.removeById(id);
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
    @AutoLog(value = "辐射许可证信息-批量删除")
    @ApiOperation(value = "辐射许可证信息-批量删除", notes = "辐射许可证信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companyRadiateWasteService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "辐射许可证信息-通过id查询")
    @ApiOperation(value = "辐射许可证信息-通过id查询", notes = "辐射许可证信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CompanyRadiateWaste companyRadiateWaste = companyRadiateWasteService.getById(id);
        if (companyRadiateWaste == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(companyRadiateWaste);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param companyRadiateWaste
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyRadiateWaste companyRadiateWaste) {
        return super.exportXls(request, companyRadiateWaste, CompanyRadiateWaste.class, "辐射许可证信息");
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
        return super.importExcel(request, response, CompanyRadiateWaste.class);
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
        List<Map<String, String>> result = companyFileService.getFileMaps(id, Constant.tables.RADIATEWASTE);
        return Result.ok(result);
    }

}
