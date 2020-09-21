package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.WarnPointRule;
import org.jeecg.modules.business.service.IWarnPointRuleService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: warn_point_rule
 * @Author: jeecg-boot
 * @Date:   2020-09-21
 * @Version: V1.0
 */
@Api(tags="warn_point_rule")
@RestController
@RequestMapping("/wpr/warnPointRule")
@Slf4j
public class WarnPointRuleController extends JeecgController<WarnPointRule, IWarnPointRuleService> {
	@Autowired
	private IWarnPointRuleService warnPointRuleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param warnPointRule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "warn_point_rule-分页列表查询")
	@ApiOperation(value="warn_point_rule-分页列表查询", notes="warn_point_rule-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WarnPointRule warnPointRule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WarnPointRule> queryWrapper = QueryGenerator.initQueryWrapper(warnPointRule, req.getParameterMap());
		Page<WarnPointRule> page = new Page<WarnPointRule>(pageNo, pageSize);
		IPage<WarnPointRule> pageList = warnPointRuleService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param warnPointRule
	 * @return
	 */
	@AutoLog(value = "warn_point_rule-添加")
	@ApiOperation(value="warn_point_rule-添加", notes="warn_point_rule-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WarnPointRule warnPointRule) {
		warnPointRuleService.save(warnPointRule);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param warnPointRule
	 * @return
	 */
	@AutoLog(value = "warn_point_rule-编辑")
	@ApiOperation(value="warn_point_rule-编辑", notes="warn_point_rule-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WarnPointRule warnPointRule) {
		warnPointRuleService.updateById(warnPointRule);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "warn_point_rule-通过id删除")
	@ApiOperation(value="warn_point_rule-通过id删除", notes="warn_point_rule-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		warnPointRuleService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "warn_point_rule-批量删除")
	@ApiOperation(value="warn_point_rule-批量删除", notes="warn_point_rule-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.warnPointRuleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "warn_point_rule-通过id查询")
	@ApiOperation(value="warn_point_rule-通过id查询", notes="warn_point_rule-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WarnPointRule warnPointRule = warnPointRuleService.getById(id);
		if(warnPointRule==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(warnPointRule);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param warnPointRule
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WarnPointRule warnPointRule) {
        return super.exportXls(request, warnPointRule, WarnPointRule.class, "warn_point_rule");
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
        return super.importExcel(request, response, WarnPointRule.class);
    }

}
