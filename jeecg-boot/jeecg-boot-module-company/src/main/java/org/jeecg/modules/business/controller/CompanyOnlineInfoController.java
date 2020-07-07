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
import org.jeecg.modules.business.entity.CompanyOnlineInfo;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.business.service.ICompanyOnlineInfoService;
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
 * @Description: 在线监控验收信息
 * @Author: jeecg-boot
 * @Date: 2020-06-03
 * @Version: V1.0
 */
@Api(tags = "在线监控验收信息")
@RestController
@RequestMapping("/onlineInfo/companyOnlineInfo")
@Slf4j
public class CompanyOnlineInfoController extends JeecgController<CompanyOnlineInfo, ICompanyOnlineInfoService> {
    @Autowired
    private ICompanyOnlineInfoService companyOnlineInfoService;
    @Autowired
    private ICompanyApplyService companyApplyService;
    @Autowired
    private ICompanyFileService companyFileService;

    /**
     * 分页列表查询
     *
     * @param companyOnlineInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "在线监控验收信息-分页列表查询")
    @ApiOperation(value = "在线监控验收信息-分页列表查询", notes = "在线监控验收信息-分页列表查询")
    @GetMapping(value = "/list/{listType}")
    public Result<?> queryPageList(CompanyOnlineInfo companyOnlineInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, @PathVariable int listType) {
        QueryWrapper<CompanyOnlineInfo> queryWrapper = QueryGenerator.initQueryWrapper(companyOnlineInfo, req.getParameterMap());
        if (listType == 2) {
            queryWrapper.eq("status", Constant.status.NORMAL);
        } else if (listType == 1) {
            queryWrapper.and(wrapper -> wrapper.eq("status", Constant.status.PEND).or().eq("status", Constant.status.NORMAL));
        } else if (listType == 0) {
            queryWrapper.ne("status", Constant.status.EXPIRED).orderByDesc("create_time");
        }
        Page<CompanyOnlineInfo> page = new Page<CompanyOnlineInfo>(pageNo, pageSize);
        IPage<CompanyOnlineInfo> pageList = companyOnlineInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "在线监控验收信息-添加")
    @ApiOperation(value = "在线监控验收信息-添加", notes = "在线监控验收信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody JSONObject jsonObject) {
		CompanyOnlineInfo companyOnlineInfo = this.getCompanyOnlineInfo(jsonObject);
        companyOnlineInfo.setStatus(Constant.status.TEMPORARY);
        companyOnlineInfoService.save(companyOnlineInfo);
        //新增申报记录（暂存）
        companyApplyService.saveByBase(companyOnlineInfo.getCompanyId(), companyOnlineInfo.getId(), companyOnlineInfo.getStatus(), "", Constant.tables.ONLINEINFO);
        companyFileService.saveFiles(jsonObject.getString("fileList"), Constant.fileType.FILE, Constant.tables.ONLINEINFO, companyOnlineInfo.getId());
        return Result.ok("添加成功！");
    }

	private CompanyOnlineInfo getCompanyOnlineInfo(JSONObject jsonObject) {
        CompanyOnlineInfo companyOnlineInfo = new CompanyOnlineInfo();
		companyOnlineInfo.setId(jsonObject.getString("id"));
		companyOnlineInfo.setCompanyId(jsonObject.getString("companyId"));
		companyOnlineInfo.setCreateBy(jsonObject.getString("createBy"));
		companyOnlineInfo.setCreateTime(jsonObject.getDate("createTime"));
		companyOnlineInfo.setUpdateBy(jsonObject.getString("updateBy"));
		companyOnlineInfo.setUpdateTime(jsonObject.getDate("updateTime"));
		companyOnlineInfo.setStatus(jsonObject.getString("status"));
        companyOnlineInfo.setEquipmentName(jsonObject.getString("equipmentName"));
        companyOnlineInfo.setEquipmentManufacturers(jsonObject.getString("equipmentManufacturers"));
        companyOnlineInfo.setOperationalUnit(jsonObject.getString("operationalUnit"));
        companyOnlineInfo.setUsedTime(jsonObject.getDate("usedTime"));
        companyOnlineInfo.setInstallLocation(jsonObject.getString("installLocation"));
        return companyOnlineInfo;
	}

	/**
     * 申报
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "在线监控验收信息-申报")
    @ApiOperation(value = "在线监控验收信息-申报", notes = "在线监控验收信息-申报")
    @PutMapping(value = "/declare")
    public Result<?> declare(@RequestBody JSONObject jsonObject) {
        CompanyOnlineInfo companyOnlineInfo = this.getCompanyOnlineInfo(jsonObject);
        companyOnlineInfo.setStatus(Constant.status.PEND);
        //判断是新增还是编辑
        if (!StrUtil.isEmpty(companyOnlineInfo.getId())) {
            //编辑
            //查询修改之前的对象
            CompanyOnlineInfo oldCompanyOnlineInfo = companyOnlineInfoService.getById(companyOnlineInfo.getId());
            //状态为正常
            if (Constant.status.NORMAL.equals(oldCompanyOnlineInfo.getStatus())) {
               /* //修改老数据状态为过期
                oldCompanyOnlineInfo.setStatus(Constant.status.EXPIRED);
                companyOnlineInfoService.updateById(oldCompanyOnlineInfo);*/
                //新增修改后的为新数据
                companyOnlineInfo.setId("");
                companyOnlineInfoService.save(companyOnlineInfo);
                //新增申报记录
                companyApplyService.saveByBase(companyOnlineInfo.getCompanyId(), companyOnlineInfo.getId(), companyOnlineInfo.getStatus(), oldCompanyOnlineInfo.getId(), Constant.tables.ONLINEINFO);
            } else if (Constant.status.NOPASS.equals(oldCompanyOnlineInfo.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyOnlineInfo.getStatus())) {
                //状态为审核未通过、暂存（直接修改）
                companyOnlineInfoService.updateById(companyOnlineInfo);
                //修改申报记录状态为待审核
                CompanyApply companyApply = companyApplyService.findByNewId(companyOnlineInfo.getId(), Constant.tables.ONLINEINFO);
                companyApply.setStatus(Constant.status.PEND);
                companyApplyService.updateById(companyApply);
                //删除原有的
                companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable, Constant.tables.ONLINEINFO)
                        .eq(CompanyFile::getTableId, companyOnlineInfo.getId()));
            }
        } else {
            //新增
            companyOnlineInfoService.save(companyOnlineInfo);
            //新增申报记录
            companyApplyService.saveByBase(companyOnlineInfo.getCompanyId(), companyOnlineInfo.getId(), companyOnlineInfo.getStatus(), "", Constant.tables.ONLINEINFO);
        }
        companyFileService.saveFiles(jsonObject.getString("fileList"), Constant.fileType.FILE, Constant.tables.ONLINEINFO, companyOnlineInfo.getId());
        return Result.ok("申报成功!");
    }

    /**
     * 编辑
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "在线监控验收信息-编辑")
    @ApiOperation(value = "在线监控验收信息-编辑", notes = "在线监控验收信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody JSONObject jsonObject) {
        CompanyOnlineInfo companyOnlineInfo = this.getCompanyOnlineInfo(jsonObject);
        CompanyOnlineInfo oldCompanyOnlineInfo = companyOnlineInfoService.getById(companyOnlineInfo.getId());
        //查询数据状态
        if (Constant.status.NORMAL.equals(companyOnlineInfo.getStatus())) {
            companyOnlineInfo.setStatus(Constant.status.TEMPORARY);
           /* //正常
            oldCompanyOnlineInfo.setStatus(Constant.status.EXPIRED);
            companyOnlineInfoService.updateById(oldCompanyOnlineInfo);*/
            //新增修改后的为新数据
            companyOnlineInfo.setId("");
            companyOnlineInfoService.save(companyOnlineInfo);
            //新增申报记录
            companyApplyService.saveByBase(companyOnlineInfo.getCompanyId(), companyOnlineInfo.getId(), companyOnlineInfo.getStatus(), oldCompanyOnlineInfo.getId(), Constant.tables.ONLINEINFO);
        } else if (Constant.status.NOPASS.equals(oldCompanyOnlineInfo.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyOnlineInfo.getStatus())) {
            companyOnlineInfo.setStatus(Constant.status.TEMPORARY);
            //状态为未通过和暂存的
            companyOnlineInfoService.updateById(companyOnlineInfo);
            //修改申报记录状态为暂存
            CompanyApply companyApply = companyApplyService.findByNewId(companyOnlineInfo.getId(), Constant.tables.ONLINEINFO);
            companyApply.setStatus(Constant.status.TEMPORARY);
            companyApplyService.updateById(companyApply);
            //删除原有的
            companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable, Constant.tables.ONLINEINFO)
                    .eq(CompanyFile::getTableId, companyOnlineInfo.getId()));
        }
        companyFileService.saveFiles(jsonObject.getString("fileList"), Constant.fileType.FILE, Constant.tables.ONLINEINFO, companyOnlineInfo.getId());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "在线监控验收信息-通过id删除")
    @ApiOperation(value = "在线监控验收信息-通过id删除", notes = "在线监控验收信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        companyOnlineInfoService.removeById(id);
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
    @AutoLog(value = "在线监控验收信息-批量删除")
    @ApiOperation(value = "在线监控验收信息-批量删除", notes = "在线监控验收信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companyOnlineInfoService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "在线监控验收信息-通过id查询")
    @ApiOperation(value = "在线监控验收信息-通过id查询", notes = "在线监控验收信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CompanyOnlineInfo companyOnlineInfo = companyOnlineInfoService.getById(id);
        if (companyOnlineInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(companyOnlineInfo);
    }

    /**
     * @Description: 批量申报
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/4
     */
    @AutoLog(value = "在线监控验收信息-批量申报")
    @ApiOperation(value = "在线监控验收信息-批量申报", notes = "在线监控验收信息-批量申报")
    @GetMapping(value = "/batchDeclare")
    public Result<?> batchDeclare(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        if (CollectionUtil.isNotEmpty(idList)) {
            for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
                String id = iterator.next();
                //查询
                CompanyOnlineInfo companyOnlineInfo = companyOnlineInfoService.getById(id);
                //判断申报的是否是暂存
                if (!Constant.status.TEMPORARY.equals(companyOnlineInfo.getStatus())) {
                    return Result.error("请选择暂存信息申报！");
                }
                //修改状态为1：待审核状态
                companyOnlineInfo.setStatus(Constant.status.PEND);
                companyOnlineInfoService.updateById(companyOnlineInfo);
                //查询申报记录
                CompanyApply companyApply = companyApplyService.findByNewId(companyOnlineInfo.getId(), Constant.tables.ONLINEINFO);
                companyApply.setStatus(Constant.status.PEND);
                companyApplyService.updateById(companyApply);
            }
        }
        return Result.ok("申报成功!");
    }

    /**
     * 导出excel
     *
     * @param request
     * @param companyOnlineInfo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyOnlineInfo companyOnlineInfo) {
        return super.exportXls(request, companyOnlineInfo, CompanyOnlineInfo.class, "在线监控验收信息");
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
        return super.importExcel(request, response, CompanyOnlineInfo.class);
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
        List<Map<String, String>> result = companyFileService.getFileMaps(id, Constant.tables.ONLINEINFO);
        return Result.ok(result);
    }
}
