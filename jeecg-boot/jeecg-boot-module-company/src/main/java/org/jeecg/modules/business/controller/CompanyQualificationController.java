package org.jeecg.modules.business.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import org.jeecg.modules.business.entity.CompanyQualification;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.business.service.ICompanyQualificationService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
* @Description: 企业资质
* @Author: jeecg-boot
* @Date:   2020-06-01
* @Version: V1.0
*/
@Api(tags="企业资质")
@RestController
@RequestMapping("/companyQualification")
@Slf4j
public class CompanyQualificationController extends JeecgController<CompanyQualification, ICompanyQualificationService> {
   @Autowired
   private ICompanyQualificationService companyQualificationService;

   @Autowired
   private ICompanyApplyService companyApplyService;

    @Autowired
    private ICompanyFileService companyFileService;
//   /**
//    * 分页列表查询
//    *
//    * @param companyQualification
//    * @param pageNo
//    * @param pageSize
//    * @param req
//    * @return
//    */
//   @AutoLog(value = "企业资质-分页列表查询")
//   @ApiOperation(value="企业资质-分页列表查询", notes="企业资质-分页列表查询")
//   @GetMapping(value = "/list")
//   public Result<?> queryPageList(CompanyQualification companyQualification,
//                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
//                                  HttpServletRequest req) {
//       QueryWrapper<CompanyQualification> queryWrapper = QueryGenerator.initQueryWrapper(companyQualification, req.getParameterMap());
//       Page<CompanyQualification> page = new Page<CompanyQualification>(pageNo, pageSize);
//       IPage<CompanyQualification> pageList = companyQualificationService.page(page, queryWrapper);
//       return Result.ok(pageList);
//   }

   /**
    *   添加
    *
    * @param companyQualification
    * @return
    */
//   @AutoLog(value = "企业资质-添加")
//   @ApiOperation(value="企业资质-添加", notes="企业资质-添加")
//   @PostMapping(value = "/add")
//   public Result<?> add(@RequestBody CompanyQualification companyQualification) {
//       companyQualificationService.save(companyQualification);
//       return Result.ok("添加成功！");
//   }
//
//   /**
//    *  编辑
//    *
//    * @param companyQualification
//    * @return
//    */
//   @AutoLog(value = "企业资质-编辑")
//   @ApiOperation(value="企业资质-编辑", notes="企业资质-编辑")
//   @PutMapping(value = "/edit")
//   public Result<?> edit(@RequestBody CompanyQualification companyQualification) {
//       companyQualificationService.updateById(companyQualification);
//       return Result.ok("编辑成功!");
//   }
//
//   /**
//    *   通过id删除
//    *
//    * @param id
//    * @return
//    */
//   @AutoLog(value = "企业资质-通过id删除")
//   @ApiOperation(value="企业资质-通过id删除", notes="企业资质-通过id删除")
//   @DeleteMapping(value = "/delete")
//   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
//       companyQualificationService.removeById(id);
//       return Result.ok("删除成功!");
//   }
//
//   /**
//    *  批量删除
//    *
//    * @param ids
//    * @return
//    */
//   @AutoLog(value = "企业资质-批量删除")
//   @ApiOperation(value="企业资质-批量删除", notes="企业资质-批量删除")
//   @DeleteMapping(value = "/deleteBatch")
//   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//       this.companyQualificationService.removeByIds(Arrays.asList(ids.split(",")));
//       return Result.ok("批量删除成功!");
//   }

   /**
    * 通过id查询
    *
    * @param jsonObject 企业id
    * @return
    */
   @AutoLog(value = "企业资质-通过企业id查询")
   @ApiOperation(value="企业资质-通过企业id查询", notes="企业资质-通过企业id查询")
   @PostMapping(value = "/queryByCompanyId")
   public Result<?> queryById(@RequestBody JSONObject jsonObject) {
       Map<String, List<Map<String,String>>> result = companyQualificationService.getQualificationFiles(jsonObject.getString("companyId"));
       return Result.ok(result);
   }



