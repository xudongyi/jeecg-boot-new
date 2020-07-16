package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.AirqAqi;
import org.jeecg.modules.business.service.IAirqAqiService;

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
 * @Description: airq_aqi
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Api(tags="airq_aqi")
@RestController
@RequestMapping("/aqi/airqAqi")
@Slf4j
public class AirqAqiController extends JeecgController<AirqAqi, IAirqAqiService> {
	@Autowired
	private IAirqAqiService airqAqiService;
	
	/**
	 * 分页列表查询
	 *
	 * @param airqAqi
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "airq_aqi-分页列表查询")
	@ApiOperation(value="airq_aqi-分页列表查询", notes="airq_aqi-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AirqAqi airqAqi,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AirqAqi> queryWrapper = QueryGenerator.initQueryWrapper(airqAqi, req.getParameterMap());
		Page<AirqAqi> page = new Page<AirqAqi>(pageNo, pageSize);
		IPage<AirqAqi> pageList = airqAqiService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param airqAqi
	 * @return
	 */
	@AutoLog(value = "airq_aqi-添加")
	@ApiOperation(value="airq_aqi-添加", notes="airq_aqi-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AirqAqi airqAqi) {
		airqAqiService.save(airqAqi);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param airqAqi
	 * @return
	 */
	@AutoLog(value = "airq_aqi-编辑")
	@ApiOperation(value="airq_aqi-编辑", notes="airq_aqi-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AirqAqi airqAqi) {
		airqAqiService.updateById(airqAqi);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_aqi-通过id删除")
	@ApiOperation(value="airq_aqi-通过id删除", notes="airq_aqi-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		airqAqiService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "airq_aqi-批量删除")
	@ApiOperation(value="airq_aqi-批量删除", notes="airq_aqi-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.airqAqiService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_aqi-通过id查询")
	@ApiOperation(value="airq_aqi-通过id查询", notes="airq_aqi-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AirqAqi airqAqi = airqAqiService.getById(id);
		if(airqAqi==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(airqAqi);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param airqAqi
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AirqAqi airqAqi) {
        return super.exportXls(request, airqAqi, AirqAqi.class, "airq_aqi");
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
        return super.importExcel(request, response, AirqAqi.class);
    }

}
