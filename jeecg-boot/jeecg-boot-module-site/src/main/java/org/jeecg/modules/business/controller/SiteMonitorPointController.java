package org.jeecg.modules.business.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.entity.CompanyBase;
import org.jeecg.modules.business.entity.SiteDataCollection;
import org.jeecg.modules.business.service.ICompanyBaseService;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.service.ISiteDataCollectionService;
import org.jeecg.modules.business.service.ISiteMonitorPointService;
import org.jeecg.modules.business.vo.SiteMonitorPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

 /**
 * @Description: 监测站点表
 * @Author: jeecg-boot
 * @Date:   2020-07-02
 * @Version: V1.0
 */
@Api(tags="监测站点表")
@RestController
@RequestMapping("/site/siteMonitorPoint")
@Slf4j
public class SiteMonitorPointController extends JeecgController<SiteMonitorPoint, ISiteMonitorPointService> {
	@Autowired
	private ISiteMonitorPointService siteMonitorPointService;

	@Autowired
	private ICompanyBaseService companyBaseService;

	@Autowired
	private ISiteDataCollectionService siteDataCollectionService;

	 /**
	 * 分页列表查询
	 *
	 * @param siteMonitorPoint
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "监测站点表-分页列表查询")
	@ApiOperation(value="监测站点表-分页列表查询", notes="监测站点表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SiteMonitorPoint siteMonitorPoint,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String siteType = req.getParameter("siteType");
		String siteName = req.getParameter("siteName");
		String companyId = req.getParameter("companyId");
		String siteLevel = req.getParameter("siteLevel");
		String area = req.getParameter("area");
		String siteState = req.getParameter("siteState");
		String mn = req.getParameter("mn");
		if(StrUtil.isEmpty(siteType)&&StrUtil.isEmpty(siteState)){
			//只查看启用状态的
			siteState = "1";
		}
		Page<SiteMonitorPointVO> page = new Page<>(pageNo, pageSize);
		IPage<SiteMonitorPointVO> pageList = siteMonitorPointService.getSiteMonitorPointList(page,siteType,siteState,siteName,companyId,siteLevel,area,mn);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param siteMonitorPoint
	 * @return
	 */
	@AutoLog(value = "监测站点表-添加")
	@ApiOperation(value="监测站点表-添加", notes="监测站点表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SiteMonitorPoint siteMonitorPoint) {
		siteMonitorPointService.save(siteMonitorPoint);
		//判断是否存在数采仪
		SiteDataCollection siteDataCollection = siteDataCollectionService.findByMnCode(siteMonitorPoint.getMn());
		//没有则新增
		if(siteDataCollection==null){
			siteDataCollection = new SiteDataCollection();
			siteDataCollection.setMn(siteMonitorPoint.getMn());
			siteDataCollection.setMonitorId(siteMonitorPoint.getId());
			siteDataCollectionService.save(siteDataCollection);
		}else{
			//有则更新
			//更新数采仪中的站点id
			siteDataCollection.setMonitorId(siteMonitorPoint.getId());
			siteDataCollectionService.updateById(siteDataCollection);
		}
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param siteMonitorPoint
	 * @return
	 */
	@AutoLog(value = "监测站点表-编辑")
	@ApiOperation(value="监测站点表-编辑", notes="监测站点表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SiteMonitorPoint siteMonitorPoint) {
		siteMonitorPointService.updateById(siteMonitorPoint);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监测站点表-通过id删除")
	@ApiOperation(value="监测站点表-通过id删除", notes="监测站点表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		siteMonitorPointService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "监测站点表-批量删除")
	@ApiOperation(value="监测站点表-批量删除", notes="监测站点表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.siteMonitorPointService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监测站点表-通过id查询")
	@ApiOperation(value="监测站点表-通过id查询", notes="监测站点表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SiteMonitorPoint siteMonitorPoint = siteMonitorPointService.getById(id);
		if(siteMonitorPoint==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(siteMonitorPoint);
	}

    /**
   /* * 导出excel
    *
    * @param request
    * @param siteMonitorPoint
    *//*
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SiteMonitorPoint siteMonitorPoint) {
        return super.exportXls(request, siteMonitorPoint, SiteMonitorPoint.class, "监测站点表");
    }

    *//**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    *//*
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SiteMonitorPoint.class);
    }*/


	 /**
	  * 查询企业名称
	  *
	  * @return
	  */
	 @AutoLog(value = "监测站点表-查询企业名称")
	 @ApiOperation(value="监测站点表-查询企业名称", notes="监测站点表-查询企业名称")
	 @GetMapping(value = "/queryCompanyName")
	 public Result<?> queryCompanyName() {
		 List<Map<String,String>> result = new ArrayList<>();
		 companyBaseService.list(new QueryWrapper<CompanyBase>().lambda()).forEach(companyBase -> {
			 Map<String,String> param = new HashMap<>();
			 param.put("key",companyBase.getId());
			 param.put("value",companyBase.getCompanyName());

			 result.add(param);
		 });

		 return Result.ok(result);
	 }

	 /**
	  *   查询菜单及其状态信息
	  *
	  * @param
	  * @return 菜单及其状态信息
	  */
	 @AutoLog(value = "监测站点表-查询菜单及其状态信息")
	 @ApiOperation(value="查询菜单及其状态信息", notes="根据企业ID查询")
	 @GetMapping(value = "/menus")
	 public Result<?> queryMenus(@RequestParam(name = "monitorId", required = true) String monitorId) {
		 Map<String,List<Map<String,String>>> result = new HashMap<>();
		 result.put("menus" , siteMonitorPointService.getMenus(monitorId));
		 return Result.ok(result);
	 }

	 @AutoLog(value = "监测站点表-查询坐标")
	 @ApiOperation(value = "监测站点表-查询坐标", notes = "监测站点表-查询坐标")
	 @GetMapping(value = "/loadBaiduMap")
	 public Result<?> loadBaiduMap(@RequestParam(name = "address", required = true) String address) {
		 String output = "json";
		 String ak = "IFtTlZIkHXmWAeA4Dupd2AtYdbOmIhxL";
		 String pathUrl = "http://api.map.baidu.com/geocoding/v3/";
		 HashMap<String, Object> paramMap = new HashMap<>();
		 paramMap.put("ak", ak);
		 paramMap.put("address", address);
		 paramMap.put("output", "json");
		 String result= HttpUtil.get(pathUrl, paramMap);
		 JSONObject jsonObject = JSONObject.parseObject(result);
		 String status = jsonObject.getString("status");
		 jsonObject.getJSONObject("");
		 if(!"0".equals(status)){
			 return Result.error(jsonObject.getString("message"));
		 }
		 return Result.ok(jsonObject.getJSONObject("result").getJSONObject("location"));
	 }
}
