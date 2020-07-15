package org.jeecg.modules.business.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SiteFile;
import org.jeecg.modules.business.entity.SiteMonitorDevice;
import org.jeecg.modules.business.entity.SysPollutionCode;
import org.jeecg.modules.business.service.ISiteFileService;
import org.jeecg.modules.business.service.ISiteMonitorDeviceService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ISysPollutionCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 监测点检测设备
 * @Author: jeecg-boot
 * @Date: 2020-07-08
 * @Version: V1.0
 */
@Api(tags = "监测点检测设备")
@RestController
@RequestMapping("/device/siteMonitorDevice")
@Slf4j
public class SiteMonitorDeviceController extends JeecgController<SiteMonitorDevice, ISiteMonitorDeviceService> {
    @Autowired
    private ISiteMonitorDeviceService siteMonitorDeviceService;

    @Autowired
    private ISysPollutionCodeService sysPollutionCodeService;

    @Autowired
    private ISiteFileService siteFileService;

    /**
     * 分页列表查询
     *
     * @param siteMonitorDevice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "监测点检测设备-分页列表查询")
    @ApiOperation(value = "监测点检测设备-分页列表查询", notes = "监测点检测设备-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SiteMonitorDevice siteMonitorDevice,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SiteMonitorDevice> queryWrapper = QueryGenerator.initQueryWrapper(siteMonitorDevice, req.getParameterMap());
        Page<SiteMonitorDevice> page = new Page<SiteMonitorDevice>(pageNo, pageSize);
        IPage<SiteMonitorDevice> pageList = siteMonitorDeviceService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "监测点检测设备-添加")
    @ApiOperation(value = "监测点检测设备-添加", notes = "监测点检测设备-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody JSONObject jsonObject) {
        SiteMonitorDevice siteMonitorDevice = this.getSiteMonitorDevice(jsonObject);
        siteMonitorDeviceService.save(siteMonitorDevice);
        siteFileService.saveFiles(jsonObject.getString("fileList"), "file", "site_monitor_device", siteMonitorDevice.getId());
        return Result.ok("添加成功！");
    }

    private SiteMonitorDevice getSiteMonitorDevice(JSONObject jsonObject) {
        SiteMonitorDevice siteMonitorDevice = new SiteMonitorDevice();
        siteMonitorDevice.setId(jsonObject.getString("id"));
        siteMonitorDevice.setMonitorId(jsonObject.getString("monitorId"));
        siteMonitorDevice.setDeviceName(jsonObject.getString("deviceName"));
        siteMonitorDevice.setDeviceNumber(jsonObject.getString("deviceNumber"));
        siteMonitorDevice.setDeviceType(jsonObject.getString("deviceType"));
        siteMonitorDevice.setPollutionCode(jsonObject.getString("pollutionCode"));
        siteMonitorDevice.setSampleCycle(jsonObject.getString("sampleCycle"));
        siteMonitorDevice.setDeviceState(jsonObject.getString("deviceState"));
        siteMonitorDevice.setDeviceModel(jsonObject.getString("deviceModel"));
        siteMonitorDevice.setDeviceFactory(jsonObject.getString("deviceFactory"));
        siteMonitorDevice.setRangeMax(jsonObject.getString("rangeMax"));
        siteMonitorDevice.setRangeMin(jsonObject.getString("rangeMin"));
        siteMonitorDevice.setCheckoutMax(jsonObject.getString("checkoutMax"));
        siteMonitorDevice.setCheckoutUnit(jsonObject.getString("checkoutUnit"));
        siteMonitorDevice.setProductDate(jsonObject.getDate("productDate"));
        siteMonitorDevice.setInstallDate(jsonObject.getDate("installDate"));
        siteMonitorDevice.setShelfLifeDate(jsonObject.getDate("shelfLifeDate"));
        siteMonitorDevice.setDeviceConcatUser(jsonObject.getString("deviceConcatUser"));
        siteMonitorDevice.setDeviceConcatMobile(jsonObject.getString("deviceConcatMobile"));
        siteMonitorDevice.setUseDate(jsonObject.getDate("useDate"));
        siteMonitorDevice.setOperationCompany(jsonObject.getString("operationCompany"));
        siteMonitorDevice.setOperationUser(jsonObject.getString("operationUser"));
        siteMonitorDevice.setOperationMobile(jsonObject.getString("operationMobile"));
        siteMonitorDevice.setContent(jsonObject.getString("content"));
        return siteMonitorDevice;
    }

    /**
     * 编辑
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "监测点检测设备-编辑")
    @ApiOperation(value = "监测点检测设备-编辑", notes = "监测点检测设备-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody JSONObject jsonObject) {
        SiteMonitorDevice siteMonitorDevice = this.getSiteMonitorDevice(jsonObject);
        siteMonitorDeviceService.updateById(siteMonitorDevice);
        //删除原有的
        siteFileService.remove(new QueryWrapper<SiteFile>().lambda().eq(SiteFile::getFromTable, "site_monitor_device")
                .eq(SiteFile::getTableId, siteMonitorDevice.getId()));
        siteFileService.saveFiles(jsonObject.getString("fileList"), "file", "site_monitor_device", siteMonitorDevice.getId());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "监测点检测设备-通过id删除")
    @ApiOperation(value = "监测点检测设备-通过id删除", notes = "监测点检测设备-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        siteMonitorDeviceService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "监测点检测设备-批量删除")
    @ApiOperation(value = "监测点检测设备-批量删除", notes = "监测点检测设备-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.siteMonitorDeviceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "监测点检测设备-通过id查询")
    @ApiOperation(value = "监测点检测设备-通过id查询", notes = "监测点检测设备-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SiteMonitorDevice siteMonitorDevice = siteMonitorDeviceService.getById(id);
        if (siteMonitorDevice == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(siteMonitorDevice);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param siteMonitorDevice
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SiteMonitorDevice siteMonitorDevice) {
        return super.exportXls(request, siteMonitorDevice, SiteMonitorDevice.class, "监测点检测设备");
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
        return super.importExcel(request, response, SiteMonitorDevice.class);
    }

    /**
     * 查询附件
     *
     * @param id
     * @return
     */
    @AutoLog(value = "查询附件")
    @ApiOperation(value = "查询附件", notes = "查询附件")
    @GetMapping(value = "/queryFiles")
    public Result<?> queryFiles(@RequestParam(name = "id", required = true) String id) {
        List<Map<String, String>> result = siteFileService.getFileMaps(id, "site_monitor_device");
        return Result.ok(result);
    }

