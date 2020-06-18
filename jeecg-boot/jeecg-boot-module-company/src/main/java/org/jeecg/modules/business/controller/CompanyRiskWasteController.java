package org.jeecg.modules.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
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
import org.jeecg.modules.business.entity.CompanyRiskWaste;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyRiskWasteService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
* @Description: 危废许可证信息
* @Author: jeecg-boot
* @Date:   2020-06-01
* @Version: V1.0
*/
@Api(tags="危废许可证信息")
@RestController
@RequestMapping("/risk/companyRiskWaste")
@Slf4j
public class CompanyRiskWasteController extends JeecgController<CompanyRiskWaste, ICompanyRiskWasteService> {
   @Autowired
   private ICompanyRiskWasteService companyRiskWasteService;
    @Autowired
    private ICompanyApplyService companyApplyService;

   /**
    * 分页列表查询
    *
    * @param companyRiskWaste
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "危废许可证信息-分页列表查询")
   @ApiOperation(value="危废许可证信息-分页列表查询", notes="危废许可证信息-分页列表查询")
   @GetMapping(value = "/list/{listType}")
   public Result<?> queryPageList(CompanyRiskWaste companyRiskWaste,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req,@PathVariable int listType) {
       QueryWrapper<CompanyRiskWaste> queryWrapper = QueryGenerator.initQueryWrapper(companyRiskWaste, req.getParameterMap());
       if (listType == 0) {
           queryWrapper.ne("status", Constant.status.EXPIRED);
       } else {
           queryWrapper.eq("status", Constant.status.PEND).or().eq("status", Constant.status.NORMAL);
       }
       Page<CompanyRiskWaste> page = new Page<CompanyRiskWaste>(pageNo, pageSize);
       IPage<CompanyRiskWaste> pageList = companyRiskWasteService.page(page, queryWrapper);
       return Result.ok(pageList);
   }

   /**
    *   添加
    *
    * @param companyRiskWaste
    * @return
    */
   @AutoLog(value = "危废许可证信息-添加")
   @ApiOperation(value="危废许可证信息-添加", notes="危废许可证信息-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody CompanyRiskWaste companyRiskWaste) {
       companyRiskWaste.setStatus(Constant.status.TEMPORARY);
       companyRiskWasteService.save(companyRiskWaste);
       //新增申报记录（暂存）
       companyApplyService.saveByBase(companyRiskWaste.getCompanyId(),companyRiskWaste.getId(),companyRiskWaste.getStatus(),"", Constant.tables.RISKWASTE);
       return Result.ok("添加成功！");
   }

    /**
     * 申报
     *
     * @param companyRiskWaste
     * @return
     */
    @AutoLog(value = "危废许可证-申报")
    @ApiOperation(value = "危废许可证-申报", notes = "危废许可证-申报")
    @PutMapping(value = "/declare")
    public Result<?> declare(@RequestBody CompanyRiskWaste companyRiskWaste) {
        companyRiskWaste.setStatus(Constant.status.PEND);
        //判断是新增还是编辑
        if (!StrUtil.isEmpty(companyRiskWaste.getId())) {
            //编辑
            //查询修改之前的对象
            CompanyRiskWaste oldCompanyRiskWaste = companyRiskWasteService.getById(companyRiskWaste.getId());
            //状态为正常
            if (Constant.status.NORMAL.equals(oldCompanyRiskWaste.getStatus())) {
                CompanyApply companyApply = companyApplyService.findByNewId(oldCompanyRiskWaste.getId(), Constant.tables.RISKWASTE);
                //修改老数据状态为过期
                oldCompanyRiskWaste.setStatus(Constant.status.EXPIRED);
                companyRiskWasteService.updateById(oldCompanyRiskWaste);
                //修改老申报记录为过期
                companyApply.setStatus(Constant.status.EXPIRED);
                companyApplyService.updateById(companyApply);
                //新增修改后的为新数据
                companyRiskWaste.setId("");
                companyRiskWasteService.save(companyRiskWaste);
                //新增申报记录
                companyApplyService.saveByBase(companyRiskWaste.getCompanyId(),companyRiskWaste.getId(),companyRiskWaste.getStatus(),oldCompanyRiskWaste.getId(), Constant.tables.RISKWASTE);
            } else if (Constant.status.NOPASS.equals(oldCompanyRiskWaste.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyRiskWaste.getStatus())) {
                //状态为审核未通过、暂存（直接修改）
                companyRiskWasteService.updateById(companyRiskWaste);
                //修改申报记录状态为待审核
                CompanyApply companyApply = companyApplyService.findByNewId(companyRiskWaste.getId(), Constant.tables.RISKWASTE);
                companyApply.setStatus(Constant.status.PEND);
                companyApplyService.updateById(companyApply);
            }
        } else {
            //新增
            companyRiskWasteService.save(companyRiskWaste);
            //新增申报记录
            companyApplyService.saveByBase(companyRiskWaste.getCompanyId(),companyRiskWaste.getId(),companyRiskWaste.getStatus(),"", Constant.tables.RISKWASTE);
        }
        return Result.ok("申报成功!");
    }

