package org.jeecg.modules.business.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyBase;
import org.jeecg.modules.business.entity.CompanySysuser;
import org.jeecg.modules.business.service.ICompanyBaseService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ICompanySysuserService;
import org.jeecg.modules.business.vo.CompanyBaseInfoSimple;
import org.jeecg.modules.business.vo.CompanySysUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 企业基础表
 * @Author: jeecg-boot
 * @Date:   2020-06-09
 * @Version: V1.0
 */
@Api(tags="企业基础表")
@RestController
@RequestMapping("/cb/companyBase")
@Slf4j
public class CompanyBaseController extends JeecgController<CompanyBase, ICompanyBaseService> {
	@Autowired
	private ICompanyBaseService companyBaseService;
	 @Autowired
	 private ICompanySysuserService companySysuserService;

	/**
	 * 分页列表查询
	 *
	 * @param companyBase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "企业基础表-分页列表查询")
	@ApiOperation(value="企业基础表-分页列表查询", notes="企业基础表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyBase companyBase,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyBase> queryWrapper = QueryGenerator.initQueryWrapper(companyBase, req.getParameterMap());
		Page<CompanyBase> page = new Page<CompanyBase>(pageNo, pageSize);
		IPage<CompanyBase> pageList = companyBaseService.page(page, queryWrapper);
		return Result.ok(pageList);
	}


	 @AutoLog(value = "查询所有公司的基本信息")
	 @ApiOperation(value="查询所有公司的基本信息-分页列表查询", notes="查询所有公司的基本信息-分页列表查询")
	 @GetMapping(value = "/baseInfolist")
	 public Result<?> baseInfolist(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										   HttpServletRequest req) {


		 Page<CompanyBaseInfoSimple> page = new Page<>(pageNo, pageSize);
		 IPage<CompanyBaseInfoSimple> pageList = companyBaseService.baseInfolist(page);
		 return Result.ok(pageList);

	 }


	 @AutoLog(value = "company_baseinfo-通过用户id-分页列表查询")
	 @ApiOperation(value="company_baseinfo-通过用户id-分页列表查询", notes="company_baseinfo-通过用户id-分页列表查询")
	 @GetMapping(value = "/queryCompanyIds")
	 public Result<?> queryCompanyIds(@RequestParam  String userId) {
		 List<CompanySysuser> companySysusers = 	companySysuserService.list(new QueryWrapper<CompanySysuser>().lambda().eq(CompanySysuser::getSysUserid,userId));
		 List<String> result = new ArrayList<>();

		 for(CompanySysuser companySysuser:companySysusers){
			 result.add(companySysuser.getCompanyId());
		 }
		 return Result.ok(result);
	 }
	 /**
	  *   添加
	  *
	  * @param companySysUserVo
	  * @return
	  */
	 @AutoLog(value = "企业用户关联表-添加")
	 @ApiOperation(value="企业用户关联表-添加", notes="企业用户关联表-添加")
	 @PostMapping(value = "/addCompanyUser")
	 public Result<?> addCompanyUser(@RequestBody CompanySysUserVo companySysUserVo) {
		 Result<String> result = new Result<String>();
		 List<CompanySysuser> companySysuserList = new ArrayList<>();
		 try {
			 String companyId = companySysUserVo.getCompanyId();
			 for(String sysUserId:companySysUserVo.getUserIdList()) {


				 CompanySysuser one = companySysuserService.getOne(new QueryWrapper<CompanySysuser>().lambda()
						 .eq(CompanySysuser::getSysUserid, sysUserId)
						 .eq(CompanySysuser::getCompanyId,companyId));
				 if(one==null){
					 CompanySysuser companySysuser = new CompanySysuser(sysUserId,companyId);
					 companySysuserList.add(companySysuser);
				 }

			 }
			 if(!companySysuserList.isEmpty())
			 	companySysuserService.saveBatch(companySysuserList);
			 result.setMessage("添加成功!");
			 result.setSuccess(true);
			 return result;
		 }catch(Exception e) {
			 log.error(e.getMessage(), e);
			 result.setSuccess(false);
			 result.setMessage("出错了: " + e.getMessage());
			 return result;
		 }
	 }
 /**
  *   添加
  *
  * @param companyBase
  * @return
  */
	@AutoLog(value = "企业基础表-添加")
	@ApiOperation(value="企业基础表-添加", notes="企业基础表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyBase companyBase) {
		companyBaseService.save(companyBase);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyBase
	 * @return
	 */
	@AutoLog(value = "企业基础表-编辑")
	@ApiOperation(value="企业基础表-编辑", notes="企业基础表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyBase companyBase) {
		companyBaseService.updateById(companyBase);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业基础表-通过id删除")
	@ApiOperation(value="企业基础表-通过id删除", notes="企业基础表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyBaseService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "企业基础表-批量删除")
	@ApiOperation(value="企业基础表-批量删除", notes="企业基础表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyBaseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业基础表-通过id查询")
	@ApiOperation(value="企业基础表-通过id查询", notes="企业基础表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyBase companyBase = companyBaseService.getById(id);
		if(companyBase==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyBase);
	}

	 /**
	  * 通过id查询
	  *
	  * @param companyIds
	  * @return
	  */
	 @AutoLog(value = "企业基础表-通过id查询")
	 @ApiOperation(value="企业基础表-通过id查询", notes="企业基础表-通过id查询")
	 @GetMapping(value = "/queryCompanyName")
	 public Result<?> queryCompanyName(@RequestParam(name="companyIds",required=true) String companyIds) {
		 List<String> idList =  Arrays.asList(companyIds.split(","));

		 List<Map<String,String>> result = new ArrayList<>();
		 //查询企业id和name
		 companyBaseService.list(new QueryWrapper<CompanyBase>().lambda().in(CompanyBase::getId,idList)).forEach(companyBase -> {
			 Map<String,String> param = new HashMap<>();
			 param.put("key",companyBase.getId());
			 param.put("value",companyBase.getCompanyName());

			 result.add(param);
		 });

		 return Result.ok(result);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param companyBase
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyBase companyBase) {
        return super.exportXls(request, companyBase, CompanyBase.class, "企业基础表");
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
        return super.importExcel(request, response, CompanyBase.class);
    }

}
