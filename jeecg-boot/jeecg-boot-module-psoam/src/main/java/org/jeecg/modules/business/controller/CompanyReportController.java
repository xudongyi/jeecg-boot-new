package org.jeecg.modules.business.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Joiner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.utils.ExcelUtil;
import org.jeecg.modules.business.view.SelfMapExcelView;
import org.jeecg.modules.business.vo.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Api(tags = "company_acceptance")
@RestController
@RequestMapping("/psoam/companyReport")
@Slf4j
public class CompanyReportController {

    @Autowired
    private ISysPollutionCodeService sysPollutionCodeService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private IWaterHourService waterHourService;
    @Autowired
    private IWaterDayService waterDayService;
    @Autowired
    private IWaterMonthService waterMonthService;

    @Autowired
    private IAirHourService airHourService;
    @Autowired
    private IAirDayService airDayService;
    @Autowired
    private IAirMonthService airMonthService;

    @Autowired
    private IVocHourService vocHourService;
    @Autowired
    private IVocDayService vocDayService;
    @Autowired
    private IVocMonthService vocMonthService;

    @Autowired
    private ISiteMonitorPointService siteMonitorPointService;

    @AutoLog(value = "查询企业-站点报表")
    @ApiOperation(value = "查询企业-站点报表", notes = "日、月、季度、年报表")
    @GetMapping(value = "/queryWaterReport")
    public Result<?> queryWaterReport(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                       HttpServletRequest req) {

        String  area = req.getParameter("area");
        String  companyId = req.getParameter("companyId");
        String  mn = req.getParameter("mn");
        String  dataType = req.getParameter("dataType");
        String  dataTime = req.getParameter("dataTime");

        //查询启用的污染因子   污水
        List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.queryCode(area, companyId, mn, "0");
        //组装 前端 表头
        if(sysPollutionCodes.size()<1){
            return Result.error(500,"不存在可查询的污染因子，请寻找管理员开启");
        }
        //拼接  查询字段  companyName monName dataTime  必查询的字段
        //根据启用污染因子拼接动态表头
        List<Column> columns = bulidUpColumns(sysPollutionCodes);
        //查询站点类型

        String siteType = siteMonitorPointService.getOne(
                new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getMn,mn)).getSiteType();
        Map<String,Object> result = new HashMap<>();
        result.put("columns",columns);
        if("0".equals(siteType))//废水
            waterReport(req, mn, dataType, dataTime, result);
        else if("1".equals(siteType))//废气
            airReport(req, mn, dataType, dataTime, result);
        else if("2".equals(siteType))//VOCS
            vocsReport(req, mn, dataType, dataTime, result);
/********************* 暂不用动态数据查询表格  table查询按*来 *************************/
//        List<String> dataIndexs = new ArrayList<>();
//        ExcelUtil.columnStatistics(columns,dataIndexs);
//        String fields = Joiner.on(",").join(dataIndexs);
        //查询

