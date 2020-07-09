package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SiteGovFacility;
import org.jeecg.modules.business.service.ISiteGovFacilityService;

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
 * @Description: 监测站治理设施
 * @Author: jeecg-boot
 * @Date:   2020-07-08
 * @Version: V1.0
 */
@Api(tags="监测站治理设施")
@RestController
@RequestMapping("/facility/siteGovFacility")
@Slf4j
public class SiteGovFacilityController extends JeecgController<SiteGovFacility, ISiteGovFacilityService> {
	@Autowired
	private ISiteGovFacilityService siteGovFacilityService;
	
	/**
	 * 分页列表查询
	 *
	 * @param siteGovFacility
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "监测站治理设施-分页列表查询")
	@ApiOperation(value="监测站治理设施-分页列表查询", notes="监测站治理设施-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SiteGovFacility siteGovFacility,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SiteGovFacility> queryWrapper = QueryGenerator.initQueryWrapper(siteGovFacility, req.getParameterMap());
		Page<SiteGovFacility> page = new Page<SiteGovFacility>(pageNo, pageSize);
		IPage<SiteGovFacility> pageList = siteGovFacilityService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param siteGovFacility
	 * @return
	 */
	@AutoLog(value = "监测站治理设施-添加")
	@ApiOperation(value="监测站治理设施-添加", notes="监测站治理设施-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SiteGovFacility siteGovFacility) {
		siteGovFacilityService.save(siteGovFacility);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param siteGovFacility
	 * @return
	 */
	@AutoLog(value = "监测站治理设施-编辑")
	@ApiOperation(value="监测站治理设施-编辑", notes="监测站治理设施-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SiteGovFacility siteGovFacility) {
		siteGovFacilityService.updateById(siteGovFacility);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监测站治理设施-通过id删除")
	@ApiOperation(value="监测站治理设施-通过id删除", notes="监测站治理设施-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		siteGovFacilityService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "监测站治理设施-批量删除")
	@ApiOperation(value="监测站治理设施-批量删除", notes="监测站治理设施-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.siteGovFacilityService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监测站治理设施-通过id查询")
	@ApiOperation(value="监测站治理设施-通过id查询", notes="监测站治理设施-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SiteGovFacility siteGovFacility = siteGovFacilityService.getById(id);
		if(siteGovFacility==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(siteGovFacility);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param siteGovFacility
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SiteGovFacility siteGovFacility) {
        return super.exportXls(request, siteGovFacility, SiteGovFacility.class, "监测站治理设施");
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
        return super.importExcel(request, response, SiteGovFacility.class);
    }

}
