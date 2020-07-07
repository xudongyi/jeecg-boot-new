package org.jeecg.modules.business.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.service.ICompanyDynamicSupervisionService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.bussiness.utils.Constant;
import org.jeecg.modules.business.vo.CompanyDynamicSupervisionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 企业年度动态监管
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
@Api(tags="企业年度动态监管")
@RestController
@RequestMapping("/cds/companyDynamicSupervision")
@Slf4j
public class CompanyDynamicSupervisionController extends JeecgController<CompanyDynamicSupervision, ICompanyDynamicSupervisionService> {
	@Autowired
	private ICompanyDynamicSupervisionService companyDynamicSupervisionService;

	@Autowired
	private ICompanyFileService companyFileService;

	 /**
	  * 分页列表查询
	  *
	  * @param companyDynamicSupervision
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "企业年度动态监管-分页列表查询")
	 @ApiOperation(value="企业年度动态监管-分页列表查询", notes="企业年度动态监管-分页列表查询")
	 @GetMapping(value = "/list/{listType}")
	 public Result<?> queryPageList(CompanyDynamicSupervisionVO companyDynamicSupervision,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req,@PathVariable int listType) {
		 String companyIds = req.getParameter("companyIds");
		 String status = req.getParameter("status");
		 String companyId = req.getParameter("companyId");
		 String reportYear = req.getParameter("reportYear");
		 if(!StrUtil.isEmpty(companyId)) {
			 companyIds= companyId;
		 }
//		 QueryWrapper<CompanyDynamicSupervisionVO> queryWrapper = QueryGenerator.initQueryWrapper(companyDynamicSupervision,req.getParameterMap());
		 Page<CompanyDynamicSupervisionVO> page = new Page<>(pageNo, pageSize);
		 IPage<CompanyDynamicSupervisionVO> pageList = companyDynamicSupervisionService.getCompanyDynamicSupervision(page,companyIds,status,reportYear,listType);
		 return Result.ok(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param jsonObject
	  * @return
	  */
	 @AutoLog(value = "企业年度动态监管-申报")
	 @ApiOperation(value="企业年度动态监管-申报", notes="企业年度动态监管-申报")
	 @PutMapping(value = "/declare")
	 public Result<?> declare(@RequestBody JSONObject jsonObject) {
	 	CompanyDynamicSupervision companyDynamicSupervision = getCompanyDynamicSupervision(jsonObject);
	 	companyDynamicSupervision.setStatus(Constant.status.PEND);
		 //判断是新增还是编辑
	 	if(!StrUtil.isEmpty(companyDynamicSupervision.getId())){
			//编辑
			//查询修改之前的对象
			CompanyDynamicSupervision oldCompanyDynamicSupervision = companyDynamicSupervisionService.getById(companyDynamicSupervision.getId());
			//状态为正常
			if(Constant.status.NORMAL.equals(oldCompanyDynamicSupervision.getStatus())){
				//修改老数据状态为过期
				oldCompanyDynamicSupervision.setStatus(Constant.status.EXPIRED);
				companyDynamicSupervisionService.updateById(oldCompanyDynamicSupervision);
				//新增修改后的为新数据
				companyDynamicSupervision.setId("");
				companyDynamicSupervision.setUpdateBy("");
				companyDynamicSupervision.setUpdateTime(null);
				companyDynamicSupervision.setContent("");
				companyDynamicSupervisionService.save(companyDynamicSupervision);
			}else if(Constant.status.NOPASS.equals(oldCompanyDynamicSupervision.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyDynamicSupervision.getStatus())){
				companyDynamicSupervision.setUpdateBy("");
				companyDynamicSupervision.setUpdateTime(null);
				companyDynamicSupervision.setContent("");
				companyDynamicSupervisionService.updateById(companyDynamicSupervision);
				companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.DYNAMICSUPERVISION)
					.eq(CompanyFile::getTableId,companyDynamicSupervision.getId()));
			}
		}else{
	 		companyDynamicSupervisionService.save(companyDynamicSupervision);
		}
	 	companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.DYNAMICSUPERVISION,companyDynamicSupervision.getId());
		 return Result.ok("申报成功！");
	 }

	 private CompanyDynamicSupervision getCompanyDynamicSupervision(@RequestBody JSONObject jsonObject) {
	 	CompanyDynamicSupervision companyDynamicSupervision = new CompanyDynamicSupervision();
	 	companyDynamicSupervision.setId(jsonObject.getString("id"));
	 	companyDynamicSupervision.setCompanyId(jsonObject.getString("companyId"));
		companyDynamicSupervision.setStatus(jsonObject.getString("status"));
		companyDynamicSupervision.setReportYear(jsonObject.getString("reportYear"));
		companyDynamicSupervision.setDocumentType(jsonObject.getString("documentType"));
		companyDynamicSupervision.setDocumentName(jsonObject.getString("documentName"));
		companyDynamicSupervision.setContent(jsonObject.getString("content"));
		companyDynamicSupervision.setCreateBy(jsonObject.getString("createBy"));
		companyDynamicSupervision.setCreateTime(jsonObject.getDate("createTime"));
		companyDynamicSupervision.setUpdateBy(jsonObject.getString("updateBy"));
		companyDynamicSupervision.setUpdateTime(jsonObject.getDate("updateTime"));
	 	return companyDynamicSupervision;
	 }

	/**
	 *   添加
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-添加")
	@ApiOperation(value="企业年度动态监管-添加", notes="企业年度动态监管-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JSONObject jsonObject) {
		CompanyDynamicSupervision companyDynamicSupervision = getCompanyDynamicSupervision(jsonObject);
		companyDynamicSupervision.setStatus(Constant.status.TEMPORARY);
		companyDynamicSupervisionService.save(companyDynamicSupervision);
		companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.DYNAMICSUPERVISION,companyDynamicSupervision.getId());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-编辑")
	@ApiOperation(value="企业年度动态监管-编辑", notes="企业年度动态监管-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JSONObject jsonObject) {
		CompanyDynamicSupervision companyDynamicSupervision = getCompanyDynamicSupervision(jsonObject);
		CompanyDynamicSupervision oldCompanyDynamicSupervision = companyDynamicSupervisionService.getById(companyDynamicSupervision.getId());
		//查询数据状态
		if(Constant.status.NORMAL.equals(companyDynamicSupervision.getStatus())) {
			companyDynamicSupervision.setStatus(Constant.status.TEMPORARY);
			oldCompanyDynamicSupervision.setStatus(Constant.status.EXPIRED);
			companyDynamicSupervisionService.updateById(oldCompanyDynamicSupervision);
			//新增修改后的为新数据
			companyDynamicSupervision.setId("");
			companyDynamicSupervision.setUpdateBy("");
			companyDynamicSupervision.setUpdateTime(null);
			companyDynamicSupervision.setContent("");
			companyDynamicSupervisionService.save(companyDynamicSupervision);
		}else if (Constant.status.TEMPORARY.equals(oldCompanyDynamicSupervision.getStatus()) || Constant.status.NOPASS.equals(oldCompanyDynamicSupervision.getStatus())) {
			//状态为暂存和未通过的
			companyDynamicSupervision.setStatus(Constant.status.TEMPORARY);
			companyDynamicSupervision.setUpdateBy("");
			companyDynamicSupervision.setUpdateTime(null);
			companyDynamicSupervision.setContent("");
			companyDynamicSupervisionService.updateById(companyDynamicSupervision);
			companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.DYNAMICSUPERVISION)
					.eq(CompanyFile::getTableId,companyDynamicSupervision.getId()));
		}
		companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.DYNAMICSUPERVISION,companyDynamicSupervision.getId());
		return Result.ok("编辑成功!");
	}

	 /**
	  *  审核
	  *
	  * @param jsonObject
	  * @return
	  */
	 @AutoLog(value = "行政处罚信息-审核")
	 @ApiOperation(value="行政处罚信息-审核", notes="行政处罚信息-审核")
	 @PutMapping(value = "/audit")
	 public Result<?> audit(@RequestBody JSONObject jsonObject) {
	 	 CompanyDynamicSupervision companyDynamicSupervision = getCompanyDynamicSupervision(jsonObject);
		 companyDynamicSupervisionService.updateById(companyDynamicSupervision);
		 return Result.ok("审核成功!");
	 }

	 /**
	  * 批量通过
	  *
	  * @param jsonObject
	  */
	 @PostMapping(value = "/batchPass")
	 @AutoLog(value = "批量通过")
	 @ApiOperation(value = "批量通过", notes = "批量通过")
	 public Result<?> batchPass(@RequestBody JSONObject jsonObject) {
		 String[]  ids = jsonObject.getString("ids").split(",");
		 if(ids.length>0){
			 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

			 LambdaUpdateWrapper updateWrapper = new UpdateWrapper<CompanyDynamicSupervision>().lambda().in(CompanyDynamicSupervision::getId,Arrays.asList(ids))
					 .set(CompanyDynamicSupervision::getStatus,Constant.status.NORMAL).set(CompanyDynamicSupervision::getUpdateTime,new Date())
					 .set(CompanyDynamicSupervision::getUpdateBy,sysUser.getId());
			 companyDynamicSupervisionService.update(updateWrapper);
		 }
		 return Result.ok();
	 }

	 /**
	  * 批量不通过
	  *
	  * @param jsonObject
	  */
	 @PostMapping(value = "/batchFail")
	 @AutoLog(value = "批量不通过")
	 @ApiOperation(value = "批量不通过", notes = "批量不通过")
	 public Result<?> batchFail(@RequestBody JSONObject jsonObject) {
		 String[]  ids = jsonObject.getString("ids").split(",");
		 if(ids.length>0){
			 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

			 LambdaUpdateWrapper updateWrapper = new UpdateWrapper<CompanyDynamicSupervision>().lambda().in(CompanyDynamicSupervision::getId,Arrays.asList(ids))
					 .set(CompanyDynamicSupervision::getStatus,Constant.status.NOPASS).set(CompanyDynamicSupervision::getUpdateTime,new Date())
					 .set(CompanyDynamicSupervision::getUpdateBy,sysUser.getId());
			 companyDynamicSupervisionService.update(updateWrapper);
		 }
		 return Result.ok();
	 }

	 /**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-通过id删除")
	@ApiOperation(value="企业年度动态监管-通过id删除", notes="企业年度动态监管-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyDynamicSupervisionService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-批量删除")
	@ApiOperation(value="企业年度动态监管-批量删除", notes="企业年度动态监管-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		//删除申报暂存记录
		companyDynamicSupervisionService.remove(new QueryWrapper<CompanyDynamicSupervision>().lambda()
				.eq(CompanyDynamicSupervision::getStatus, Constant.status.TEMPORARY)
				.in(CompanyDynamicSupervision::getId, Arrays.asList(ids.split(","))));
		return Result.ok("批量删除暂存记录成功!");
	}

	 /**
	  *  批量申报
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "企业年度动态监管-批量申报")
	 @ApiOperation(value="企业年度动态监管-批量申报", notes="企业年度动态监管-批量申报")
	 @GetMapping(value = "/batchDeclare")
	 public Result<?> batchDeclare(@RequestParam(name="ids",required=true) String ids) {
		 //修改
		 companyDynamicSupervisionService.update(new UpdateWrapper<CompanyDynamicSupervision>().lambda()
				 .eq(CompanyDynamicSupervision::getStatus,Constant.status.TEMPORARY)
				 .in(CompanyDynamicSupervision::getId,Arrays.asList(ids.split(",")))
				 .set(CompanyDynamicSupervision::getStatus,Constant.status.PEND));
		 return Result.ok("批量申报暂存记录成功!");
	 }

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-通过id查询")
	@ApiOperation(value="企业年度动态监管-通过id查询", notes="企业年度动态监管-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyDynamicSupervision companyDynamicSupervision = companyDynamicSupervisionService.getById(id);
		if(companyDynamicSupervision==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyDynamicSupervision);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyDynamicSupervision
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyDynamicSupervision companyDynamicSupervision) {
        return super.exportXls(request, companyDynamicSupervision, CompanyDynamicSupervision.class, "企业年度动态监管");
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
        return super.importExcel(request, response, CompanyDynamicSupervision.class);
    }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "查询企业年度动态监管信息附件")
	 @ApiOperation(value="查询企业年度动态监管信息附件", notes="查询企业年度动态监管信息附件")
	 @GetMapping(value = "/queryFiles")
	 public Result<?> queryFiles(@RequestParam(name="id",required=true) String id) {
		 List<Map<String, String>> result = companyFileService.getFileMaps(id,Constant.tables.DYNAMICSUPERVISION);
		 return Result.ok(result);
	 }

}
