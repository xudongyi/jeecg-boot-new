package org.jeecg.modules.business.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import org.apache.shiro.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.AirqHour;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.service.IAirqHourService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ISiteMonitorPointService;
import org.jeecg.modules.business.view.SelfEntityExcelView;
import org.jeecg.modules.business.vo.AirqHourInputVO;
import org.jeecg.modules.business.vo.AirqHourManInsertVO;
import org.jeecg.modules.business.vo.AirqHourMonitorVO;
import org.jeecg.modules.business.vo.AirqHourQualityVo;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Api(tags="airq_hour")
@RestController
@RequestMapping("/hour/airqHour")
@Slf4j
public class AirqHourController extends JeecgController<AirqHour, IAirqHourService> {
	@Autowired
	private IAirqHourService airqHourService;

	@Autowired
	private ISiteMonitorPointService siteMonitorPointService;
	
	/**
	 * 分页列表查询
	 *
	 * @param airqHour
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "airq_hour-分页列表查询")
	@ApiOperation(value="airq_hour-分页列表查询", notes="airq_hour-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AirqHour airqHour,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AirqHour> queryWrapper = QueryGenerator.initQueryWrapper(airqHour, req.getParameterMap());
		Page<AirqHour> page = new Page<AirqHour>(pageNo, pageSize);
		IPage<AirqHour> pageList = airqHourService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	 /**
	  * 分页列表查询
	  *
	  * @param companyIds
	  * @return
	  */
	 @AutoLog(value = "查询站点最新的")
	 @ApiOperation(value="airq_hour-查询站点最新的", notes="airq_hour-查询站点最新的")
	 @GetMapping(value = "/queryLastAirInfo")
	 public Result<?> queryLastAirInfo(@RequestParam(name="companyIds",required=true) String companyIds) {

	 	return Result.ok(airqHourService.queryInfoByCompanyId(Arrays.asList(companyIds.split(","))));
	 }
	 /**
	  * 空气质量实时报
	  *
	  * @param companyIds
	  * @return
	  */
	 @AutoLog(value = "空气质量实时报")
	 @ApiOperation(value="airq_hour-空气质量实时报", notes="airq_hour-空气质量实时报")
	 @GetMapping(value = "/queryHourAirQuality")
	 public Result<?> queryHourAirQuality(@RequestParam(name="companyIds",required=true) String companyIds
										  ,@RequestParam(name="datatime",required=true) String datatime
			 								,@RequestParam(name="datatime2",required=true) String datatime2
			 								,@RequestParam(name="area",required=false) String area
			 								,@RequestParam(name="mn",required=false) String mn) {
		//根据小时查询
	 	return Result.ok(airqHourService.queryHourAirQuality(Arrays.asList(companyIds.split(",")),datatime,datatime2,area,mn));
	 }
	 /**
	  * 分页列表查询-实时小时数据
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "查询站点最新的")
	 @ApiOperation(value="airq_hour-分页列表查询", notes="airq_hour-分页列表查询")
	 @GetMapping(value = "/queryLastAirqHour")
	 public Result<?> queryAirqHourMonitor(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										   HttpServletRequest req) throws ParseException {
		 String companyIds = req.getParameter("companyIds");
	 	 String area = req.getParameter("area");
	 	 //通过选择站点名称获取站点mn号
		 String mn = req.getParameter("mn");
		 String dataTimeBegin = req.getParameter("dataTime_begin");
		 String dataTimeEnd = req.getParameter("dataTime_end");
		 Date dateBegin;
		 Date dateEnd;
		 if(StrUtil.isEmpty(dataTimeBegin) && StrUtil.isEmpty(dataTimeEnd)) {
			 dateBegin = null;
			 dateEnd = null;
		 }else{
			 dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeBegin);
			 dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeEnd);
		 }
		 Page<AirqHourMonitorVO> page = new Page<AirqHourMonitorVO>(pageNo, pageSize);
		 IPage<AirqHourMonitorVO> pageList = airqHourService.queryAirqHourMonitor(companyIds,page, area,mn,dateBegin,dateEnd);
		 return Result.ok(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "查询站点录入")
	 @ApiOperation(value="airq_hour-分页列表查询", notes="airq_hour-分页列表查询")
	 @GetMapping(value = "/queryInputAirqHour")
	 public Result<?> queryAirqHourInput(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										   HttpServletRequest req) throws ParseException {
		 String companyIds = req.getParameter("companyIds");
	 	 String area = req.getParameter("area");
		 //通过选择站点名称获取站点mn号
		 String mn = req.getParameter("mn");
		 String dataTimeBegin = req.getParameter("dataTime_begin");
		 String dataTimeEnd = req.getParameter("dataTime_end");
		 Date dateBegin;
		 Date dateEnd;
		 if(StrUtil.isEmpty(dataTimeBegin) && StrUtil.isEmpty(dataTimeEnd)) {
			 dateBegin = null;
			 dateEnd = null;
		 }else{
			 dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeBegin);
			 dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeEnd);
		 }
		 Page<AirqHourInputVO> page = new Page<AirqHourInputVO>(pageNo, pageSize);
		 IPage<AirqHourInputVO> pageList = airqHourService.queryAirqHourInput(companyIds,page, area,mn,dateBegin,dateEnd);
		 return Result.ok(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "查询站点录入")
	 @ApiOperation(value="airq_hour-分页列表查询", notes="airq_hour-分页列表查询")
	 @GetMapping(value = "/queryManInsertAirqHour")
	 public Result<?> queryAirqHourManInsert(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) throws ParseException {
		 String companyIds = req.getParameter("companyIds");
		 String area = req.getParameter("area");
		 //通过选择站点名称获取站点mn号
		 String mn = req.getParameter("mn");
		 String dataState = req.getParameter("state");
		 Integer state = null;
		 if(!StrUtil.isEmpty(dataState)) {
		 	state = Integer.valueOf(dataState);
		 }
		 String dataTimeBegin = req.getParameter("dataTime_begin");
		 String dataTimeEnd = req.getParameter("dataTime_end");
		 Date dateBegin;
		 Date dateEnd;
		 if(StrUtil.isEmpty(dataTimeBegin) && StrUtil.isEmpty(dataTimeEnd)) {
			 dateBegin = null;
			 dateEnd = null;
		 }else{
			 dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeBegin);
			 dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeEnd);
		 }
		 Page<AirqHourManInsertVO> page = new Page<AirqHourManInsertVO>(pageNo, pageSize);
		 IPage<AirqHourManInsertVO> pageList = airqHourService.queryAirqHourManInsert(companyIds,page, area,mn,state,dateBegin,dateEnd);
		 return Result.ok(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @return
	  */
	 @AutoLog(value = "查询站点最新的")
	 @ApiOperation(value="airq_hour-分页列表查询", notes="airq_hour-分页列表查询")
	 @GetMapping(value = "/querySiteNameAndMn")
	 public Result<?> querySiteNameAndMn(@RequestParam(name="companyIds",required=true) String companyIds) {
		 List<String> idList =  Arrays.asList(companyIds.split(","));
		 List<Map<String,String>> result = new ArrayList<>();
		 siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda().in(SiteMonitorPoint::getCompanyId,idList)).forEach(siteMonitorPoint -> {
			 Map<String,String> param = new HashMap<>();
			 param.put("key",siteMonitorPoint.getMn());
			 param.put("value",siteMonitorPoint.getSiteName());
			 result.add(param);
		 });
	 	 return Result.ok(result);
	 }

	 /**
	  *   提交
	  *
	  * @param airqHour
	  * @return
	  */
	 @AutoLog(value = "airq_hour-提交")
	 @ApiOperation(value="airq_hour-提交", notes="airq_hour-提交")
	 @PostMapping(value = "/submit")
	 public Result<?> submit(@RequestBody AirqHour airqHour) {
		 //3-审核中
		 airqHour.setState(3);
		 airqHourService.updateById(airqHour);
		 return Result.ok("提交成功！");
	 }

	 /**
	  *  批量提交
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "人工录入数据批量提交")
	 @ApiOperation(value="人工录入数据批量提交", notes="人工录入数据批量提交")
	 @GetMapping(value = "/batchSubmit")
	 public Result<?> batchSubmit(@RequestParam(name="ids",required=true) String ids) {
		 //修改
		 airqHourService.update(new UpdateWrapper<AirqHour>().lambda()
				 .eq(AirqHour::getState,2)
				 .in(AirqHour::getId,Arrays.asList(ids.split(",")))
				 .set(AirqHour::getState,3));
		 return Result.ok("批量提交成功!");
	 }

	/**
	 *   添加
	 *
	 * @param airqHour
	 * @return
	 */
	@AutoLog(value = "airq_hour-添加")
	@ApiOperation(value="airq_hour-添加", notes="airq_hour-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AirqHour airqHour) {
		//2-暂存
		airqHour.setState(2);
		airqHourService.save(airqHour);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param airqHour
	 * @return
	 */
	@AutoLog(value = "airq_hour-编辑")
	@ApiOperation(value="airq_hour-编辑", notes="airq_hour-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AirqHour airqHour) {
		//2-暂存
		airqHour.setState(2);
		airqHourService.updateById(airqHour);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_hour-通过id删除")
	@ApiOperation(value="airq_hour-通过id删除", notes="airq_hour-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		airqHourService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "airq_hour-批量删除")
	@ApiOperation(value="airq_hour-批量删除", notes="airq_hour-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.airqHourService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_hour-通过id查询")
	@ApiOperation(value="airq_hour-通过id查询", notes="airq_hour-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AirqHour airqHour = airqHourService.getById(id);
		if(airqHour==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(airqHour);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param airqHour
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AirqHour airqHour) {
        return super.exportXls(request, airqHour, AirqHour.class, "airq_hour");
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
        return super.importExcel(request, response, AirqHour.class);
    }

	 /**
	  * 导出excel
	  *
	  * @param request
	  */
	 @RequestMapping(value = "/exportQuality")
	 public ModelAndView exportQuality(HttpServletRequest request) {

		 List<AirqHourQualityVo> exportList =  airqHourService.queryHourAirQuality(Arrays.asList(request.getParameter("companyIds").split(","))
				 ,request.getParameter("datatime"),request.getParameter("datatime2"),request.getParameter("area"),request.getParameter("mn"));
		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new SelfEntityExcelView());
		 mv.addObject(SelfExcelConstants.TITLE, "空气质量指数实时报"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(SelfExcelConstants.SHEET_NAME, "空气质量指数实时报");
		 mv.addObject(SelfExcelConstants.CLAZZ, AirqHourQualityVo.class);
		 mv.addObject(SelfExcelConstants.DATA_LIST, exportList);
		 return mv;

	 }
}
