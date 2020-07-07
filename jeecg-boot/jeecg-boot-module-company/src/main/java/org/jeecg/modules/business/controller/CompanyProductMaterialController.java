package org.jeecg.modules.business.controller;

import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyApply;
import org.jeecg.modules.business.entity.CompanyProductMaterial;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyProductMaterialService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.bussiness.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: company_product_material
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Api(tags="company_product_material")
@RestController
@RequestMapping("/companyProductMaterial")
@Slf4j
public class CompanyProductMaterialController extends JeecgController<CompanyProductMaterial, ICompanyProductMaterialService> {
	@Autowired
	private ICompanyProductMaterialService companyProductMaterialService;
	@Autowired
	private ICompanyApplyService companyApplyService;
	/**
	 * 分页列表查询
	 *
	 * @param companyProductMaterial
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "company_product_material-分页列表查询")
	@ApiOperation(value="company_product_material-分页列表查询", notes="company_product_material-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyProductMaterial companyProductMaterial,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyProductMaterial> queryWrapper = QueryGenerator.initQueryWrapper(companyProductMaterial, req.getParameterMap());
		Page<CompanyProductMaterial> page = new Page<CompanyProductMaterial>(pageNo, pageSize);
		IPage<CompanyProductMaterial> pageList = companyProductMaterialService.page(page, queryWrapper.orderByDesc("create_time"));
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyProductMaterial
	 * @return
	 */
	@AutoLog(value = "company_product_material-添加")
	@ApiOperation(value="company_product_material-添加", notes="company_product_material-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyProductMaterial companyProductMaterial) {
		companyProductMaterial.setStatus(Constant.status.TEMPORARY);//暂存
		companyProductMaterialService.save(companyProductMaterial);
		//暂存需要新增  apply表
		companyApplyService.saveByBase(companyProductMaterial.getCompanyId(),companyProductMaterial.getId(),Constant.status.TEMPORARY
				,"",Constant.tables.PRODUCTMATERIAL);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyProductMaterial
	 * @return
	 */
	@AutoLog(value = "company_product_material-编辑")
	@ApiOperation(value="company_product_material-编辑", notes="company_product_material-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyProductMaterial companyProductMaterial) {
		if(Constant.status.TEMPORARY.equals(companyProductMaterial.getStatus())
				||Constant.status.NOPASS.equals(companyProductMaterial.getStatus())) {
			//不通过修改
			if(Constant.status.NOPASS.equals(companyProductMaterial.getStatus())){
				companyApplyService.update(new UpdateWrapper<CompanyApply>().lambda()
						.eq(CompanyApply::getNewId,companyProductMaterial.getId()).set(CompanyApply::getStatus,Constant.status.TEMPORARY)
						.set(CompanyApply::getUpdateBy,"").set(CompanyApply::getUpdateTime,null));
			}
			companyProductMaterial.setStatus(Constant.status.TEMPORARY);//暂存
			companyProductMaterialService.updateById(companyProductMaterial);
		}
		else{
			String oldId = companyProductMaterial.getId();
			//不是暂存的编辑  都是新增暂存状态
			companyProductMaterial.setStatus(Constant.status.TEMPORARY);//暂存
			companyProductMaterial.setId(null);
			companyProductMaterialService.save(companyProductMaterial);
			companyApplyService.saveByBase(companyProductMaterial.getCompanyId(),companyProductMaterial.getId(),Constant.status.TEMPORARY,oldId,Constant.tables.PRODUCTMATERIAL);
		}
		return Result.ok("编辑成功!");
	}
	 /**
	  *  产品物料信息 申报
	  *
	  * @param companyProductMaterial 产品物料信息
	  * @return
	  */
	 @AutoLog(value = "用户信息申报（新增或编辑时直接申报）")
	 @ApiOperation(value="用户信息申报", notes="新增或编辑时直接申报")
	 @PostMapping(value = "/editAndApply")
	 public Result<?> editAndApply(@RequestBody  CompanyProductMaterial companyProductMaterial) {

		 //新增申报记录
		 String oldId = "";
		 //申报   1新增申报
		 if(StrUtil.isEmpty(companyProductMaterial.getId())) {
			 companyProductMaterial.setStatus(Constant.status.PEND);//待审核
			 companyProductMaterialService.save(companyProductMaterial);
			 //新增申报记录
			 companyApplyService.saveByBase(companyProductMaterial.getCompanyId(),companyProductMaterial.getId(),Constant.status.PEND,oldId,Constant.tables.PRODUCTMATERIAL);
		 }
		 //编辑申报 2、编辑暂存数据
		 else if(Constant.status.TEMPORARY.equals(companyProductMaterial.getStatus())
				 ||Constant.status.NOPASS.equals(companyProductMaterial.getStatus())) {
			 companyProductMaterial.setStatus(Constant.status.PEND);//待审核
			 companyProductMaterialService.updateById(companyProductMaterial);
			 companyApplyService.update(new UpdateWrapper<CompanyApply>().lambda()
					 .eq(CompanyApply::getNewId,companyProductMaterial.getId())
					 .eq(CompanyApply::getStatus,Constant.status.TEMPORARY)
					 .set(CompanyApply::getStatus,Constant.status.PEND)
					 .set(CompanyApply::getCreateTime,new Date()));
		 }else{
			 //编辑申报 3、编辑正常数据
			 oldId = companyProductMaterial.getId();
			 companyProductMaterial.setStatus(Constant.status.PEND);//待审核
			 companyProductMaterial.setId(null);
			 companyProductMaterialService.save(companyProductMaterial);
			 //新增申报记录
			 companyApplyService.saveByBase(companyProductMaterial.getCompanyId(),companyProductMaterial.getId(),Constant.status.PEND,oldId,Constant.tables.PRODUCTMATERIAL);
		 }

		 return Result.ok("申报成功!");

	 }
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "company_product_material-通过id删除")
	@ApiOperation(value="company_product_material-通过id删除", notes="company_product_material-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		//删除
		companyProductMaterialService.remove(new QueryWrapper<CompanyProductMaterial>().lambda()
				.eq(CompanyProductMaterial::getStatus, Constant.status.TEMPORARY)
				.eq(CompanyProductMaterial::getId,id ));

		//删除申报记录
		companyApplyService.remove(new QueryWrapper<CompanyApply>().lambda()
				.eq(CompanyApply::getStatus,Constant.status.TEMPORARY)
				.in(CompanyApply::getNewId,id ));
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "company_product_material-批量删除")
	@ApiOperation(value="company_product_material-批量删除", notes="company_product_material-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		//删除
		companyProductMaterialService.remove(new QueryWrapper<CompanyProductMaterial>().lambda()
				.eq(CompanyProductMaterial::getStatus,Constant.status.TEMPORARY)
				.in(CompanyProductMaterial::getId,Arrays.asList(ids.split(","))) );

		//删除申报记录
		companyApplyService.remove(new QueryWrapper<CompanyApply>().lambda()
				.eq(CompanyApply::getStatus,Constant.status.TEMPORARY)
				.in(CompanyApply::getNewId,Arrays.asList(ids.split(","))) );

		return Result.ok("批量删除暂存数据成功!");
	}

