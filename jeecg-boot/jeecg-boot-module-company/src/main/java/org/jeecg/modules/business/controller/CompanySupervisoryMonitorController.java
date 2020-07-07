package org.jeecg.modules.business.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.jeecg.modules.business.entity.CompanyFile;
import org.jeecg.modules.business.entity.CompanySupervisoryMonitor;
import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.business.service.ICompanySupervisoryMonitorService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.utils.Constant;
import org.jeecg.modules.business.vo.CompanySupervisoryMonitorVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 监督性监测信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Api(tags="监督性监测信息")
@RestController
@RequestMapping("/csm/companySupervisoryMonitor")
@Slf4j
public class CompanySupervisoryMonitorController extends JeecgController<CompanySupervisoryMonitor, ICompanySupervisoryMonitorService> {
	@Autowired
	private ICompanySupervisoryMonitorService companySupervisoryMonitorService;

	 @Autowired
	 private ICompanyFileService companyFileService;
	/**
	 * 分页列表查询
	 *
	 * @param companySupervisoryMonitor
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "监督性监测信息-分页列表查询")
	@ApiOperation(value="监督性监测信息-分页列表查询", notes="监督性监测信息-分页列表查询")
	@GetMapping(value = "/list/{listType}")
	public Result<?> queryPageList(CompanySupervisoryMonitor companySupervisoryMonitor,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req,@PathVariable int listType) throws ParseException {
		String companyIds = req.getParameter("companyIds");
		String status = req.getParameter("status");
		String companyId = req.getParameter("companyId");
		String reportDateBegin = req.getParameter("reportDate_begin");
		String reportDateEnd = req.getParameter("reportDate_end");
		Date dateBegin;
		Date dateEnd;
		if(!StrUtil.isEmpty(companyId)) {
			companyIds= companyId;
		}
		if(StrUtil.isEmpty(reportDateBegin) && StrUtil.isEmpty(reportDateEnd)) {
			dateBegin = null;
			dateEnd = null;
		}else{
			dateBegin = new SimpleDateFormat("yyyy-MM-dd").parse(reportDateBegin);
			dateEnd = new SimpleDateFormat("yyyy-MM-dd").parse(reportDateEnd);
		}
		Page<CompanySupervisoryMonitorVO> page = new Page<CompanySupervisoryMonitorVO>(pageNo, pageSize);
		IPage<CompanySupervisoryMonitorVO> pageList = companySupervisoryMonitorService.getCompanySupervisoryMonitor(page, companyIds,status,dateBegin,dateEnd,listType);
		return Result.ok(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param jsonObject
	  * @return
	  */
	 @AutoLog(value = "监督性监测信息-申报")
	 @ApiOperation(value="监督性监测信息-申报", notes="监督性监测信息-申报")
	 @PutMapping(value = "/declare")
	 public Result<?> declare(@RequestBody JSONObject jsonObject) {
	 	 CompanySupervisoryMonitor companySupervisoryMonitor = getCompanySupervisoryMonitor(jsonObject);
		 companySupervisoryMonitor.setStatus(Constant.status.PEND);
		 //判断是新增还是编辑
		 if(!StrUtil.isEmpty(companySupervisoryMonitor.getId())){
			 //编辑
			 //查询修改之前的对象
			 CompanySupervisoryMonitor oldCompanySupervisoryMonitor = companySupervisoryMonitorService.getById(companySupervisoryMonitor.getId());
			 //状态为正常
			 if(Constant.status.NORMAL.equals(oldCompanySupervisoryMonitor.getStatus())){
				 //修改老数据状态为过期
				 oldCompanySupervisoryMonitor.setStatus(Constant.status.EXPIRED);
				 companySupervisoryMonitorService.updateById(oldCompanySupervisoryMonitor);
				 //新增修改后的为新数据
				 companySupervisoryMonitor.setId("");
				 companySupervisoryMonitor.setUpdateBy("");
				 companySupervisoryMonitor.setUpdateTime(null);
				 companySupervisoryMonitor.setContent("");
				 companySupervisoryMonitorService.save(companySupervisoryMonitor);
			 }else if(Constant.status.NOPASS.equals(oldCompanySupervisoryMonitor.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanySupervisoryMonitor.getStatus())){
				 companySupervisoryMonitor.setUpdateBy("");
				 companySupervisoryMonitor.setUpdateTime(null);
				 companySupervisoryMonitor.setContent("");
			 	 companySupervisoryMonitorService.updateById(companySupervisoryMonitor);
				 companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.SUPERVISORYMONITOR)
						 .eq(CompanyFile::getTableId,companySupervisoryMonitor.getId()));
			 }
		 }else {
			 companySupervisoryMonitorService.save(companySupervisoryMonitor);
		 }
		 companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.SUPERVISORYMONITOR,companySupervisoryMonitor.getId());
		 return Result.ok("申报成功！");
	 }

	 private CompanySupervisoryMonitor getCompanySupervisoryMonitor(@RequestBody JSONObject jsonObject) {
	 	CompanySupervisoryMonitor companySupervisoryMonitor = new CompanySupervisoryMonitor();
		 companySupervisoryMonitor.setId(jsonObject.getString("id"));
		 companySupervisoryMonitor.setCompanyId(jsonObject.getString("companyId"));
		 companySupervisoryMonitor.setStatus(jsonObject.getString("status"));
		 companySupervisoryMonitor.setReportDate(jsonObject.getDate("reportDate"));
		 companySupervisoryMonitor.setReportType(jsonObject.getString("reportType"));
		 companySupervisoryMonitor.setReportName(jsonObject.getString("reportName"));
		 companySupervisoryMonitor.setContent(jsonObject.getString("content"));
		 companySupervisoryMonitor.setCreateBy(jsonObject.getString("createBy"));
		 companySupervisoryMonitor.setCreateTime(jsonObject.getDate("createTime"));
		 companySupervisoryMonitor.setUpdateBy(jsonObject.getString("updateBy"));
		 companySupervisoryMonitor.setUpdateTime(jsonObject.getDate("updateTime"));
	 	return companySupervisoryMonitor;
	 }

	/**
	 *   添加
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "监督性监测信息-添加")
	@ApiOperation(value="监督性监测信息-添加", notes="监督性监测信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JSONObject jsonObject) {
		CompanySupervisoryMonitor companySupervisoryMonitor = getCompanySupervisoryMonitor(jsonObject);
		companySupervisoryMonitor.setStatus(Constant.status.TEMPORARY);
		companySupervisoryMonitorService.save(companySupervisoryMonitor);
		companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.SUPERVISORYMONITOR,companySupervisoryMonitor.getId());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "监督性监测信息-编辑")
	@ApiOperation(value="监督性监测信息-编辑", notes="监督性监测信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JSONObject jsonObject) {
		CompanySupervisoryMonitor companySupervisoryMonitor = getCompanySupervisoryMonitor(jsonObject);
		CompanySupervisoryMonitor oldCompanySupervisoryMonitor = companySupervisoryMonitorService.getById(companySupervisoryMonitor.getId());
		//查询数据状态
		if(Constant.status.NORMAL.equals(companySupervisoryMonitor.getStatus())) {
			companySupervisoryMonitor.setStatus(Constant.status.TEMPORARY);
			oldCompanySupervisoryMonitor.setStatus(Constant.status.EXPIRED);
			companySupervisoryMonitorService.updateById(oldCompanySupervisoryMonitor);
			//新增修改后的为新数据
			companySupervisoryMonitor.setId("");
			companySupervisoryMonitor.setUpdateBy("");
			companySupervisoryMonitor.setUpdateTime(null);
			companySupervisoryMonitor.setContent("");
			companySupervisoryMonitorService.save(companySupervisoryMonitor);
		}else if (Constant.status.TEMPORARY.equals(oldCompanySupervisoryMonitor.getStatus()) || Constant.status.NOPASS.equals(oldCompanySupervisoryMonitor.getStatus())) {
			//状态为暂存和未通过的
			companySupervisoryMonitor.setStatus(Constant.status.TEMPORARY);
			companySupervisoryMonitor.setUpdateBy("");
			companySupervisoryMonitor.setUpdateTime(null);
			companySupervisoryMonitor.setContent("");
			companySupervisoryMonitorService.updateById(companySupervisoryMonitor);
			companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.SUPERVISORYMONITOR)
					.eq(CompanyFile::getTableId,companySupervisoryMonitor.getId()));
		}
		companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.SUPERVISORYMONITOR,companySupervisoryMonitor.getId());
		return Result.ok("编辑成功!");
	}

	 /**
	  *  审核
	  *
	  * @param companySupervisoryMonitor
	  * @return
	  */
	 @AutoLog(value = "监督性监测信息-审核")
	 @ApiOperation(value="监督性监测信息-审核", notes="监督性监测信息-审核")
	 @PutMapping(value = "/audit")
	 public Result<?> audit(@RequestBody CompanySupervisoryMonitor companySupervisoryMonitor) {
		 companySupervisoryMonitorService.updateById(companySupervisoryMonitor);
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

			 LambdaUpdateWrapper updateWrapper = new UpdateWrapper<CompanySupervisoryMonitor>().lambda().in(CompanySupervisoryMonitor::getId,Arrays.asList(ids))
					 .set(CompanySupervisoryMonitor::getStatus,Constant.status.NORMAL).set(CompanySupervisoryMonitor::getUpdateTime,new Date())
					 .set(CompanySupervisoryMonitor::getUpdateBy,sysUser.getId());
			 companySupervisoryMonitorService.update(updateWrapper);
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

			 LambdaUpdateWrapper updateWrapper = new UpdateWrapper<CompanySupervisoryMonitor>().lambda().in(CompanySupervisoryMonitor::getId,Arrays.asList(ids))
					 .set(CompanySupervisoryMonitor::getStatus,Constant.status.NOPASS).set(CompanySupervisoryMonitor::getUpdateTime,new Date())
					 .set(CompanySupervisoryMonitor::getUpdateBy,sysUser.getId());
			 companySupervisoryMonitorService.update(updateWrapper);
		 }
		 return Result.ok();
	 }

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监督性监测信息-通过id删除")
	@ApiOperation(value="监督性监测信息-通过id删除", notes="监督性监测信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companySupervisoryMonitorService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "监督性监测信息-批量删除")
	@ApiOperation(value="监督性监测信息-批量删除", notes="监督性监测信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		//删除申报暂存记录
		companySupervisoryMonitorService.remove(new QueryWrapper<CompanySupervisoryMonitor>().lambda()
				.eq(CompanySupervisoryMonitor::getStatus, Constant.status.TEMPORARY)
				.in(CompanySupervisoryMonitor::getId, Arrays.asList(ids.split(","))));
		return Result.ok("批量删除暂存记录成功!");
	}

	 /**
	  *  批量申报
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "监督性监测信息-批量申报")
	 @ApiOperation(value="监督性监测信息-批量申报", notes="监督性监测信息-批量申报")
	 @GetMapping(value = "/batchDeclare")
	 public Result<?> batchDeclare(@RequestParam(name="ids",required=true) String ids) {
		 //修改
		 companySupervisoryMonitorService.update(new UpdateWrapper<CompanySupervisoryMonitor>().lambda()
				 .eq(CompanySupervisoryMonitor::getStatus,Constant.status.TEMPORARY)
				 .in(CompanySupervisoryMonitor::getId,Arrays.asList(ids.split(",")))
				 .set(CompanySupervisoryMonitor::getStatus,Constant.status.PEND));
		 return Result.ok("批量申报暂存记录成功!");
	 }
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监督性监测信息-通过id查询")
	@ApiOperation(value="监督性监测信息-通过id查询", notes="监督性监测信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanySupervisoryMonitor companySupervisoryMonitor = companySupervisoryMonitorService.getById(id);
		if(companySupervisoryMonitor==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companySupervisoryMonitor);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companySupervisoryMonitor
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanySupervisoryMonitor companySupervisoryMonitor) {
        return super.exportXls(request, companySupervisoryMonitor, CompanySupervisoryMonitor.class, "监督性监测信息");
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
        return super.importExcel(request, response, CompanySupervisoryMonitor.class);
    }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "查询监督性监测信息附件")
	 @ApiOperation(value="查询监督性监测信息附件", notes="查询监督性监测信息附件")
	 @GetMapping(value = "/queryFiles")
	 public Result<?> queryFiles(@RequestParam(name="id",required=true) String id) {
		 List<Map<String, String>> result = companyFileService.getFileMaps(id,Constant.tables.SUPERVISORYMONITOR);
		 return Result.ok(result);
	 }

}
