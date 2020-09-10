package org.jeecg.modules.business.controller;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.WarnLog;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.business.service.IWarnLogService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.view.SelfEntityExcelView;
import org.jeecg.modules.business.vo.OverEntry;
import org.jeecg.modules.business.vo.RealTimeWarn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: warn_log
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Api(tags="warn_log")
@RestController
@RequestMapping("/warn/warnLog")
@Slf4j
public class WarnLogController extends JeecgController<WarnLog, IWarnLogService> {
	@Autowired
	private IWarnLogService warnLogService;

	 @Autowired
	 private ISysDictService sysDictService;
	
	/**
	 * 分页列表查询
	 *
	 * @param warnLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "warn_log-分页列表查询")
	@ApiOperation(value="warn_log-分页列表查询", notes="warn_log-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WarnLog warnLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		//查询
		String  area = req.getParameter("area");
		String  companyId = req.getParameter("companyId");
		String  companyIds = req.getParameter("companyIds");
		String  mn = req.getParameter("mn");
		String  dataTime_begin = req.getParameter("dataTime_begin");
		String  dataTime_end = req.getParameter("dataTime_end");
		String  warnType = req.getParameter("warnType");
		String  type = req.getParameter("type");

		companyIds = StrUtil.isEmpty(companyId)?companyIds:companyId; //可以查询到的企业
		List<String> companyIdList = Arrays.asList(companyIds.split(","));
		Page<RealTimeWarn> page = new Page<>(pageNo, pageSize);

		Timestamp end = DateUtil.parse(dataTime_end+" 23:59:59","yyyy-MM-dd HH:mm:ss").toTimestamp();
		Timestamp begin = DateUtil.parse(dataTime_begin,"yyyy-MM-dd").toTimestamp();
		IPage<RealTimeWarn> pageList = warnLogService.queryWarn(page,companyIdList,area,type,warnType,mn,end,begin);
		for (RealTimeWarn realTimeWarn:pageList.getRecords()){
			String warnTypeName = sysDictService.queryDictTextByKey("warnType", realTimeWarn.getWarnType());
			String warnLevelName = sysDictService.queryDictTextByKey("warnLevel", realTimeWarn.getWarnLevel());
			realTimeWarn.setWarnType(warnTypeName);
			realTimeWarn.setWarnLevel(warnLevelName);
		}
		return Result.ok(pageList);
	}

	 /**
	  * 导出excel
	  *
	  * @param req
	  */
	 @RequestMapping(value = "/exportXls")
	 public ModelAndView exportXls(HttpServletRequest req) {
		 String  area = req.getParameter("area");
		 String  companyId = req.getParameter("companyId");
		 String  companyIds = req.getParameter("companyIds");
		 String  mn = req.getParameter("mn");
		 String  dataTime_begin = req.getParameter("dataTime_begin");
		 String  dataTime_end = req.getParameter("dataTime_end");
		 String  warnType = req.getParameter("warnType");
		 String  type = req.getParameter("type");

		 companyIds = StrUtil.isEmpty(companyId)?companyIds:companyId; //可以查询到的企业
		 List<String> companyIdList = Arrays.asList(companyIds.split(","));

		 Timestamp end = DateUtil.parse(dataTime_end+" 23:59:59","yyyy-MM-dd HH:mm:ss").toTimestamp();
		 Timestamp begin = DateUtil.parse(dataTime_begin,"yyyy-MM-dd").toTimestamp();
		 List<RealTimeWarn> realTimeWarns = warnLogService.queryWarn(companyIdList,area,type,warnType,mn,end,begin);
		 for (RealTimeWarn realTimeWarn:realTimeWarns){
			 String warnTypeName = sysDictService.queryDictTextByKey("warnType", realTimeWarn.getWarnType());
			 String warnLevelName = sysDictService.queryDictTextByKey("warnLevel", realTimeWarn.getWarnLevel());
			 realTimeWarn.setWarnType(warnTypeName);
			 realTimeWarn.setWarnLevel(warnLevelName);
		 }
		 String sheetName = null;
		 if("0".equals(type)){
		 	sheetName = "报警信息（废水）";
		 }else if ("1".equals(type)){
		 	sheetName = "报警信息（废气）";
		 }else {
		 	sheetName = "报警信息（VOCs）";
		 }
		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new SelfEntityExcelView(null,null));
		 mv.addObject(SelfExcelConstants.TITLE, sheetName); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(SelfExcelConstants.SHEET_NAME, sheetName);
		 mv.addObject(SelfExcelConstants.CLAZZ, RealTimeWarn.class);
		 mv.addObject(SelfExcelConstants.DATA_LIST, realTimeWarns);
		 mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");
		 return mv;
	 }
	
	/**
	 *   添加
	 *
	 * @param warnLog
	 * @return
	 */
	@AutoLog(value = "warn_log-添加")
	@ApiOperation(value="warn_log-添加", notes="warn_log-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WarnLog warnLog) {
		warnLogService.save(warnLog);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param warnLog
	 * @return
	 */
	@AutoLog(value = "warn_log-编辑")
	@ApiOperation(value="warn_log-编辑", notes="warn_log-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WarnLog warnLog) {
		warnLogService.updateById(warnLog);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "warn_log-通过id删除")
	@ApiOperation(value="warn_log-通过id删除", notes="warn_log-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		warnLogService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "warn_log-批量删除")
	@ApiOperation(value="warn_log-批量删除", notes="warn_log-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.warnLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "warn_log-通过id查询")
	@ApiOperation(value="warn_log-通过id查询", notes="warn_log-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WarnLog warnLog = warnLogService.getById(id);
		if(warnLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(warnLog);
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
        return super.importExcel(request, response, WarnLog.class);
    }

}
