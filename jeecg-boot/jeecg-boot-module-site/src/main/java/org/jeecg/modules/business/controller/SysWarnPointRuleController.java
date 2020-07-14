package org.jeecg.modules.business.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.sf.saxon.expr.instruct.ForEach;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.entity.SysWarnPointRule;
import org.jeecg.modules.business.entity.SysWarnRule;
import org.jeecg.modules.business.service.ISiteMonitorPointService;
import org.jeecg.modules.business.service.ISysWarnPointRuleService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.business.service.ISysWarnRuleService;
import org.jeecg.modules.business.vo.SysWarnPointListVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 站点报警策略表
 * @Author: jeecg-boot
 * @Date:   2020-07-06
 * @Version: V1.0
 */
@Api(tags="站点报警策略表")
@RestController
@RequestMapping("/swpr/sysWarnPointRule")
@Slf4j
public class SysWarnPointRuleController extends JeecgController<SysWarnPointRule, ISysWarnPointRuleService> {
	@Autowired
	private ISysWarnPointRuleService sysWarnPointRuleService;

	@Autowired
	private ISiteMonitorPointService siteMonitorPointService;

	@Autowired
	private ISysWarnRuleService sysWarnRuleService;

	/**
	 * 分页列表查询
	 *
	 * @param sysWarnPointRule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "站点报警策略表-分页列表查询")
	@ApiOperation(value="站点报警策略表-分页列表查询", notes="站点报警策略表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysWarnPointRule sysWarnPointRule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<SysWarnPointListVO> page = new Page<>(pageNo, pageSize);
		IPage<SysWarnPointListVO> pageList = sysWarnPointRuleService.getSysWarnPointList(page);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "站点报警策略表-添加")
	@ApiOperation(value="站点报警策略表-添加", notes="站点报警策略表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JSONObject jsonObject) {
		List<String> monitorIds = jsonObject.getJSONArray("monitorIds").toJavaList(String.class);
		List<String> ruleIds = jsonObject.getJSONArray("ruleIds").toJavaList(String.class);

//		//删除  根据monitorIds
		sysWarnPointRuleService
				.remove(new QueryWrapper<SysWarnPointRule>().lambda().in(SysWarnPointRule::getMonitorId,monitorIds).in(SysWarnPointRule::getRuleId,ruleIds));

		List<SysWarnPointRule> sysWarnPointRules = new ArrayList<>();
		for(String monitor : monitorIds) {
			for (String rule : ruleIds) {
				SysWarnPointRule sysWarnPointRule = new SysWarnPointRule();
				sysWarnPointRule.setMonitorId(monitor);
				sysWarnPointRule.setRuleId(rule);
				String isUsed = sysWarnRuleService.getById(rule).getIsUsed();
				sysWarnPointRule.setIsUsed(isUsed);
				sysWarnPointRules.add(sysWarnPointRule);
			}
		}
		sysWarnPointRuleService.saveBatch(sysWarnPointRules);

		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "站点报警策略表-编辑")
	@ApiOperation(value="站点报警策略表-编辑", notes="站点报警策略表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JSONObject jsonObject) {
		List<String> monitorIds = jsonObject.getJSONArray("monitorIds").toJavaList(String.class);
		List<String> ruleIds = jsonObject.getJSONArray("ruleIds").toJavaList(String.class);
		QueryWrapper<SysWarnPointRule> lambdaQueryWrapper = new QueryWrapper<SysWarnPointRule>();
		if(!monitorIds.isEmpty()){
			lambdaQueryWrapper.in("monitor_id",monitorIds);
		}
		if(!ruleIds.isEmpty())
			lambdaQueryWrapper.in("rule_id",ruleIds);
//		//删除  根据monitorIds
		sysWarnPointRuleService.remove(lambdaQueryWrapper);

		List<SysWarnPointRule> sysWarnPointRules = new ArrayList<>();
		for(String monitor : monitorIds) {
			for (String rule : ruleIds) {
				SysWarnPointRule sysWarnPointRule = new SysWarnPointRule();
				sysWarnPointRule.setMonitorId(monitor);
				sysWarnPointRule.setRuleId(rule);
				String isUsed = sysWarnRuleService.getById(rule).getIsUsed();
				sysWarnPointRule.setIsUsed(isUsed);
				sysWarnPointRules.add(sysWarnPointRule);
			}
		}
		sysWarnPointRuleService.saveBatch(sysWarnPointRules);

		return Result.ok("添加成功！");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "站点报警策略表-通过id删除")
	@ApiOperation(value="站点报警策略表-通过id删除", notes="站点报警策略表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysWarnPointRuleService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "站点报警策略表-批量删除")
	@ApiOperation(value="站点报警策略表-批量删除", notes="站点报警策略表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysWarnPointRuleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "站点报警策略表-通过id查询")
	@ApiOperation(value="站点报警策略表-通过id查询", notes="站点报警策略表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysWarnPointRule sysWarnPointRule = sysWarnPointRuleService.getById(id);
		if(sysWarnPointRule==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sysWarnPointRule);
	}

	 /**
	  * 查询站点名称
	  *
	  * @param siteType
	  * @param area
	  * @return
	  */
	 @AutoLog(value = "站点报警策略表-通过id查询")
	 @ApiOperation(value="站点报警策略表-通过id查询", notes="站点报警策略表-通过id查询")
	 @GetMapping(value = "/querySiteName")
	 public Result<?> querySiteName(@RequestParam(name="siteType",required=false) String siteType,
									@RequestParam(name="area",required=false) String area){
		 List<Map<String,String>> result = new ArrayList<>();
		 List<SiteMonitorPoint> list;
		 if(StrUtil.isEmpty(siteType) && StrUtil.isEmpty(area)) {
			 list = siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda());

		 }else if(StrUtil.isEmpty(siteType) && !StrUtil.isEmpty(area)){
			 list = siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda()
					 .eq(SiteMonitorPoint::getArea,area));

		 }else if(!StrUtil.isEmpty(siteType) && StrUtil.isEmpty(area)) {
			 list = siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda()
					 .eq(SiteMonitorPoint::getSignType, siteType));

		 }else {
			 list = siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getSignType, siteType)
					 .eq(SiteMonitorPoint::getArea,area));
		 }
		 list.forEach(siteMonitorPoint -> {
			 Map<String,String> param = new HashMap<>();
			 param.put("key",siteMonitorPoint.getId());
			 param.put("siteName",siteMonitorPoint.getSiteName());
			 param.put("area",siteMonitorPoint.getArea());
			 param.put("siteType", siteMonitorPoint.getSiteType());
			 result.add(param);
		 });
		 return Result.ok(result);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param sysWarnPointRule
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysWarnPointRule sysWarnPointRule) {
        return super.exportXls(request, sysWarnPointRule, SysWarnPointRule.class, "站点报警策略表");
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
        return super.importExcel(request, response, SysWarnPointRule.class);
    }





}
