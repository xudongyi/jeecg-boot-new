package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.WarnLog;
import org.jeecg.modules.business.service.IWarnLogService;

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
 * @Description: warn_log
 * @Author: jeecg-boot
 * @Date:   2020-09-08
 * @Version: V1.0
 */
@Api(tags="warn_log")
@RestController
@RequestMapping("/warnInfo/warnLog")
@Slf4j
public class WarnLogController extends JeecgController<WarnLog, IWarnLogService> {
	@Autowired
	private IWarnLogService warnLogService;
	
	/**
	 * 分页列表查询
	 *
	 * @param warnLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "warn_log-分页列表查询")
	@ApiOperation(value="warn_log-分页列表查询", notes="warn_log-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WarnLog warnLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WarnLog> queryWrapper = QueryGenerator.initQueryWrapper(warnLog, req.getParameterMap());
		Page<WarnLog> page = new Page<WarnLog>(pageNo, pageSize);
		IPage<WarnLog> pageList = warnLogService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param warnLog
	 * @return
	 */
	@AutoLog(value = "warn_log-添加")
	@ApiOperation(value="warn_log-添加", notes="warn_log-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WarnLog warnLog) {
		warnLogService.save(warnLog);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param warnLog
	 * @return
	 */
	@AutoLog(value = "warn_log-编辑")
	@ApiOperation(value="warn_log-编辑", notes="warn_log-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WarnLog warnLog) {
		warnLogService.updateById(warnLog);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "warn_log-通过id删除")
	@ApiOperation(value="warn_log-通过id删除", notes="warn_log-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		warnLogService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "warn_log-批量删除")
	@ApiOperation(value="warn_log-批量删除", notes="warn_log-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.warnLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "warn_log-通过id查询")
	@ApiOperation(value="warn_log-通过id查询", notes="warn_log-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WarnLog warnLog = warnLogService.getById(id);
		if(warnLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(warnLog);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param warnLog
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WarnLog warnLog) {
        return super.exportXls(request, warnLog, WarnLog.class, "warn_log");
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
        return super.importExcel(request, response, WarnLog.class);
    }

}
