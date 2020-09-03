package org.jeecg.modules.business.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SysPollutionCode;
import org.jeecg.modules.business.entity.WaterCurrentTr;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.business.service.ISysPollutionCodeService;
import org.jeecg.modules.business.service.IWaterCurrentTrService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.Column;
import org.jeecg.modules.business.vo.SiteMonitorPointVO;
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
@Api(tags = "water_current_tr")
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
                    columnq.setDataIndex("w00000Total");
                    columns.add(columnq);
                    String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
                    column.setTitle(sysPollutionCode.getMeaning() + "(" + chromaUnit + ")");
                    column.setDataIndex(sysPollutionCode.getCode() + "rtd");
                    columns.add(column);
                } else {
                    String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
                    column.setTitle(sysPollutionCode.getMeaning() + "(" + chromaUnit + ")");
                    column.setDataIndex(sysPollutionCode.getCode() + "rtd");
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
                childColumn.setDataIndex(sysPollutionCode.getCode() + "_rtd");
                childColumns.add(childColumn);
                if ("Y".equalsIgnoreCase(sysPollutionCode.getIsZs())) {
                    Column childColumnZs = new Column();
                    childColumnZs.setTitle("折算浓度");
                    childColumnZs.setDataIndex(sysPollutionCode.getCode() + "zsrtd");
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
                if (!"a01011".equalsIgnoreCase(sysPollutionCode.getCode()) && !"a01012".equalsIgnoreCase(sysPollutionCode.getCode()) && !"a01013".equalsIgnoreCase(sysPollutionCode.getCode()) && !"a01014".equalsIgnoreCase(sysPollutionCode.getCode())&&!"a01017".equalsIgnoreCase(sysPollutionCode.getCode())) {
                    Column childColumn = new Column();
                    childColumn.setTitle("浓度");
                    childColumn.setDataIndex(sysPollutionCode.getCode() + "rtd");
                    childColumns.add(childColumn);
                }
                if ("Y".equalsIgnoreCase(sysPollutionCode.getIsZs())) {
                    Column childColumnZs = new Column();
                    childColumnZs.setTitle("折算浓度");
                    childColumnZs.setDataIndex(sysPollutionCode.getCode() + "zsrtd");
                    childColumns.add(childColumnZs);
                }
                if(CollectionUtil.isNotEmpty(childColumns)){
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
    @AutoLog(value = "废水实时监控列表查询")
    @ApiOperation(value = "废水实时监控列表查询", notes = "废水实时监控列表查询")
    @GetMapping(value = "/waterCurrentTr/list")
    public Result<?> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String area = req.getParameter("area");
        String companyId = req.getParameter("companyId");
        String mn = req.getParameter("mn");
        String type = req.getParameter("type");
        //表名
        String currTime = DateUtil.format(DateUtil.date(), "yyyyMM");
        String tableName = "water_current_tr_"+currTime.substring(2);
        Page<Map<String,Object>> page = new Page<>(pageNo, pageSize);
        IPage<Map<String,Object>> pageList = waterCurrentTrService.getWaterCurrentTrList(page,area,companyId,mn,type,tableName);
        return Result.ok(pageList);
    }


}
