package org.jeecg.modules.business.controller;

import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.entity.SysWarnLog;
import org.jeecg.modules.business.service.ISiteMonitorPointService;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.business.service.ISysWarnLogService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.view.SelfEntityExcelView;
import org.jeecg.modules.business.vo.AirqYearQualityVO;
import org.jeecg.modules.business.vo.WarnLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 报警日志表
 * @Author: jeecg-boot
 * @Date: 2020-07-30
 * @Version: V1.0
 */
@Api(tags = "报警日志表")
@RestController
@RequestMapping("/warn/sysWarnLog")
@Slf4j
public class SysWarnLogController extends JeecgController<SysWarnLog, ISysWarnLogService> {
    @Autowired
    private ISysWarnLogService sysWarnLogService;

    @Autowired
    private ISiteMonitorPointService siteMonitorPointService;

    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param sysWarnLog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "报警日志表-分页列表查询")
    @ApiOperation(value = "报警日志表-分页列表查询", notes = "报警日志表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysWarnLog sysWarnLog,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SysWarnLog> queryWrapper = QueryGenerator.initQueryWrapper(sysWarnLog, req.getParameterMap());
        Page<SysWarnLog> page = new Page<SysWarnLog>(pageNo, pageSize);
        IPage<SysWarnLog> pageList = sysWarnLogService.page(page, queryWrapper);
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
    @AutoLog(value = "报警日志表-分页列表查询")
    @ApiOperation(value = "报警日志表-分页列表查询", notes = "报警日志表-分页列表查询")
    @GetMapping(value = "/warnLogList")
    public Result<?> queryWarnLog(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req) {
        String companyIds = req.getParameter("companyIds");
        String area = req.getParameter("area");
        String monitorId = req.getParameter("id");
        String flag = req.getParameter("flag");
        String dateBegin = req.getParameter("warnTime_begin");
        String dateEnd = req.getParameter("warnTime_end");
        Page<WarnLogVO> page = new Page<>(pageNo, pageSize);
        IPage<WarnLogVO> pageList = sysWarnLogService.queryWarnLogInfo(companyIds, page, area, monitorId, dateBegin, dateEnd, flag);
        return Result.ok(pageList);
    }

    /**
     * 分页列表查询
     *
     * @return
     */
    @AutoLog(value = "查询站点最新的")
    @ApiOperation(value = "airq_hour-分页列表查询", notes = "airq_hour-分页列表查询")
    @GetMapping(value = "/querySiteNameAndId")
    public Result<?> querySiteNameAndId(@RequestParam(name = "companyIds", required = true) String companyIds) {
        List<String> idList = Arrays.asList(companyIds.split(","));
        List<Map<String, String>> result = new ArrayList<>();
        siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getSiteType, 3).in(SiteMonitorPoint::getCompanyId, idList)).forEach(siteMonitorPoint -> {
            Map<String, String> param = new HashMap<>();
            param.put("key", siteMonitorPoint.getId());
            param.put("value", siteMonitorPoint.getSiteName());
            param.put("area", siteMonitorPoint.getArea());
            result.add(param);
        });
        return Result.ok(result);
    }

    /**
     * 添加
     *
     * @param sysWarnLog
     * @return
     */
    @AutoLog(value = "报警日志表-添加")
    @ApiOperation(value = "报警日志表-添加", notes = "报警日志表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysWarnLog sysWarnLog) {
        sysWarnLogService.save(sysWarnLog);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysWarnLog
     * @return
     */
    @AutoLog(value = "报警日志表-编辑")
    @ApiOperation(value = "报警日志表-编辑", notes = "报警日志表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SysWarnLog sysWarnLog) {
        sysWarnLogService.updateById(sysWarnLog);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "报警日志表-通过id删除")
    @ApiOperation(value = "报警日志表-通过id删除", notes = "报警日志表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysWarnLogService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "报警日志表-批量删除")
    @ApiOperation(value = "报警日志表-批量删除", notes = "报警日志表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysWarnLogService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "报警日志表-通过id查询")
    @ApiOperation(value = "报警日志表-通过id查询", notes = "报警日志表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysWarnLog sysWarnLog = sysWarnLogService.getById(id);
        if (sysWarnLog == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(sysWarnLog);
    }

    /**
     * 导出excel
     * 报警信息
     *
     * @param req
     */
    @RequestMapping(value = "/exportSysWarnLog")
    public ModelAndView exportSysWarnLog(HttpServletRequest req) {
        String companyIds = req.getParameter("companyIds");
        String area = req.getParameter("area");
        String monitorId = req.getParameter("id");
        String flag = req.getParameter("flag");
        String dateBegin = req.getParameter("warnTime_begin");
        String dateEnd = req.getParameter("warnTime_end");
        List<WarnLogVO> exportList = sysWarnLogService.exportWarnLogInfo(companyIds, area, monitorId, flag, dateBegin, dateEnd);
        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new SelfEntityExcelView(sysDictService, redisUtil));
        mv.addObject(SelfExcelConstants.TITLE, "报警信息"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(SelfExcelConstants.SHEET_NAME, "报警信息");
        mv.addObject(SelfExcelConstants.CLAZZ, WarnLogVO.class);
        mv.addObject(SelfExcelConstants.DATA_LIST, exportList);
        mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");

        return mv;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysWarnLog
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysWarnLog sysWarnLog) {
        return super.exportXls(request, sysWarnLog, SysWarnLog.class, "报警日志表");
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
        return super.importExcel(request, response, SysWarnLog.class);
    }

    /**
     * 空气质量app 报警信息
     *
     * @return
     */
    @AutoLog(value = "报警信息")
    @ApiOperation(value = "报警信息", notes = "报警信息")
    @GetMapping(value = "/queryWarnInfo")
    public Result<?> queryWarnInfo(@RequestParam(name = "companyIds", required = true) String companyIds
            , @RequestParam(name = "monitorId", required = false) String monitorId
            , @RequestParam(name = "beginTime", required = false) String beginTime
            , @RequestParam(name = "endTime", required = false) String endTime
            , @RequestParam(name = "flag", required = false) String flag) {
        List<Map<String, Object>> warnInfoList =
                sysWarnLogService.queryWarnInfo(Arrays.asList(companyIds.split(",")), monitorId, beginTime, endTime, flag);
        Map<String, Object> result = new HashMap<>();
        for (Map<String, Object> param : warnInfoList) {
            String value = sysDictService.queryDictTextByKey("flag", param.get("flag").toString());
            param.put("flagName", value);

        }
        result.put("dataList", warnInfoList);
        return Result.ok(result);
    }

    /**
     * 大气首页 报警信息
     *
     * @return
     */
    @AutoLog(value = "报警信息")
    @ApiOperation(value = "报警信息", notes = "报警信息")
    @GetMapping(value = "/queryAlarmInfo")
    public Result<?> queryAlarmInfo(@RequestParam(name = "companyIds", required = true) String companyIds) {
        List<Map<String, Object>> alarmInfoList =
                sysWarnLogService.queryAlarmInfo(Arrays.asList(companyIds.split(",")));
        Map<String, Object> result = new HashMap<>();
        for (Map<String, Object> param : alarmInfoList) {
            String value = sysDictService.queryDictTextByKey("flagName", param.get("flag").toString());
            param.put("flagName", value);

        }
        result.put("dataList", alarmInfoList);
        return Result.ok(result);
    }

    /**
     * 大气首页 站点实时统计
     *
     * @return
     */
    @AutoLog(value = "报警信息")
    @ApiOperation(value = "报警信息", notes = "报警信息")
    @GetMapping(value = "/querySiteState")
    public Result<?> querySiteState(@RequestParam(name = "companyIds", required = true) String companyIds, @RequestParam(name = "startTime", required = true) String startTime, @RequestParam(name = "endTime", required = true) String endTime) {
		List<Integer> counts = new ArrayList<>();
		if (StrUtil.isNotEmpty(companyIds)) {
			LambdaQueryWrapper<SiteMonitorPoint> lambdaWrapper = new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getSiteType, "3");
			lambdaWrapper.in(SiteMonitorPoint::getCompanyId, Arrays.asList(companyIds.split(",")));
			int siteSum = siteMonitorPointService.count(lambdaWrapper);
			List<Map<String, Object>> warns = sysWarnLogService.querySiteState(Arrays.asList(companyIds.split(",")),startTime,endTime);
            if (CollectionUtil.isNotEmpty(warns)) {

				//计算正常的数量
				List<Map<String, Object>> sites = sysWarnLogService.querySiteNum(Arrays.asList(companyIds.split(",")),startTime,endTime);
				if(CollectionUtil.isNotEmpty(sites)){
					counts.add(siteSum-sites.size()<0?0:siteSum-sites.size());
				}else{
					counts.add(siteSum);
				}
                for (int i = 0; i < warns.size(); i++) {
                    Map<String, Object> map = warns.get(i);
					counts.add(Integer.parseInt(StrUtil.toString(map.get("siteNum"))));
                }
            }else{
				counts.add(siteSum);
				counts.add(0);
				counts.add(0);
				counts.add(0);
				counts.add(0);
			}
        }
        return Result.ok(counts);
    }
}
