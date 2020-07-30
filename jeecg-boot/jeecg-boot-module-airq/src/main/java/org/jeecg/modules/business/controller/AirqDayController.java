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
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.AirqDay;
import org.jeecg.modules.business.service.IAirqDayService;
import org.jeecg.modules.business.view.SelfEntityExcelView;
import org.jeecg.modules.business.vo.AirqDayQualityVo;
import org.jeecg.modules.business.vo.SiteQualityRankDayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
		 Set<String> months = new HashSet<>();
		for(Map<String,Object> airqQuality:airqList){
			airqQuality.put("dataTime",DateUtil.format((Timestamp)airqQuality.get("dataTime"),"yyyy-MM-dd"));

			months.add(airqQuality.get("dataTime").toString().substring(0,7));
		}

		result.put("dataList",airqList);
		result.put("months",months);
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
		 ModelAndView mv = new ModelAndView(new SelfEntityExcelView());
		 mv.addObject(SelfExcelConstants.TITLE, "空气质量指数日报"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(SelfExcelConstants.SHEET_NAME, "空气质量指数日报");
		 mv.addObject(SelfExcelConstants.CLAZZ, AirqDayQualityVo.class);
		 mv.addObject(SelfExcelConstants.DATA_LIST, exportList);
		 mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");

		 return mv;

	 }

	 /**
	  * 站点质量日排名
	  *
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "站点质量日排名")
	 @ApiOperation(value="airq_Day-站点质量日排名", notes="airq_Day-站点质量日排名")
	 @GetMapping(value = "/querySiteDay")
	 public Result<?> querySiteDay(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) throws ParseException {
		 String companyIds = req.getParameter("companyIds");
		 String area = req.getParameter("area");
		 //通过选择站点名称获取站点mn号
		 String mn = req.getParameter("mn");
		 String dataTime = req.getParameter("dataTime");
		 Timestamp queryDate;
		 if(StrUtil.isEmpty(dataTime)) {
		 	queryDate = null;
		 }else {
		 	queryDate = DateUtils.parseTimestamp(dataTime,"yyyy-MM-dd");
		 }
		 Page<SiteQualityRankDayVO> page = new Page<>(pageNo, pageSize);
		 IPage<SiteQualityRankDayVO> pageList = airqDayService.querySiteDay(companyIds,page, area,mn,queryDate);
		 return Result.ok(pageList);
	 }
}
