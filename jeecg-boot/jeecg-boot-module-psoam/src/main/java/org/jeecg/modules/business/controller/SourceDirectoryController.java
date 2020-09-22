package org.jeecg.modules.business.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SourceDirectory;
import org.jeecg.modules.business.service.ISourceDirectoryService;

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
 * @Description: 污染源名录库
 * @Author: jeecg-boot
 * @Date:   2020-09-15
 * @Version: V1.0
 */
@Api(tags="污染源名录库")
@RestController
@RequestMapping("/sourceDirectory/sourceDirectory")
@Slf4j
public class SourceDirectoryController extends JeecgController<SourceDirectory, ISourceDirectoryService> {
	@Autowired
	private ISourceDirectoryService sourceDirectoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sourceDirectory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "污染源名录库-分页列表查询")
	@ApiOperation(value="污染源名录库-分页列表查询", notes="污染源名录库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SourceDirectory sourceDirectory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String area = req.getParameter("area");
		String companyId = req.getParameter("companyId");
		String companyType = req.getParameter("companyType");
		String industry = req.getParameter("industry");
		String intensiveUnit = req.getParameter("intensiveUnit");
		String intensiveCompany = req.getParameter("intensiveCompany");
		String siteType = req.getParameter("siteType");
		String siteLevel = req.getParameter("siteLevel");
		String siteState = req.getParameter("siteState");
		//查询列表
		Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
		IPage<Map<String, Object>> pageList = sourceDirectoryService.getSourceDirectoryList(page,area,companyId,companyType,industry,intensiveUnit,intensiveCompany,siteType,siteLevel,siteState);
		return Result.ok(pageList);
	}

	 /**
	  *	未选择企业查询
	  *
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "未选择企业")
	 @ApiOperation(value="未选择企业", notes="未选择企业")
	 @GetMapping(value = "/unSelectList")
	 public Result<?> unSelectList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo, @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
		 String companyId = req.getParameter("companyId");
		 //查询列表
		 Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
		 IPage<Map<String, Object>> pageList = sourceDirectoryService.getUnSelectCompany(page,companyId);
		 return Result.ok(pageList);
	 }

	 /**
	  * 已选择的企业
	  *
	  * @param sourceDirectory
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "已选择的企业")
	 @ApiOperation(value="已选择的企业", notes="已选择的企业")
	 @GetMapping(value = "/selectList")
	 public Result<?> selectList(SourceDirectory sourceDirectory,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 String companyId = req.getParameter("companyId");
		 //查询列表
		 Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
		 IPage<Map<String, Object>> pageList = sourceDirectoryService.getSelectCompany(page,companyId);
		 return Result.ok(pageList);
	 }
	
	/**
	 *
	 * @return
	 */
	@AutoLog(value = "污染源名录库-添加")
	@ApiOperation(value="污染源名录库-添加", notes="污染源名录库-添加")
	@PostMapping(value = "/edit")
	public Result<?> add(@RequestBody JSONObject jsonObject) {
		List<String> stayAdd = jsonObject.getObject("stayAdd",new TypeReference<List<String>>(){} );
		List<String> stayDelete = jsonObject.getObject("stayDelete",new TypeReference<List<String>>(){} );
		//新增
		if(CollectionUtil.isNotEmpty(stayAdd)){
			for (String id : stayAdd) {
				int count = sourceDirectoryService.count(new QueryWrapper<SourceDirectory>().lambda().eq(SourceDirectory::getCompanyId,id));
				if(count==0){
					SourceDirectory sourceDirectory = new SourceDirectory();
					sourceDirectory.setCompanyId(id);
					sourceDirectoryService.save(sourceDirectory);
				}
			}
		}
		//删除
		if(CollectionUtil.isNotEmpty(stayDelete)){
			for (String id : stayDelete) {
				SourceDirectory sourceDirectory = sourceDirectoryService.getOne(new QueryWrapper<SourceDirectory>().lambda().eq(SourceDirectory::getCompanyId,id));
				if(sourceDirectory!=null){
					sourceDirectoryService.removeById(sourceDirectory.getId());
				}
			}
		}
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @return
	 */
	@AutoLog(value = "污染源名录库-通过id删除")
	@ApiOperation(value="污染源名录库-通过id删除", notes="污染源名录库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sourceDirectoryService.remove(new QueryWrapper<SourceDirectory>().lambda().eq(SourceDirectory::getCompanyId,id));
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	/*@AutoLog(value = "污染源名录库-批量删除")
	@ApiOperation(value="污染源名录库-批量删除", notes="污染源名录库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sourceDirectoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}*/

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "污染源名录库-通过id查询")
	@ApiOperation(value="污染源名录库-通过id查询", notes="污染源名录库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SourceDirectory sourceDirectory = sourceDirectoryService.getById(id);
		if(sourceDirectory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sourceDirectory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sourceDirectory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SourceDirectory sourceDirectory) {
        return super.exportXls(request, sourceDirectory, SourceDirectory.class, "污染源名录库");
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
        return super.importExcel(request, response, SourceDirectory.class);
    }

}
