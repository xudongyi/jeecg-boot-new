package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.AirqQuarter;
import org.jeecg.modules.business.service.IAirqQuarterService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.AirqMonthQualityVO;
import org.jeecg.modules.business.vo.AirqQuarterQualityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: airq_quarter
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Api(tags="airq_quarter")
@RestController
@RequestMapping("/quarter/airqQuarter")
@Slf4j
public class AirqQuarterController extends JeecgController<AirqQuarter, IAirqQuarterService> {
	@Autowired
	private IAirqQuarterService airqQuarterService;
	
	/**
	 * 分页列表查询
	 *
	 * @param airqQuarter
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	/*@AutoLog(value = "airq_quarter-分页列表查询")
	@ApiOperation(value="airq_quarter-分页列表查询", notes="airq_quarter-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AirqQuarter airqQuarter,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AirqQuarter> queryWrapper = QueryGenerator.initQueryWrapper(airqQuarter, req.getParameterMap());
		Page<AirqQuarter> page = new Page<AirqQuarter>(pageNo, pageSize);
		IPage<AirqQuarter> pageList = airqQuarterService.page(page, queryWrapper);
		return Result.ok(pageList);
	}*/

	 /**
	  * 空气质量月报
	  *
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "空气质量季报")
	 @ApiOperation(value="空气质量季报", notes="空气质量季报")
	 @GetMapping(value = "/queryAirqQuarterQuality")
	 public Result<?> queryAirqQuarterQuality(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										   HttpServletRequest req) {
		 String companyIds = req.getParameter("companyIds");
		 String area = req.getParameter("area");
		 //通过选择站点名称获取站点mn号
		 String mn = req.getParameter("mn");
		 String year = req.getParameter("year");
		 String quarter = req.getParameter("quarter");
		 //判断是否选择年和季度
		 if(StrUtil.isEmpty(year)){
		 	//获取当前年
			 int currYear = DateUtil.thisYear();
			 //获取当前季度
			 int currQuarter = DateUtil.quarter(DateUtil.date());
			 if(currQuarter==1){
				 year = StrUtil.toString(currYear-1);
				 quarter = "第四季度";
			 }
		 }
		 Page<AirqQuarterQualityVO> page = new Page<>(pageNo, pageSize);
		 IPage<AirqQuarterQualityVO> pageList = airqQuarterService.queryAirqQuarterQuality(companyIds,page, area,mn,year,quarter);
		 return Result.ok(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param airqQuarter
	 * @return
	 */
	@AutoLog(value = "airq_quarter-添加")
	@ApiOperation(value="airq_quarter-添加", notes="airq_quarter-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AirqQuarter airqQuarter) {
		airqQuarterService.save(airqQuarter);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param airqQuarter
	 * @return
	 */
	@AutoLog(value = "airq_quarter-编辑")
	@ApiOperation(value="airq_quarter-编辑", notes="airq_quarter-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AirqQuarter airqQuarter) {
		airqQuarterService.updateById(airqQuarter);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_quarter-通过id删除")
	@ApiOperation(value="airq_quarter-通过id删除", notes="airq_quarter-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		airqQuarterService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "airq_quarter-批量删除")
	@ApiOperation(value="airq_quarter-批量删除", notes="airq_quarter-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.airqQuarterService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_quarter-通过id查询")
	@ApiOperation(value="airq_quarter-通过id查询", notes="airq_quarter-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AirqQuarter airqQuarter = airqQuarterService.getById(id);
		if(airqQuarter==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(airqQuarter);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param airqQuarter
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AirqQuarter airqQuarter) {
        return super.exportXls(request, airqQuarter, AirqQuarter.class, "airq_quarter");
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
        return super.importExcel(request, response, AirqQuarter.class);
    }

}
