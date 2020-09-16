package org.jeecg.modules.business.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.service.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.business.utils.Util;
import org.jeecg.modules.business.utils.Zpage;
import org.jeecg.modules.business.view.SelfMapExcelView;
import org.jeecg.modules.business.vo.Column;
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
 * @Description: voc_day
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
@Api(tags="voc_day")
@RestController
@RequestMapping("/psoam/voc")
@Slf4j
public class VocHistoryController {
	 @Autowired
	 private IVocCurrentTrService vocCurrentTrService;
	 @Autowired
	 private IVocMinuteService vocMinuteService;
	 @Autowired
	 private IVocHourService vocHourServicel;
	 @Autowired
	 private IVocDayService vocDayService;

	 @Autowired
	 private ISiteMonitorPointService siteMonitorPointService;

	 @Autowired
	 private ISysPollutionCodeService sysPollutionCodeService;

	 @Autowired
	 private ISysDictService sysDictService;


	 /**
	  * 查询站点名称和mn号
	  * @param companyIds
	  * @return
	  */
	 @AutoLog(value = "查询站点名称")
	 @ApiOperation(value = "voc_day-分页列表查询", notes = "voc_day-分页列表查询")
	 @GetMapping(value = "/querySiteNameAndMn")
	 public Result<?> querySiteNameAndMn(@RequestParam(name = "companyIds", required = true) String companyIds,@RequestParam(name = "siteType", required = false) String siteType) {
		 List<String> idList = Arrays.asList(companyIds.split(","));
		 List<String> siteTypeList = Arrays.asList(siteType.split(","));
		 List<Map<String, String>> result = new ArrayList<>();
		 LambdaQueryWrapper<SiteMonitorPoint> queryWrapper = new QueryWrapper<SiteMonitorPoint>().lambda();
		 if(!StrUtil.isEmpty(siteType)){
			 queryWrapper.in(SiteMonitorPoint::getSiteType, siteTypeList);
		 }
		 queryWrapper.in(SiteMonitorPoint::getCompanyId, idList);
		 siteMonitorPointService.list(queryWrapper).forEach(siteMonitorPoint -> {
			 Map<String, String> param = new HashMap<>();
			 param.put("key", siteMonitorPoint.getMn());
			 param.put("value", siteMonitorPoint.getSiteName());
			 param.put("siteId", siteMonitorPoint.getId());
			 param.put("area", siteMonitorPoint.getArea());
			 param.put("companyId", siteMonitorPoint.getCompanyId());
			 param.put("siteType", siteMonitorPoint.getSiteType());
			 result.add(param);
		 });
		 return Result.ok(result);
	 }

	 /**
	  * 查询企业名称
	  *
	  * @return
	  */
	 @AutoLog(value = "查询企业名称")
	 @ApiOperation(value="voc_day-查询企业名称", notes="voc_day-查询企业名称")
	 @GetMapping(value = "/queryCompanyName")
	 public Result<?> queryCompanyName(@RequestParam(name = "companyIds", required = true) String companyIds) {
		 List<Map<String, Object>> companyNames = vocDayService.queryCompanyName(Arrays.asList(companyIds.split(",")));
		 Map<String, Object> result = new HashMap<>();
		 for(Map<String,Object> param:companyNames){
		 	String companyId = param.get("companyId").toString();
		 	String companyName = param.get("companyName").toString();
		 	param.put("key", companyId);
		 	param.put("value", companyName);
		 }
		 result.put("companyNames", companyNames);
		 return Result.ok(result);
	 }

	 /**
	  * 查询站点名称和mn号
	  * @param siteType
	  * @return
	  */
	 @AutoLog(value = "查询站点名称")
	 @ApiOperation(value = "voc_day-分页列表查询", notes = "voc_day-分页列表查询")
	 @GetMapping(value = "/queryPollutionCode")
	 public Result<?> queryPollutionCode(@RequestParam(name = "siteType", required = true) String siteType) {
		 List<Map<String, String>> result = new ArrayList<>();
		 sysPollutionCodeService.list(new QueryWrapper<SysPollutionCode>().lambda().eq(SysPollutionCode::getType,siteType).eq(SysPollutionCode::getIsUse,"Y").
				 eq(SysPollutionCode::getIsImportant,'Y')).forEach(sysPollutionCode -> {
			 Map<String, String> param = new HashMap<>();
			 param.put("key", sysPollutionCode.getCode());
			 param.put("value",sysPollutionCode.getMeaning());
			 result.add(param);
		 });
		 return Result.ok(result);
	 }

