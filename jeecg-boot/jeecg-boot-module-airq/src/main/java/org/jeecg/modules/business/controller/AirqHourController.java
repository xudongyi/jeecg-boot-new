package org.jeecg.modules.business.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.AirqHour;
import org.jeecg.modules.business.entity.AirqLevel;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.service.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.impl.AirqLevelServiceImpl;
import org.jeecg.modules.business.utils.AirQualityUtil;
import org.jeecg.modules.business.view.SelfEntityExcelView;
import org.jeecg.modules.business.vo.*;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	 private IAirqDayService airqDayService;

	 @Autowired
	 private IAirqMonthService airqMonthService;

	@Autowired
	private ISiteMonitorPointService siteMonitorPointService;

	@Autowired
    AirQualityUtil airQualityUtil;
	 @Autowired
	 private ISysDictService sysDictService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private IAirqLevelService airqLevelService;

	 private static   String factorJson = "[{name: \"PM10\", color:\"#666666\"}, { name: \"PM2.5\", color:\"#666666\"}, { name: \"SO₂\", color:\"#666666\"}, { name: \"NO₂\", color:\"#666666\"}, { name: \"CO\", color:\"#666666\"}, { name: \"O₃\", color:\"#666666\"}]";
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
	  * 查询站点 一段时间内的数据
	  *
	  * @param companyIds
	  * @return
	  */
	 @AutoLog(value = "查询站点最新的")
	 @ApiOperation(value="airq_hour-查询站点最新的", notes="airq_hour-查询站点最新的")
	 @GetMapping(value = "/queryPollutionCloud")
	 public Result<?> queryPollutionCloud(@RequestParam(name="companyIds",required=true) String companyIds
			 ,@RequestParam(name="datatime",required=true) String datatime
			 ,@RequestParam(name="datatime2",required=true) String datatime2) {
	 		//查询所给时间内的所有小时数据
		 return Result.ok(airqHourService.queryPollutionCloud(Arrays.asList(companyIds.split(",")),datatime,datatime2));
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
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "查询站点录入")
	 @ApiOperation(value="airq_hour-分页列表查询", notes="airq_hour-分页列表查询")
	 @GetMapping(value = "/queryAirqHourAudit")
	 public Result<?> queryAirqHourAudit(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
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
		 Page<AirqHourAuditVO> page = new Page<>(pageNo, pageSize);
		 IPage<AirqHourAuditVO> pageList = airqHourService.queryAirqHourAudit(companyIds,page, area,mn,state,dateBegin,dateEnd);
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
	 @GetMapping(value = "/querySiteQualityEvaluate")
	 public Result<?> querySiteQualityEvaluate(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) throws ParseException {
		 String companyIds = req.getParameter("companyIds");
		 String area = req.getParameter("area");
		 //通过选择站点名称获取站点mn号
		 String mn = req.getParameter("mn");
		 String dataState = req.getParameter("state");
		 String level = req.getParameter("level");
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
		 Page<SiteQualityEvaluateVO> page = new Page<>(pageNo, pageSize);
		 IPage<SiteQualityEvaluateVO> pageList = airqHourService.querySiteQualityEvaluate(companyIds,page, area,mn,level,state,dateBegin,dateEnd);
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
		 siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getSiteType,3).in(SiteMonitorPoint::getCompanyId,idList)).forEach(siteMonitorPoint -> {
			 Map<String,String> param = new HashMap<>();
			 param.put("key",siteMonitorPoint.getMn());
			 param.put("value",siteMonitorPoint.getSiteName());
			 param.put("area",siteMonitorPoint.getArea());
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
	@SneakyThrows
	@AutoLog(value = "airq_hour-添加")
	@ApiOperation(value="airq_hour-添加", notes="airq_hour-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AirqHour airqHour) {
		Map<String,Double> map = new HashMap<>();
        for(Field field:airqHour.getClass().getDeclaredFields()){
			field.setAccessible(true);
        	String fieldName =  field.getName();
        	//是不是值
			if(fieldName.endsWith("Avg")){

				//对象
				Object val = field.get(airqHour);
				if(val!=null&&!val.toString().equals("")){
					String code = fieldName.substring(0,6);
					code = code.substring(0, 1).toUpperCase() + code.substring(1);
					int type = fieldName.length()>=11? Integer.valueOf(fieldName.substring(7,8)):1;

					double aqi =	airQualityUtil.getAQI( code,  type,Double.valueOf(val.toString()));
					String newFieldName = fieldName.replace("Avg", "Iaqi");
					String newCode = newFieldName.substring(0,1).toUpperCase() + newFieldName.substring(1);
					Method mtd = airqHour.getClass().getMethod("set"+newCode, Double.class);
					mtd.invoke(airqHour, aqi);
					//污染因子编码
					String pollutionCode = fieldName.length()>10? fieldName.substring(0,8):fieldName.substring(0,6);
					pollutionCode = pollutionCode.substring(0,1).toUpperCase() + pollutionCode.substring(1);
					map.put(pollutionCode,aqi);
				}
			}
        }
		List<Map.Entry<String,Double>> list = new ArrayList(map.entrySet());
		Collections.sort(list, (o1, o2) -> {
			if ((o2.getValue() - o1.getValue())>0)
				return 1;
			else if((o2.getValue() - o1.getValue())==0)
				return 0;
			else
				return -1;
		});
		Double aqi = list.get(0).getValue();
		airqHour.setAqi(aqi);
		airqHour.setLevel(airQualityUtil.getLevel(aqi));
		String firstCode = list.get(0).getKey();
		for(int i = 1;i < list.size();i++) {
			if(Double.toString(list.get(i).getValue()).equals(Double.toString(aqi))) {
				firstCode = firstCode + "," + list.get(i).getKey();
			}else {
				break;
			}
		}
		airqHour.setFirstCode(firstCode);
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
	  *  审核
	  *
	  * @param airqHour
	  * @return
	  */
	 @AutoLog(value = "airq_hour-审核")
	 @ApiOperation(value="airq_hour-审核", notes="airq_hour-审核")
	 @PutMapping(value = "/audit")
	 public Result<?> audit(@RequestBody AirqHour airqHour) {
		 airqHourService.updateById(airqHour);
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
			 //3-审核通过
			 LambdaUpdateWrapper updateWrapper = new UpdateWrapper<AirqHour>().lambda().in(AirqHour::getId,Arrays.asList(ids))
					 .set(AirqHour::getState,3).set(AirqHour::getUpdateTime,new Date())
					 .set(AirqHour::getUpdateBy,sysUser.getId());
			 airqHourService.update(updateWrapper);
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
			 //4-审核不通过
			 LambdaUpdateWrapper updateWrapper = new UpdateWrapper<AirqHour>().lambda().in(AirqHour::getId,Arrays.asList(ids))
					 .set(AirqHour::getState,4).set(AirqHour::getUpdateTime,new Date())
					 .set(AirqHour::getUpdateBy,sysUser.getId());
			 airqHourService.update(updateWrapper);
		 }
		 return Result.ok();
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
		 ModelAndView mv = new ModelAndView(new SelfEntityExcelView(sysDictService,redisUtil));
		 mv.addObject(SelfExcelConstants.TITLE, "空气质量指数实时报"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(SelfExcelConstants.SHEET_NAME, "空气质量指数实时报");
		 mv.addObject(SelfExcelConstants.CLAZZ, AirqHourQualityVo.class);
		 mv.addObject(SelfExcelConstants.DATA_LIST, exportList);
		 mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");

		 return mv;

	 }

	 /**
	  * 导出excel
	  *
	  * @param req
	  */
	 @RequestMapping(value = "/exportManInput")
	 public ModelAndView exportManInput(HttpServletRequest req) throws ParseException {
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
		 List<AirqHourInputVO> exportList = airqHourService.queryManInputExport(companyIds,area,mn,dateBegin,dateEnd);
		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new SelfEntityExcelView(sysDictService,redisUtil));
		 mv.addObject(SelfExcelConstants.TITLE, "人工采集数据"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(SelfExcelConstants.SHEET_NAME, "人工采集数据");
		 mv.addObject(SelfExcelConstants.CLAZZ, AirqHourInputVO.class);
		 mv.addObject(SelfExcelConstants.DATA_LIST, exportList);
		 mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");

		 return mv;
	 }


	 /**
	  * 空气质量app 监测站点
	  *
	  * @return
	  */
	 @AutoLog(value = "监测站点")
	 @ApiOperation(value="监测站点", notes="监测站点")
	 @GetMapping(value = "/queryAirSiteInfo")
	 public Result<?> queryAirSiteInfo(@RequestParam(name="companyIds",required=true) String companyIds
			 ,@RequestParam(name="mn",required=false) String mn) {
		 List<Map<String,Object>>  airSiteList =  airqHourService.queryAirSiteInfo(Arrays.asList(companyIds.split(",")),mn);
		 Map<String,Object> result = new HashMap<>();
		 for(Map<String,Object> param:airSiteList){
			 String value =  sysDictService.queryDictTextByKey("siteLevel", param.get("siteLevel").toString());
			 param.put("siteLevelName",value);
			 String levelValue = sysDictService.queryDictTextByKey("level",param.get("level").toString());
			 if(levelValue.length()>1) {
			 	levelValue = levelValue.substring(0, 2);
			 }
			 param.put("levelName", levelValue);
			 String firstCode = param.get("firstCode").toString();
			 List<String> arr = Arrays.asList(firstCode.split(","));
			 List<String> codeArr = new ArrayList<>();
			 for (int i=0;i<arr.size();i++) {
			 	codeArr.add(arr.get(i).substring(0, 6));
			 }
			 String code = StringUtils.join(codeArr, ",");
			 param.put("code", code);
		 }
		 result.put("dataList",airSiteList);
		 return Result.ok(result);
	 }

	 /**
	  * 空气质量app 监测站点折线图数据
	  *
	  * @return
	  */
	 @AutoLog(value = "监测站点折线图数据")
	 @ApiOperation(value="监测站点折线图数据", notes="监测站点折线图数据")
	 @GetMapping(value = "/queryChartInfo")
	 public Result<?> queryChartInfo(@RequestParam(name="mn",required=true) String mn
			 ,@RequestParam(name="timeType",required=false) String timeType,@RequestParam(name="code",required=false) String code) {
		 List<Map<String,Object>>  airChartList = null;
		 Map<String,Object> result = new HashMap<>();
		 List<Double> codeList = null;
		 List<String> timeList = null;
	 	 if("0".equals(timeType)) {
			 airChartList =  airqHourService.queryHourChartInfo(mn);
			 codeList = new ArrayList<>();
			 timeList = new ArrayList<>();
			 for(Map<String,Object> param:airChartList){
				String value = param.get(code).toString();
				Double codeValue = Double.parseDouble(value);
				codeList.add(codeValue);
				String time = param.get("dataTime").toString().substring(0, 19);
				timeList.add(time);
			 }

			 result.put("series",codeList);
			 result.put("categories",timeList);

		 }else if ("1".equals(timeType)) {
	 	 	 airChartList = airqDayService.queryDayChartInfo(mn);
			 codeList = new ArrayList<>();
			 timeList = new ArrayList<>();
			 for(Map<String,Object> param:airChartList){
				 String value = param.get(code).toString();
				 Double codeValue = Double.parseDouble(value);
				 codeList.add(codeValue);
				 String time = param.get("dataTime").toString().substring(0, 19);
				 timeList.add(time);
			 }
			 result.put("series",codeList);
			 result.put("categories",timeList);
		 }else if("2".equals(timeType)) {
	 	 	 airChartList = airqMonthService.queryMonthChartInfo(mn);
			 codeList = new ArrayList<>();
			 timeList = new ArrayList<>();
			 for(Map<String,Object> param:airChartList){
				 String value = param.get(code).toString();
				 Double codeValue = Double.parseDouble(value);
				 codeList.add(codeValue);
				 String time = param.get("dataTime").toString().substring(0, 19);
				 timeList.add(time);
			 }
			 result.put("series",codeList);
			 result.put("categories",timeList);
		 }

		 return Result.ok(result);
	 }

	 @AutoLog(value = "获取最新数据的指数")
	 @ApiOperation(value="获取最新数据的指数", notes="获取最新数据的指数")
	 @GetMapping(value = "/queryExponent")
	 public Result<?> queryExponent(@RequestParam(name="companyIds",required=true) String companyIds) {
		 Map<String,Object> mapResult = null;
	 	//获取当前时间
		 String curr = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH");
		 DateTime nowDate = DateUtil.parse(curr, "yyyy-MM-dd HH");
		 //获取所属站点当前数据的平均值
		 List<AirHourPlayVo> airHourPlayVos = airqHourService.queryAirAvgInfo(Arrays.asList(companyIds.split(",")),nowDate);
		 List<Double> aqis = new ArrayList<>();
		 List<Object> exponents = new ArrayList<>();
		 List<Map<String, String>> factors = (List<Map<String, String>>) JSONObject.parse(factorJson);
		 if(airHourPlayVos!=null && airHourPlayVos.size()>0){
			 mapResult = new HashMap<>();
			 AirHourPlayVo airHourPlayVo = airHourPlayVos.get(0);
			 Double aqi = airHourPlayVo.getAqi();
			 String level = airQualityUtil.getLevel(aqi);
			 mapResult.put("aqiLevel", Integer.valueOf(level));
			 AirqLevel airqLevel = airqLevelService.getOne(new QueryWrapper<AirqLevel>().lambda().eq(AirqLevel::getLevel, level));
			 mapResult.put("levelContent",airqLevel.getLevelContent());
			 mapResult.put("grade",airqLevel.getLevelGrade());
			 mapResult.put("gradeColor",airqLevel.getLevelRgb());
			 mapResult.put("aqi",Math.round(aqi));
			 //计算各污染因子aqi
			 //pm10 pm2.5 so2 no2 co 03
			 double a3400201Iaqi = airHourPlayVo.getA3400201Iaqi()==null?-1:airHourPlayVo.getA3400201Iaqi();
			 double a34002aqi = airQualityUtil.getAQI("A34002", 1, a3400201Iaqi);
			 aqis.add(a34002aqi);

			 double a3400401Iaqi = airHourPlayVo.getA3400401Iaqi()==null?-1:airHourPlayVo.getA3400401Iaqi();
			 double a3400401aqi = airQualityUtil.getAQI("A34004", 1, a3400401Iaqi);
			 aqis.add(a3400401aqi);

			 double a21026Iaqi = airHourPlayVo.getA21026Iaqi()==null?-1:airHourPlayVo.getA21026Iaqi();
			 double a21026aqi = airQualityUtil.getAQI("A21026", 1, a21026Iaqi);
			 aqis.add(a21026aqi);

			 double a21004Iaqi = airHourPlayVo.getA21004Iaqi()==null?-1:airHourPlayVo.getA21004Iaqi();
			 double a21004aqi = airQualityUtil.getAQI("A21004", 1, a21004Iaqi);
			 aqis.add(a21004aqi);

			 double a21005Iaqi = airHourPlayVo.getA21005Iaqi()==null?-1:airHourPlayVo.getA21005Iaqi();
			 double a21005aqi = airQualityUtil.getAQI("A21005", 1, a21005Iaqi);
			 aqis.add(a21005aqi);

			 double a0502401Iaqi = airHourPlayVo.getA0502401Iaqi()==null?-1:airHourPlayVo.getA0502401Iaqi();
			 double a0502401aqi = airQualityUtil.getAQI("A05024", 1, a0502401Iaqi);
			 aqis.add(a0502401aqi);
			 Double maxAqi = Collections.max(aqis);
			 for (int i = 0; i < aqis.size(); i++) {
				 Map<String,Object> exponentMap = new HashMap<>();
				 Double pollutionAqi =  aqis.get(i);
				//获取level
				 String pollutionLevel = airQualityUtil.getLevel(pollutionAqi);
				 AirqLevel airqPollutionLevel = airqLevelService.getOne(new QueryWrapper<AirqLevel>().lambda().eq(AirqLevel::getLevel, pollutionLevel));
				 exponentMap.put("color",airqPollutionLevel.getLevelRgb());
				 exponentMap.put("content",pollutionAqi==-1?"":pollutionAqi);
				 exponents.add(exponentMap);
				 //判断首要污染物
				 if(maxAqi==pollutionAqi){
					 Map<String, String> factorMap = factors.get(i);
					 factorMap.put("color","#FF0000");
					 factors.set(i,factorMap);
				 }
			 }
			 mapResult.put("factors",factors);
			 mapResult.put("exponents",exponents);
		 }
		return Result.ok(mapResult);
	 }
}
