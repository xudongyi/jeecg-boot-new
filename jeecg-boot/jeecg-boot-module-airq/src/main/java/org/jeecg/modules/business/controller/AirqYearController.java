package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.AirqYear;
import org.jeecg.modules.business.service.IAirqYearService;

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
 * @Description: airq_year
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Api(tags="airq_year")
@RestController
@RequestMapping("/year/airqYear")
@Slf4j
public class AirqYearController extends JeecgController<AirqYear, IAirqYearService> {
	@Autowired
	private IAirqYearService airqYearService;
	
	/**
	 * 分页列表查询
	 *
	 * @param airqYear
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "airq_year-分页列表查询")
	@ApiOperation(value="airq_year-分页列表查询", notes="airq_year-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AirqYear airqYear,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AirqYear> queryWrapper = QueryGenerator.initQueryWrapper(airqYear, req.getParameterMap());
		Page<AirqYear> page = new Page<AirqYear>(pageNo, pageSize);
		IPage<AirqYear> pageList = airqYearService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param airqYear
	 * @return
	 */
	@AutoLog(value = "airq_year-添加")
	@ApiOperation(value="airq_year-添加", notes="airq_year-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AirqYear airqYear) {
		airqYearService.save(airqYear);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param airqYear
	 * @return
	 */
	@AutoLog(value = "airq_year-编辑")
	@ApiOperation(value="airq_year-编辑", notes="airq_year-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AirqYear airqYear) {
		airqYearService.updateById(airqYear);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_year-通过id删除")
	@ApiOperation(value="airq_year-通过id删除", notes="airq_year-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		airqYearService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "airq_year-批量删除")
	@ApiOperation(value="airq_year-批量删除", notes="airq_year-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.airqYearService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_year-通过id查询")
	@ApiOperation(value="airq_year-通过id查询", notes="airq_year-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AirqYear airqYear = airqYearService.getById(id);
		if(airqYear==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(airqYear);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param airqYear
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AirqYear airqYear) {
        return super.exportXls(request, airqYear, AirqYear.class, "airq_year");
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
        return super.importExcel(request, response, AirqYear.class);
    }

}
