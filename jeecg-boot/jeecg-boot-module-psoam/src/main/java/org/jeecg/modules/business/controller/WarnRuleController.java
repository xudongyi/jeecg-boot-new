package org.jeecg.modules.business.controller;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.entity.WarnRule;
import org.jeecg.modules.business.service.IWarnRuleService;

import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.WarnPointRule;
import org.jeecg.modules.business.vo.WarnRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: warn_rule
 * @Author: jeecg-boot
 * @Date:   2020-09-15
 * @Version: V1.0
 */
@Api(tags="warn_rule")
@RestController
@RequestMapping("/wr/warnRule")
@Slf4j
public class WarnRuleController extends JeecgController<WarnRule, IWarnRuleService> {
	@Autowired
	private IWarnRuleService warnRuleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param warnRule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "warn_rule-分页列表查询")
	@ApiOperation(value="warn_rule-分页列表查询", notes="warn_rule-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WarnRule warnRule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String  area = req.getParameter("area");
		String  companyId = req.getParameter("companyId");
		String  companyIds = req.getParameter("companyIds");
		String  mn = req.getParameter("mn");
		String  siteType = req.getParameter("siteType");

		companyIds = StrUtil.isEmpty(companyId)?companyIds:companyId; //可以查询到的企业
		List<String> companyIdList = Arrays.asList(companyIds.split(","));
		List<Map<String,Object>> mnInfoList = warnRuleService.queryMnInfo(companyIdList, area,siteType);

		//所有查出来的mn
		List<String> allMn = new ArrayList<>();
		for (Map<String,Object> oneRecord:mnInfoList){
			allMn.add(oneRecord.get("mn").toString());
		}
		List<String> mns = StrUtil.isEmpty(mn)? allMn:Arrays.asList(mn);
		//关联查询中间表和策略表
		List<WarnRuleVO> warnRuleVOS = warnRuleService.queryWarnRuleVO(mns);
		Map<String,List<WarnRuleVO>> warnRules = new HashMap<>();
		for(String oneMn:allMn){
			warnRules.putIfAbsent(oneMn, new ArrayList<>());
		}
		for(WarnRuleVO warnRuleVO:warnRuleVOS){
			warnRules.get(warnRuleVO.getMn()).add(warnRuleVO);
		}
		//汇总策略查看
		List<WarnPointRule> warnPointRules = new ArrayList<>();
		//处理全量数据map
		for(Map.Entry<String,List<WarnRuleVO>> mp:warnRules.entrySet()){
			String realTimeOver = deal(mp.getValue(), "0");
			String hourOver = deal(mp.getValue(), "1");
			String dayOver = deal(mp.getValue(), "2");
			String offLine = deal(mp.getValue(), "3");
			String deviceFail = deal(mp.getValue(), "4");
			String constant = deal(mp.getValue(), "6");
			String measureDistance = deal(mp.getValue(), "5");
			String dataAbnormal = deal(mp.getValue(), "7");
			String companyName = null;
			String siteName = null;
			String type = null;
			//获取mn对应公司名称和站点名称
			for (Map<String,Object> e:mnInfoList){
				if(mp.getKey().equals(e.get("mn").toString())){
					companyName = e.get("companyName").toString();
					siteName = e.get("siteName").toString();
					type = e.get("siteType").toString();
				}
			}
			WarnPointRule warnPointRule = new WarnPointRule();
			warnPointRule.setId(mp.getKey());
			warnPointRule.setCompanyName(companyName);
			warnPointRule.setSiteName(siteName);
			warnPointRule.setSiteType(type);
			warnPointRule.setRealTimeOver(realTimeOver);
			warnPointRule.setHourOver(hourOver);
			warnPointRule.setDayOver(dayOver);
			warnPointRule.setOffLine(offLine);
			warnPointRule.setDeviceFail(deviceFail);
			warnPointRule.setConstant(constant);
			warnPointRule.setMeasureDistance(measureDistance);
			warnPointRule.setDataAbnormal(dataAbnormal);
			warnPointRules.add(warnPointRule);
		}
		warnPointRules.sort(new Comparator<WarnPointRule>() {
			@Override
			public int compare(WarnPointRule o1, WarnPointRule o2) {
				return o1.getCompanyName().compareTo(o2.getCompanyName());
			}
		});
		return Result.ok(warnPointRules);
	}

	public String deal(List<WarnRuleVO> list,String ruleType){
		List<WarnRuleVO> newRules = new ArrayList<>();
		for(WarnRuleVO warnRuleVO:list){
			if(ruleType.equals(warnRuleVO.getRuleType())){
				newRules.add(warnRuleVO);
			}
		}
		int size = 0;
		if(newRules.isEmpty()){
			//2 未设置
			return "2";
		}else {
			for(WarnRuleVO ruleVO: newRules){
				//0 启用  1 停用
				if ("0".equals(ruleVO.getIsUsed())){
					size++;
				}
			}
			if(size == 0){
				//1 停用
				return "1";
			}else {
				//0 启用
				return "0";
			}
		}
	}
	
	/**
	 *   添加
	 *
	 * @param warnRule
	 * @return
	 */
	@AutoLog(value = "warn_rule-添加")
	@ApiOperation(value="warn_rule-添加", notes="warn_rule-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WarnRule warnRule) {
		warnRuleService.save(warnRule);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param warnRule
	 * @return
	 */
	@AutoLog(value = "warn_rule-编辑")
	@ApiOperation(value="warn_rule-编辑", notes="warn_rule-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WarnRule warnRule) {
		warnRuleService.updateById(warnRule);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "warn_rule-通过id删除")
	@ApiOperation(value="warn_rule-通过id删除", notes="warn_rule-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		warnRuleService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "warn_rule-批量删除")
	@ApiOperation(value="warn_rule-批量删除", notes="warn_rule-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.warnRuleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "warn_rule-通过id查询")
	@ApiOperation(value="warn_rule-通过id查询", notes="warn_rule-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WarnRule warnRule = warnRuleService.getById(id);
		if(warnRule==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(warnRule);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param warnRule
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WarnRule warnRule) {
        return super.exportXls(request, warnRule, WarnRule.class, "warn_rule");
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
        return super.importExcel(request, response, WarnRule.class);
    }

}
