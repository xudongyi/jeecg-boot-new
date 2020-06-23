package org.jeecg.modules.business.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyAdminPenalties;
import org.jeecg.modules.business.entity.CompanyComplaintLetter;
import org.jeecg.modules.business.entity.CompanyFile;
import org.jeecg.modules.business.entity.CompanySupervisoryMonitor;
import org.jeecg.modules.business.service.ICompanyComplaintLetterService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.business.utils.Constant;
import org.jeecg.modules.business.vo.CompanyComplaintLetterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 信访投诉信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Api(tags="信访投诉信息")
@RestController
@RequestMapping("/ccl/companyComplaintLetter")
@Slf4j
public class CompanyComplaintLetterController extends JeecgController<CompanyComplaintLetter, ICompanyComplaintLetterService> {
	@Autowired
	private ICompanyComplaintLetterService companyComplaintLetterService;

	 @Autowired
	 private ICompanyFileService companyFileService;
	/**
	 * 分页列表查询
	 *
	 * @param companyComplaintLetter
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-分页列表查询")
	@ApiOperation(value="信访投诉信息-分页列表查询", notes="信访投诉信息-分页列表查询")
	@GetMapping(value = "/list/{listType}")
	public Result<?> queryPageList(CompanyComplaintLetter companyComplaintLetter,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req,@PathVariable int listType) throws ParseException {
		String companyIds = req.getParameter("companyIds");
		String status = req.getParameter("status");
		String companyId = req.getParameter("companyId");
		String reportDateBegin = req.getParameter("complaintDate_begin");
		String reportDateEnd = req.getParameter("complaintDate_end");
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
		Page<CompanyComplaintLetterVO> page = new Page<>(pageNo, pageSize);
		IPage<CompanyComplaintLetterVO> pageList = companyComplaintLetterService.getCompanyComplaintLetter(page,companyIds,status,dateBegin,dateEnd,listType);
		return Result.ok(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param jsonObject
	  * @return
	  */
	 @AutoLog(value = "信访投诉信息-申报")
	 @ApiOperation(value="信访投诉信息-申报", notes="信访投诉信息-申报")
	 @PutMapping(value = "/declare")
	 public Result<?> declare(@RequestBody JSONObject jsonObject) {
	 	 CompanyComplaintLetter companyComplaintLetter = getCompanyComplaintLetter(jsonObject);
		 companyComplaintLetter.setStatus(Constant.status.PEND);
		 //判断是新增还是编辑
		 if(!StrUtil.isEmpty(companyComplaintLetter.getId())){
			 //编辑
			 //查询修改之前的对象
			 CompanyComplaintLetter oldCompanyComplaintLetter = companyComplaintLetterService.getById(companyComplaintLetter.getId());
			 //状态为正常
			 if(Constant.status.NORMAL.equals(oldCompanyComplaintLetter.getStatus())){
				 //修改老数据状态为过期
				 oldCompanyComplaintLetter.setStatus(Constant.status.EXPIRED);
				 companyComplaintLetterService.updateById(oldCompanyComplaintLetter);
				 //新增修改后的为新数据
				 companyComplaintLetter.setId("");
				 companyComplaintLetter.setUpdateBy("");
				 companyComplaintLetter.setUpdateTime(null);
				 companyComplaintLetter.setContent("");
				 companyComplaintLetterService.save(companyComplaintLetter);
			 }else if(Constant.status.NOPASS.equals(oldCompanyComplaintLetter.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyComplaintLetter.getStatus())){
				 companyComplaintLetter.setUpdateBy("");
				 companyComplaintLetter.setUpdateTime(null);
				 companyComplaintLetter.setContent("");
			 	 companyComplaintLetterService.updateById(companyComplaintLetter);
				 companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.COMPLAINTLETTER)
						 .eq(CompanyFile::getTableId,companyComplaintLetter.getId()));
			 }
		 }else {
			 companyComplaintLetterService.save(companyComplaintLetter);
		 }
		 companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.COMPLAINTLETTER,companyComplaintLetter.getId());
		 return Result.ok("申报成功！");
	 }

	 private CompanyComplaintLetter getCompanyComplaintLetter(@RequestBody JSONObject jsonObject) {
	 	CompanyComplaintLetter companyComplaintLetter = new CompanyComplaintLetter();
		 companyComplaintLetter.setId(jsonObject.getString("id"));
		 companyComplaintLetter.setCompanyId(jsonObject.getString("companyId"));
		 companyComplaintLetter.setStatus(jsonObject.getString("status"));
		 companyComplaintLetter.setComplaintDate(jsonObject.getDate("complaintDate"));
		 companyComplaintLetter.setPollutionType(jsonObject.getString("pollutionType"));
		 companyComplaintLetter.setComplaintTitle(jsonObject.getString("complaintTitle"));
		 companyComplaintLetter.setContent(jsonObject.getString("content"));
		 companyComplaintLetter.setCreateBy(jsonObject.getString("createBy"));
		 companyComplaintLetter.setCreateTime(jsonObject.getDate("createTime"));
		 companyComplaintLetter.setUpdateBy(jsonObject.getString("updateBy"));
		 companyComplaintLetter.setUpdateTime(jsonObject.getDate("updateTime"));
	 	return companyComplaintLetter;
	 }

	/**
	 *   添加
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-添加")
	@ApiOperation(value="信访投诉信息-添加", notes="信访投诉信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JSONObject jsonObject) {
		CompanyComplaintLetter companyComplaintLetter = getCompanyComplaintLetter(jsonObject);
		companyComplaintLetter.setStatus(Constant.status.TEMPORARY);
		companyComplaintLetterService.save(companyComplaintLetter);
		companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.COMPLAINTLETTER,companyComplaintLetter.getId());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-编辑")
	@ApiOperation(value="信访投诉信息-编辑", notes="信访投诉信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JSONObject jsonObject) {
		CompanyComplaintLetter companyComplaintLetter = getCompanyComplaintLetter(jsonObject);
		CompanyComplaintLetter oldCompanyComplaintLetter = companyComplaintLetterService.getById(companyComplaintLetter.getId());
		//查询数据状态
		if(Constant.status.NORMAL.equals(companyComplaintLetter.getStatus())) {
			companyComplaintLetter.setStatus(Constant.status.TEMPORARY);
			oldCompanyComplaintLetter.setStatus(Constant.status.EXPIRED);
			companyComplaintLetterService.updateById(oldCompanyComplaintLetter);
			//新增修改后的为新数据
			companyComplaintLetter.setId("");
			companyComplaintLetter.setUpdateBy("");
			companyComplaintLetter.setUpdateTime(null);
			companyComplaintLetter.setContent("");
			companyComplaintLetterService.save(companyComplaintLetter);
		}else if (Constant.status.TEMPORARY.equals(oldCompanyComplaintLetter.getStatus()) || Constant.status.NOPASS.equals(oldCompanyComplaintLetter.getStatus())) {
			//状态为暂存和未通过的
			companyComplaintLetter.setStatus(Constant.status.TEMPORARY);
			companyComplaintLetter.setUpdateBy("");
			companyComplaintLetter.setUpdateTime(null);
			companyComplaintLetter.setContent("");
			companyComplaintLetterService.updateById(companyComplaintLetter);
			companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.COMPLAINTLETTER)
					.eq(CompanyFile::getTableId,companyComplaintLetter.getId()));
		}
		companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.COMPLAINTLETTER,companyComplaintLetter.getId());
		return Result.ok("编辑成功!");
	}

	 /**
	  *  审核
	  *
	  * @param companyComplaintLetter
	  * @return
	  */
	 @AutoLog(value = "信访投诉信息-审核")
	 @ApiOperation(value="信访投诉信息-审核", notes="信访投诉信息-审核")
	 @PutMapping(value = "/audit")
	 public Result<?> audit(@RequestBody CompanyComplaintLetter companyComplaintLetter) {
		 companyComplaintLetterService.updateById(companyComplaintLetter);
		 return Result.ok("编辑成功!");
	 }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-通过id删除")
	@ApiOperation(value="信访投诉信息-通过id删除", notes="信访投诉信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyComplaintLetterService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-批量删除")
	@ApiOperation(value="信访投诉信息-批量删除", notes="信访投诉信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyComplaintLetterService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	 /**
	  *  批量申报
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "信访投诉信息-批量申报")
	 @ApiOperation(value="信访投诉信息-批量申报", notes="信访投诉信息-批量申报")
	 @GetMapping(value = "/batchDeclare")
	 public Result<?> batchDeclare(@RequestParam(name="ids",required=true) String ids) {
		 List<String> idList = Arrays.asList(ids.split(","));
		 if (CollectionUtil.isNotEmpty(idList)) {
			 for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
				 String id = iterator.next();
				 //查询
				 CompanyComplaintLetter companyComplaintLetter = companyComplaintLetterService.getById(id);
				 //判断申报的是否是暂存
				 if (!Constant.status.TEMPORARY.equals(companyComplaintLetter.getStatus())) {
					 return Result.error("请选择暂存的信息申报！");
				 }
				 //修改状态为1：待审核状态
				 companyComplaintLetter.setStatus(Constant.status.PEND);
				 companyComplaintLetterService.updateById(companyComplaintLetter);

			 }
		 }
		 return Result.ok("批量申报成功!");
	 }
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-通过id查询")
	@ApiOperation(value="信访投诉信息-通过id查询", notes="信访投诉信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyComplaintLetter companyComplaintLetter = companyComplaintLetterService.getById(id);
		if(companyComplaintLetter==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyComplaintLetter);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyComplaintLetter
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyComplaintLetter companyComplaintLetter) {
        return super.exportXls(request, companyComplaintLetter, CompanyComplaintLetter.class, "信访投诉信息");
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
        return super.importExcel(request, response, CompanyComplaintLetter.class);
    }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "查询信访投诉信息附件")
	 @ApiOperation(value="查询信访投诉信息附件", notes="查询信访投诉信息附件")
	 @GetMapping(value = "/queryFiles")
	 public Result<?> queryFiles(@RequestParam(name="id",required=true) String id) {
		 List<Map<String, String>> result = companyFileService.getFileMaps(id,Constant.tables.COMPLAINTLETTER);
		 return Result.ok(result);
	 }

}
