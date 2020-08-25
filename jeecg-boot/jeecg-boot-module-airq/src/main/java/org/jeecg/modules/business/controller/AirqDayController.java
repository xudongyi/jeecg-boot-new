package org.jeecg.modules.business.controller;

import cn.hutool.core.date.DateUtil;
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
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.AirqDay;
import org.jeecg.modules.business.entity.AirqLevel;
import org.jeecg.modules.business.service.IAirqDayService;
import org.jeecg.modules.business.service.IAirqMonthService;
import org.jeecg.modules.business.service.IAirqYearService;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.business.utils.RedisCacheUtil;
import org.jeecg.modules.business.view.SelfEntityExcelView;
import org.jeecg.modules.business.vo.AirqDayQualityVo;
import org.jeecg.modules.business.vo.SiteQualityRankDayVO;
import org.jeecg.modules.business.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

 /**
 * @Description: airq_day
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Api(tags="airq_day")
@RestController
@RequestMapping("/day/airqDay")
@Slf4j
public class AirqDayController extends JeecgController<AirqDay, IAirqDayService> {
	@Autowired
	private IAirqDayService airqDayService;
	 @Autowired
	 private IAirqMonthService airqMonthService;
	 @Autowired
	 private IAirqYearService airqYearService;
	@Autowired
	private ISysDictService sysDictService;
	@Autowired
	private RedisUtil redisUtil;
	 @Resource
	 private RedisCacheUtil<AirqLevel> redisCacheUtil;
	/**
	 * 分页列表查询
	 *
	 * @param airqDay
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "airq_day-分页列表查询")
	@ApiOperation(value="airq_day-分页列表查询", notes="airq_day-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AirqDay airqDay,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AirqDay> queryWrapper = QueryGenerator.initQueryWrapper(airqDay, req.getParameterMap());
		Page<AirqDay> page = new Page<AirqDay>(pageNo, pageSize);
		IPage<AirqDay> pageList = airqDayService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param airqDay
	 * @return
	 */
	@AutoLog(value = "airq_day-添加")
	@ApiOperation(value="airq_day-添加", notes="airq_day-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AirqDay airqDay) {
		airqDayService.save(airqDay);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param airqDay
	 * @return
	 */
	@AutoLog(value = "airq_day-编辑")
	@ApiOperation(value="airq_day-编辑", notes="airq_day-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AirqDay airqDay) {
		airqDayService.updateById(airqDay);
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_day-通过id删除")
	@ApiOperation(value="airq_day-通过id删除", notes="airq_day-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		airqDayService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "airq_day-批量删除")
	@ApiOperation(value="airq_day-批量删除", notes="airq_day-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.airqDayService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_day-通过id查询")
	@ApiOperation(value="airq_day-通过id查询", notes="airq_day-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AirqDay airqDay = airqDayService.getById(id);
		if(airqDay==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(airqDay);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param airqDay
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AirqDay airqDay) {
        return super.exportXls(request, airqDay, AirqDay.class, "airq_day");
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
        return super.importExcel(request, response, AirqDay.class);
    }
	 /**
	  * 空气质量实日报
	  *
	  * @param companyIds
	  * @return
	  */
	 @AutoLog(value = "空气质量实日报")
	 @ApiOperation(value="airq_Day-空气质量实日报", notes="airq_Day-空气质量实日报")
	 @GetMapping(value = "/queryDayAirQuality")
	 public Result<?> queryDayAirQuality(@RequestParam(name="companyIds",required=true) String companyIds
			 ,@RequestParam(name="datatime",required=true) String datatime
			 ,@RequestParam(name="datatime2",required=true) String datatime2
			 ,@RequestParam(name="area",required=false) String area
			 ,@RequestParam(name="mn",required=false) String mn) {
		 //根据日起查询
		 return Result.ok(airqDayService.queryDayAirQuality(Arrays.asList(companyIds.split(",")),datatime,datatime2,area,mn));
	 }

	 /**
	  * 空气质量实日报
	  *
	  * @return
	  */
	 @AutoLog(value = "蓝天白云日历")
	 @ApiOperation(value="airq_Day-蓝天白云日历", notes="airq_Day-蓝天白云日历")
	 @GetMapping(value = "/queryCalendarAirQuality")
	 public Result<?> queryCalendarAirQuality(@RequestParam(name="area",required=false) String area
			 ,@RequestParam(name="year",required=true) String year,
			 @RequestParam(name = "checkedKeys", required = true) String checkedKeys) {
	 	String datatime = year + "-01-01";
	 	String datatime2 = year + "-12-31";
		 //根据日起查询
		 List<Map<String,Object>>  airqList =  airqDayService.queryCalendarAirQuality(datatime,datatime2
				,area,Arrays.asList(checkedKeys.split(",")));
		Map<String,Object> result = new HashMap<>();
		result.put("dataList",airqList);
		return Result.ok(result);
	 }

	 /**
	  * 空气质量 首页蓝天日历
	  *
	  * @return
	  */
	 @AutoLog(value = "蓝天白云日历")
	 @ApiOperation(value="airq_Day-蓝天白云日历", notes="airq_Day-蓝天白云日历")
	 @GetMapping(value = "/queryAirHomeCalendar")
	 public Result<?> queryAirHomeCalendar(@RequestParam(name="queryTime",required=true) String queryTime
			 ) {
		 String datatime = queryTime + "-01";
		 String datatime2 = queryTime + "-31";
		 //根据日起查询
		 List<Map<String,Object>>  airHomeCalendarList =  airqDayService.queryAirHomeCalendar(datatime,datatime2);
		 Map<String,Object> result = new HashMap<>();
		 for(Map<String,Object> param:airHomeCalendarList){
		 	 Map<String,Double> code = new HashMap<>();
		 	 String aqi = param.get("AQI").toString();
		 	 if(!("0.0".equals(aqi)) && StrUtil.isNotEmpty(aqi)) {
				 code.put("A34004", Double.valueOf(param.get("A34004").toString()));
				 code.put("A34002", Double.valueOf(param.get("A34002").toString()));
				 code.put("A21026", Double.valueOf(param.get("A21026").toString()));
				 code.put("A21005", Double.valueOf(param.get("A21005").toString()));
				 code.put("A21004", Double.valueOf(param.get("A21004").toString()));
				 code.put("A05024", Double.valueOf(param.get("A05024").toString()));
				 List<Map.Entry<String,Double>> list = new ArrayList(code.entrySet());
				 Collections.sort(list, (o1, o2) -> {
					 if ((o2.getValue() - o1.getValue()) > 0)
						 return 1;
					 else if ((o2.getValue() - o1.getValue()) == 0)
						 return 0;
					 else
						 return -1;
				 });
				 Double maxValue = list.get(0).getValue();
				 String firstCode = list.get(0).getKey();
				 for (int i = 1; i < list.size(); i++) {
					 if (Double.toString(list.get(i).getValue()).equals(Double.toString(maxValue))) {
						 firstCode = firstCode + "," + list.get(i).getKey();
					 } else {
						 break;
					 }
				 }
				 String firstCodeName = redisCacheUtil.transformCode(firstCode);
				 param.put("firstCode", firstCodeName);
			 }else {
		 	 	param.put("firstCode", "无");
			 }

		 }
		 result.put("dataList",airHomeCalendarList);
		 return Result.ok(result);

	 }

	 /**
	  * 导出excel
	  *
	  * @param request
	  */
	 @RequestMapping(value = "/exportQuality")
	 public ModelAndView exportQuality(HttpServletRequest request) {

		 List<AirqDayQualityVo> exportList =  airqDayService.queryDayAirQuality(Arrays.asList(request.getParameter("companyIds").split(","))
				 ,request.getParameter("datatime"),request.getParameter("datatime2"),request.getParameter("area"),request.getParameter("mn"));
		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new SelfEntityExcelView(sysDictService,redisUtil));
		 mv.addObject(SelfExcelConstants.TITLE, "空气质量指数日报"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(SelfExcelConstants.SHEET_NAME, "空气质量指数日报");
		 mv.addObject(SelfExcelConstants.CLAZZ, AirqDayQualityVo.class);
		 mv.addObject(SelfExcelConstants.DATA_LIST, exportList);
		 mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");

		 return mv;

	 }

	 /**
	  * 站点质量排名
	  *
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "站点质量排名")
	 @ApiOperation(value="站点质量排名", notes="站点质量排名")
	 @GetMapping(value = "/querySiteDay")
	 public Result<?> querySiteDay(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) throws ParseException {
		 String companyIds = req.getParameter("companyIds");
		 String area = req.getParameter("area");
		 String mn = req.getParameter("mn");
		 String dataTime = req.getParameter("searchTime");
		 String dataType = req.getParameter("dataType");
		 Timestamp queryDate;
		 if("day".equals(dataType)) {
			 if(StrUtil.isEmpty(dataTime)) {
				 queryDate = null;
			 }else {
				 queryDate = DateUtils.parseTimestamp(dataTime,"yyyy-MM-dd");
			 }
			 Page<SiteQualityRankDayVO> page = new Page<>(pageNo, pageSize);
			 IPage<SiteQualityRankDayVO> dayList = airqDayService.querySiteDay(companyIds,page, area,mn,queryDate);
			 return Result.ok(dayList);
		 }
		 else if("month".equals(dataType)) {
			 Page<SiteQualityRankMonthVO> page = new Page<>(pageNo, pageSize);
			 IPage<SiteQualityRankMonthVO> monthList = airqMonthService.querySiteMonth(companyIds,page, area,mn,dataTime);
			 return Result.ok(monthList);
		 }else{
			 Page<SiteQualityRankYearVO> page = new Page<>(pageNo, pageSize);
			 IPage<SiteQualityRankYearVO> yearList = airqYearService.querySiteYear(companyIds,page, area,mn,dataTime);
			 return Result.ok(yearList);
		 }


	 }

	 /**
	  * 导出excel 站点质量日排名
	  *
	  * @param req
	  */
	 @RequestMapping(value = "/exportSiteDay")
	 public ModelAndView exportSiteDay(HttpServletRequest req) throws ParseException {
		 String companyIds = req.getParameter("companyIds");
		 String area = req.getParameter("area");
		 String mn = req.getParameter("mn");
		 String dataTime = req.getParameter("searchTime");
		 String dataType = req.getParameter("dataType");
		 Timestamp queryDate;
		 if("day".equals(dataType)) {
			 if(StrUtil.isEmpty(dataTime)) {
				 queryDate = null;
			 }else {
				 queryDate = DateUtils.parseTimestamp(dataTime,"yyyy-MM-dd");
			 }
			 List<SiteQualityRankDayVO> dayList = airqDayService.querySiteDayExport(companyIds,area,mn,queryDate);
			 // Step.3 AutoPoi 导出Excel
			 ModelAndView mvDay = new ModelAndView(new SelfEntityExcelView(sysDictService,redisUtil));
			 mvDay.addObject(SelfExcelConstants.TITLE, "站点质量日排名"); //此处设置的filename无效 ,前端会重更新设置一下
			 mvDay.addObject(SelfExcelConstants.SHEET_NAME, "站点质量日排名");
			 mvDay.addObject(SelfExcelConstants.CLAZZ, SiteQualityRankDayVO.class);
			 mvDay.addObject(SelfExcelConstants.DATA_LIST, dayList);
			 mvDay.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");

			 return mvDay;
		 }else if("month".equals(dataType)) {
		 	List<SiteQualityRankMonthVO> monthList = airqMonthService.exportSiteMonth(companyIds, area,mn,dataTime);
			 // Step.3 AutoPoi 导出Excel
			 ModelAndView mvMonth = new ModelAndView(new SelfEntityExcelView(sysDictService,redisUtil));
			 mvMonth.addObject(SelfExcelConstants.TITLE, "站点质量月排名"); //此处设置的filename无效 ,前端会重更新设置一下
			 mvMonth.addObject(SelfExcelConstants.SHEET_NAME, "站点质量月排名");
			 mvMonth.addObject(SelfExcelConstants.CLAZZ, SiteQualityRankMonthVO.class);
			 mvMonth.addObject(SelfExcelConstants.DATA_LIST, monthList);
			 mvMonth.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");

			 return mvMonth;
		 }else {
			 List<SiteQualityRankYearVO> yearList = airqYearService.exportSiteYear(companyIds, area,mn,dataTime);
			 // Step.3 AutoPoi 导出Excel
			 ModelAndView mvYear = new ModelAndView(new SelfEntityExcelView(sysDictService,redisUtil));
			 mvYear.addObject(SelfExcelConstants.TITLE, "站点质量年排名"); //此处设置的filename无效 ,前端会重更新设置一下
			 mvYear.addObject(SelfExcelConstants.SHEET_NAME, "站点质量年排名");
			 mvYear.addObject(SelfExcelConstants.CLAZZ, SiteQualityRankYearVO.class);
			 mvYear.addObject(SelfExcelConstants.DATA_LIST, yearList);
			 mvYear.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");

			 return mvYear;
		 }
	 }
}
