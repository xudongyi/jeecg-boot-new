package org.jeecg.modules.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.utils.Util;
import org.jeecg.modules.business.utils.Zpage;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.entity.SysPollutionCode;
import org.jeecg.modules.business.view.SelfEntityExcelView;
import org.jeecg.modules.business.view.SelfMapExcelView;
import org.jeecg.modules.business.vo.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Api(tags = "污染源 -废水")
@RestController
@RequestMapping("/psoam/Water")
@Slf4j
public class WaterHistoryController {


    @Autowired
    private IWaterCurrentTrService waterCurrentTrService;
    @Autowired
    private IWaterMinuteService waterMinuteService;
    @Autowired
    private IWaterHourService waterHourService;
    @Autowired
    private IWaterDayService waterDayService;
    @Autowired
    private ISysPollutionCodeService sysPollutionCodeService;
    @Autowired
    private ISysDictService sysDictService;

    @AutoLog(value = "查询废水历史数据")
    @ApiOperation(value = "查询废水历史数据", notes = "实时，分钟，小时，日数据")
    @GetMapping(value = "/queryWaterColumns")
    public Result<?> queryWaterColumns( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
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
        List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.queryCode(area, companyId, mn, "0");

        companyIds = StrUtil.isEmpty(companyId)?companyIds:companyId; //可以查询到的企业
        List<String> companyIDs = Arrays.asList(companyIds.split(","));
        Page<Map<String,Object>> page = new Page<>(pageNo, pageSize);
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
            String tableName = Util.getTableName("water_current_tr_",dataTime_begin);
            Zpage<Map<String,Object>> result= new Zpage(waterCurrentTrService.queryRealTime(page,field,tableName,companyIDs,area,mn,dataTime_begin,dataTime_end));
            result.setColumns(columns);
            return Result.ok(result);
        }
        //拼接  查询字段  companyName monName dataTime  必查询的字段
        //根据启用污染因子拼接动态表头
        List<Column> columns = queryWaterColumns(sysPollutionCodes);
        String field = getFields(columns);
        Zpage<Map<String,Object>> result;
        if(PollutionSource.DataType.MINUTE.equals(dataType))
        {
            String tableName = Util.getTableName("water_minute_",dataTime_begin);
            result= new Zpage(waterMinuteService.queryMinute(page,field,tableName,companyIDs,area,mn,dataTime_begin,dataTime_end));
        }
        else if(PollutionSource.DataType.HOUR.equals(dataType)){
            result = new Zpage(waterHourService.queryHour(page,field,companyIDs,area,mn,dataTime_begin,dataTime_end));
        }
        else
        {
            result= new Zpage(waterDayService.queryDay(page,field,companyIDs,area,mn,dataTime_begin,dataTime_end));
        }
        result.setColumns(columns);
        return Result.ok(result);
    }
    @AutoLog(value = "查询废水历史数据")
    @ApiOperation(value = "查询废水历史数据", notes = "实时，分钟，小时，日数据")
    @GetMapping(value = "/exportWaterHistory")
    public ModelAndView exportWaterHistory(HttpServletRequest req) {

        String area = req.getParameter("area");
        String companyId = req.getParameter("companyId");
        String companyIds = req.getParameter("companyIds");
        String mn = req.getParameter("mn");
        String dataType = req.getParameter("dataType");
        String dataTime_begin = req.getParameter("dataTime_begin");
        String dataTime_end = req.getParameter("dataTime_end");
        //查询启用的污染因子   污水
        List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.queryCode(area, companyId, mn, "0");

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
            String tableName = Util.getTableName("water_current_tr_", dataTime_begin);
            result = waterCurrentTrService.queryRealTime(field, tableName, companyIDs, area, mn, dataTime_begin, dataTime_end);
            timeFormat = PollutionSource.DataFormat.REALTIME;
        }else{
            columns = queryWaterColumns(sysPollutionCodes);
            String field = getFields(columns);

            if (PollutionSource.DataType.MINUTE.equals(dataType)) {
                timeFormat = PollutionSource.DataFormat.MINUTE;
                String tableName = Util.getTableName("water_minute_", dataTime_begin);
                result = waterMinuteService.queryMinute( field, tableName, companyIDs, area, mn, dataTime_begin, dataTime_end);
            } else if (PollutionSource.DataType.HOUR.equals(dataType)) {
                timeFormat = PollutionSource.DataFormat.HOUR;

                result = waterHourService.queryHour(field, companyIDs, area, mn, dataTime_begin, dataTime_end);
            } else {
                timeFormat = PollutionSource.DataFormat.DAY;

                result = waterDayService.queryDay( field, companyIDs, area, mn, dataTime_begin, dataTime_end);
            }
        }
        //columns  需要增加前面的固定列
        List<Column> execlColumn = new ArrayList<>();
        execlColumn.add(new Column("企业名称","company_name"));
        execlColumn.add(new Column("监测点名称","site_name"));
        execlColumn.add(new Column("时间","data_time"));
        execlColumn.addAll(columns);
        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new SelfMapExcelView());
        mv.addObject(SelfExcelConstants.TITLE, "历史数据（废水）"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(SelfExcelConstants.SHEET_NAME, "历史数据（废水）");
        mv.addObject(SelfExcelConstants.COLUMNS, execlColumn);
        mv.addObject(SelfExcelConstants.DATA_LIST, result);
        mv.addObject(SelfExcelConstants.TIMEFORMAT,timeFormat);
//        mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");
        return mv;

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
                if ("w00000".equalsIgnoreCase(sysPollutionCode.getCode())) {
                    Column columnq = new Column();
                    String amountUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getAmountUnit());
                    columnq.setTitle("污水排放量(" + amountUnit + ")");
                    columnq.setDataIndex("w00000_total");
                    columns.add(columnq);
                    String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
                    column.setTitle(sysPollutionCode.getMeaning() + "(" + chromaUnit + ")");
                    column.setDataIndex(sysPollutionCode.getCode() + "_rtd");
                    columns.add(column);
                } else {
                    String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
                    column.setTitle(sysPollutionCode.getMeaning() + "(" + chromaUnit + ")");
                    column.setDataIndex(sysPollutionCode.getCode() + "_rtd");
                    columns.add(column);
                }
            }
        }
        return columns;
    }
    private List<Column> queryWaterColumns( List<SysPollutionCode> sysPollutionCodes) {
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