    /**
   * 导出excel
   *
   * @param request
   * @param companyQualification
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, CompanyQualification companyQualification) {
       return super.exportXls(request, companyQualification, CompanyQualification.class, "企业资质");
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
       return super.importExcel(request, response, CompanyQualification.class);
   }
    /**
     * 资质信息申报
     * @param applyObj  申报信息
     * @return
     */
    @PostMapping(value = "/qualificationApply")
    public Result<?> qualificationApply(@RequestBody JSONObject applyObj) {

        String companyId = applyObj.getString("companyId");
        //先插入 申请表
        CompanyApply apply = new CompanyApply();
        apply.setStatus(Constant.status.PEND);
        apply.setFromTable(Constant.tables.QUALIFICATION);
        apply.setCompanyId(applyObj.getString("companyId"));
        companyApplyService.save(apply);

        JSONArray deleteImgs = applyObj.getJSONArray("delete");
        if(!deleteImgs.isEmpty()){
            Map<String,Object> updateparams = new HashMap<>();
            updateparams.put("APPLY_DELETE_ID",apply.getId());//状态更改为过期
//            updateparams.put("STATUS",Constant.status.EXPIRED);//状态更改为过期  审批时改为过期
            companyQualificationService.updateQualificationFiles(deleteImgs.toJavaList(String.class),updateparams);
        }

        JSONObject addImgs = applyObj.getJSONObject("add");
        List<CompanyFile> companyFiles = new ArrayList<>();
        if(!addImgs.isEmpty()){
            for(Map.Entry<String,Object> entry:addImgs.entrySet()){
                //强转list
                List<String>  files = (ArrayList<String>)entry.getValue();
                for(String file:files){
                    //资质信息表 一条数据对应一条附件表中的文件
                    //新增的
                    CompanyQualification qualification = new CompanyQualification();
                    qualification.setCompanyId(companyId);
                    qualification.setApplyAddId(apply.getId());
                    qualification.setStatus(Constant.status.PEND);//待审核
                    qualification.setType(entry.getKey());//资质类型
                    companyQualificationService.save(qualification);//需要提供Id不能批量存储
                    CompanyFile companyFile = new CompanyFile();
                    // 得到 每个对象中的属性值
                    String[] array =  file.split("/");
                    companyFile.setFilename(array[array.length-1]);
                    array[array.length-1] = "";
                    companyFile.setFilepath(String.join("/",array));
                    companyFile.setFiletype(Constant.fileType.IMAGE);//图片类型
                    companyFile.setFromTable(Constant.tables.QUALIFICATION);
                    companyFile.setTableId(qualification.getId());
                    companyFiles.add(companyFile);
                }
            }
            //批量存储
            companyFileService.saveBatch(companyFiles);
        }
        return Result.ok();
    }
    /**
     * 资质信息申报审核时获取
     * @param applyId  申报信息
     * @return
     */
    @GetMapping(value = "/queryQualificationAudit")
    public Result<?> queryQualificationAudit(@RequestParam String  applyId) {

        CompanyApply companyApply = companyApplyService.getById(applyId);
        return Result.ok( companyQualificationService.queryQualificationAudit(companyApply));
    }
    /**
     * 资质信息申报审核时获取
     * @param applyObj  申报信息
     * @return
     */
    @PostMapping(value = "/submitQualificationAudit")
    public Result<?> submitQualificationAudit(@RequestBody JSONObject applyObj) {
        String applyId = applyObj.getString("applyId");
        String status = applyObj.getString("result");
        String userId = applyObj.getString("userId");
        companyApplyService.update(new UpdateWrapper<CompanyApply>().lambda().eq(CompanyApply::getId,applyId).set(CompanyApply::getStatus,status)
                .set(CompanyApply::getUpdateBy,userId).set(CompanyApply::getUpdateTime,new Date()));

        companyQualificationService.update(new UpdateWrapper<CompanyQualification>().lambda().eq(CompanyQualification::getApplyAddId,applyId).set(CompanyQualification::getStatus,status));
        if(status.equals(Constant.status.NORMAL))
            companyQualificationService.update(new UpdateWrapper<CompanyQualification>().lambda().eq(CompanyQualification::getApplyDeleteId,applyId).set(CompanyQualification::getStatus,Constant.status.EXPIRED));
        return Result.ok( );
    }


}
