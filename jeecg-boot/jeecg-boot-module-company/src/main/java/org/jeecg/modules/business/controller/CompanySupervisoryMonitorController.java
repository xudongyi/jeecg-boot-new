package org.jeecg.modules.business.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyAdminPenalties;
import org.jeecg.modules.business.entity.CompanySupervisoryMonitor;
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
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanySupervisoryMonitor companySupervisoryMonitor,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) throws ParseException {
		String companyId = req.getParameter("companyId");
		String status = req.getParameter("status");
		String companyName = req.getParameter("companyName");
		String reportDateBegin = req.getParameter("reportDate_begin");
		String reportDateEnd = req.getParameter("reportDate_end");
		Date dateBegin;
		Date dateEnd;
		if(StrUtil.isEmpty(reportDateBegin) && StrUtil.isEmpty(reportDateEnd)) {
			dateBegin = null;
			dateEnd = null;
		}else{
			dateBegin = new SimpleDateFormat("yyyy-MM-dd").parse(reportDateBegin);
			dateEnd = new SimpleDateFormat("yyyy-MM-dd").parse(reportDateEnd);
		}
		Page<CompanySupervisoryMonitorVO> page = new Page<CompanySupervisoryMonitorVO>(pageNo, pageSize);
		IPage<CompanySupervisoryMonitorVO> pageList = companySupervisoryMonitorService.getCompanySupervisoryMonitor(page, companyId,status,companyName,dateBegin,dateEnd);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companySupervisoryMonitor
	 * @return
	 */
	@AutoLog(value = "监督性监测信息-添加")
	@ApiOperation(value="监督性监测信息-添加", notes="监督性监测信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanySupervisoryMonitor companySupervisoryMonitor) {
		companySupervisoryMonitorService.save(companySupervisoryMonitor);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companySupervisoryMonitor
	 * @return
	 */
	@AutoLog(value = "监督性监测信息-编辑")
	@ApiOperation(value="监督性监测信息-编辑", notes="监督性监测信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanySupervisoryMonitor companySupervisoryMonitor) {
		companySupervisoryMonitorService.updateById(companySupervisoryMonitor);
		return Result.ok("编辑成功!");
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
		this.companySupervisoryMonitorService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
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
		 List<String> idList = Arrays.asList(ids.split(","));
		 if (CollectionUtil.isNotEmpty(idList)) {
			 for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
				 String id = iterator.next();
				 //查询
				 CompanySupervisoryMonitor companySupervisoryMonitor = companySupervisoryMonitorService.getById(id);
				 //判断申报的是否是暂存
				 if (!Constant.status.TEMPORARY.equals(companySupervisoryMonitor.getStatus())) {
					 return Result.error("请选择暂存的信息申报！");
				 }
				 //修改状态为1：待审核状态
				 companySupervisoryMonitor.setStatus(Constant.status.PEND);
				 companySupervisoryMonitorService.updateById(companySupervisoryMonitor);

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

}
