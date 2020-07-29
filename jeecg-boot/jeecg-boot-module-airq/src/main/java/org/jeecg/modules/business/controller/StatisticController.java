package org.jeecg.modules.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.RET;
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
import java.math.BigDecimal;
import java.sql.Timestamp;
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
     * 趋势分析结果
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
                    if ("hour".equals(dataType)) {
                        String colum = "";
                        if (pollutionType.equals("A34004") || pollutionType.equals("A34002")) {
                            colum = pollutionType + "01_avg";
                        } else {
                            colum = pollutionType + "_avg";
                        }
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
                        String colum = "";
                        if (pollutionType.equals("A34004") || pollutionType.equals("A34002")) {
                            colum = pollutionType + "24_AVG";
                        } else {
                            colum = pollutionType + "_AVG";
                        }
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


    /**
     * 同比分析结果
     *
     * @return
     */
    @AutoLog(value = "同比分析结果")
    @ApiOperation(value = "同比分析结果", notes = "同比分析结果")
    @GetMapping(value = "/querySameCompare")
    public Result<?> querySameCompare(@RequestParam(name = "dataType", required = true) String dataType,
                                      @RequestParam(name = "pollutionType", required = true) String pollutionType,
                                      @RequestParam(name = "searchTime", required = true) String searchTime,
                                      @RequestParam(name = "selectedKeys", required = true) String selectedKeys) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StrUtil.isNotEmpty(selectedKeys)) {
            SiteMonitorPoint siteMonitorPoint = siteMonitorPointService.getOne(new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getId, selectedKeys));
            String curr = searchTime;
            if (siteMonitorPoint != null) {
                String mn = siteMonitorPoint.getMn();
                //获取查询需要的时间
                if (StrUtil.isEmpty(searchTime)) {
                    searchTime = DateUtil.format(DateUtil.date(), "yyyy");
                }
                //获取查询时间的前一个月和第一月
                String startTime = this.getFirstMonth(searchTime);
                String endTime = this.getLastMonth(searchTime);
                //获取去年的同比起始月
                String lastYear = this.getLastYear(searchTime);
                String lastStartTime = this.getFirstMonth(lastYear);
                String lastEndTime = this.getLastMonth(lastYear);
                if ("month".equals(dataType)) {
                    List<Map<String, Object>> airqMonths = airqMonthService.listMaps(new QueryWrapper<AirqMonth>().select("month", pollutionType + "_i").eq("mn", mn).between("month", startTime, endTime).orderByAsc("month"));
                    List<Map<String, Object>> lastAirqMonths = airqMonthService.listMaps(new QueryWrapper<AirqMonth>().select("month", pollutionType + "_i").eq("mn", mn).between("month", lastStartTime, lastEndTime).orderByAsc("month"));
                    //获取查询到的时间和aqi集合
                    List<String> months = new ArrayList<>();
                    List<Double> aqis = new ArrayList<>();
                    List<Double> lastAqis = new ArrayList<>();
                    //判断是否需要计算aqi
                    if (CollectionUtil.isNotEmpty(airqMonths) && CollectionUtil.isNotEmpty(lastAirqMonths)) {
                        if (!"total".equals(pollutionType)) {
                            for (int i = 0; i < airqMonths.size(); i++) {
                                Map<String, Object> map = airqMonths.get(i);
                                Map<String, Object> lastMap = lastAirqMonths.get(i);
                                months.add(StrUtil.toString(map.get("month")));
                                //计算aqi
                                aqis.add(airQualityUtil.getAQI(pollutionType, 24, (Double) map.get(pollutionType + "_i")));
                                lastAqis.add(airQualityUtil.getAQI(pollutionType, 24, (Double) lastMap.get(pollutionType + "_i")));
                            }
                        } else {
                            for (int i = 0; i < airqMonths.size(); i++) {
                                Map<String, Object> map = airqMonths.get(i);
                                Map<String, Object> lastMap = lastAirqMonths.get(i);
                                aqis.add((Double) map.get(pollutionType + "_i"));
                                months.add(StrUtil.toString(map.get("month")));
                                lastAqis.add((Double) lastMap.get(pollutionType + "_i"));
                            }
                        }
                    }
                    //最大的aqi
                    Double aqiMax = Collections.max(aqis);
                    Double lastAqiMax = Collections.max(lastAqis);
                    Double maxaqi = aqiMax > lastAqiMax ? aqiMax : lastAqiMax;
                    maxaqi = (Math.floor(maxaqi / 100) + 1) * 100;
                    resultMap.put("aqiMax", maxaqi);
                    //组织legend
                    List<String> legend = new ArrayList<>();
                    legend.add(lastYear);
                    legend.add(searchTime);
                    legend.add("同比增长率");
                    resultMap.put("legend", legend);
                    //组织X轴数据
                    List<String> monthNums = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(months)) {
                        //组织
                        for (int i = 0; i < months.size(); i++) {
                            String month = months.get(i);
                            String monthNum = month.substring(5, 7) + "月";
                            monthNums.add(monthNum);
                        }
                    }
                    resultMap.put("xDate", monthNums);
                    //组织series
                    //计算同比增长率
                    List<Double> percents = new ArrayList<>();
                    for (int i = 0; i < aqis.size(); i++) {
                        Double aqi = aqis.get(i);
                        Double lastAqi = lastAqis.get(i);
                        double percent = (aqi - lastAqi) / lastAqi;
                        BigDecimal b = BigDecimal.valueOf(percent * 100);
                        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        percents.add(f1);
                    }
                    //最大的增长率
                    Double percentMax = null;
                    Double percentMin = null;
                    if (Collections.max(percents) < 0) {
                        percentMax = (Math.ceil(Collections.max(percents) / 100) - 1) * 100;
                    } else {
                        percentMax = (Math.floor(Collections.max(percents) / 100) + 1) * 100;
                    }
                    if (Collections.min(percents) < 0) {
                        percentMin = (Math.ceil(Collections.min(percents) / 100) - 1) * 100;
                    } else {
                        percentMin = (Math.floor(Collections.min(percents) / 100) + 1) * 100;
                    }

                    resultMap.put("percentMax", percentMax);
                    resultMap.put("percentMin", percentMin);
                    List<Map<String, Object>> series = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        Map<String, Object> serieMap = new HashMap<>();
                        if (i == 0) {
                            serieMap.put("name", lastYear);
                            serieMap.put("type", "bar");
                            serieMap.put("barWidth", 15);
                            serieMap.put("data", lastAqis);
                            serieMap.put("barGap", 0);
                        } else if (i == 1) {
                            serieMap.put("name", searchTime);
                            serieMap.put("type", "bar");
                            serieMap.put("barWidth", 15);
                            serieMap.put("data", aqis);
                            serieMap.put("barGap", 0);
                        } else if (i == 2) {
                            serieMap.put("name", "同比增长率");
                            serieMap.put("type", "line");
                            serieMap.put("yAxisIndex", 1);
                            serieMap.put("data", percents);
                        }
                        series.add(serieMap);
                    }
                    resultMap.put("series", series);
                } else {
                    Map<String, Object> lastAirqYear = airqYearService.getMap(new QueryWrapper<AirqYear>().select("year", pollutionType + "_i").eq("mn", mn).eq("year", lastYear));
                    Map<String, Object> airqYear = airqYearService.getMap(new QueryWrapper<AirqYear>().select("year", pollutionType + "_i").eq("mn", mn).eq("year", searchTime));
                    //组织legend
                    List<String> legend = new ArrayList<>();
                    legend.add("检测值");
                    legend.add("同比增长率");
                    resultMap.put("legend", legend);
                    //组织X轴数据
                    List<String> xData = new ArrayList<>();
                    xData.add(lastYear);
                    xData.add(searchTime);
                    resultMap.put("xDate", xData);
                    //组织data
                    List<Double> aqis = new ArrayList<>();
                    Double lastAqi = null;
                    Double aqi = null;
                    if ("total".equals(pollutionType)) {
                        lastAqi = (Double) lastAirqYear.get(pollutionType + "_i");
                        aqi = (Double) airqYear.get(pollutionType + "_i");
                    } else {
                        //计算sqi
                        lastAqi = airQualityUtil.getAQI(pollutionType, 24, (Double) lastAirqYear.get(pollutionType + "_i"));
                        aqi = airQualityUtil.getAQI(pollutionType, 24, (Double) airqYear.get(pollutionType + "_i"));
                    }
                    aqis.add(lastAqi);
                    aqis.add(aqi);
                    //计算aqi上下限
                    double aqiMax = lastAqi > aqi ? lastAqi : aqi;
                    aqiMax = (Math.floor(aqiMax / 100) + 1) * 100;
                    resultMap.put("aqiMax", aqiMax);
                    //计算同比增长率
                    double percent = (aqi - lastAqi) / lastAqi;
                    BigDecimal b = BigDecimal.valueOf(percent * 100);
                    double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    List<Double> percents = new ArrayList<>();
                    percents.add(f1);
                    double max = 0;
                    double min = 0;
                    if (percent < 0) {
                        min = (Math.ceil(percent / 100) - 1) * 100;
                    } else {
                        max = (Math.floor(percent / 100) + 1) * 100;
                    }
                    resultMap.put("percentMax", max);
                    resultMap.put("percentMin", min);
                    //组织series
                    List<Map<String, Object>> series = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        Map<String, Object> serieMap = new HashMap<>();
                        if (i == 0) {
                            serieMap.put("name", "监测值");
                            serieMap.put("type", "bar");
                            serieMap.put("barWidth", 15);
                            serieMap.put("data", aqis);
                        } else if (i == 1) {
                            serieMap.put("name", "同比增长率");
                            serieMap.put("type", "line");
                            serieMap.put("yAxisIndex", 1);
                            serieMap.put("data", percents);
                        }
                        series.add(serieMap);
                    }
                    resultMap.put("series", series);
                }
            }
        }
        return Result.ok(resultMap);
    }

    @AutoLog(value = "环比分析结果")
    @ApiOperation(value = "环比分析结果", notes = "环比分析结果")
    @GetMapping(value = "/queryChainCompare")
    public Result<?> queryChainCompare(@RequestParam(name = "dataType", required = true) String dataType,
                                       @RequestParam(name = "pollutionType", required = true) String pollutionType,
                                       @RequestParam(name = "searchTime", required = true) String searchTime,
                                       @RequestParam(name = "selectedKeys", required = true) String selectedKeys) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StrUtil.isNotEmpty(selectedKeys)) {
            SiteMonitorPoint siteMonitorPoint = siteMonitorPointService.getOne(new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getId, selectedKeys));
            String mn = siteMonitorPoint.getMn();
            //时间
            String timeStart = null;
            String timeEnd = null;
            String format = "yyyy-MM-dd";
            if ("month".equals(dataType)) {
                format = "yyyy-MM";
            }
            if (StrUtil.isEmpty(searchTime)) {
                if("month".equals(dataType)){
                    timeEnd = DateUtil.format(DateUtil.offsetMonth(DateUtil.date(),-1),format);
                    timeStart = DateUtil.format(DateUtil.offsetMonth(DateUtil.date(),-6),format);
                }else{
                    timeEnd = DateUtil.format(DateUtil.offsetDay(DateUtil.date(), -1), format);
                    timeStart = DateUtil.format(DateUtil.offsetDay(DateUtil.date(), -15), format);
                }
            } else {
                String[] split = searchTime.split(",");
                timeStart = split[0];
                timeEnd = split[1];
            }
            //组织legend
            List<String> legend = new ArrayList<>();
            legend.add("监测值");
            legend.add("环比增长率");
            resultMap.put("legend", legend);
            List<String> dateTimes = new ArrayList<>();
            List<Double> aqis = new ArrayList<>();
            List<Double> percents = new ArrayList<>();
            if ("day".equals(dataType)) {
                QueryWrapper<AirqDay> queryWrapper = new QueryWrapper<AirqDay>().eq("mn", mn);
                if ("AQI".equals(pollutionType)) {
                    queryWrapper.select("aqi", "data_time").between("data_time", timeStart, timeEnd).orderByAsc("data_time");
                    List<AirqDay> airqDays = airqDayServic.list(queryWrapper);
                    for (int i = 0; i < airqDays.size(); i++) {
                        AirqDay airqDay = airqDays.get(i);
                        if (i > 0) {
                            AirqDay lastAirqDay = airqDays.get(i - 1);
                            //计算环比增长率
                            double percent = (airqDay.getAqi() - lastAirqDay.getAqi()) / lastAirqDay.getAqi();
                            BigDecimal b = BigDecimal.valueOf(percent * 100);
                            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            percents.add(f1);
                        }
                        aqis.add(airqDay.getAqi());
                        dateTimes.add(DateUtil.format(airqDay.getDataTime(), format));
                    }
                    resultMap.put("xDate", dateTimes);
                    //aqi最大值
                    getResultMap(resultMap, aqis, percents);
                } else {
                    queryWrapper.select(pollutionType + "_iaqi", "data_time").between("data_time", timeStart, timeEnd).orderByAsc("data_time");
                    List<Map<String, Object>> airqDayMaps = airqDayServic.listMaps(queryWrapper);
                    String code = "";
                    //污染因子编码
                    if (pollutionType.length() > 6) {
                        code = pollutionType.substring(0, 6);
                    } else {
                        code = pollutionType;
                    }
                    if (CollectionUtil.isNotEmpty(airqDayMaps)) {
                        for (int i = 0; i < airqDayMaps.size(); i++) {
                            Map<String, Object> airqDayMap = airqDayMaps.get(i);
                            String data_time = DateUtil.format((Date) airqDayMap.get("data_time"), format);
                            dateTimes.add(data_time);
                            double iaqi = (Double) airqDayMap.get(pollutionType + "_iaqi");
                            //计算aqi
                            double aqi = airQualityUtil.getAQI(code, 24, iaqi);
                            aqis.add(aqi);
                            //计算增长率
                            if (i > 0) {
                                Map<String, Object> lastAirqDay = airqDayMaps.get(i - 1);
                                double lastiaqi = (Double) lastAirqDay.get(pollutionType + "_iaqi");
                                double lastAqi = airQualityUtil.getAQI(code, 24, lastiaqi);
                                //计算环比增长率
                                double percent = (aqi - lastAqi) / lastAqi;
                                BigDecimal b = BigDecimal.valueOf(percent * 100);
                                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                percents.add(f1);
                            }
                        }
                        resultMap.put("xDate", dateTimes);
                        getResultMap(resultMap, aqis, percents);
                    }
                }
            } else {
                QueryWrapper<AirqMonth> queryWrapper = new QueryWrapper<AirqMonth>().eq("mn", mn);
                String colum = "";
                if (pollutionType.length() > 6) {
                    pollutionType = pollutionType.substring(0, 6);
                }
                if ("AQI".equals(pollutionType)) {
                    colum = "total";
                } else {
                    colum = pollutionType;
                }
                List<Map<String, Object>> airqDayMaps = airqMonthService.listMaps(queryWrapper.select(colum + "_i", "month").between("month", timeStart, timeEnd).orderByAsc("month"));
                if (CollectionUtil.isNotEmpty(airqDayMaps)) {
                    for (int i = 0; i < airqDayMaps.size(); i++) {
                        Map<String, Object> airqDayMap = airqDayMaps.get(i);
                        String month = StrUtil.toString(airqDayMap.get("month")) ;
                        dateTimes.add(month);
                        if ("AQI".equals(pollutionType)) {
                            double aqi = (Double) airqDayMap.get(colum + "_i");
                            aqis.add(aqi);
                            //计算增长率
                            if (i > 0) {
                                Map<String, Object> lastAirqMonth = airqDayMaps.get(i - 1);
                                Double lastAqi = (Double) lastAirqMonth.get(colum + "_i");
                                //计算环比增长率
                                double percent = (aqi - lastAqi) / lastAqi;
                                BigDecimal b = BigDecimal.valueOf(percent * 100);
                                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                percents.add(f1);
                            }
                        } else {
                            double iaqi = (Double) airqDayMap.get(colum + "_i");
                            //计算aqi
                            double aqi = airQualityUtil.getAQI(pollutionType, 24, iaqi);
                            //计算增长率
                            if (i > 0) {
                                Map<String, Object> lastAirqMonth = airqDayMaps.get(i - 1);
                                double lastIaqi = (Double) lastAirqMonth.get(colum + "_i");
                                double lastAqi = airQualityUtil.getAQI(pollutionType, 24, lastIaqi);
                                //计算环比增长率
                                double percent = (aqi - lastAqi) / lastAqi;
                                BigDecimal b = BigDecimal.valueOf(percent * 100);
                                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                percents.add(f1);
                            }
                            aqis.add(aqi);
                        }
                    }
                    resultMap.put("xDate", dateTimes);
                    getResultMap(resultMap, aqis, percents);
                }
            }

        }
        return Result.ok(resultMap);
    }

    private void getResultMap(Map<String, Object> resultMap, List<Double> aqis, List<Double> percents) {
        //aqi最大值
        if(CollectionUtil.isNotEmpty(aqis)){
            Double maxaqi = Collections.max(aqis);
            if (maxaqi % 100 != 0) {
                maxaqi = (Math.floor(maxaqi / 100) + 1) * 100;
            }
            resultMap.put("aqiMax", maxaqi);
        }
        if(CollectionUtil.isNotEmpty(percents)){
            Double percentMax = Collections.max(percents);
            Double percentMin = Collections.min(percents);
            if (percentMax % 100 != 0) {
                if (Collections.max(percents) < 0) {
                    percentMax = (Math.ceil(percentMax / 100) - 1) * 100;
                } else {
                    percentMax = (Math.floor(percentMax / 100) + 1) * 100;
                }
            }
            if (percentMin % 100 != 0) {
                if (Collections.min(percents) < 0) {
                    percentMin = (Math.ceil(percentMin / 100) - 1) * 100;
                } else {
                    percentMin = (Math.floor(percentMin / 100) + 1) * 100;
                }
            }
            resultMap.put("percentMax", percentMax);
            resultMap.put("percentMin", percentMin);
        }
        //组织series
        List<Map<String, Object>> series = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Map<String, Object> serieMap = new HashMap<>();
            if (i == 0) {
                serieMap.put("name", "监测值");
                serieMap.put("type", "bar");
                serieMap.put("barWidth", 15);
                serieMap.put("data", aqis);
            } else if (i == 1) {
                serieMap.put("name", "环比增长率");
                serieMap.put("type", "line");
                serieMap.put("yAxisIndex", 1);
                serieMap.put("data", percents);
            }
            series.add(serieMap);
        }
        resultMap.put("series", series);
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

    /**
     * @Description: 获取指定年的一月
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/7/27
     */
    private String getFirstMonth(String searchTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(searchTime));
        calendar.set(Calendar.MONTH, 0);
        Timestamp ts2 = new Timestamp(calendar.getTimeInMillis());
        return format.format(ts2);
    }

    private String getLastYear(String searchTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(searchTime));
        calendar.add(Calendar.YEAR, -1);
        Timestamp ts2 = new Timestamp(calendar.getTimeInMillis());
        return format.format(ts2);
    }

    private String getLastMonth(String searchTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(searchTime));
        calendar.add(Calendar.MONTH, -1);
        Date m = calendar.getTime();
        return format.format(m);
    }
}
