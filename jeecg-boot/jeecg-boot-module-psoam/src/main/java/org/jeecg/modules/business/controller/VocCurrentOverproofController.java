package org.jeecg.modules.business.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.VocCurrentOverproof;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.business.service.IVocCurrentOverproofService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.business.view.SelfEntityExcelView;
import org.jeecg.modules.business.vo.OverEntry;
import org.jeecg.modules.business.vo.OverEntryReport;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: voc_current_overproof
 * @Author: jeecg-boot
 * @Date:   2020-09-02
 * @Version: V1.0
 */
@Api(tags="voc_current_overproof")
@RestController
@RequestMapping("/vocOver/vocCurrentOverproof")
@Slf4j
public class VocCurrentOverproofController extends JeecgController<VocCurrentOverproof, IVocCurrentOverproofService> {
	@Autowired
	private IVocCurrentOverproofService vocCurrentOverproofService;

	 @Autowired
	 private ISysDictService sysDictService;

	 /**
	  * 分页列表查询
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "voc_current_overproof-分页列表查询")
	 @ApiOperation(value="voc_current_overproof-分页列表查询", notes="voc_current_overproof-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<?> queryPageList(
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
		 String  pollutionCode = req.getParameter("pollutionCode");

		 companyIds = StrUtil.isEmpty(companyId)?companyIds:companyId; //可以查询到的企业
		 List<String> companyIdList = Arrays.asList(companyIds.split(","));
		 Page<OverEntry> page = new Page<>(pageNo, pageSize);

		 Timestamp end = DateUtil.parse(dataTime_end+" 23:59:59","yyyy-MM-dd HH:mm:ss").toTimestamp();
		 Timestamp begin = DateUtil.parse(dataTime_begin,"yyyy-MM-dd").toTimestamp();
		 IPage<OverEntry> pageList = vocCurrentOverproofService.queryOverVoc(page,companyIdList ,area ,pollutionCode ,mn ,end ,begin);
		 for (OverEntry overEntry:pageList.getRecords()) {
		 	String amountUnit = sysDictService.queryDictTextByKey("allUnit", overEntry.getChromaUnit());
		 	overEntry.setChromaUnit(amountUnit);
		 }
		 return Result.ok(pageList);
	 }
	 /**
	  * 导出excel
	  *
	  */
	 @RequestMapping(value = "/exportXls")
	 public ModelAndView exportXls(HttpServletRequest req) {
		 String  area = req.getParameter("area");
		 String  companyId = req.getParameter("companyId");
		 String  companyIds = req.getParameter("companyIds");
		 String  mn = req.getParameter("mn");
		 String  dataTime_begin = req.getParameter("dataTime_begin");
		 String  dataTime_end = req.getParameter("dataTime_end");
		 String  pollutionCode = req.getParameter("pollutionCode");

		 companyIds = StrUtil.isEmpty(companyId)?companyIds:companyId; //可以查询到的企业
		 List<String> companyIdList = Arrays.asList(companyIds.split(","));


		 Timestamp end = DateUtil.parse(dataTime_end+" 23:59:59","yyyy-MM-dd HH:mm:ss").toTimestamp();
		 Timestamp begin = DateUtil.parse(dataTime_begin,"yyyy-MM-dd").toTimestamp();
		 List<OverEntry> overEntries = vocCurrentOverproofService.queryOverVoc(companyIdList ,area ,pollutionCode ,mn ,end ,begin);
		 for (OverEntry overEntry:overEntries) {
			 String amountUnit = sysDictService.queryDictTextByKey("allUnit", overEntry.getChromaUnit());
			 overEntry.setChromaUnit(amountUnit);
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new SelfEntityExcelView(null,null));
		 mv.addObject(SelfExcelConstants.TITLE, "超标数据（VOCs）"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(SelfExcelConstants.SHEET_NAME, "超标数据（VOCs）");
		 mv.addObject(SelfExcelConstants.CLAZZ, OverEntry.class);
		 mv.addObject(SelfExcelConstants.DATA_LIST, overEntries);
		 mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");
		 return mv;
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "voc_current_overproof-分页列表查询")
	 @ApiOperation(value="voc_current_overproof-分页列表查询", notes="voc_current_overproof-分页列表查询")
	 @GetMapping(value = "/vocOverReport")
	 public Result<?> queryOverVocReport(
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
		 String  pollutionCode = req.getParameter("pollutionCode");

		 companyIds = StrUtil.isEmpty(companyId)?companyIds:companyId; //可以查询到的企业
		 List<String> companyIdList = Arrays.asList(companyIds.split(","));
		 Page<OverEntryReport> page = new Page<>(pageNo, pageSize);

		 Timestamp end = DateUtil.parse(dataTime_end+" 23:59:59","yyyy-MM-dd HH:mm:ss").toTimestamp();
		 Timestamp begin = DateUtil.parse(dataTime_begin,"yyyy-MM-dd").toTimestamp();
		 IPage<OverEntryReport> pageList = vocCurrentOverproofService.queryOverVocReport(page,companyIdList ,area ,pollutionCode ,mn ,end ,begin);
		 for (OverEntryReport overEntryReport:pageList.getRecords()) {
			 String amountUnitMath = sysDictService.queryDictTextByKey("allUnit", overEntryReport.getChromaUnitMath());
			 Date beginTime = new Date(overEntryReport.getBeginTime().getTime());
			 Date endTime = new Date(overEntryReport.getEndTime().getTime());
			 String timeLength = DateUtil.formatBetween(beginTime, endTime, BetweenFormater.Level.SECOND);
			 overEntryReport.setTimeLength(timeLength);
			 overEntryReport.setChromaUnitMath(amountUnitMath);
		 }
		 return Result.ok(pageList);
	 }

	 /**
	  * 导出excel
	  *
	  */
	 @RequestMapping(value = "/exportVocReport")
	 public ModelAndView exportVocReport(HttpServletRequest req) {
		 String  area = req.getParameter("area");
		 String  companyId = req.getParameter("companyId");
		 String  companyIds = req.getParameter("companyIds");
		 String  mn = req.getParameter("mn");
		 String  dataTime_begin = req.getParameter("dataTime_begin");
		 String  dataTime_end = req.getParameter("dataTime_end");
		 String  pollutionCode = req.getParameter("pollutionCode");

		 companyIds = StrUtil.isEmpty(companyId)?companyIds:companyId; //可以查询到的企业
		 List<String> companyIdList = Arrays.asList(companyIds.split(","));


		 Timestamp end = DateUtil.parse(dataTime_end+" 23:59:59","yyyy-MM-dd HH:mm:ss").toTimestamp();
		 Timestamp begin = DateUtil.parse(dataTime_begin,"yyyy-MM-dd").toTimestamp();
		 List<OverEntryReport> overEntries = vocCurrentOverproofService.queryOverVocReport(companyIdList ,area ,pollutionCode ,mn ,end ,begin);
		 for (OverEntryReport overEntryReport:overEntries) {
			 String amountUnitMath = sysDictService.queryDictTextByKey("allUnit", overEntryReport.getChromaUnitMath());
			 Date beginTime = new Date(overEntryReport.getBeginTime().getTime());
			 Date endTime = new Date(overEntryReport.getEndTime().getTime());
			 String timeLength = DateUtil.formatBetween(beginTime, endTime, BetweenFormater.Level.SECOND);
			 overEntryReport.setTimeLength(timeLength);
			 overEntryReport.setChromaUnitMath(amountUnitMath);
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new SelfEntityExcelView(null,null));
		 mv.addObject(SelfExcelConstants.TITLE, "超标报表（VOCs）"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(SelfExcelConstants.SHEET_NAME, "超标报表（VOCs）");
		 mv.addObject(SelfExcelConstants.CLAZZ, OverEntryReport.class);
		 mv.addObject(SelfExcelConstants.DATA_LIST, overEntries);
		 mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");
		 return mv;
	 }
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "voc_current_overproof-通过id查询")
	@ApiOperation(value="voc_current_overproof-通过id查询", notes="voc_current_overproof-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		VocCurrentOverproof vocCurrentOverproof = vocCurrentOverproofService.getById(id);
		if(vocCurrentOverproof==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(vocCurrentOverproof);
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
        return super.importExcel(request, response, VocCurrentOverproof.class);
    }

}
