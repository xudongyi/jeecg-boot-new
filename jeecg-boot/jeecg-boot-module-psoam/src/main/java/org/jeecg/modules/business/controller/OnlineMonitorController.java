package org.jeecg.modules.business.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.entity.SysPollutionCode;
import org.jeecg.modules.business.entity.WaterCurrentTr;
import org.jeecg.modules.business.service.ISiteMonitorPointService;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.business.service.ISysPollutionCodeService;
import org.jeecg.modules.business.service.IWaterCurrentTrService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: water_current_tr
 * @Author: jeecg-boot
 * @Date: 2020-08-31
 * @Version: V1.0
 */
@Api(tags = "在线监测")
@RestController
@RequestMapping("/onlineMonitor")
@Slf4j
public class OnlineMonitorController extends JeecgController<WaterCurrentTr, IWaterCurrentTrService> {
    @Autowired
    private IWaterCurrentTrService waterCurrentTrService;
    @Autowired
    private ISysPollutionCodeService sysPollutionCodeService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISiteMonitorPointService siteMonitorPointService;

    //预警
    List<Integer> warns = Arrays.asList(-2, -3, -4, -5, 5, 4, 3, 2);
    //超标
    List<Integer> standards = Arrays.asList(-1, 1);
    //异常
    List<Integer> abNormals = Arrays.asList(-6, 6,9);
    //正常
    List<Integer> normals = Arrays.asList(0);

    @AutoLog(value = "查询废水表头")
    @ApiOperation(value = "查询废水表头", notes = "查询废水表头")
    @GetMapping(value = "/queryWaterColumns")
    public Result<?> queryWaterColumns(HttpServletRequest req) {
        String area = req.getParameter("area");
        String companyId = req.getParameter("companyId");
        String mn = req.getParameter("mn");
        String type = req.getParameter("type");
        List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.queryCode(area, companyId, mn, type);
        //查询表头
        List<Column> columns = null;
        if (CollectionUtil.isNotEmpty(sysPollutionCodes)) {
            columns = new ArrayList<>();
            for (SysPollutionCode sysPollutionCode : sysPollutionCodes) {
                Column column = new Column();
                if ("w00000".equalsIgnoreCase(sysPollutionCode.getCode())) {
                    Column columnq = new Column();
                    String amountUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getAmountUnit());
                    columnq.setTitle("污水排放量(" + amountUnit + ")");
                    columnq.setDataIndex("W00000_TOTAL");
                    columns.add(columnq);
                    String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
                    column.setTitle(sysPollutionCode.getMeaning() + "(" + chromaUnit + ")");
                    column.setDataIndex(sysPollutionCode.getCode() + "_RTD");
                    columns.add(column);
                } else {
                    String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
                    column.setTitle(sysPollutionCode.getMeaning() + "(" + chromaUnit + ")");
                    column.setDataIndex(sysPollutionCode.getCode() + "_RTD");
                    columns.add(column);
                }
            }
        }

