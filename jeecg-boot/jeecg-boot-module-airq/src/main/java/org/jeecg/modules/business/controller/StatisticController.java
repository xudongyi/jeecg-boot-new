package org.jeecg.modules.business.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hmef.attribute.MAPIAttribute;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.service.impl.*;
import org.jeecg.modules.business.utils.AirQualityUtil;
import org.jeecg.modules.business.vo.AirqVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: airq_aqi
 * @Author: jeecg-boot
 * @Date: 2020-07-16
 * @Version: V1.0
 */
@RestController
@RequestMapping("/statistic")
@Slf4j
public class StatisticController {

    @Resource
    private SiteMonitorPointServiceImpl siteMonitorPointService;

    @Resource
    private AirqDayServiceImpl airqDayServic;

    @Resource
    private AirqMonthServiceImpl airqMonthService;

    @Resource
    private AirqYearServiceImpl airqYearService;

    @Resource
    private AirQualityUtil airQualityUtil;

    @Resource
    private AirqLevelServiceImpl airqLevelService;

    @Resource
    private AirqHourServiceImpl airqHourService;


    /**
     * 查询站点名称
     *
     * @param area
     * @return
     */
    @AutoLog(value = "站点报警策略表-通过id查询")
    @ApiOperation(value = "站点报警策略表-通过id查询", notes = "站点报警策略表-通过id查询")
    @GetMapping(value = "/querySiteName")
    public Result<?> querySiteName(@RequestParam(name = "area", required = false) String area, @RequestParam(name = "companyIds", required = true) String companyIds, @RequestParam(name = "siteType", required = true) String siteType) {
        List<Map<String, String>> result = new ArrayList<>();
        LambdaQueryWrapper<SiteMonitorPoint> lambdaWrapper = new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getSiteType, siteType);
        if (StrUtil.isNotEmpty(area)) {
            lambdaWrapper.eq(SiteMonitorPoint::getArea, area);
        }
        if (StrUtil.isNotEmpty(companyIds)) {
            lambdaWrapper.in(SiteMonitorPoint::getCompanyId, Arrays.asList(companyIds.split(",")));
        }
        siteMonitorPointService.list(lambdaWrapper).forEach(siteMonitorPoint -> {
            Map<String, String> param = new HashMap<>();
            param.put("key", siteMonitorPoint.getId());
            param.put("siteName", siteMonitorPoint.getSiteName());
            param.put("area", siteMonitorPoint.getArea());
            param.put("siteType", siteMonitorPoint.getSiteType());
            result.add(param);
        });
        return Result.ok(result);
    }

    /**
     * 评价分析结果
     *
     * @return
     */
    @AutoLog(value = "评价分析结果")
    @ApiOperation(value = "评价分析结果", notes = "评价分析结果")
    @GetMapping(value = "/queryEvaluate")
    public Result<?> queryEvaluate(@RequestParam(name = "dataType", required = true) String dataType,
                                   @RequestParam(name = "pollutionType", required = true) String pollutionType,
                                   @RequestParam(name = "searchTime", required = true) String searchTime,
                                   @RequestParam(name = "checkedKeys", required = true) String checkedKeys) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (StrUtil.isNotEmpty(checkedKeys)) {
            List<String> siteIds = Arrays.asList(checkedKeys.split(","));
            List<SiteMonitorPoint> siteMonitorPoints = siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda().in(SiteMonitorPoint::getId, siteIds));
            if (CollectionUtil.isNotEmpty(siteMonitorPoints)) {
                List<String> mns = siteMonitorPoints.stream().map(SiteMonitorPoint::getMn).collect(Collectors.toList());
                if ("AQI".equals(pollutionType)) {
                    List<AirqVO> airqVOs = null;
                    if ("day".equals(dataType)) {
                        airqVOs = airqDayServic.findEvaluate(searchTime, mns);
                    } else if ("month".equals(dataType)) {
                        airqVOs = airqMonthService.findEvaluate(searchTime, mns);
                    } else if ("year".equals(dataType)) {
                        airqVOs = airqYearService.findEvaluate(searchTime, mns);
                    }
                    //组织饼图数据
                    if (CollectionUtil.isNotEmpty(airqVOs)) {
                        //判断是否都不为0
                        Integer subNum = airqVOs.stream().collect(Collectors.summingInt(AirqVO::getValue));
                        if (subNum > 0) {
                            for (int i = 0; i < airqVOs.size(); i++) {
                                Map<String, Object> resultMap = new HashMap<>();
                                AirqVO airqVO = airqVOs.get(i);
                                resultMap.put("name", airqVO.getName());
                                resultMap.put("value", airqVO.getValue());
                                resultList.add(resultMap);
                            }
                        }
                    }
                } else {
                    if ("day".equals(dataType)) {
                        QueryWrapper<AirqDay> wrapper = new QueryWrapper<AirqDay>().select(pollutionType + "24_AVG", pollutionType + "24_IAQI").in("MN", mns);
                        if (StrUtil.isNotEmpty(searchTime)) {
                            String[] times = searchTime.split(",");
                            String timeStart = times[0];
                            String timeEnd = times[1];
                            wrapper.between("DATA_TIME", timeStart, timeEnd);
                        }
                        List<Map<String, Object>> airqDayMaps = airqDayServic.listMaps(wrapper);
                        resultList = this.parseEvaluate(airqDayMaps, pollutionType, "24_AVG", 24);
                    } else if ("month".equals(dataType)) {
                        QueryWrapper<AirqMonth> wrapper = new QueryWrapper<AirqMonth>().select(pollutionType + "_I").in("MN", mns);
                        if (StrUtil.isNotEmpty(searchTime)) {
                            String[] times = searchTime.split(",");
                            String timeStart = times[0];
                            String timeEnd = times[1];
                            wrapper.between("MONTH", timeStart, timeEnd);
                        }
                        List<Map<String, Object>> airqMonthMaps = airqMonthService.listMaps(wrapper);
                        resultList = this.parseEvaluate(airqMonthMaps, pollutionType, "_I", 24);
                    } else if ("year".equals(dataType)) {
                        QueryWrapper<AirqYear> wrapper = new QueryWrapper<AirqYear>().select(pollutionType + "_I").in("MN", mns);
                        if (StrUtil.isNotEmpty(searchTime)) {
                            String[] times = searchTime.split(",");
                            String timeStart = times[0];
                            String timeEnd = times[1];
                            wrapper.between("YEAR", timeStart, timeEnd);
                        }
                        List<Map<String, Object>> airqYearMaps = airqYearService.listMaps(wrapper);
                        resultList = this.parseEvaluate(airqYearMaps, pollutionType, "_I", 24);
                    }
                }
            }
        }
        return Result.ok(resultList);
    }

    /**
     * 评价分析结果
     *
     * @return
     */
    @AutoLog(value = "趋势分析结果")
    @ApiOperation(value = "趋势分析结果", notes = "趋势分析结果")
    @GetMapping(value = "/queryTrend")
    public Result<?> queryTrend(@RequestParam(name = "dataType", required = true) String dataType,
                                @RequestParam(name = "pollutionType", required = true) String pollutionType,
                                @RequestParam(name = "searchTime", required = true) String searchTime,
                                @RequestParam(name = "checkedKeys", required = true) String checkedKeys) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StrUtil.isNotEmpty(checkedKeys)) {
            List<String> siteIds = Arrays.asList(checkedKeys.split(","));
            List<SiteMonitorPoint> siteMonitorPoints = siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda().in(SiteMonitorPoint::getId, siteIds));
            if (CollectionUtil.isNotEmpty(siteMonitorPoints)) {
                List<String> mns = siteMonitorPoints.stream().map(SiteMonitorPoint::getMn).collect(Collectors.toList());
                List<String> siteNames = siteMonitorPoints.stream().map(SiteMonitorPoint::getSiteName).collect(Collectors.toList());
                resultMap.put("siteName", siteNames);
                if ("AQI".equals(pollutionType)) {
                    String timeStart = null;
                    String timeEnd = null;
                    if ("hour".equals(dataType)) {
                        if (StrUtil.isEmpty(searchTime)) {
                            DateTime yesterday = DateUtil.yesterday();
                            String yesterFormat = DateUtil.format(yesterday, "yyyy-MM-dd");
                            timeStart = yesterFormat + " 09";
                            timeEnd = yesterFormat + " 19";
                        } else {
                            String[] split = searchTime.split(",");
                            timeStart = split[0];
                            timeEnd = split[1];
                        }
                        List<AirqHour> airqHours = airqHourService.list(new QueryWrapper<AirqHour>().select("data_time", "aqi", "mn").in("mn", mns).between("data_time", DateUtil.parse(timeStart, "yyyy-MM-dd HH"), DateUtil.parse(timeEnd, "yyyy-MM-dd HH")).orderByAsc("data_time"));
                        List<Date> dateTimes = airqHours.stream().map(AirqHour::getDataTime).collect(Collectors.toList());
                        List<String> dateString = new ArrayList<>();
                        //转换时间格式
                        for (int i = 0; i < dateTimes.size(); i++) {
                            Date date = dateTimes.get(i);
                            dateString.add(DateUtil.format(date, "HH") + "时");
                        }
                        resultMap.put("dateTimes", dateString);
                        Map<String, List<AirqHour>> airqHourGroup = airqHours.stream().collect(Collectors.groupingBy(AirqHour::getMn));
                        List<Map<String, Object>> series = new ArrayList<>();
                        for (int i = 0; i < siteMonitorPoints.size(); i++) {
                            Map<String, Object> aqiMap = new HashMap<>();
                            SiteMonitorPoint siteMonitorPoint = siteMonitorPoints.get(i);
                            List<AirqHour> siteAirqHours = airqHourGroup.get(siteMonitorPoint.getMn());
                            List<Double> aqis = siteAirqHours.stream().map(AirqHour::getAqi).collect(Collectors.toList());
                            aqiMap.put("data", aqis);
                            aqiMap.put("type", "line");
                            aqiMap.put("name", siteMonitorPoint.getSiteName());
                            aqiMap.put("smooth", true);
                            series.add(aqiMap);
                        }
                        resultMap.put("series", series);
                    } else {
                        if (StrUtil.isEmpty(searchTime)) {
                            DateTime lastWeek = DateUtil.lastWeek();
                            timeStart = DateUtil.format(lastWeek, "yyyy-MM-dd");
                            timeEnd = DateUtil.format(DateUtil.date(), "yyyy-MM-dd");
                        } else {
                            String[] split = searchTime.split(",");
                            timeStart = split[0];
                            timeEnd = split[1];
                        }
                        List<AirqDay> airqDays = airqDayServic.list(new QueryWrapper<AirqDay>().select("data_time", "aqi", "mn").in("mn", mns).between("data_time", DateUtil.parse(timeStart, "yyyy-MM-dd"), DateUtil.parse(timeEnd, "yyyy-MM-dd")).orderByAsc("data_time"));
                        List<Date> dateTimes = airqDays.stream().map(AirqDay::getDataTime).collect(Collectors.toList());
                        List<String> dateString = new ArrayList<>();
                        //转换时间格式
                        for (int i = 0; i < dateTimes.size(); i++) {
                            Date date = dateTimes.get(i);
                            dateString.add(DateUtil.format(date, "yyyy-MM-dd"));
                        }
                        resultMap.put("dateTimes", dateString);
                        Map<String, List<AirqDay>> airqHourGroup = airqDays.stream().collect(Collectors.groupingBy(AirqDay::getMn));
                        List<Map<String, Object>> series = new ArrayList<>();
                        for (int i = 0; i < siteMonitorPoints.size(); i++) {
                            Map<String, Object> aqiMap = new HashMap<>();
                            SiteMonitorPoint siteMonitorPoint = siteMonitorPoints.get(i);
                            List<AirqDay> siteAirqDates = airqHourGroup.get(siteMonitorPoint.getMn());
                            List<Double> aqis = airqDays.stream().map(AirqDay::getAqi).collect(Collectors.toList());
                            aqiMap.put("data", aqis);
                            aqiMap.put("type", "line");
                            aqiMap.put("name", siteMonitorPoint.getSiteName());
                            aqiMap.put("smooth", true);
                            series.add(aqiMap);
                        }
                        resultMap.put("series", series);
                    }
                } else {
                    String timeStart = null;
                    String timeEnd = null;
                    String colum = "";
                    if(pollutionType.equals("A34004")||pollutionType.equals("A34002")){
                        colum = pollutionType + "01_avg";
                    }else {
                        colum = pollutionType + "_avg";
                    }
                    if ("hour".equals(dataType)) {
                        if (StrUtil.isEmpty(searchTime)) {
                            DateTime yesterday = DateUtil.yesterday();
                            String yesterFormat = DateUtil.format(yesterday, "yyyy-MM-dd");
                            timeStart = yesterFormat + " 09";
                            timeEnd = yesterFormat + " 19";
                        } else {
                            String[] split = searchTime.split(",");
                            timeStart = split[0];
                            timeEnd = split[1];
                        }
                        QueryWrapper<AirqHour> wrapper = new QueryWrapper<AirqHour>().select(colum, "data_time", "mn").in("mn", mns).between("data_time", timeStart, timeEnd);
                        List<Map<String, Object>> airqHours = airqHourService.listMaps(wrapper);
                        List<Map<String, Object>> series = new ArrayList<>();
                        if (CollectionUtil.isNotEmpty(airqHours)) {
                            Map<String, List<Map<String, Object>>> grouplist = airqHours.stream().collect(Collectors.groupingBy(e -> e.get("mn").toString()));
                            //查询每个站点下的数据
                            for (int i = 0; i < siteMonitorPoints.size(); i++) {
                                Map<String, Object> aqiMap = new HashMap<>();
                                SiteMonitorPoint siteMonitorPoint = siteMonitorPoints.get(i);
                                String mn = siteMonitorPoint.getMn();
                                List<Map<String, Object>> dayMaps = grouplist.get(mn);
                                List<Double> aqis = new ArrayList<>();
                                List<String> dateStr = new ArrayList<>();
                                //计算aqi
                                for (int j = 0; j < dayMaps.size(); j++) {
                                    Map<String, Object> dayMap = dayMaps.get(j);
                                    double avg = Double.parseDouble(StrUtil.toString(dayMap.get(colum)));
                                    double aqi = airQualityUtil.getAQI(pollutionType, 1, avg);
                                    aqis.add(aqi);
                                    dateStr.add(DateUtil.format(DateUtil.parse(StrUtil.toString(dayMap.get("data_time"))), "HH"));
                                }
                                aqiMap.put("data", aqis);
                                aqiMap.put("type", "line");
                                aqiMap.put("name", siteMonitorPoint.getSiteName());
                                aqiMap.put("smooth", true);
                                series.add(aqiMap);
                                resultMap.put("dateTimes", dateStr);
                            }
                        }
                        resultMap.put("series", series);
                    } else {
                        if (StrUtil.isEmpty(searchTime)) {
                            DateTime lastWeek = DateUtil.lastWeek();
                            timeStart = DateUtil.format(lastWeek, "yyyy-MM-dd");
                            timeEnd = DateUtil.format(DateUtil.date(), "yyyy-MM-dd");
                        } else {
                            String[] split = searchTime.split(",");
                            timeStart = split[0];
                            timeEnd = split[1];
                        }
                        QueryWrapper<AirqDay> wrapper = new QueryWrapper<AirqDay>().select(colum, "data_time", "mn").in("MN", mns).between("data_time", timeStart, timeEnd);
                        List<Map<String, Object>> airqDayMaps = airqDayServic.listMaps(wrapper);
                        List<Map<String, Object>> series = new ArrayList<>();
                        if (CollectionUtil.isNotEmpty(airqDayMaps)) {
                            Map<String, List<Map<String, Object>>> grouplist = airqDayMaps.stream().collect(Collectors.groupingBy(e -> e.get("mn").toString()));
                            //查询每个站点下的数据
                            for (int i = 0; i < siteMonitorPoints.size(); i++) {
                                Map<String, Object> aqiMap = new HashMap<>();
                                SiteMonitorPoint siteMonitorPoint = siteMonitorPoints.get(i);
                                String mn = siteMonitorPoint.getMn();
                                List<Map<String, Object>> dayMaps = grouplist.get(mn);
                                List<Double> aqis = new ArrayList<>();
                                List<String> dateStr = new ArrayList<>();
                                //计算aqi
                                for (int j = 0; j < dayMaps.size(); j++) {
                                    Map<String, Object> dayMap = dayMaps.get(j);
                                    double avg = Double.parseDouble(StrUtil.toString(dayMap.get(colum)));
                                    double aqi = airQualityUtil.getAQI(pollutionType, 24, avg);
                                    aqis.add(aqi);
                                    dateStr.add(DateUtil.format(DateUtil.parse(StrUtil.toString(dayMap.get("data_time"))), "yyyy-MM-dd"));
                                }
                                aqiMap.put("data", aqis);
                                aqiMap.put("type", "line");
                                aqiMap.put("name", siteMonitorPoint.getSiteName());
                                aqiMap.put("smooth", true);
                                series.add(aqiMap);
                                resultMap.put("dateTimes", dateStr);
                            }
                        }
                        resultMap.put("series", series);
                    }
                }
            }
        }
        return Result.ok(resultMap);
    }

    private List<Map<String, Object>> parseEvaluate(List<Map<String, Object>> airqMaps, String pollutionType, String suffix, int type) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(airqMaps)) {
            List<Map<String, Object>> tempList = new ArrayList<>();
            for (int i = 0; i < airqMaps.size(); i++) {
                Map<String, Object> airqDayMap = airqMaps.get(i);
                double avg = Double.parseDouble(StrUtil.toString(airqDayMap.get(pollutionType + suffix)));
                //计算aqi
                double aqi = airQualityUtil.getAQI(pollutionType, type, avg);
                //获取级别
                String level = airQualityUtil.getLevel(aqi);
                //获取级别名
                AirqLevel airqLevel = airqLevelService.getOne(new QueryWrapper<AirqLevel>().lambda().eq(AirqLevel::getLevel, level));
                String levelGrade = airqLevel.getLevelGrade();
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("name", levelGrade);
                tempMap.put("level", level);
                tempList.add(tempMap);
            }
            Map<String, List<Map<String, Object>>> grouplist = tempList.stream().collect(Collectors.groupingBy(e -> e.get("name").toString()));
            List<AirqLevel> airqLevels = airqLevelService.list();
            for (int i = 0; i < airqLevels.size(); i++) {
                int value = 0;
                Map<String, Object> resultMap = new HashMap<>();
                AirqLevel airqLevel = airqLevels.get(i);
                resultMap.put("name", airqLevel.getLevelGrade());
                List<Map<String, Object>> maps = grouplist.get(airqLevel.getLevelGrade());
                if (maps != null) {
                    value = maps.size();
                }
                resultMap.put("value", value);
                resultList.add(resultMap);
            }
        }
        return resultList;
    }
}
