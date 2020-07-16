package org.jeecg.modules.business.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.AirqLevel;
import org.jeecg.modules.business.service.IAirqLevelService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: airq_level
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Api(tags="airq_level")
@RestController
@RequestMapping("/level/airqLevel")
@Slf4j
public class AirqLevelController extends JeecgController<AirqLevel, IAirqLevelService> {
	@Autowired
	private IAirqLevelService airqLevelService;
	
	/**
	 * 分页列表查询
	 *
	 * @param airqLevel
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "airq_level-分页列表查询")
	@ApiOperation(value="airq_level-分页列表查询", notes="airq_level-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AirqLevel airqLevel,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AirqLevel> queryWrapper = QueryGenerator.initQueryWrapper(airqLevel, req.getParameterMap());
		Page<AirqLevel> page = new Page<AirqLevel>(pageNo, pageSize);
		IPage<AirqLevel> pageList = airqLevelService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param airqLevel
	 * @return
	 */
	@AutoLog(value = "airq_level-添加")
	@ApiOperation(value="airq_level-添加", notes="airq_level-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AirqLevel airqLevel) {
		airqLevelService.save(airqLevel);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param airqLevel
	 * @return
	 */
	@AutoLog(value = "airq_level-编辑")
	@ApiOperation(value="airq_level-编辑", notes="airq_level-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AirqLevel airqLevel) {
		airqLevelService.updateById(airqLevel);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_level-通过id删除")
	@ApiOperation(value="airq_level-通过id删除", notes="airq_level-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		airqLevelService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "airq_level-批量删除")
	@ApiOperation(value="airq_level-批量删除", notes="airq_level-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.airqLevelService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airq_level-通过id查询")
	@ApiOperation(value="airq_level-通过id查询", notes="airq_level-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AirqLevel airqLevel = airqLevelService.getById(id);
		if(airqLevel==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(airqLevel);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param airqLevel
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AirqLevel airqLevel) {
        return super.exportXls(request, airqLevel, AirqLevel.class, "airq_level");
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
        return super.importExcel(request, response, AirqLevel.class);
    }

}
