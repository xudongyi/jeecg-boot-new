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
import org.jeecg.modules.business.entity.CompanySolidWaste;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.business.service.ICompanySolidWasteService;
import org.jeecg.modules.bussiness.utils.Constant;
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
 * @Description: 固废许可证信息
 * @Author: jeecg-boot
 * @Date: 2020-06-01
 * @Version: V1.0
 */
@Api(tags = "固废许可证信息")
@RestController
@RequestMapping("/solid/companySolidWaste")
@Slf4j
public class CompanySolidWasteController extends JeecgController<CompanySolidWaste, ICompanySolidWasteService> {
    @Autowired
    private ICompanySolidWasteService companySolidWasteService;
    @Autowired
    private ICompanyApplyService companyApplyService;
    @Autowired
    private ICompanyFileService companyFileService;

    /**
     * 分页列表查询
     *
     * @param companySolidWaste
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "固废许可证信息-分页列表查询")
    @ApiOperation(value = "固废许可证信息-分页列表查询", notes = "固废许可证信息-分页列表查询")
    @GetMapping(value = "/list/{listType}")
    public Result<?> queryPageList(CompanySolidWaste companySolidWaste,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, @PathVariable int listType) {
        QueryWrapper<CompanySolidWaste> queryWrapper = QueryGenerator.initQueryWrapper(companySolidWaste, req.getParameterMap());
        if (listType == 2) {
            queryWrapper.eq("status", Constant.status.NORMAL);
        } else if (listType == 1) {
            queryWrapper.and(wrapper -> wrapper.eq("status", Constant.status.PEND).or().eq("status", Constant.status.NORMAL));
        } else if (listType == 0) {
            queryWrapper.ne("status", Constant.status.EXPIRED).orderByDesc("create_time");
        }
        Page<CompanySolidWaste> page = new Page<CompanySolidWaste>(pageNo, pageSize);
        IPage<CompanySolidWaste> pageList = companySolidWasteService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "固废许可证信息-添加")
    @ApiOperation(value = "固废许可证信息-添加", notes = "固废许可证信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody JSONObject jsonObject) {
        CompanySolidWaste companySolidWaste = this.getCompanySolidWaste(jsonObject);
        companySolidWaste.setStatus(Constant.status.TEMPORARY);
        companySolidWasteService.save(companySolidWaste);
        //新增申报记录（暂存）
        companyApplyService.saveByBase(companySolidWaste.getCompanyId(), companySolidWaste.getId(), companySolidWaste.getStatus(), "", Constant.tables.SOLIDWASTE);
        companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.SOLIDWASTE,companySolidWaste.getId());
        return Result.ok("添加成功！");
    }

    private CompanySolidWaste getCompanySolidWaste(JSONObject jsonObject) {
        CompanySolidWaste companySolidWaste = new CompanySolidWaste();
        companySolidWaste.setId(jsonObject.getString("id"));
        companySolidWaste.setCompanyId(jsonObject.getString("companyId"));
        companySolidWaste.setCreateBy(jsonObject.getString("createBy"));
        companySolidWaste.setCreateTime(jsonObject.getDate("createTime"));
        companySolidWaste.setUpdateBy(jsonObject.getString("updateBy"));
        companySolidWaste.setUpdateTime(jsonObject.getDate("updateTime"));
        companySolidWaste.setStatus(jsonObject.getString("status"));
        companySolidWaste.setLicenceCode(jsonObject.getString("licenceCode"));
        companySolidWaste.setValidStarttime(jsonObject.getDate("validStarttime"));
        companySolidWaste.setValidEndtime(jsonObject.getDate("validEndtime"));
        companySolidWaste.setCertificateTime(jsonObject.getDate("certificateTime"));
        companySolidWaste.setCertificateOffice(jsonObject.getString("certificateOffice"));
        companySolidWaste.setDirtyType(jsonObject.getString("dirtyType"));
        return companySolidWaste;
    }

    /**
     * 申报
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "固废许可证-申报")
    @ApiOperation(value = "固废许可证-申报", notes = "固废许可证-申报")
    @PutMapping(value = "/declare")
    public Result<?> declare(@RequestBody JSONObject jsonObject) {
        CompanySolidWaste companySolidWaste = this.getCompanySolidWaste(jsonObject);
        companySolidWaste.setStatus(Constant.status.PEND);
        //判断是新增还是编辑
        if (!StrUtil.isEmpty(companySolidWaste.getId())) {
            //编辑
            //查询修改之前的对象
            CompanySolidWaste oldCompanySolidWaste = companySolidWasteService.getById(companySolidWaste.getId());
            //状态为正常
            if (Constant.status.NORMAL.equals(oldCompanySolidWaste.getStatus())) {
               /* CompanyApply companyApply = companyApplyService.findByNewId(oldCompanySolidWaste.getId(), Constant.tables.SOLIDWASTE);
                //修改老数据状态为过期
                oldCompanySolidWaste.setStatus(Constant.status.EXPIRED);
                companySolidWasteService.updateById(oldCompanySolidWaste);
                //修改老申报记录为过期
                companyApply.setStatus(Constant.status.EXPIRED);
                companyApplyService.updateById(companyApply);*/
                //新增修改后的为新数据
                companySolidWaste.setId("");
                companySolidWasteService.save(companySolidWaste);
                //新增申报记录
                companyApplyService.saveByBase(companySolidWaste.getCompanyId(), companySolidWaste.getId(), companySolidWaste.getStatus(), oldCompanySolidWaste.getId(), Constant.tables.SOLIDWASTE);
            } else if (Constant.status.NOPASS.equals(oldCompanySolidWaste.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanySolidWaste.getStatus())) {
                //状态为审核未通过、暂存（直接修改）
                companySolidWasteService.updateById(companySolidWaste);
                //修改申报记录状态为待审核
                CompanyApply companyApply = companyApplyService.findByNewId(companySolidWaste.getId(), Constant.tables.SOLIDWASTE);
                companyApply.setStatus(Constant.status.PEND);
                companyApplyService.updateById(companyApply);
                //删除原有的
                companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.SOLIDWASTE)
                        .eq(CompanyFile::getTableId,companySolidWaste.getId()));
            }
        } else {
            //新增
            companySolidWasteService.save(companySolidWaste);
            //新增申报记录
            companyApplyService.saveByBase(companySolidWaste.getCompanyId(), companySolidWaste.getId(), companySolidWaste.getStatus(), "", Constant.tables.SOLIDWASTE);
        }
        companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.SOLIDWASTE,companySolidWaste.getId());
        return Result.ok("申报成功!");
    }

    /**
     * @Description: 批量申报
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/4
     */
    @AutoLog(value = "固废许可证-批量申报")
    @ApiOperation(value = "固废许可证-批量申报", notes = "固废许可证-批量申报")
    @GetMapping(value = "/batchDeclare")
    public Result<?> batchDeclare(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        if (CollectionUtil.isNotEmpty(idList)) {
            for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
                String id = iterator.next();
                //查询
                CompanySolidWaste companySolidWaste = companySolidWasteService.getById(id);
                //判断申报的是否是暂存
                if (!Constant.status.TEMPORARY.equals(companySolidWaste.getStatus())) {
                    return Result.error("请选择暂存的信息申报！");
                }
                //修改状态为1：待审核状态
                companySolidWaste.setStatus(Constant.status.PEND);
                companySolidWasteService.updateById(companySolidWaste);
                //查询申报记录
                CompanyApply companyApply = companyApplyService.findByNewId(companySolidWaste.getId(), Constant.tables.SOLIDWASTE);
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
    @AutoLog(value = "固废许可证信息-编辑")
    @ApiOperation(value = "固废许可证信息-编辑", notes = "固废许可证信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody JSONObject jsonObject) {
        CompanySolidWaste companySolidWaste = this.getCompanySolidWaste(jsonObject);
        CompanySolidWaste oldcompanySolidWaste = companySolidWasteService.getById(companySolidWaste.getId());
        //查询数据状态
        if (Constant.status.NORMAL.equals(companySolidWaste.getStatus())) {
            companySolidWaste.setStatus(Constant.status.TEMPORARY);
          /*  CompanyApply companyApply = companyApplyService.findByNewId(oldcompanySolidWaste.getId(), Constant.tables.SOLIDWASTE);
            //正常
            oldcompanySolidWaste.setStatus(Constant.status.EXPIRED);
            oldcompanySolidWaste.setUpdateBy("");
            oldcompanySolidWaste.setUpdateTime(null);
            companySolidWasteService.updateById(oldcompanySolidWaste);*/
            //新增修改后的为新数据
            companySolidWaste.setId("");
            companySolidWasteService.save(companySolidWaste);
            //新增申报记录
            companyApplyService.saveByBase(companySolidWaste.getCompanyId(), companySolidWaste.getId(), companySolidWaste.getStatus(), oldcompanySolidWaste.getId(), Constant.tables.SOLIDWASTE);
        } else if (Constant.status.NOPASS.equals(oldcompanySolidWaste.getStatus()) || Constant.status.TEMPORARY.equals(oldcompanySolidWaste.getStatus())) {
            companySolidWaste.setStatus(Constant.status.TEMPORARY);
            //状态为未通过和暂存的
            companySolidWasteService.updateById(companySolidWaste);
            //修改申报记录状态为暂存
            CompanyApply companyApply = companyApplyService.findByNewId(companySolidWaste.getId(), Constant.tables.SOLIDWASTE);
            companyApply.setStatus(Constant.status.TEMPORARY);
            companyApplyService.updateById(companyApply);
            //删除原有的
            companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.SOLIDWASTE)
                    .eq(CompanyFile::getTableId,companySolidWaste.getId()));
        }
        companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.SOLIDWASTE,companySolidWaste.getId());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "固废许可证信息-通过id删除")
    @ApiOperation(value = "固废许可证信息-通过id删除", notes = "固废许可证信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        companySolidWasteService.removeById(id);
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
    @AutoLog(value = "固废许可证信息-批量删除")
    @ApiOperation(value = "固废许可证信息-批量删除", notes = "固废许可证信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companySolidWasteService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "固废许可证信息-通过id查询")
    @ApiOperation(value = "固废许可证信息-通过id查询", notes = "固废许可证信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CompanySolidWaste companySolidWaste = companySolidWasteService.getById(id);
        if (companySolidWaste == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(companySolidWaste);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param companySolidWaste
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanySolidWaste companySolidWaste) {
        return super.exportXls(request, companySolidWaste, CompanySolidWaste.class, "固废许可证信息");
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
        return super.importExcel(request, response, CompanySolidWaste.class);
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
        List<Map<String, String>> result = companyFileService.getFileMaps(id, Constant.tables.SOLIDWASTE);
        return Result.ok(result);
    }

}
