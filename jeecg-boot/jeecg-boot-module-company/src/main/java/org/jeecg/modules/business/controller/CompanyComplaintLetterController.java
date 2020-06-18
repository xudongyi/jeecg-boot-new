package org.jeecg.modules.business.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyAdminPenalties;
import org.jeecg.modules.business.entity.CompanyComplaintLetter;
import org.jeecg.modules.business.entity.CompanySupervisoryMonitor;
import org.jeecg.modules.business.service.ICompanyComplaintLetterService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
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
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyComplaintLetter companyComplaintLetter,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) throws ParseException {
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
		IPage<CompanyComplaintLetterVO> pageList = companyComplaintLetterService.getCompanyComplaintLetter(page,companyIds,status,dateBegin,dateEnd);
		return Result.ok(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param companyComplaintLetter
	  * @return
	  */
	 @AutoLog(value = "信访投诉信息-申报")
	 @ApiOperation(value="信访投诉信息-申报", notes="信访投诉信息-申报")
	 @PutMapping(value = "/declare")
	 public Result<?> declare(@RequestBody CompanyComplaintLetter companyComplaintLetter) {
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
				 companyComplaintLetterService.updateById(companyComplaintLetter);
				 //新增修改后的为新数据
				 companyComplaintLetter.setId("");
				 companyComplaintLetterService.save(companyComplaintLetter);
			 }else if(Constant.status.NOPASS.equals(oldCompanyComplaintLetter.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyComplaintLetter.getStatus())){
				 companyComplaintLetterService.updateById(companyComplaintLetter);
			 }
		 }else {
			 companyComplaintLetterService.save(companyComplaintLetter);
		 }
		 return Result.ok("申报成功！");
	 }

	/**
	 *   添加
	 *
	 * @param companyComplaintLetter
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-添加")
	@ApiOperation(value="信访投诉信息-添加", notes="信访投诉信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyComplaintLetter companyComplaintLetter) {
		companyComplaintLetterService.save(companyComplaintLetter);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyComplaintLetter
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-编辑")
	@ApiOperation(value="信访投诉信息-编辑", notes="信访投诉信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyComplaintLetter companyComplaintLetter) {
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

}
