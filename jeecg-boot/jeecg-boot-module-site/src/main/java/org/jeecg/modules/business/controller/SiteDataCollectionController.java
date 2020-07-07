package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SiteDataCollection;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.service.ISiteDataCollectionService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ISiteMonitorPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 数采仪
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
@Api(tags="数采仪")
@RestController
@RequestMapping("/collection/siteDataCollection")
@Slf4j
public class SiteDataCollectionController extends JeecgController<SiteDataCollection, ISiteDataCollectionService> {
	@Autowired
	private ISiteDataCollectionService siteDataCollectionService;

	@Autowired
	private ISiteMonitorPointService siteMonitorPointService;

	 /**
	 * 分页列表查询
	 *
	 * @param siteDataCollection
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "数采仪-分页列表查询")
	@ApiOperation(value="数采仪-分页列表查询", notes="数采仪-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SiteDataCollection siteDataCollection,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SiteDataCollection> queryWrapper = QueryGenerator.initQueryWrapper(siteDataCollection, req.getParameterMap());
		Page<SiteDataCollection> page = new Page<SiteDataCollection>(pageNo, pageSize);
		IPage<SiteDataCollection> pageList = siteDataCollectionService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param siteDataCollection
	 * @return
	 */
	@AutoLog(value = "数采仪-添加")
	@ApiOperation(value="数采仪-添加", notes="数采仪-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SiteDataCollection siteDataCollection) {
		siteDataCollectionService.save(siteDataCollection);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param siteDataCollection
	 * @return
	 */
	@AutoLog(value = "数采仪-编辑")
	@ApiOperation(value="数采仪-编辑", notes="数采仪-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SiteDataCollection siteDataCollection) {
		SiteDataCollection old = siteDataCollectionService.getById(siteDataCollection.getId());
		siteDataCollectionService.updateById(siteDataCollection);
		if(!siteDataCollection.getMnCode().equals(old.getMnCode())){
			//修改站点表的mncode
			SiteMonitorPoint siteMonitorPoint = siteMonitorPointService.getById(siteDataCollection.getMonitorId());
			siteMonitorPoint.setMnCode(siteDataCollection.getMnCode());
			siteMonitorPointService.updateById(siteMonitorPoint);
		}
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数采仪-通过id删除")
	@ApiOperation(value="数采仪-通过id删除", notes="数采仪-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		siteDataCollectionService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "数采仪-批量删除")
	@ApiOperation(value="数采仪-批量删除", notes="数采仪-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.siteDataCollectionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数采仪-通过id查询")
	@ApiOperation(value="数采仪-通过id查询", notes="数采仪-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SiteDataCollection siteDataCollection = siteDataCollectionService.getById(id);
		if(siteDataCollection==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(siteDataCollection);
	}

	 /**
	  * 通过站点id查询
	  *
	  * @param monitorId
	  * @return
	  */
	 @AutoLog(value = "数采仪-通过站点id查询")
	 @ApiOperation(value="数采仪-通过站点id查询", notes="数采仪-通过站点id查询")
	 @GetMapping(value = "/queryByMonitorId")
	 public Result<?> queryByMonitorId(@RequestParam(name="id",required=true) String monitorId) {
		 SiteDataCollection siteDataCollection = siteDataCollectionService.getByMonitorId(monitorId);
		 if(siteDataCollection==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(siteDataCollection);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param siteDataCollection
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SiteDataCollection siteDataCollection) {
        return super.exportXls(request, siteDataCollection, SiteDataCollection.class, "数采仪");
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
        return super.importExcel(request, response, SiteDataCollection.class);
    }

}
