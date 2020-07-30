package org.jeecg.modules.business.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.AirqMonth;
import org.jeecg.modules.business.service.IAirqMonthService;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.business.view.SelfEntityExcelView;
import org.jeecg.modules.business.vo.AirqMonthQualityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

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
	 @Autowired
	 private ISysDictService sysDictService;
	 @Autowired
	 private RedisUtil redisUtil;
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
	  * 空气质量月报
	  *
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "空气质量月报")
	 @ApiOperation(value="空气质量月报", notes="空气质量月报")
	 @GetMapping(value = "/queryAirqMonthQuality")
	 public Result<?> queryAirqYearQuality(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										   HttpServletRequest req) {
		 String companyIds = req.getParameter("companyIds");
		 String area = req.getParameter("area");
		 //通过选择站点名称获取站点mn号
		 String mn = req.getParameter("mn");
		 String startTime = null;
		 String endTime = null;
		 String searchTime = req.getParameter("searchTime");
		 if(StrUtil.isNotEmpty(searchTime)){
			 String[] times = searchTime.split(",");
			 startTime = times[0];
			 endTime = times[1];
		 }
		 Page<AirqMonthQualityVO> page = new Page<>(pageNo, pageSize);
		 IPage<AirqMonthQualityVO> pageList = airqMonthService.queryAirqMonthQuality(companyIds,page, area,mn,searchTime,startTime,endTime);
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
   /* @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AirqMonth airqMonth) {
        return super.exportXls(request, airqMonth, AirqMonth.class, "airq_month");
    }*/

	 /**
	  * 导出excel
	  * 空气质量年报
	  *
	  * @param req
	  */
	 @RequestMapping(value = "/exportAirqMonthQuality")
	 public ModelAndView exportAirqMonthQuality(HttpServletRequest req) throws ParseException {
		 String companyIds = req.getParameter("companyIds");
		 String area = req.getParameter("area");
		 //通过选择站点名称获取站点mn号
		 String mn = req.getParameter("mn");
		 String startTime = null;
		 String endTime = null;
		 String searchTime = req.getParameter("searchTime");
		 if(StrUtil.isNotEmpty(searchTime)){
			 String[] times = searchTime.split(",");
			 startTime = times[0];
			 endTime = times[1];
		 }
		 List<AirqMonthQualityVO> exportList = airqMonthService.exportAirqMonthQuality(companyIds, area,mn,searchTime,startTime,endTime);
		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new SelfEntityExcelView(sysDictService,redisUtil));
		 mv.addObject(SelfExcelConstants.TITLE, "空气质量月报"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(SelfExcelConstants.SHEET_NAME, "空气质量月报");
		 mv.addObject(SelfExcelConstants.CLAZZ, AirqMonthQualityVO.class);
		 mv.addObject(SelfExcelConstants.DATA_LIST, exportList);
		 mv.addObject(SelfExcelConstants.FOOTER, "注：缺测指标的浓度及分指数均使用NA标识。");
		 return mv;
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