	 @AutoLog(value = "查询VOCs历史数据")
	 @ApiOperation(value = "查询VOCs历史数据", notes = "实时，分钟，小时，日数据")
	 @GetMapping(value = "/queryVocColumns")
	 public Result<?> queryVocColumns( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) {
		 String  area = req.getParameter("area");
		 String  companyId = req.getParameter("companyId");
		 String  companyIds = req.getParameter("companyIds");
		 String  mn = req.getParameter("mn");
		 String  dataType = req.getParameter("dataType");
		 String  dataTime_begin = req.getParameter("dataTime_begin");
		 String  dataTime_end = req.getParameter("dataTime_end");
		 //查询启用的污染因子   污水
		 List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.queryCode(area, companyId, mn, "2");

		 companyIds = StrUtil.isEmpty(companyId)?companyIds:companyId; //可以查询到的企业
		 List<String> companyIDs = Arrays.asList(companyIds.split(","));
		 Page<List<Map<String,Object>>> page = new Page<List<Map<String,Object>>>(pageNo, pageSize);
		 if(sysPollutionCodes.size()<1){
			 return Result.error(500,"不存在可查询的污染因子，请寻找管理员开启");
		 }
		 //都为空  默认查询 所有站点最近一条日数据
		 if(PollutionSource.DataType.REALTIME.equals(dataType))
		 {
			 //拼接  查询字段  companyName monName dataTime  必查询的字段
			 //根据启用污染因子拼接动态表头
			 List<Column> columns = queryRealTimeColumns(sysPollutionCodes);
			 String field = "";
			 for(Column column:columns){
				 field+=column.getDataIndex()+","+column.getDataIndex()+"_STATE,";
			 }
			 field = field.substring(0,field.length()-1).replaceAll("rtd_","");
			 String tableName = Util.getTableName("voc_current_tr_",dataTime_begin);
			 Zpage<Map<String,Object>> result= new Zpage(vocCurrentTrService.queryRealTime(page,field,tableName,companyIDs,area,mn,dataTime_begin,dataTime_end));
			 result.setColumns(columns);
			 result.setRecords(keyToLowerCase(result.getRecords()));
			 return Result.ok(result);
		 }
		 //拼接  查询字段  companyName monName dataTime  必查询的字段
		 //根据启用污染因子拼接动态表头
		 List<Column> columns = queryVocColumns(sysPollutionCodes);
		 String field = getFields(columns);
		 Zpage<Map<String,Object>> result;
		 if(PollutionSource.DataType.MINUTE.equals(dataType))
		 {
			 String tableName = Util.getTableName("voc_minute_",dataTime_begin);
			 result= new Zpage(vocMinuteService.queryMinute(page,field,tableName,companyIDs,area,mn,dataTime_begin,dataTime_end));
		 }
		 else if(PollutionSource.DataType.HOUR.equals(dataType)){
			 result = new Zpage(vocHourServicel.queryHour(page,field,companyIDs,area,mn,dataTime_begin,dataTime_end));
		 }
		 else
		 {
			 result= new Zpage(vocDayService.queryDay(page,field,companyIDs,area,mn,dataTime_begin,dataTime_end));
		 }
		 result.setColumns(columns);
		 result.setRecords(keyToLowerCase(result.getRecords()));
		 return Result.ok(result);
	 }

	 @AutoLog(value = "查询VOCs历史数据")
	 @ApiOperation(value = "查询VOCs历史数据", notes = "实时，分钟，小时，日数据")
	 @GetMapping(value = "/exportVocHistory")
	 public ModelAndView exportVocHistory(HttpServletRequest req) {

		 String area = req.getParameter("area");
		 String companyId = req.getParameter("companyId");
		 String companyIds = req.getParameter("companyIds");
		 String mn = req.getParameter("mn");
		 String dataType = req.getParameter("dataType");
		 String dataTime_begin = req.getParameter("dataTime_begin");
		 String dataTime_end = req.getParameter("dataTime_end");
		 //查询启用的污染因子   污水
		 List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.queryCode(area, companyId, mn, "2");

		 companyIds = StrUtil.isEmpty(companyId) ? companyIds : companyId; //可以查询到的企业
		 List<String> companyIDs = Arrays.asList(companyIds.split(","));
		 List<Map<String, Object>> result;
		 List<Column> columns;
		 String timeFormat;
		 //都为空  默认查询 所有站点最近一条日数据
		 if (PollutionSource.DataType.REALTIME.equals(dataType)) {
			 //拼接  查询字段  companyName monName dataTime  必查询的字段
			 //根据启用污染因子拼接动态表头
			 columns = queryRealTimeColumns(sysPollutionCodes);
			 String field = "";
			 for (Column column : columns) {
				 field += column.getDataIndex() + "," + column.getDataIndex() + "_state,";
			 }
			 field = field.substring(0, field.length() - 1).replaceAll("rtd_", "");
			 String tableName = Util.getTableName("voc_current_tr_", dataTime_begin);
			 result = vocCurrentTrService.queryRealTime(field, tableName, companyIDs, area, mn, dataTime_begin, dataTime_end);
			 result = keyToLowerCase(result);
			 timeFormat = PollutionSource.DataFormat.REALTIME;
		 }else{
			 columns = queryVocColumns(sysPollutionCodes);
			 String field = getFields(columns);

			 if (PollutionSource.DataType.MINUTE.equals(dataType)) {
				 timeFormat = PollutionSource.DataFormat.MINUTE;
				 String tableName = Util.getTableName("voc_minute_", dataTime_begin);
				 result = vocMinuteService.queryMinute( field, tableName, companyIDs, area, mn, dataTime_begin, dataTime_end);
			 } else if (PollutionSource.DataType.HOUR.equals(dataType)) {
				 timeFormat = PollutionSource.DataFormat.HOUR;

				 result = vocHourServicel.queryHour(field, companyIDs, area, mn, dataTime_begin, dataTime_end);
			 } else {
				 timeFormat = PollutionSource.DataFormat.DAY;

				 result = vocDayService.queryDay( field, companyIDs, area, mn, dataTime_begin, dataTime_end);
			 }
			 result = keyToLowerCase(result);
		 }
		 //columns  需要增加前面的固定列
		 List<Column> execlColumn = new ArrayList<>();
		 execlColumn.add(new Column("企业名称","company_name"));
		 execlColumn.add(new Column("监测点名称","site_name"));
		 execlColumn.add(new Column("时间","data_time"));
		 execlColumn.addAll(columns);
		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new SelfMapExcelView());
		 mv.addObject(SelfExcelConstants.TITLE, "历史数据（VOCs）"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(SelfExcelConstants.SHEET_NAME, "历史数据（VOCs）");
		 mv.addObject(SelfExcelConstants.COLUMNS, execlColumn);
		 mv.addObject(SelfExcelConstants.DATA_LIST, result);
		 mv.addObject(SelfExcelConstants.TIMEFORMAT,timeFormat);
//        mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");
		 return mv;

	 }

