package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SysWarnRule;
import org.jeecg.modules.business.service.ISysWarnRuleService;

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
 * @Description: 报警策略表
 * @Author: jeecg-boot
 * @Date:   2020-07-01
 * @Version: V1.0
 */
@Api(tags="报警策略表")
@RestController
@RequestMapping("/swr/sysWarnRule")
@Slf4j
public class SysWarnRuleController extends JeecgController<SysWarnRule, ISysWarnRuleService> {
	@Autowired
	private ISysWarnRuleService sysWarnRuleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysWarnRule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "报警策略表-分页列表查询")
	@ApiOperation(value="报警策略表-分页列表查询", notes="报警策略表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysWarnRule sysWarnRule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysWarnRule> queryWrapper = QueryGenerator.initQueryWrapper(sysWarnRule, req.getParameterMap());
		Page<SysWarnRule> page = new Page<SysWarnRule>(pageNo, pageSize);
		IPage<SysWarnRule> pageList = sysWarnRuleService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysWarnRule
	 * @return
	 */
	@AutoLog(value = "报警策略表-添加")
	@ApiOperation(value="报警策略表-添加", notes="报警策略表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysWarnRule sysWarnRule) {
		sysWarnRuleService.save(sysWarnRule);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysWarnRule
	 * @return
	 */
	@AutoLog(value = "报警策略表-编辑")
	@ApiOperation(value="报警策略表-编辑", notes="报警策略表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysWarnRule sysWarnRule) {
		sysWarnRuleService.updateById(sysWarnRule);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报警策略表-通过id删除")
	@ApiOperation(value="报警策略表-通过id删除", notes="报警策略表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysWarnRuleService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "报警策略表-批量删除")
	@ApiOperation(value="报警策略表-批量删除", notes="报警策略表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysWarnRuleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报警策略表-通过id查询")
	@ApiOperation(value="报警策略表-通过id查询", notes="报警策略表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysWarnRule sysWarnRule = sysWarnRuleService.getById(id);
		if(sysWarnRule==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sysWarnRule);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysWarnRule
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysWarnRule sysWarnRule) {
        return super.exportXls(request, sysWarnRule, SysWarnRule.class, "报警策略表");
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
        return super.importExcel(request, response, SysWarnRule.class);
    }

}