    /**
     * @Description: 批量申报
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/4
     */
    @AutoLog(value = "危废许可证-批量申报")
    @ApiOperation(value = "危废许可证-批量申报", notes = "危废许可证-批量申报")
    @GetMapping(value = "/batchDeclare")
    public Result<?> batchDeclare(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        if (CollectionUtil.isNotEmpty(idList)) {
            for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
                String id = iterator.next();
                //查询
                CompanyRiskWaste companyRiskWaste = companyRiskWasteService.getById(id);
                //判断申报的是否是暂存
                if (!Constant.status.TEMPORARY.equals(companyRiskWaste.getStatus())) {
                    return Result.error("请选择暂存的信息申报！");
                }
                //修改状态为1：待审核状态
                companyRiskWaste.setStatus(Constant.status.PEND);
                companyRiskWasteService.updateById(companyRiskWaste);
                //查询申报记录
                CompanyApply companyApply = companyApplyService.findByNewId(companyRiskWaste.getId(), Constant.tables.RISKWASTE);
                companyApply.setStatus(Constant.status.PEND);
                companyApplyService.updateById(companyApply);
            }
        }
        return Result.ok("申报成功!");
    }

   /**
    *  编辑
    *
    * @param companyRiskWaste
    * @return
    */
   @AutoLog(value = "危废许可证信息-编辑")
   @ApiOperation(value="危废许可证信息-编辑", notes="危废许可证信息-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody CompanyRiskWaste companyRiskWaste) {
       CompanyRiskWaste oldCompanyRiskWaste = companyRiskWasteService.getById(companyRiskWaste.getId());
       //查询数据状态
       if (Constant.status.NORMAL.equals(companyRiskWaste.getStatus())) {
           CompanyApply companyApply = companyApplyService.findByNewId(oldCompanyRiskWaste.getId(), Constant.tables.RISKWASTE);
           companyRiskWaste.setStatus(Constant.status.TEMPORARY);
           //正常
           oldCompanyRiskWaste.setStatus(Constant.status.EXPIRED);
           oldCompanyRiskWaste.setUpdateBy("");
           oldCompanyRiskWaste.setUpdateTime(null);
           companyRiskWasteService.updateById(oldCompanyRiskWaste);
           //新增修改后的为新数据
           companyRiskWaste.setId("");
           companyRiskWasteService.save(companyRiskWaste);
           //新增申报记录
           companyApplyService.saveByBase(companyRiskWaste.getCompanyId(),companyRiskWaste.getId(),companyRiskWaste.getStatus(),oldCompanyRiskWaste.getId(), Constant.tables.RISKWASTE);
       } else if (Constant.status.NOPASS.equals(oldCompanyRiskWaste.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyRiskWaste.getStatus())) {
           companyRiskWaste.setStatus(Constant.status.TEMPORARY);
           //状态为未通过和暂存的
           companyRiskWasteService.updateById(companyRiskWaste);
           //修改申报记录状态为暂存
           CompanyApply companyApply = companyApplyService.findByNewId(companyRiskWaste.getId(), Constant.tables.RISKWASTE);
           companyApply.setStatus(Constant.status.TEMPORARY);
           companyApplyService.updateById(companyApply);
       }
       return Result.ok("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "危废许可证信息-通过id删除")
   @ApiOperation(value="危废许可证信息-通过id删除", notes="危废许可证信息-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       companyRiskWasteService.removeById(id);
       //删除申报记录
       companyApplyService.remove(new QueryWrapper<CompanyApply>().lambda()
               .eq(CompanyApply::getStatus, Constant.status.TEMPORARY)
               .in(CompanyApply::getNewId,id ));
       return Result.ok("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "危废许可证信息-批量删除")
   @ApiOperation(value="危废许可证信息-批量删除", notes="危废许可证信息-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.companyRiskWasteService.removeByIds(Arrays.asList(ids.split(",")));
       //删除申报记录
       companyApplyService.remove(new QueryWrapper<CompanyApply>().lambda()
               .eq(CompanyApply::getStatus, Constant.status.TEMPORARY)
               .in(CompanyApply::getNewId,Arrays.asList(ids.split(","))) );
       return Result.ok("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "危废许可证信息-通过id查询")
   @ApiOperation(value="危废许可证信息-通过id查询", notes="危废许可证信息-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       CompanyRiskWaste companyRiskWaste = companyRiskWasteService.getById(id);
       if(companyRiskWaste==null) {
           return Result.error("未找到对应数据");
       }
       return Result.ok(companyRiskWaste);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param companyRiskWaste
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, CompanyRiskWaste companyRiskWaste) {
       return super.exportXls(request, companyRiskWaste, CompanyRiskWaste.class, "危废许可证信息");
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
       return super.importExcel(request, response, CompanyRiskWaste.class);
   }

}
