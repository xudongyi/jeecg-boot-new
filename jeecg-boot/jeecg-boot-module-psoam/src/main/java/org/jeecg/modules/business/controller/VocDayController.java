package org.jeecg.modules.business.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.CompanyBase;
import org.jeecg.modules.business.entity.CompanyBaseinfo;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.entity.VocDay;
import org.jeecg.modules.business.service.ISiteMonitorPointService;
import org.jeecg.modules.business.service.ICompanyBaseinfoService;
import org.jeecg.modules.business.service.IVocDayService;

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
 * @Description: voc_day
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
@Api(tags="voc_day")
@RestController
@RequestMapping("/vocDay/vocDay")
@Slf4j
public class VocDayController extends JeecgController<VocDay, IVocDayService> {
	@Autowired
	private IVocDayService vocDayService;

	 @Autowired
	 private ISiteMonitorPointService siteMonitorPointService;

	
	/**
	 * 分页列表查询
	 *
	 * @param vocDay
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "voc_day-分页列表查询")
	@ApiOperation(value="voc_day-分页列表查询", notes="voc_day-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(VocDay vocDay,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<VocDay> queryWrapper = QueryGenerator.initQueryWrapper(vocDay, req.getParameterMap());
		Page<VocDay> page = new Page<VocDay>(pageNo, pageSize);
		IPage<VocDay> pageList = vocDayService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param vocDay
	 * @return
	 */
	@AutoLog(value = "voc_day-添加")
	@ApiOperation(value="voc_day-添加", notes="voc_day-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody VocDay vocDay) {
		vocDayService.save(vocDay);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param vocDay
	 * @return
	 */
	@AutoLog(value = "voc_day-编辑")
	@ApiOperation(value="voc_day-编辑", notes="voc_day-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody VocDay vocDay) {
		vocDayService.updateById(vocDay);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "voc_day-通过id删除")
	@ApiOperation(value="voc_day-通过id删除", notes="voc_day-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		vocDayService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "voc_day-批量删除")
	@ApiOperation(value="voc_day-批量删除", notes="voc_day-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.vocDayService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "voc_day-通过id查询")
	@ApiOperation(value="voc_day-通过id查询", notes="voc_day-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		VocDay vocDay = vocDayService.getById(id);
		if(vocDay==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(vocDay);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param vocDay
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, VocDay vocDay) {
        return super.exportXls(request, vocDay, VocDay.class, "voc_day");
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
        return super.importExcel(request, response, VocDay.class);
    }

	 /**
	  * 查询VOC站点名称和mn号
	  * @param companyIds
	  * @return
	  */
	 @AutoLog(value = "查询VOC站点名称")
	 @ApiOperation(value = "voc_day-分页列表查询", notes = "voc_day-分页列表查询")
	 @GetMapping(value = "/querySiteNameAndMn")
	 public Result<?> querySiteNameAndMn(@RequestParam(name = "companyIds", required = true) String companyIds,@RequestParam(name = "siteType", required = true) String siteType) {
		 List<String> idList = Arrays.asList(companyIds.split(","));
		 List<Map<String, String>> result = new ArrayList<>();
		 siteMonitorPointService.list(new QueryWrapper<SiteMonitorPoint>().lambda().eq(SiteMonitorPoint::getSiteType, siteType).in(SiteMonitorPoint::getCompanyId, idList)).forEach(siteMonitorPoint -> {
			 Map<String, String> param = new HashMap<>();
			 param.put("key", siteMonitorPoint.getMn());
			 param.put("value", siteMonitorPoint.getSiteName());
			 param.put("siteId", siteMonitorPoint.getId());
			 param.put("area", siteMonitorPoint.getArea());
			 param.put("companyId", siteMonitorPoint.getCompanyId());
			 result.add(param);
		 });
		 return Result.ok(result);
	 }

	 /**
	  * 查询企业名称
	  *
	  * @return
	  */
	 @AutoLog(value = "查询企业名称")
	 @ApiOperation(value="voc_day-查询企业名称", notes="voc_day-查询企业名称")
	 @GetMapping(value = "/queryCompanyName")
	 public Result<?> queryCompanyName(@RequestParam(name = "companyIds", required = true) String companyIds) {
		 List<Map<String, Object>> companyNames = vocDayService.queryCompanyName(Arrays.asList(companyIds.split(",")));
		 Map<String, Object> result = new HashMap<>();
		 for(Map<String,Object> param:companyNames){
		 	String companyId = param.get("companyId").toString();
		 	String companyName = param.get("companyName").toString();
		 	param.put("key", companyId);
		 	param.put("value", companyName);
		 }
		 result.put("companyNames", companyNames);
		 return Result.ok(result);
	 }

}
