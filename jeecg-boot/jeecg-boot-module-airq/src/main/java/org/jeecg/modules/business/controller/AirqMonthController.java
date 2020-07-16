package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.AirqMonth;
import org.jeecg.modules.business.service.IAirqMonthService;

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
 * @Description: airq_month
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Api(tags="airq_month")
@RestController
@RequestMapping("/month/airqMonth")
@Slf4j
public class AirqMonthController extends JeecgController<AirqMonth, IAirqMonthService> {
	@Autowired
	private IAirqMonthService airqMonthService;
	
	/**
	 * 分页列表查询
	 *
	 * @param airqMonth
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "airq_month-分页列表查询")
	@ApiOperation(value="airq_month-分页列表查询", notes="airq_month-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AirqMonth airqMonth,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AirqMonth> queryWrapper = QueryGenerator.initQueryWrapper(airqMonth, req.getParameterMap());
		Page<AirqMonth> page = new Page<AirqMonth>(pageNo, pageSize);
		IPage<AirqMonth> pageList = airqMonthService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param airqMonth
	 * @return
	 */
	@AutoLog(value = "airq_month-添加")
	@ApiOperation(value="airq_month-添加", notes="airq_month-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AirqMonth airqMonth) {
		airqMonthService.save(airqMonth);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param airqMonth
	 * @return
	 */
	@AutoLog(value = "airq_month-编辑")
	@ApiOperation(value="airq_month-编辑", notes="airq_month-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AirqMonth airqMonth) {
		airqMonthService.updateById(airqMonth);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_month-通过id删除")
	@ApiOperation(value="airq_month-通过id删除", notes="airq_month-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		airqMonthService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "airq_month-批量删除")
	@ApiOperation(value="airq_month-批量删除", notes="airq_month-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.airqMonthService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_month-通过id查询")
	@ApiOperation(value="airq_month-通过id查询", notes="airq_month-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AirqMonth airqMonth = airqMonthService.getById(id);
		if(airqMonth==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(airqMonth);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param airqMonth
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AirqMonth airqMonth) {
        return super.exportXls(request, airqMonth, AirqMonth.class, "airq_month");
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
        return super.importExcel(request, response, AirqMonth.class);
    }

}