        return Result.ok(result);
    }
    @AutoLog(value = "导出企业-站点报表")
    @ApiOperation(value = "导出企业-站点报表", notes = "日、月、季度、年报表")
    @GetMapping(value = "/exportWaterReport")
    public ModelAndView exportWaterReport(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                      @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                      HttpServletRequest req) {

        String  area = req.getParameter("area");
        String  companyId = req.getParameter("companyId");
        String  mn = req.getParameter("mn");
        String  dataType = req.getParameter("dataType");
        String  dataTime = req.getParameter("dataTime");

        //查询启用的污染因子   污水
        List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.queryCode(area, companyId, mn, "0");

        //拼接  查询字段  companyName monName dataTime  必查询的字段
        //根据启用污染因子拼接动态表头
        List<Column> columns = bulidUpColumns(sysPollutionCodes);
        //查询站点类型

        String siteType = siteMonitorPointService.getOne(
                new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getMn,mn)).getSiteType();
        Map<String,Object> result = new HashMap<>();
        result.put("columns",columns);
        if("0".equals(siteType))//废水
            waterReport(req, mn, dataType, dataTime, result);
        else if("1".equals(siteType))//废气
            airReport(req, mn, dataType, dataTime, result);
        else if("2".equals(siteType))//VOCS
            vocsReport(req, mn, dataType, dataTime, result);

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new SelfMapExcelView());
        mv.addObject(SelfExcelConstants.TITLE, "历史数据（废水）"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(SelfExcelConstants.SHEET_NAME, "历史数据（废水）");
        mv.addObject(SelfExcelConstants.COLUMNS, columns);
        mv.addObject(SelfExcelConstants.DATA_LIST, result.get("records"));
//        mv.addObject(SelfExcelConstants.TIMEFORMAT,timeFormat);

        return mv;
    }

    private void waterReport(HttpServletRequest req, String mn, String dataType, String dataTime, Map<String, Object> result) {
        StartEndTime startEndTime = new StartEndTime();
        List<Map<String,Object>> records;
        //日期表
        if(PollutionSource.ReportDateType.DATE.equals(dataType)){
            parseSartEndTime(dataTime, startEndTime, "yyyy-MM-dd", dataType);
            records = waterHourService.listMaps(new QueryWrapper<WaterHour>().lambda()
                    .eq(WaterHour::getMn,mn).between(WaterHour::getDataTime,startEndTime.getBegin(),startEndTime.getEnd()).orderByAsc(WaterHour::getDataTime));
            records = hourTimeHandler(records,result);

        }
        //月份表
        else if(PollutionSource.ReportDateType.MONTH.equals(dataType)){
            parseSartEndTime(dataTime, startEndTime, "yyyy-MM", dataType);
            records = waterDayService.listMaps(new QueryWrapper<WaterDay>().lambda()
                    .eq(WaterDay::getMn,mn).between(WaterDay::getDataTime,startEndTime.getBegin(),startEndTime.getEnd()).orderByAsc(WaterDay::getDataTime));
            records = dayHourHandler(records,result);
        }
        else{
            //季度
            if(PollutionSource.ReportDateType.QUARTERLY.equals(dataType))
                quarterlyStartEndTime(req, dataTime, startEndTime);
            else //Year 年
                parseSartEndTime(dataTime, startEndTime, "yyyy", dataType);
            records = waterMonthService.listMaps(new QueryWrapper<WaterMonth>().lambda()
                    .eq(WaterMonth::getMn,mn).between(WaterMonth::getDataTime,startEndTime.getBegin(),startEndTime.getEnd()).orderByAsc(WaterMonth::getDataTime));
            records = monthTimeHandler(records,result);
        }
        result.put("records",records) ;
    }
    private void vocsReport(HttpServletRequest req, String mn, String dataType, String dataTime, Map<String, Object> result) {
        StartEndTime startEndTime = new StartEndTime();
        List<Map<String,Object>> records;
        //日期表
        if(PollutionSource.ReportDateType.DATE.equals(dataType)){
            parseSartEndTime(dataTime, startEndTime, "yyyy-MM-dd", dataType);
            records = vocHourService.listMaps(new QueryWrapper<VocHour>().lambda()
                    .eq(VocHour::getMn,mn).between(VocHour::getDataTime,startEndTime.getBegin(),startEndTime.getEnd()).orderByAsc(VocHour::getDataTime));
            records = hourTimeHandler(records,result);

        }
        //月份表
        else if(PollutionSource.ReportDateType.MONTH.equals(dataType)){
            parseSartEndTime(dataTime, startEndTime, "yyyy-MM", dataType);
            records = vocDayService.listMaps(new QueryWrapper<VocDay>().lambda()
                    .eq(VocDay::getMn,mn).between(VocDay::getDataTime,startEndTime.getBegin(),startEndTime.getEnd()).orderByAsc(VocDay::getDataTime));
            records = dayHourHandler(records,result);
        }
        else{
            //季度
            if(PollutionSource.ReportDateType.QUARTERLY.equals(dataType))
                quarterlyStartEndTime(req, dataTime, startEndTime);
            else //Year 年
                parseSartEndTime(dataTime, startEndTime, "yyyy", dataType);
            records = vocMonthService.listMaps(new QueryWrapper<VocMonth>().lambda()
                    .eq(VocMonth::getMn,mn).between(VocMonth::getDataTime,startEndTime.getBegin(),startEndTime.getEnd()).orderByAsc(VocMonth::getDataTime));
            records = monthTimeHandler(records,result);
        }
        result.put("records",records) ;
    }
    private void airReport(HttpServletRequest req, String mn, String dataType, String dataTime, Map<String, Object> result) {
        StartEndTime startEndTime = new StartEndTime();
        List<Map<String,Object>> records;
        //日期表
        if(PollutionSource.ReportDateType.DATE.equals(dataType)){
            parseSartEndTime(dataTime, startEndTime, "yyyy-MM-dd",dataType);
            records = airHourService.listMaps(new QueryWrapper<AirHour>().lambda()
                    .eq(AirHour::getMn,mn).between(AirHour::getDataTime,startEndTime.getBegin(),startEndTime.getEnd()).orderByAsc(AirHour::getDataTime));
            records = hourTimeHandler(records,result);
        }
        //月份表
        else if(PollutionSource.ReportDateType.MONTH.equals(dataType)){
            parseSartEndTime(dataTime, startEndTime, "yyyy-MM",dataType);
            records = airDayService.listMaps(new QueryWrapper<AirDay>().lambda()
                    .eq(AirDay::getMn,mn).between(AirDay::getDataTime,startEndTime.getBegin(),startEndTime.getEnd()).orderByAsc(AirDay::getDataTime));
            records = dayHourHandler(records,result);
        }
        else{
            //季度
            if(PollutionSource.ReportDateType.QUARTERLY.equals(dataType))
                quarterlyStartEndTime(req, dataTime, startEndTime);
            else //Year 年
                parseSartEndTime(dataTime, startEndTime, "yyyy",dataType);
            records = airMonthService.listMaps(new QueryWrapper<AirMonth>().lambda()
                    .eq(AirMonth::getMn,mn).between(AirMonth::getDataTime,startEndTime.getBegin(),startEndTime.getEnd()).orderByAsc(AirMonth::getDataTime));
            records =  monthTimeHandler(records,result);
        }
        result.put("records",records) ;
    }

    private void quarterlyStartEndTime(HttpServletRequest req, String dataTime, StartEndTime startEndTime) {
        int quarterly = Integer.parseInt(req.getParameter("quarterly"));
        startEndTime.setBegin(DateUtil.offsetMonth(DateUtil.parse(dataTime,"yyyy"),quarterly*3-3));
        startEndTime.setEnd(DateUtil.endOfMonth(DateUtil.offsetMonth(startEndTime.getBegin(),quarterly*3-1)));
    }

    private void parseSartEndTime(String dataTime, StartEndTime startEndTime, String s,String dataType) {
        //当天所有时间
        startEndTime.setBegin(DateUtil.parse(dataTime, s));
        if(PollutionSource.ReportDateType.DATE.equals(dataType)) {
            startEndTime.setEnd(DateUtil.offsetDay(startEndTime.getBegin(), 1));
        } else if(PollutionSource.ReportDateType.MONTH.equals(dataType)) {
            startEndTime.setEnd(DateUtil.endOfMonth(startEndTime.getBegin()));
        }else{
            startEndTime.setEnd( DateUtil.endOfYear(startEndTime.getBegin()));
        }

    }
    private List<Map<String, Object>> monthTimeHandler(List<Map<String, Object>> records,Map<String, Object> resultMap) {
        List<String> dataIndexs = new ArrayList<>();
        ExcelUtil.columnStatistics( (List<Column>)resultMap.get("columns"),dataIndexs);
        Map<String,Object> max = new HashMap<>();
        Map<String,Object> min = new HashMap<>();
        Map<String,Object> avg = new HashMap<>();
        Map<String,Object> cou = new HashMap<>();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> param : records) {
            Map<String,Object> record = keyToUpperCase(param);
            Date date = (Date) record.get("DATA_TIME");
            record.put("DATA_TIME", DateUtil.month(date) + "月");
            result.add(record);
            statistics(records, dataIndexs, max, min, avg, cou, record);
        }
        result.add(max);
        result.add(min);
        result.add(avg);
        result.add(cou);
        return result;
    }

    /**
     *  处理时间和平均值 最小值 最大值  总量数据
     * @param records
     * @param resultMap
     * @return
     */
    private List<Map<String, Object>> dayHourHandler(List<Map<String, Object>> records,Map<String, Object> resultMap) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> dataIndexs = new ArrayList<>();
        ExcelUtil.columnStatistics( (List<Column>)resultMap.get("columns"),dataIndexs);
        Map<String,Object> max = new HashMap<>();
        Map<String,Object> min = new HashMap<>();
        Map<String,Object> avg = new HashMap<>();
        Map<String,Object> cou = new HashMap<>();
        //平均值
        for(Map<String,Object> param: records){
            Map<String,Object> record = keyToUpperCase(param);
            Date date = (Date)record.get("DATA_TIME");
            record.put("DATA_TIME", DateUtil.dayOfMonth(date));
            result.add(record);
            statistics(records, dataIndexs, max, min, avg, cou, record);

        }
        result.add(max);
        result.add(min);
        result.add(avg);
        result.add(cou);
        return result;
    }

    private List<Map<String, Object>> hourTimeHandler(List<Map<String, Object>> records,Map<String, Object> resultMap) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> dataIndexs = new ArrayList<>();
        ExcelUtil.columnStatistics( (List<Column>)resultMap.get("columns"),dataIndexs);
        Map<String,Object> max = new HashMap<>();
        Map<String,Object> min = new HashMap<>();
        Map<String,Object> avg = new HashMap<>();
        Map<String,Object> cou = new HashMap<>();
        //dataTime  处理
        for(Map<String,Object> param: records){
            Map<String,Object> record = keyToUpperCase(param);
            Date date = (Date)record.get("DATA_TIME");
            int endHour = DateUtil.hour(date,true)==0?24:DateUtil.hour(date,true);
            record.put("DATA_TIME",endHour-1+"~"+endHour);
            result.add(record);
            statistics(records, dataIndexs, max, min, avg, cou, record);

        }
        result.add(max);
        result.add(min);
        result.add(avg);
        result.add(cou);
        return result;
    }
    private void statistics(List<Map<String, Object>> records, List<String> dataIndexs, Map<String, Object> max, Map<String, Object> min, Map<String, Object> avg, Map<String, Object> cou, Map<String, Object> param) {
        //第一次
        if(max.isEmpty()){
            for(String dataindex:dataIndexs)
            {
                if(dataindex.equals("DATA_TIME"))
                {
                    max.put(dataindex,"最大值");
                    min.put(dataindex,"最小值");
                    avg.put(dataindex,"平均值");
                    cou.put(dataindex,"排放总量（kg）");
                }else{
                    max.put(dataindex,param.get(dataindex));
                    min.put(dataindex,param.get(dataindex));
                    avg.put(dataindex,param.get(dataindex));
                    if(dataindex.endsWith("COU"))
                        cou.put(dataindex,param.get(dataindex));
                    else
                        cou.put(dataindex,"-");
                }
            }

        }else{
            for(String dataindex:dataIndexs) {
                if(!dataindex.equals("DATA_TIME")) {
                    max.put(dataindex, Math.max(Double.parseDouble(max.get(dataindex).toString())
                            , Double.parseDouble(param.get(dataindex).toString())));
                    min.put(dataindex, Math.min(Double.parseDouble(min.get(dataindex).toString())
                            , Double.parseDouble(param.get(dataindex).toString())));
                    avg.put(dataindex, new BigDecimal(param.get(dataindex).toString())
                            .add(new BigDecimal(avg.get(dataindex).toString())).doubleValue());
                    if (dataindex.endsWith("COU"))
                        cou.put(dataindex, new BigDecimal(param.get(dataindex).toString())
                                .add(new BigDecimal(cou.get(dataindex).toString())).doubleValue());
                    else
                        cou.put(dataindex, "-");
                }
            }
            for (Map.Entry<String,Object> entry:avg.entrySet()){
                if(!entry.getKey().equals("DATA_TIME")){
                    avg.put(entry.getKey(),new BigDecimal(entry.getValue().toString())
                            .divide(BigDecimal.valueOf(records.size()),2,BigDecimal.ROUND_HALF_UP));
                }
            }
        }
    }

    //转大写
    private  Map<String, Object> keyToUpperCase(Map<String, Object> orgMap) {
        Map<String, Object> resultMap = new HashMap<>();

        if (orgMap == null || orgMap.isEmpty()) {
            return resultMap;
        }

        Set<Map.Entry<String,Object>> entrySet = orgMap.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();
            resultMap.put(key.toUpperCase(), value);
        }

        return resultMap;
    }



    private List<Column>  bulidUpColumns( List<SysPollutionCode> sysPollutionCodes){
        List<Column> result = new ArrayList<>();
        result.add(new Column("时间","DATA_TIME"));
        for(SysPollutionCode sysPollutionCode:sysPollutionCodes){
            Column column = new Column();
            column.setTitle(sysPollutionCode.getMeaning());
            List<Column> childColumns = new ArrayList<>();
            if (!StrUtil.isEmpty(sysPollutionCode.getAmountUnitMath())) {
                Column childavg = new Column();
                String amountUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getAmountUnitMath());
                childavg.setTitle("平均浓度（" + amountUnit + "）");
                childavg.setDataIndex(sysPollutionCode.getCode().toUpperCase() + "_AVG");
                childColumns.add(childavg);
                //是否有折算值
                if ("Y".equals(sysPollutionCode.getIsZs())) {
                    Column childzs = new Column();
                    childavg.setTitle("实测平均浓度（" + amountUnit + "）");
                    childzs.setTitle("折算平均浓度（" + amountUnit + "）");
                    childzs.setDataIndex(sysPollutionCode.getCode().toUpperCase() + "_ZSAVG");
                    childColumns.add(childzs);
                }
                //是否有排放量
                if (StrUtil.isEmpty(sysPollutionCode.getChromaUnitMath())) {
                    Column childcou = new Column();
                    String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnitMath());
                    childcou.setTitle("排放量（" + chromaUnit + "）");
                    childcou.setDataIndex(sysPollutionCode.getCode().toUpperCase() + "_COU");
                    childColumns.add(childcou);
                }

                column.setChildren(childColumns);
            }else{
                String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnitMath());
                column.setTitle(sysPollutionCode.getMeaning()+"（" + chromaUnit + "）");
                column.setDataIndex(sysPollutionCode.getCode().toUpperCase() + "_COU");
            }
            result.add(column);
        }
        return result;
    }
    @Data
    private class StartEndTime{
        DateTime begin;
        DateTime end;
    }
}
