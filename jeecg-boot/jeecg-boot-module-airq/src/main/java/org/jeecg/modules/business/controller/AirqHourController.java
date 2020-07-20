package org.jeecg.modules.business.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.AirqHour;
import org.jeecg.modules.business.service.IAirqHourService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.AirqHourMonitorVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Api(tags="airq_hour")
@RestController
@RequestMapping("/hour/airqHour")
@Slf4j
public class AirqHourController extends JeecgController<AirqHour, IAirqHourService> {
	@Autowired
	private IAirqHourService airqHourService;
	
	/**
	 * 分页列表查询
	 *
	 * @param airqHour
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "airq_hour-分页列表查询")
	@ApiOperation(value="airq_hour-分页列表查询", notes="airq_hour-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AirqHour airqHour,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AirqHour> queryWrapper = QueryGenerator.initQueryWrapper(airqHour, req.getParameterMap());
		Page<AirqHour> page = new Page<AirqHour>(pageNo, pageSize);
		IPage<AirqHour> pageList = airqHourService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	 /**
	  * 分页列表查询
	  *
	  * @param companyIds
	  * @return
	  */
	 @AutoLog(value = "查询站点最新的")
	 @ApiOperation(value="airq_hour-分页列表查询", notes="airq_hour-分页列表查询")
	 @GetMapping(value = "/queryLastAirInfo")
	 public Result<?> queryLastAirInfo(@RequestParam(name="companyIds",required=true) String companyIds) {

		 return Result.ok(airqHourService.queryInfoByCompanyId(Arrays.asList(companyIds.split(","))));
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "查询站点最新的")
	 @ApiOperation(value="airq_hour-分页列表查询", notes="airq_hour-分页列表查询")
	 @GetMapping(value = "/queryLastAirqHour")
	 public Result<?> queryAirqHourMonitor(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										   HttpServletRequest req) throws ParseException {
	 	 String area = req.getParameter("area");
		 String siteName = req.getParameter("siteName");
		 String createTimeBegin = req.getParameter("createTime_begin");
		 String createTimeEnd = req.getParameter("createTime_end");
		 Date dateBegin;
		 Date dateEnd;
		 if(StrUtil.isEmpty(createTimeBegin) && StrUtil.isEmpty(createTimeEnd)) {
			 dateBegin = null;
			 dateEnd = null;
		 }else{
			 dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTimeBegin);
			 dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTimeEnd);
		 }
		 Page<AirqHourMonitorVO> page = new Page<AirqHourMonitorVO>(pageNo, pageSize);
		 IPage<AirqHourMonitorVO> pageList = airqHourService.queryAirqHourMonitor(page, area,siteName,dateBegin,dateEnd);
		 return Result.ok(pageList);
	 }

	/**
	 *   添加
	 *
	 * @param airqHour
	 * @return
	 */
	@AutoLog(value = "airq_hour-添加")
	@ApiOperation(value="airq_hour-添加", notes="airq_hour-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AirqHour airqHour) {
		airqHourService.save(airqHour);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param airqHour
	 * @return
	 */
	@AutoLog(value = "airq_hour-编辑")
	@ApiOperation(value="airq_hour-编辑", notes="airq_hour-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AirqHour airqHour) {
		airqHourService.updateById(airqHour);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_hour-通过id删除")
	@ApiOperation(value="airq_hour-通过id删除", notes="airq_hour-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		airqHourService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "airq_hour-批量删除")
	@ApiOperation(value="airq_hour-批量删除", notes="airq_hour-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.airqHourService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_hour-通过id查询")
	@ApiOperation(value="airq_hour-通过id查询", notes="airq_hour-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AirqHour airqHour = airqHourService.getById(id);
		if(airqHour==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(airqHour);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param airqHour
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AirqHour airqHour) {
        return super.exportXls(request, airqHour, AirqHour.class, "airq_hour");
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
        return super.importExcel(request, response, AirqHour.class);
    }

}