	 private List<Map<String,Object>> keyToLowerCase(List<Map<String,Object>> oldList){
		 List<Map<String,Object>> newList = new ArrayList<>();
		 for(Map<String,Object> record:oldList) {
			 Map<String,Object> resultMap = new HashMap<>();
			 if (record == null || record.isEmpty()){
				 resultMap = record;
			 }
			 Set<String> keySet = record.keySet();
			 for (String key:keySet){
				 String newKey = key.toLowerCase();
				 resultMap.put(newKey, record.get(key));
			 }
			 newList.add(resultMap);
		 }
	 	return newList;
	 }

	 private String getFields(List<Column> columns) {
		 String field = "";
		 for(Column column:columns){
			 for(Column c:column.getChildren())
				 field+=c.getDataIndex()+",";
		 }
		 field = field.substring(0,field.length()-1);
		 return field;
	 }


	 private List<Column> queryRealTimeColumns( List<SysPollutionCode> sysPollutionCodes) {
		 List<Column> columns = null;
		 if (CollectionUtil.isNotEmpty(sysPollutionCodes)) {
			 columns = new ArrayList<>();
			 for (SysPollutionCode sysPollutionCode : sysPollutionCodes) {
				 Column column = new Column();
				 String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
				 column.setTitle(sysPollutionCode.getMeaning() + "(" + chromaUnit + ")");
				 List<Column> childColumns = new ArrayList<>();
				 Column realTime = new Column();
				 realTime.setTitle("实时值");
				 realTime.setDataIndex(sysPollutionCode.getCode().toLowerCase() + "_rtd");
				 childColumns.add(realTime);
				 column.setChildren(childColumns);
				 columns.add(column);
			 }
		 }
		 return columns;
	 }
	 private List<Column> queryVocColumns( List<SysPollutionCode> sysPollutionCodes) {
		 List<Column> columns = null;
		 if (CollectionUtil.isNotEmpty(sysPollutionCodes)) {
			 columns = new ArrayList<>();
			 for (SysPollutionCode sysPollutionCode : sysPollutionCodes) {
				 Column column = new Column();
				 column.setTitle(sysPollutionCode.getMeaning() );
				 List<Column> childColumns = new ArrayList<>();
				 Column avg = new Column();
				 avg.setTitle("平均值（L/s）");
				 avg.setDataIndex(sysPollutionCode.getCode().toLowerCase() + "_avg");
				 avg.setWidth(100);
				 childColumns.add(avg);
				 Column max = new Column();
				 max.setTitle("最大值（L/s）");
				 max.setDataIndex(sysPollutionCode.getCode().toLowerCase() + "_max");
				 max.setWidth(100);
				 childColumns.add(max);
				 Column min = new Column();
				 min.setTitle("最小值（L/s）");
				 min.setDataIndex(sysPollutionCode.getCode().toLowerCase() + "_min");
				 min.setWidth(100);
				 childColumns.add(min);
				 Column cou = new Column();
				 cou.setTitle("排放量（吨）");
				 cou.setDataIndex(sysPollutionCode.getCode().toLowerCase() + "_cou");
				 cou.setWidth(100);
				 childColumns.add(cou);
				 column.setChildren(childColumns);
				 columns.add(column);
			 }
		 }
		 return columns;
	 }

 }
