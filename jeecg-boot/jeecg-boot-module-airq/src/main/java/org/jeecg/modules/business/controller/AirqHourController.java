package org.jeecg.modules.business.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.AirqHour;
import org.jeecg.modules.business.entity.AirqLevel;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.service.IAirqHourService;
import org.jeecg.modules.business.service.ISiteMonitorPointService;
import org.jeecg.modules.business.service.ISysDictService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date: 2020-07-16
 * @Version: V1.0
 */
@Api(tags = "airq_hour")
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
    private AirqLevelServiceImpl airqLevelService;

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
    @ApiOperation(value = "airq_hour-分页列表查询", notes = "airq_hour-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AirqHour airqHour,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
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
    @ApiOperation(value = "airq_hour-查询站点最新的", notes = "airq_hour-查询站点最新的")
    @GetMapping(value = "/queryLastAirInfo")
    public Result<?> queryLastAirInfo(@RequestParam(name = "companyIds", required = true) String companyIds) {

        return Result.ok(airqHourService.queryInfoByCompanyId(Arrays.asList(companyIds.split(","))));
    }

    /**
     * 查询站点 一段时间内的数据
     *
     * @param companyIds
     * @return
     */
    @AutoLog(value = "查询站点最新的")
    @ApiOperation(value = "airq_hour-查询站点最新的", notes = "airq_hour-查询站点最新的")
    @GetMapping(value = "/queryPollutionCloud")
    public Result<?> queryPollutionCloud(@RequestParam(name = "companyIds", required = true) String companyIds
            , @RequestParam(name = "datatime", required = true) String datatime
            , @RequestParam(name = "datatime2", required = true) String datatime2) {
        //查询所给时间内的所有小时数据
        return Result.ok(airqHourService.queryPollutionCloud(Arrays.asList(companyIds.split(",")), datatime, datatime2));
    }


    /**
     * 空气质量实时报
     *
     * @param companyIds
     * @return
     */
    @AutoLog(value = "空气质量实时报")
    @ApiOperation(value = "airq_hour-空气质量实时报", notes = "airq_hour-空气质量实时报")
    @GetMapping(value = "/queryHourAirQuality")
    public Result<?> queryHourAirQuality(@RequestParam(name = "companyIds", required = true) String companyIds
            , @RequestParam(name = "datatime", required = true) String datatime
            , @RequestParam(name = "datatime2", required = true) String datatime2
            , @RequestParam(name = "area", required = false) String area
            , @RequestParam(name = "mn", required = false) String mn) {
        //根据小时查询
        return Result.ok(airqHourService.queryHourAirQuality(Arrays.asList(companyIds.split(",")), datatime, datatime2, area, mn));
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
    @ApiOperation(value = "airq_hour-分页列表查询", notes = "airq_hour-分页列表查询")
    @GetMapping(value = "/queryLastAirqHour")
    public Result<?> queryAirqHourMonitor(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) throws ParseException {
        String companyIds = req.getParameter("companyIds");
        String area = req.getParameter("area");
        //通过选择站点名称获取站点mn号
        String mn = req.getParameter("mn");
        String dataTimeBegin = req.getParameter("dataTime_begin");
        String dataTimeEnd = req.getParameter("dataTime_end");
        Date dateBegin;
        Date dateEnd;
        if (StrUtil.isEmpty(dataTimeBegin) && StrUtil.isEmpty(dataTimeEnd)) {
            dateBegin = null;
            dateEnd = null;
        } else {
            dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeBegin);
            dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeEnd);
        }
        Page<AirqHourMonitorVO> page = new Page<AirqHourMonitorVO>(pageNo, pageSize);
        IPage<AirqHourMonitorVO> pageList = airqHourService.queryAirqHourMonitor(companyIds, page, area, mn, dateBegin, dateEnd);
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
    @ApiOperation(value = "airq_hour-分页列表查询", notes = "airq_hour-分页列表查询")
    @GetMapping(value = "/queryInputAirqHour")
    public Result<?> queryAirqHourInput(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req) throws ParseException {
        String companyIds = req.getParameter("companyIds");
        String area = req.getParameter("area");
        //通过选择站点名称获取站点mn号
        String mn = req.getParameter("mn");
        String dataTimeBegin = req.getParameter("dataTime_begin");
        String dataTimeEnd = req.getParameter("dataTime_end");
        Date dateBegin;
        Date dateEnd;
        if (StrUtil.isEmpty(dataTimeBegin) && StrUtil.isEmpty(dataTimeEnd)) {
            dateBegin = null;
            dateEnd = null;
        } else {
            dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeBegin);
            dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeEnd);
        }
        Page<AirqHourInputVO> page = new Page<AirqHourInputVO>(pageNo, pageSize);
        IPage<AirqHourInputVO> pageList = airqHourService.queryAirqHourInput(companyIds, page, area, mn, dateBegin, dateEnd);
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
    @ApiOperation(value = "airq_hour-分页列表查询", notes = "airq_hour-分页列表查询")
    @GetMapping(value = "/queryManInsertAirqHour")
    public Result<?> queryAirqHourManInsert(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                            HttpServletRequest req) throws ParseException {
        String companyIds = req.getParameter("companyIds");
        String area = req.getParameter("area");
        //通过选择站点名称获取站点mn号
        String mn = req.getParameter("mn");
        String dataState = req.getParameter("state");
        Integer state = null;
        if (!StrUtil.isEmpty(dataState)) {
            state = Integer.valueOf(dataState);
        }
        String dataTimeBegin = req.getParameter("dataTime_begin");
        String dataTimeEnd = req.getParameter("dataTime_end");
        Date dateBegin;
        Date dateEnd;
        if (StrUtil.isEmpty(dataTimeBegin) && StrUtil.isEmpty(dataTimeEnd)) {
            dateBegin = null;
            dateEnd = null;
        } else {
            dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeBegin);
            dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeEnd);
        }
        Page<AirqHourManInsertVO> page = new Page<AirqHourManInsertVO>(pageNo, pageSize);
        IPage<AirqHourManInsertVO> pageList = airqHourService.queryAirqHourManInsert(companyIds, page, area, mn, state, dateBegin, dateEnd);
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
    @ApiOperation(value = "airq_hour-分页列表查询", notes = "airq_hour-分页列表查询")
    @GetMapping(value = "/queryAirqHourAudit")
    public Result<?> queryAirqHourAudit(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest req) throws ParseException {
        String companyIds = req.getParameter("companyIds");
        String area = req.getParameter("area");
        //通过选择站点名称获取站点mn号
        String mn = req.getParameter("mn");
        String dataState = req.getParameter("state");
        Integer state = null;
        if (!StrUtil.isEmpty(dataState)) {
            state = Integer.valueOf(dataState);
        }
        String dataTimeBegin = req.getParameter("dataTime_begin");
        String dataTimeEnd = req.getParameter("dataTime_end");
        Date dateBegin;
        Date dateEnd;
        if (StrUtil.isEmpty(dataTimeBegin) && StrUtil.isEmpty(dataTimeEnd)) {
            dateBegin = null;
            dateEnd = null;
        } else {
            dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeBegin);
            dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeEnd);
        }
        Page<AirqHourAuditVO> page = new Page<>(pageNo, pageSize);
        IPage<AirqHourAuditVO> pageList = airqHourService.queryAirqHourAudit(companyIds, page, area, mn, state, dateBegin, dateEnd);
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
    @ApiOperation(value = "airq_hour-分页列表查询", notes = "airq_hour-分页列表查询")
    @GetMapping(value = "/querySiteQualityEvaluate")
    public Result<?> querySiteQualityEvaluate(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                              HttpServletRequest req) throws ParseException {
        String companyIds = req.getParameter("companyIds");
        String area = req.getParameter("area");
        //通过选择站点名称获取站点mn号
        String mn = req.getParameter("mn");
        String dataState = req.getParameter("state");
        String level = req.getParameter("level");
        Integer state = null;
        if (!StrUtil.isEmpty(dataState)) {
            state = Integer.valueOf(dataState);
        }
        String dataTimeBegin = req.getParameter("dataTime_begin");
        String dataTimeEnd = req.getParameter("dataTime_end");
        Date dateBegin;
        Date dateEnd;
        if (StrUtil.isEmpty(dataTimeBegin) && StrUtil.isEmpty(dataTimeEnd)) {
            dateBegin = null;
            dateEnd = null;
        } else {
            dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeBegin);
            dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeEnd);
        }
        Page<SiteQualityEvaluateVO> page = new Page<>(pageNo, pageSize);
        IPage<SiteQualityEvaluateVO> pageList = airqHourService.querySiteQualityEvaluate(companyIds, page, area, mn, level, state, dateBegin, dateEnd);
        return Result.ok(pageList);
    }

    /**
     * 分页列表查询
     *
     * @return
     */
    @AutoLog(value = "查询站点最新的")
    @ApiOperation(value = "airq_hour-分页列表查询", notes = "airq_hour-分页列表查询")
    @GetMapping(value = "/querySiteNameAndMn")
    public Result<?> querySiteNameAndMn(@RequestParam(name = "companyIds", required = true) String companyIds) {
        List<String> idList = Arrays.asList(companyIds.split(","));
        List<Map<String, String>> result = new ArrayList<>();
        siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getSiteType, 3).in(SiteMonitorPoint::getCompanyId, idList)).forEach(siteMonitorPoint -> {
            Map<String, String> param = new HashMap<>();
            param.put("key", siteMonitorPoint.getMn());
            param.put("value", siteMonitorPoint.getSiteName());
            param.put("siteId",siteMonitorPoint.getId());
            param.put("area", siteMonitorPoint.getArea());
            param.put("lng", siteMonitorPoint.getSiteLongitude());
            param.put("lat", siteMonitorPoint.getSiteLatitude());
            result.add(param);
        });
        return Result.ok(result);
    }

    /**
     * 提交
     *
     * @param airqHour
     * @return
     */
    @AutoLog(value = "airq_hour-提交")
    @ApiOperation(value = "airq_hour-提交", notes = "airq_hour-提交")
    @PostMapping(value = "/submit")
    public Result<?> submit(@RequestBody AirqHour airqHour) {
        //3-审核中
        airqHour.setState(3);
        airqHourService.updateById(airqHour);
        return Result.ok("提交成功！");
    }

    /**
     * 批量提交
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "人工录入数据批量提交")
    @ApiOperation(value = "人工录入数据批量提交", notes = "人工录入数据批量提交")
    @GetMapping(value = "/batchSubmit")
    public Result<?> batchSubmit(@RequestParam(name = "ids", required = true) String ids) {
        //修改
        airqHourService.update(new UpdateWrapper<AirqHour>().lambda()
                .eq(AirqHour::getState, 2)
                .in(AirqHour::getId, Arrays.asList(ids.split(",")))
                .set(AirqHour::getState, 3));
        return Result.ok("批量提交成功!");
    }

    /**
     * 添加
     *
     * @param airqHour
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "airq_hour-添加")
    @ApiOperation(value = "airq_hour-添加", notes = "airq_hour-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AirqHour airqHour) {
        Map<String, Double> map = new HashMap<>();
        for (Field field : airqHour.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            //是不是值
            if (fieldName.endsWith("Avg")) {

                //对象
                Object val = field.get(airqHour);
                if (val != null && !val.toString().equals("")) {
                    String code = fieldName.substring(0, 6);
                    code = code.substring(0, 1).toUpperCase() + code.substring(1);
                    int type = fieldName.length() >= 11 ? Integer.valueOf(fieldName.substring(7, 8)) : 1;

                    double aqi = airQualityUtil.getAQI(code, type, Double.valueOf(val.toString()));
                    String newFieldName = fieldName.replace("Avg", "Iaqi");
                    String newCode = newFieldName.substring(0, 1).toUpperCase() + newFieldName.substring(1);
                    Method mtd = airqHour.getClass().getMethod("set" + newCode, Double.class);
                    mtd.invoke(airqHour, aqi);
                    //污染因子编码
                    String pollutionCode = fieldName.length() > 10 ? fieldName.substring(0, 8) : fieldName.substring(0, 6);
                    pollutionCode = pollutionCode.substring(0, 1).toUpperCase() + pollutionCode.substring(1);
                    map.put(pollutionCode, aqi);
                }
            }
        }
        List<Map.Entry<String, Double>> list = new ArrayList(map.entrySet());
        Collections.sort(list, (o1, o2) -> {
            if ((o2.getValue() - o1.getValue()) > 0)
                return 1;
            else if ((o2.getValue() - o1.getValue()) == 0)
                return 0;
            else
                return -1;
        });
        Double aqi = list.get(0).getValue();
        airqHour.setAqi(aqi);
        airqHour.setLevel(airQualityUtil.getLevel(aqi));
        String firstCode = list.get(0).getKey();
        for (int i = 1; i < list.size(); i++) {
            if (Double.toString(list.get(i).getValue()).equals(Double.toString(aqi))) {
                firstCode = firstCode + "," + list.get(i).getKey();
            } else {
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
     * 编辑
     *
     * @param airqHour
     * @return
     */
    @AutoLog(value = "airq_hour-编辑")
    @ApiOperation(value = "airq_hour-编辑", notes = "airq_hour-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AirqHour airqHour) {
        //2-暂存
        airqHour.setState(2);
        airqHourService.updateById(airqHour);
        return Result.ok("编辑成功!");
    }

    /**
     * 审核
     *
     * @param airqHour
     * @return
     */
    @AutoLog(value = "airq_hour-审核")
    @ApiOperation(value = "airq_hour-审核", notes = "airq_hour-审核")
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
        String[] ids = jsonObject.getString("ids").split(",");
        if (ids.length > 0) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            //3-审核通过
            LambdaUpdateWrapper updateWrapper = new UpdateWrapper<AirqHour>().lambda().in(AirqHour::getId, Arrays.asList(ids))
                    .set(AirqHour::getState, 3).set(AirqHour::getUpdateTime, new Date())
                    .set(AirqHour::getUpdateBy, sysUser.getId());
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
        String[] ids = jsonObject.getString("ids").split(",");
        if (ids.length > 0) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            //4-审核不通过
            LambdaUpdateWrapper updateWrapper = new UpdateWrapper<AirqHour>().lambda().in(AirqHour::getId, Arrays.asList(ids))
                    .set(AirqHour::getState, 4).set(AirqHour::getUpdateTime, new Date())
                    .set(AirqHour::getUpdateBy, sysUser.getId());
            airqHourService.update(updateWrapper);
        }
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "airq_hour-通过id删除")
    @ApiOperation(value = "airq_hour-通过id删除", notes = "airq_hour-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        airqHourService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "airq_hour-批量删除")
    @ApiOperation(value = "airq_hour-批量删除", notes = "airq_hour-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
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
    @ApiOperation(value = "airq_hour-通过id查询", notes = "airq_hour-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AirqHour airqHour = airqHourService.getById(id);
        if (airqHour == null) {
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

        List<AirqHourQualityVo> exportList = airqHourService.queryHourAirQuality(Arrays.asList(request.getParameter("companyIds").split(","))
                , request.getParameter("datatime"), request.getParameter("datatime2"), request.getParameter("area"), request.getParameter("mn"));
        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new SelfEntityExcelView(sysDictService, redisUtil));
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
        if (StrUtil.isEmpty(dataTimeBegin) && StrUtil.isEmpty(dataTimeEnd)) {
            dateBegin = null;
            dateEnd = null;
        } else {
            dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeBegin);
            dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataTimeEnd);
        }
        List<AirqHourInputVO> exportList = airqHourService.queryManInputExport(companyIds, area, mn, dateBegin, dateEnd);
        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new SelfEntityExcelView(sysDictService, redisUtil));
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
			 if(param.get("firstCode") != null) {
				 String firstCode = param.get("firstCode").toString();
				 List<String> arr = Arrays.asList(firstCode.split(","));
				 List<String> codeArr = new ArrayList<>();
				 for (int i=0;i<arr.size();i++) {
					 codeArr.add(arr.get(i).substring(0, 6));
				 }
				 String code = StringUtils.join(codeArr, ",");
				 param.put("code", code);
			 }else {
			 	param.put("code", "");
			 }

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
    @ApiOperation(value = "监测站点折线图数据", notes = "监测站点折线图数据")
    @GetMapping(value = "/queryChartInfo")
    public Result<?> queryChartInfo(@RequestParam(name = "mn", required = true) String mn
            , @RequestParam(name = "timeType", required = false) String timeType, @RequestParam(name = "code", required = false) String code) {
        List<Map<String, Object>> airChartList = null;
        Map<String, Object> result = new HashMap<>();
        List<Double> codeList = null;
        List<String> timeList = null;
        if ("0".equals(timeType)) {
            airChartList = airqHourService.queryHourChartInfo(mn);
            codeList = new ArrayList<>();
            timeList = new ArrayList<>();
            for (Map<String, Object> param : airChartList) {
                Double codeValue = null;
                if (param.get(code) != null) {
                    String value = param.get(code).toString();
                    BigDecimal max = new BigDecimal(value);
                    codeValue = max.doubleValue();
                    codeList.add(codeValue);
                } else {
                    codeList.add(-1.0);
                }
                String time = param.get("dataTime").toString().substring(5, 19);
                timeList.add(time);
            }

            result.put("series", codeList);
            result.put("categories", timeList);

        } else if ("1".equals(timeType)) {
            airChartList = airqDayService.queryDayChartInfo(mn);
            codeList = new ArrayList<>();
            timeList = new ArrayList<>();
            for (Map<String, Object> param : airChartList) {
                Double codeValue = null;
                if (param.get(code) != null) {
                    String value = param.get(code).toString();
                    BigDecimal max = new BigDecimal(value);
                    codeValue = max.doubleValue();
                    codeList.add(codeValue);
                } else {
                    codeList.add(-1.0);
                }
                String time = param.get("dataTime").toString().substring(0, 11)+"  ";
                timeList.add(time);
            }
            result.put("series", codeList);
            result.put("categories", timeList);
        } else if ("2".equals(timeType)) {
            airChartList = airqMonthService.queryMonthChartInfo(mn);
            codeList = new ArrayList<>();
            timeList = new ArrayList<>();
            for (Map<String, Object> param : airChartList) {
                Double codeValue = null;
                if (param.get(code) != null) {
                    String value = param.get(code).toString();
                    BigDecimal max = new BigDecimal(value);
                    codeValue = max.doubleValue();
                    codeList.add(codeValue);
                } else {
                    codeList.add(-1.0);
                }
                String time = param.get("dataTime").toString().substring(0, 7);
                timeList.add(time);
            }
            result.put("series", codeList);
            result.put("categories", timeList);
        }

        return Result.ok(result);
    }

	 /**
	  * 空气质量app 监测站点详情
	  *
	  * @return
	  */
	 @AutoLog(value = "监测站点详情")
	 @ApiOperation(value="监测站点详情", notes="监测站点详情")
	 @GetMapping(value = "/queryAirMoreInfo")
	 public Result<?> queryAirMoreInfo(@RequestParam(name="mn",required=true) String mn
			 ,@RequestParam(name="dateBegin",required=false) String dateBegin
			 ,@RequestParam(name="time",required=true) String time) {
		 List<Map<String,Object>>  airMoreList =  null;
		 Map<String,Object> result = new HashMap<>();
		 if("0".equals(time)) {
			 airMoreList =  airqHourService.queryAirMoreInfo(mn,dateBegin);
		 }else if("1".equals(time)) {
			 airMoreList = airqDayService.queryAirDayMoreInfo(mn, dateBegin);
		 }
		 for(Map<String,Object> param:airMoreList){
			 if(param.get("level") != null) {
				 String levelValue = sysDictService.queryDictTextByKey("level",param.get("level").toString());
				 param.put("levelName", levelValue);
			 }
			 if(param.get("A01008") != null) {
				 String windDirection = sysDictService.queryDictTextByKey("wind",param.get("A01008").toString());
				 param.put("windDirection", windDirection);
			 }else {
				 param.put("windDirection", "");
			 }
			 if(param.get("firstCode") != null) {
				 String firstCode = param.get("firstCode").toString();
				 List<String> arr = Arrays.asList(firstCode.split(","));
				 List<String> codeArr = new ArrayList<>();
				 for (int i=0;i<arr.size();i++) {
					 codeArr.add(arr.get(i).substring(0, 6));
				 }
				 String code = StringUtils.join(codeArr, ",");
				 param.put("code", code);
			 }else {
				 param.put("code", "");
			 }

		 }
		 result.put("dataList",airMoreList);
		 return Result.ok(result);
	 }

    /**
     * @return
     */
    @AutoLog(value = "gis地图")
    @ApiOperation(value = "gis地图", notes = "gis地图")
    @GetMapping(value = "/queryMapSite")
    public Result<?> queryMapSite(@RequestParam(name = "companyIds", required = true) String companyIds, @RequestParam(name = "pollutionCode", required = true) String pollutionCode) {
        List<Map<String, Object>> hours = airqHourService.queryMapSite(Arrays.asList(companyIds.split(",")));
        //组织
        List<SiteMapVO> siteMapVOs = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(hours)) {
            for (int i = 0; i < hours.size(); i++) {
                Map<String, Object> map = hours.get(i);
                SiteMapVO siteMapVO = new SiteMapVO();
                siteMapVO.setId(StrUtil.toString(map.get("id")));
                siteMapVO.setName(StrUtil.toString(map.get("site_name")));
                siteMapVO.setLng(StrUtil.toString(map.get("site_longitude")));
                siteMapVO.setLat(StrUtil.toString(map.get("site_latitude")));
                if ("0".equals(map.get("device_status"))) {
                    siteMapVO.setLevel("error");
                } else if ("0".equals(map.get("online_status"))) {
                    siteMapVO.setLevel("offline");
                }
                String level = "";
                if (pollutionCode.equals("AQI")) {
                    //AQI级别直接获取不要计算
                    level = StrUtil.toString(map.get("level"));
                } else {
                    //其他计算并获取level
                    Double iaqi = (Double) map.get(pollutionCode);
                    level = airQualityUtil.getLevel(iaqi);
                }
                switch (level) {
                    case "1":
                        siteMapVO.setLevel("excellent");
                        break;
                    case "2":
                        siteMapVO.setLevel("good");
                        break;
                    case "3":
                        siteMapVO.setLevel("mild");
                        break;
                    case "4":
                        siteMapVO.setLevel("moderate");
                        break;
                    case "5":
                        siteMapVO.setLevel("serious");
                        break;
                    case "6":
                        siteMapVO.setLevel("severe");
                        break;
                }
                siteMapVOs.add(siteMapVO);
            }
        }
        return Result.ok(siteMapVOs);
    }

    /**
     * @return
     */
    @AutoLog(value = "gis地图站点详情")
    @ApiOperation(value = "gis地图站点详情", notes = "gis地图站点详情")
    @GetMapping(value = "/querySiteDetail")
    public Result<?> querySiteDetail(@RequestParam(name = "companyIds", required = true) String companyIds, @RequestParam(name = "siteId", required = true) String siteId) {
        Map<String, Object> map = airqHourService.querySiteDetail(Arrays.asList(companyIds.split(",")), siteId);
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> siteDetails = new ArrayList<>();
        if (map != null) {
            String site_name = StrUtil.toString(map.get("site_name"));
            map.remove("site_name");
            resultMap.put("siteName", site_name);
            String siteLevel = sysDictService.queryDictTextByKey("siteLevel", map.get("site_level").toString());
            map.remove("site_level");
            resultMap.put("siteLevel", siteLevel);
            resultMap.put("location", map.get("location").toString());
            map.remove("location");
            String data_time = map.get("data_time").toString();
            resultMap.put("dataTime", data_time.substring(0,data_time.length()-2));
            map.remove("data_time");
            String level = map.get("level").toString();
            map.remove("level");
            //aqi
            Map<String, Object> siteDetail = new HashMap<>();
            Map<String, String> style = new HashMap<>();
            String aqi = map.get("AQI").toString();
            resultMap.put("aqiValue", aqi.substring(0,aqi.length()-2));
            AirqLevel airqLevel = airqLevelService.getOne(new QueryWrapper<AirqLevel>().lambda().eq(AirqLevel::getLevel, level));
            resultMap.put("levelGrade", airqLevel.getLevelGrade());
            style.put("color", airqLevel.getLevelRgb());
            resultMap.put("aqiStyle", style);
            map.remove("AQI");
            //首要污染物
            Collection<Object> c = map.values();
            Object[] obj = c.toArray();
            Arrays.sort(obj);
            String maxAqi = obj[map.size() - 1].toString();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                style = new HashMap<>();
                siteDetail = new HashMap<>();
                siteDetail.put("key", entry.getKey());
                siteDetail.put("value", entry.getValue().toString());
                siteDetail.put("unit", "ug/m³");
                if(entry.getValue().toString().equals(maxAqi)){
                    style.put("color", "#FF0000");
                    siteDetail.put("style",style);
                }
                siteDetails.add(siteDetail);
            }
            resultMap.put("siteDetails",siteDetails);
        }
        return Result.ok(resultMap);
    }
}
