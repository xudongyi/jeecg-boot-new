package org.jeecg.modules.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.SysPollutionCode;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.utils.Util;
import org.jeecg.modules.business.utils.Zpage;
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

@Api(tags = "污染源 -废气")
@RestController
@RequestMapping("/psoam/air")
@Slf4j
public class AirHistoryController {


    @Autowired
    private IAirCurrentTrService airCurrentTrService;
    @Autowired
    private IAirMinuteService airMinuteService;
    @Autowired
    private IAirHourService airHourService;
    @Autowired
    private IAirDayService airDayService;
    @Autowired
    private ISysPollutionCodeService sysPollutionCodeService;
    @Autowired
    private ISysDictService sysDictService;

    private Integer width = 150;

    @AutoLog(value = "查询废气历史数据")
    @ApiOperation(value = "查询废气历史数据", notes = "实时，分钟，小时，日数据")
    @GetMapping(value = "/queryAirHistory")
    public Result<?> queryAirHistory( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
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
        List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.queryCode(area, companyId, mn, "1");

        companyIds = StrUtil.isEmpty(companyId)?companyIds:companyId; //可以查询到的企业
        List<String> companyIDs = Arrays.asList(companyIds.split(","));
        Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNo, pageSize);
        if(sysPollutionCodes.size()<1){
            return Result.error(500,"不存在可查询的污染因子，请寻找管理员开启");
        }
        //都为空  默认查询 所有站点最近一条日数据
        if(PollutionSource.DataType.REALTIME.equals(dataType))
        {
            //拼接  查询字段  companyName monName dataTime  必查询的字段
            //根据启用污染因子拼接动态表头
            List<Column> columns = this.queryRealTimeColumns(sysPollutionCodes);
            String field = "";
            /*
            for(Column column:columns){
                field+=column.getDataIndex()+","+column.getDataIndex()+"_STATE,";
            }
            field = field.substring(0,field.length()-1).replaceAll("rtd_","");*/
            String tableName = Util.getTableName("air_current_tr_",dataTime_begin);
            Zpage<Map<String,Object>> result= new Zpage(airCurrentTrService.queryRealTime(page,field,tableName,companyIDs,area,mn,dataTime_begin,dataTime_end));
            result.setColumns(columns);
            return Result.ok(result);
        }
        //拼接  查询字段  companyName monName dataTime  必查询的字段
        //根据启用污染因子拼接动态表头
        List<Column> columns = queryAirColumns(sysPollutionCodes);
        String field = getFields(columns);
        Zpage<List<Map<String,Object>>> result;
        if(PollutionSource.DataType.MINUTE.equals(dataType))
        {
            String tableName = Util.getTableName("air_minute_",dataTime_begin);
            result= new Zpage(airMinuteService.queryMinute(page,field,tableName,companyIDs,area,mn,dataTime_begin,dataTime_end));
        }
        else if(PollutionSource.DataType.HOUR.equals(dataType)){
            result = new Zpage(airHourService.queryHour(page,field,companyIDs,area,mn,dataTime_begin,dataTime_end));
        }
        else
        {
            result= new Zpage(airDayService.queryDay(page,field,companyIDs,area,mn,dataTime_begin,dataTime_end));
        }
        result.setColumns(columns);
        return Result.ok(result);
    }

    @AutoLog(value = "导出废气历史数据")
    @ApiOperation(value = "导出废气历史数据", notes = "实时，分钟，小时，日数据")
    @GetMapping(value = "/exportAirHistory")
    public ModelAndView exportAirHistory(HttpServletRequest req) {

        String area = req.getParameter("area");
        String companyId = req.getParameter("companyId");
        String companyIds = req.getParameter("companyIds");
        String mn = req.getParameter("mn");
        String dataType = req.getParameter("dataType");
        String dataTime_begin = req.getParameter("dataTime_begin");
        String dataTime_end = req.getParameter("dataTime_end");
        //查询启用的污染因子   污水
        List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.queryCode(area, companyId, mn, "1");

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
          /*  for (Column column : columns) {
                field += column.getDataIndex() + "," + column.getDataIndex() + "_state,";
            }
            field = field.substring(0, field.length() - 1).replaceAll("rtd_", "");*/
            String tableName = Util.getTableName("air_current_tr_", dataTime_begin);
            result = airCurrentTrService.queryRealTime(field, tableName, companyIDs, area, mn, dataTime_begin, dataTime_end);
            timeFormat = PollutionSource.DataFormat.REALTIME;
        }else{
            columns = queryAirColumns(sysPollutionCodes);
            String field = getFields(columns);

            if (PollutionSource.DataType.MINUTE.equals(dataType)) {
                timeFormat = PollutionSource.DataFormat.MINUTE;
                String tableName = Util.getTableName("air_minute_", dataTime_begin);
                result = airMinuteService.queryMinute( field, tableName, companyIDs, area, mn, dataTime_begin, dataTime_end);
            } else if (PollutionSource.DataType.HOUR.equals(dataType)) {
                timeFormat = PollutionSource.DataFormat.HOUR;

                result = airHourService.queryHour(field, companyIDs, area, mn, dataTime_begin, dataTime_end);
            } else {
                timeFormat = PollutionSource.DataFormat.DAY;

                result = airDayService.queryDay( field, companyIDs, area, mn, dataTime_begin, dataTime_end);
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
        mv.addObject(SelfExcelConstants.TITLE, "历史数据（废气）"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(SelfExcelConstants.SHEET_NAME, "历史数据（废气）");
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

    /**
    * @Description: 空气实时表头
    * @Param:
    * @return:
    * @Author: 周志远
    * @Date: 2020/9/7
    */
    private List<Column> queryRealTimeColumns( List<SysPollutionCode> sysPollutionCodes) {
        List<Column> columns = null;
        if (CollectionUtil.isNotEmpty(sysPollutionCodes)) {
            columns = new ArrayList<>();
            for (SysPollutionCode sysPollutionCode : sysPollutionCodes) {
                List<Column> childColumns = new ArrayList<>();
                Column column = new Column();
                Column childColumn = new Column();
                String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
                column.setTitle(sysPollutionCode.getMeaning() + "(" + chromaUnit + ")");
                childColumn.setTitle("实时值");
                childColumn.setDataIndex(sysPollutionCode.getCode() + "_RTD");
                childColumns.add(childColumn);
                if ("Y".equalsIgnoreCase(sysPollutionCode.getIsZs())) {
                    Column childColumnZs = new Column();
                    childColumnZs.setTitle("折算实时值");
                    childColumnZs.setDataIndex(sysPollutionCode.getCode() + "_ZSRTD");
                    childColumnZs.setWidth(width);
                    childColumns.add(childColumnZs);
                }
                column.setChildren(childColumns);
                columns.add(column);
            }
        }
        return columns;
    }

    /**
     * @Description: 空气小时、分钟、日表头
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/9/7
     */
    private List<Column> queryAirColumns(List<SysPollutionCode> sysPollutionCodes) {
        List<Column> columns = null;
        if (CollectionUtil.isNotEmpty(sysPollutionCodes)) {
            columns = new ArrayList<>();
            for (SysPollutionCode sysPollutionCode : sysPollutionCodes) {
                String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
                Column column = new Column();
                column.setTitle(sysPollutionCode.getMeaning() );
                List<Column> childColumns = new ArrayList<>();
                if("Y".equalsIgnoreCase(sysPollutionCode.getIsZs())){
                    Column zsAvg = new Column();
                    zsAvg.setTitle("折算平均值（"+chromaUnit+"）");
                    zsAvg.setDataIndex(sysPollutionCode.getCode() + "_ZSAVG");
                    zsAvg.setWidth(width);
                    childColumns.add(zsAvg);
                    Column zsMax = new Column();
                    zsMax.setTitle("折算最大值（"+chromaUnit+"）");
                    zsMax.setDataIndex(sysPollutionCode.getCode() + "_ZSMAX");
                    zsMax.setWidth(width);
                    childColumns.add(zsMax);
                    Column zsMin = new Column();
                    zsMin.setTitle("折算最小值（"+chromaUnit+"）");
                    zsMin.setDataIndex(sysPollutionCode.getCode() + "_ZSMIN");
                    zsMin.setWidth(width);
                    childColumns.add(zsMin);
                }else{
                    Column avg = new Column();
                    avg.setTitle("平均值（"+chromaUnit+"）");
                    avg.setDataIndex(sysPollutionCode.getCode() + "_AVG");
                    avg.setWidth(width);
                    childColumns.add(avg);
                    Column max = new Column();
                    max.setTitle("最大值（"+chromaUnit+"）");
                    max.setDataIndex(sysPollutionCode.getCode() + "_MAX");
                    max.setWidth(width);
                    childColumns.add(max);
                    Column min = new Column();
                    min.setTitle("最小值（"+chromaUnit+"）");
                    min.setDataIndex(sysPollutionCode.getCode() + "_MIN");
                    min.setWidth(width);
                    childColumns.add(min);
                }
                if(!"a19001".equalsIgnoreCase(sysPollutionCode.getCode())){
                    Column cou = new Column();
                    cou.setTitle("排放量（kg）");
                    cou.setDataIndex(sysPollutionCode.getCode() + "_COU");
                    cou.setWidth(width);
                    childColumns.add(cou);
                    column.setChildren(childColumns);
                    columns.add(column);
                }
            }
        }
        return columns;
    }

}