	 /**
	  *
	  *
	  * @param ids 批量申报
	  * @return
	  */
	 @AutoLog(value = "批量申报")
	 @ApiOperation(value="批量申报", notes="批量申报")
	 @GetMapping(value = "/batchApply")
	 public Result<?> batchApply(@RequestParam(name="ids",required=true) String ids) {
		 //修改
		 companyProductMaterialService.update(new UpdateWrapper<CompanyProductMaterial>().lambda()
				 .eq(CompanyProductMaterial::getStatus,Constant.status.TEMPORARY)
				 .in(CompanyProductMaterial::getId,Arrays.asList(ids.split(",")))
				 .set(CompanyProductMaterial::getStatus,Constant.status.PEND));

		 //修改申报记录
		 companyApplyService.update(new UpdateWrapper<CompanyApply>().lambda()
				 .eq(CompanyApply::getStatus,Constant.status.TEMPORARY)
				 .in(CompanyApply::getNewId,Arrays.asList(ids.split(",")))
				 .set(CompanyApply::getStatus,Constant.status.PEND)
				 .set(CompanyApply::getCreateTime,new Date()));
		 return Result.ok("申报成功!");
	 }

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "company_product_material-通过id查询")
	@ApiOperation(value="company_product_material-通过id查询", notes="company_product_material-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyProductMaterial companyProductMaterial = companyProductMaterialService.getById(id);
		if(companyProductMaterial==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyProductMaterial);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyProductMaterial
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyProductMaterial companyProductMaterial) {
        return super.exportXls(request, companyProductMaterial, CompanyProductMaterial.class, "company_product_material");
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
        return super.importExcel(request, response, CompanyProductMaterial.class);
    }

}