        return Result.ok(columns);
    }

    @AutoLog(value = "查询废气表头")
    @ApiOperation(value = "查询废气表头", notes = "查询废气表头")
    @GetMapping(value = "/queryAirColumns")
    public Result<?> queryAirColumns(HttpServletRequest req) {
        String area = req.getParameter("area");
        String companyId = req.getParameter("companyId");
        String mn = req.getParameter("mn");
        String type = req.getParameter("type");
        List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.queryCode(area, companyId, mn, type);
        List<Column> columns = null;
        if (CollectionUtil.isNotEmpty(sysPollutionCodes)) {
            columns = new ArrayList<>();
            for (SysPollutionCode sysPollutionCode : sysPollutionCodes) {
                List<Column> childColumns = new ArrayList<>();
                Column column = new Column();
                Column childColumn = new Column();
                String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
                column.setTitle(sysPollutionCode.getMeaning() + "(" + chromaUnit + ")");
                childColumn.setTitle("浓度");
                childColumn.setDataIndex(sysPollutionCode.getCode() + "_RTD");
                childColumns.add(childColumn);
                if ("Y".equalsIgnoreCase(sysPollutionCode.getIsZs())) {
                    Column childColumnZs = new Column();
                    childColumnZs.setTitle("折算浓度");
                    childColumnZs.setDataIndex(sysPollutionCode.getCode() + "_ZSRTD");
                    childColumns.add(childColumnZs);
                }
                column.setChildren(childColumns);
                columns.add(column);
            }
        }
        return Result.ok(columns);
    }

    @AutoLog(value = "查询vocs表头")
    @ApiOperation(value = "查询vocs表头", notes = "查询vocs表头")
    @GetMapping(value = "/queryVocsColumns")
    public Result<?> queryVocsColumns(HttpServletRequest req) {
        String area = req.getParameter("area");
        String companyId = req.getParameter("companyId");
        String mn = req.getParameter("mn");
        String type = req.getParameter("type");
        List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.queryCode(area, companyId, mn, type);
        List<Column> columns = null;
        if (CollectionUtil.isNotEmpty(sysPollutionCodes)) {
            columns = new ArrayList<>();
            for (SysPollutionCode sysPollutionCode : sysPollutionCodes) {
                List<Column> childColumns = new ArrayList<>();
                Column column = new Column();
                String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
                column.setTitle(sysPollutionCode.getMeaning() + "(" + chromaUnit + ")");
                if (!"a01011".equalsIgnoreCase(sysPollutionCode.getCode()) && !"a01012".equalsIgnoreCase(sysPollutionCode.getCode()) && !"a01013".equalsIgnoreCase(sysPollutionCode.getCode()) && !"a01014".equalsIgnoreCase(sysPollutionCode.getCode()) && !"a01017".equalsIgnoreCase(sysPollutionCode.getCode())) {
                    Column childColumn = new Column();
                    childColumn.setTitle("浓度");
                    childColumn.setDataIndex(sysPollutionCode.getCode() + "_RTD");
                    childColumns.add(childColumn);
                }
                if ("Y".equalsIgnoreCase(sysPollutionCode.getIsZs())) {
                    Column childColumnZs = new Column();
                    childColumnZs.setTitle("折算浓度");
                    childColumnZs.setDataIndex(sysPollutionCode.getCode() + "_ZSRTD");
                    childColumns.add(childColumnZs);
                }
                if (CollectionUtil.isNotEmpty(childColumns)) {
                    column.setChildren(childColumns);
                }
                columns.add(column);
            }
        }
        return Result.ok(columns);
    }

    /**
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "实时监控列表查询")
    @ApiOperation(value = "实时监控列表查询", notes = "实时监控列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String area = req.getParameter("area");
        String companyId = req.getParameter("companyId");
        String mn = req.getParameter("mn");
        String dataStatus = req.getParameter("dataStatus");
        //表名
        String currTime = DateUtil.format(DateUtil.date(), "yyyyMM");
        String tableName = req.getParameter("tableName") + currTime.substring(2);
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        IPage<Map<String, Object>> pageList = null;
        switch(dataStatus){
            case "total" :
                pageList = waterCurrentTrService.getWaterCurrentTrList(page, area, companyId, mn, tableName,null,null);
                break;
            case "normal" ://正常
                pageList = waterCurrentTrService.getWaterCurrentTrList(page, area, companyId, mn, tableName,normals,null);
                break;
            case "warn" ://预警
                pageList = waterCurrentTrService.getWaterCurrentTrList(page, area, companyId, mn, tableName,warns,null);
                break;
            case "standard" ://超标
                pageList = waterCurrentTrService.getWaterCurrentTrList(page, area, companyId, mn, tableName,standards,null);
                break;
            case "abnormal" ://异常
                pageList = waterCurrentTrService.getWaterCurrentTrList(page, area, companyId, mn, tableName,abNormals,null);
                break;
            case "offline" ://离线
                pageList = waterCurrentTrService.getWaterCurrentTrList(page, area, companyId, mn, tableName,null,0);
                break;
            default : //可选
                //语句
        }
        return Result.ok(pageList);
    }


    /*
    1：超过超标上限值 -1：超过超标下限值
    2：超过一级预警上限值 -2：超过一级预警下限值
    3：超过二级预警上限值 -3：超过二级预警下限值
    4：超过三级预警上限值 -4：超过三级预警下限值
    5：超过四级预警上限值 -5：超过四级预警下限值
    6：超过异常上限值 -6：超过异常下限值
    7：数据超量程
    8：超过量程上限值 -8：超过量程下限值
    9：数据异常
    */
    @AutoLog(value = "查询站点各状态数量")
    @ApiOperation(value = "查询站点数量", notes = "查询站点数量")
    @GetMapping(value = "/queryCompanyFlagNum")
    public Result<?> queryCompanyFlagNum(HttpServletRequest req) {
        String area = req.getParameter("area");
        String companyId = req.getParameter("companyId");
        String mn = req.getParameter("mn");
        String type = req.getParameter("type");
        String currTime = DateUtil.format(DateUtil.date(), "yyyyMM");
        String tableName = req.getParameter("tableName") + currTime.substring(2);
        //查询到全部数量
        int total = siteMonitorPointService.queryCompanyFlagNum(area, companyId, mn, type, null,tableName,null);
        int normal = 0;
        int warn = 0;
        int standard = 0;
        int abnormal = 0;
        int offline = 0;
        if (total > 0) {
            //查询预警数量
            warn = siteMonitorPointService.queryCompanyFlagNum(area, companyId, mn, type, warns,tableName,1);
            //查询超标数量
            standard = siteMonitorPointService.queryCompanyFlagNum(area, companyId, mn, type, standards,tableName,1);
            //查询异常数量
            abnormal = siteMonitorPointService.queryCompanyFlagNum(area, companyId, mn, type, abNormals,tableName,1);
            //离线
            offline = siteMonitorPointService.queryCompanyFlagNum(area, companyId, mn, type, null,tableName,0);
            //查询正常
            normal = siteMonitorPointService.queryCompanyFlagNum(area,companyId,mn,type,normals,tableName,1);
        }
        //组织返回值
        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("total", total);
        resultMap.put("normal", normal);
        resultMap.put("warn", warn);
        resultMap.put("standard", standard);
        resultMap.put("abnormal", abnormal);
        resultMap.put("offline", offline);
        return Result.ok(resultMap);
    }

}
