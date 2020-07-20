package org.jeecg.modules.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.business.entity.AirqDay;
import org.jeecg.modules.business.entity.AirqLevel;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.service.impl.AirqDayServiceImpl;
import org.jeecg.modules.business.service.impl.AirqLevelServiceImpl;
import org.jeecg.modules.business.service.impl.SiteMonitorPointServiceImpl;
import org.jeecg.modules.business.vo.AirqDayVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    private AirqLevelServiceImpl airqLevelService;


    /**
     * 查询站点名称
     *
     * @param area
     * @return
     */
    @AutoLog(value = "站点报警策略表-通过id查询")
    @ApiOperation(value = "站点报警策略表-通过id查询", notes = "站点报警策略表-通过id查询")
    @GetMapping(value = "/querySiteName")
    public Result<?> querySiteName(@RequestParam(name = "area", required = false) String area) {
        List<Map<String, String>> result = new ArrayList<>();
        List<SiteMonitorPoint> list;
        if (StrUtil.isEmpty(area)) {
            list = siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda());

        } else if (!StrUtil.isEmpty(area)) {
            list = siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda()
                    .eq(SiteMonitorPoint::getArea, area));

        } else if (StrUtil.isEmpty(area)) {
            list = siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda());

        } else {
            list = siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getArea, area));
        }
        list.forEach(siteMonitorPoint -> {
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
    public Result<?> queryEvaluate(@RequestParam(name = "dataType", required = true) String dataType, @RequestParam(name = "searchTime", required = true) String searchTime, @RequestParam(name = "checkedKeys", required = true) String checkedKeys) {
        List<Map<String,Object>> resultList = new ArrayList<>();
        if (StrUtil.isNotEmpty(checkedKeys)) {
            List<String> siteIds = Arrays.asList(checkedKeys.split(","));
            List<SiteMonitorPoint> siteMonitorPoints = siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda().in(SiteMonitorPoint::getId, siteIds));
            if (CollectionUtil.isNotEmpty(siteMonitorPoints)) {
                List<String> mns = siteMonitorPoints.stream().map(SiteMonitorPoint::getMn).collect(Collectors.toList());
                if (dataType.equals("day")) {
                    List<AirqDayVO> airqDays = airqDayServic.findEvaluate(searchTime, mns);
                    //组织饼图数据
                    if(CollectionUtil.isNotEmpty(airqDays)){
                        for (int i = 0; i < airqDays.size(); i++) {
                            Map<String,Object> resultMap = new HashMap<>();
                            AirqDayVO airqDayVO = airqDays.get(i);
                            resultMap.put("name",airqDayVO.getName());
                            resultMap.put("value",airqDayVO.getValue());
                            resultList.add(resultMap);
                        }
                    }
                } else if (dataType.equals("month")) {

                } else if (dataType.equals("year")) {

                }
            }
        }
        return Result.ok(resultList);
    }

}
