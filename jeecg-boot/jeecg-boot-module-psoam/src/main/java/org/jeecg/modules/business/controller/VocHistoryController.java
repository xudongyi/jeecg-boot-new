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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.service.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.business.utils.Util;
import org.jeecg.modules.business.utils.Zpage;
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
	 public Result<?> querySiteNameAndMn(@RequestParam(name = "companyIds", required = true) String companyIds,@RequestParam(name = "siteType", required = true) String siteType) {
		 List<String> idList = Arrays.asList(companyIds.split(","));
		 List<Map<String, String>> result = new ArrayList<>();
		 siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getSiteType, siteType).in(SiteMonitorPoint::getCompanyId, idList)).forEach(siteMonitorPoint -> {
			 Map<String, String> param = new HashMap<>();
			 param.put("key", siteMonitorPoint.getMn());
			 param.put("value", siteMonitorPoint.getSiteName());
			 param.put("siteId", siteMonitorPoint.getId());
			 param.put("area", siteMonitorPoint.getArea());
			 param.put("companyId", siteMonitorPoint.getCompanyId());
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
				 field+=column.getDataIndex()+","+column.getDataIndex()+"_state,";
			 }
			 field = field.substring(0,field.length()-1).replaceAll("rtd_","");
			 String tableName = Util.getTableName("voc_current_tr_",dataTime_begin);
			 Zpage<List<Map<String,Object>>> result= new Zpage(vocCurrentTrService.queryRealTime(page,field,tableName,companyIDs,area,mn,dataTime_begin,dataTime_end));
			 result.setColumns(columns);
			 return Result.ok(result);
		 }
		 //拼接  查询字段  companyName monName dataTime  必查询的字段
		 //根据启用污染因子拼接动态表头
		 List<Column> columns = queryVocColumns(sysPollutionCodes);
		 String field = getFields(columns);
		 Zpage<List<Map<String,Object>>> result;
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
		 return Result.ok(result);
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
				 column.setDataIndex(sysPollutionCode.getCode() + "_rtd");
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
				 avg.setDataIndex(sysPollutionCode.getCode() + "_avg");
				 childColumns.add(avg);
				 Column max = new Column();
				 max.setTitle("最大值（L/s）");
				 max.setDataIndex(sysPollutionCode.getCode() + "_max");
				 childColumns.add(max);
				 Column min = new Column();
				 min.setTitle("最小值（L/s）");
				 min.setDataIndex(sysPollutionCode.getCode() + "_min");
				 childColumns.add(min);
				 Column cou = new Column();
				 cou.setTitle("排放量（吨）");
				 cou.setDataIndex(sysPollutionCode.getCode() + "_cou");
				 childColumns.add(cou);
				 column.setChildren(childColumns);
				 columns.add(column);
			 }
		 }
		 return columns;
	 }

 }