    /**
     * 查询污染因子
     *
     * @return
     */
    @AutoLog(value = "查询污染因子")
    @ApiOperation(value = "查询污染因子", notes = "查询污染因子")
    @GetMapping(value = "/queryPollution")
    public Result<?> queryPollution(@RequestParam(name = "siteType", required = true) String type) {
        List<Map<String, String>> result = new ArrayList<>();
        sysPollutionCodeService.list(new QueryWrapper<SysPollutionCode>().lambda().eq(SysPollutionCode::getType,type).eq(SysPollutionCode::getIsUse,"Y")).forEach(SysPollutionCode -> {
            Map<String, String> param = new HashMap<>();
            param.put("key", SysPollutionCode.getCode());
            param.put("value", SysPollutionCode.getMeaning());
            result.add(param);
        });
        return Result.ok(result);
    }

    /**
     * 查询污染因子浓度单位
     *
     * @return
     */
    @AutoLog(value = "查询污染因子浓度单位")
    @ApiOperation(value = "查询污染因子浓度单位", notes = "查询污染因子浓度单位")
    @GetMapping(value = "/queryUnit")
    public Result<?> queryUnit(@RequestParam(name = "code") String code,@RequestParam(name = "siteType", required = true) String type) {
        SysPollutionCode sysPollutionCode = sysPollutionCodeService.getOne(new QueryWrapper<SysPollutionCode>().lambda().eq(SysPollutionCode::getCode, code).eq(SysPollutionCode::getType,type));
        Map<String, String> param = new HashMap<>();
        param.put("unit", sysPollutionCode.getChromaUnit());
        return Result.ok(param);
    }
}
