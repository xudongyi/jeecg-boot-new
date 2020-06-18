package org.jeecg.modules.business.controller;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyEnvTrialService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 环评审批信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Api(tags="环评审批信息")
@RestController
@RequestMapping("/company/envTrial")
@Slf4j
public class CompanyEnvTrialController extends JeecgController<CompanyEnvTrial, ICompanyEnvTrialService> {
	@Autowired
	private ICompanyEnvTrialService companyEnvTrialService;
	 @Autowired
	 private ICompanyApplyService companyApplyService;
	 @Autowired
	 private ICompanyFileService companyFileService;
	/**
	 * 分页列表查询
	 *
	 * @param companyEnvTrial
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "环评审批信息-分页列表查询")
	@ApiOperation(value="环评审批信息-分页列表查询", notes="环评审批信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyEnvTrial companyEnvTrial,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyEnvTrial> queryWrapper = QueryGenerator.initQueryWrapper(companyEnvTrial, req.getParameterMap());
		Page<CompanyEnvTrial> page = new Page<CompanyEnvTrial>(pageNo, pageSize);
		IPage<CompanyEnvTrial> pageList = companyEnvTrialService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "环评审批信息-添加")
	@ApiOperation(value="环评审批信息-添加", notes="环评审批信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JSONObject jsonObject) {
		CompanyEnvTrial companyEnvTrial = getCompanyEnvTrial(jsonObject);
		companyEnvTrial.setStatus(Constant.status.TEMPORARY);//暂存
		companyEnvTrialService.save(companyEnvTrial);
		//暂存需要新增  apply表
		companyApplyService.saveByBase(companyEnvTrial.getCompanyId(),companyEnvTrial.getId(),Constant.status.TEMPORARY
				,"",Constant.tables.ENVTRIAL);
		companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.ENVTRIAL,companyEnvTrial.getId());
		return Result.ok("添加成功！");
	}

	 private CompanyEnvTrial getCompanyEnvTrial(@RequestBody JSONObject jsonObject) {
		 CompanyEnvTrial companyEnvTrial = new CompanyEnvTrial();
		 companyEnvTrial.setId(jsonObject.getString("id"));
		 companyEnvTrial.setCompanyId(jsonObject.getString("companyId"));
		 companyEnvTrial.setProjectName(jsonObject.getString("projectName"));
		 companyEnvTrial.setApproveFilenum(jsonObject.getString("approveFilenum"));
		 companyEnvTrial.setApproveUnit(jsonObject.getString("approveUnit"));
		 companyEnvTrial.setApproveDate(jsonObject.getDate("approveDate"));
		 companyEnvTrial.setCreateBy(jsonObject.getString("createBy"));
		 companyEnvTrial.setCreateTime(jsonObject.getDate("createTime"));
		 companyEnvTrial.setUpdateBy(jsonObject.getString("updateBy"));
		 companyEnvTrial.setUpdateTime(jsonObject.getDate("updateTime"));
		 return companyEnvTrial;
	 }

	 /**
	 *  编辑
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "环评审批信息-编辑")
	@ApiOperation(value="环评审批信息-编辑", notes="环评审批信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JSONObject jsonObject) {
		CompanyEnvTrial companyEnvTrial = getCompanyEnvTrial(jsonObject);
		if(Constant.status.TEMPORARY.equals(companyEnvTrial.getStatus())) {
			companyEnvTrialService.updateById(companyEnvTrial);
			//删除原有的
			companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.ENVTRIAL)
			.eq(CompanyFile::getTableId,companyEnvTrial.getId()));
		}
		else{
			String oldId = companyEnvTrial.getId();
			//不是暂存的编辑  都是新增暂存状态
			companyEnvTrial.setStatus(Constant.status.TEMPORARY);//暂存
			companyEnvTrial.setId(null);
			companyEnvTrialService.save(companyEnvTrial);
			companyApplyService.saveByBase(companyEnvTrial.getCompanyId(),companyEnvTrial.getId(),Constant.status.TEMPORARY,oldId,Constant.tables.ENVTRIAL);

		}
		//保存文件
		companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.ENVTRIAL,companyEnvTrial.getId());
		return Result.ok("编辑成功!");
	}
	 /**
	  *  环评审批信息申报
	  *
	  * @param   jsonObject 环评审批信息
	  * @return
	  */
	 @AutoLog(value = "环评审批信息（新增或编辑时直接申报）")
	 @ApiOperation(value="环评审批信息", notes="新增或编辑时直接申报")
	 @PostMapping(value = "/editAndApply")
	 public Result<?> editAndApply(@RequestBody  JSONObject jsonObject) {
		 CompanyEnvTrial companyEnvTrial = getCompanyEnvTrial(jsonObject);

		 //新增申报记录
		 String oldId = "";
		 //申报   1新增申报
		 if(StrUtil.isEmpty(companyEnvTrial.getId())) {
			 companyEnvTrial.setStatus(Constant.status.PEND);//待审核
			 companyEnvTrialService.save(companyEnvTrial);
			 //新增申报记录
			 companyApplyService.saveByBase(companyEnvTrial.getCompanyId(),companyEnvTrial.getId(),Constant.status.PEND,oldId,Constant.tables.ENVTRIAL);
		 }
		 //编辑申报 2、编辑暂存数据
		 else if(Constant.status.TEMPORARY.equals(companyEnvTrial.getStatus())) {
			 companyEnvTrial.setStatus(Constant.status.PEND);//待审核
			 companyEnvTrialService.updateById(companyEnvTrial);
			 companyApplyService.update(new UpdateWrapper<CompanyApply>().lambda()
					 .eq(CompanyApply::getNewId,companyEnvTrial.getId())
					 .eq(CompanyApply::getStatus,Constant.status.TEMPORARY)
					 .set(CompanyApply::getStatus,Constant.status.PEND));
			 //删除原有的
			 companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.ENVTRIAL)
					 .eq(CompanyFile::getTableId,companyEnvTrial.getId()));
		 }else{
			 //编辑申报 3、编辑正常数据
			 oldId = companyEnvTrial.getId();
			 companyEnvTrial.setStatus(Constant.status.PEND);//待审核
			 companyEnvTrial.setId(null);
			 companyEnvTrialService.save(companyEnvTrial);
			 //新增申报记录
			 companyApplyService.saveByBase(companyEnvTrial.getCompanyId(),companyEnvTrial.getId(),Constant.status.PEND,oldId,Constant.tables.ENVTRIAL);
		 }
		 companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.ENVTRIAL,companyEnvTrial.getId());

		 return Result.ok("申报成功!");

	 }
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "环评审批信息-通过id删除")
	@ApiOperation(value="环评审批信息-通过id删除", notes="环评审批信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		//删除
		companyEnvTrialService.remove(new QueryWrapper<CompanyEnvTrial>().lambda()
				.eq(CompanyEnvTrial::getStatus,Constant.status.TEMPORARY)
				.eq(CompanyEnvTrial::getId,id ));

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
	@AutoLog(value = "环评审批信息-批量删除")
	@ApiOperation(value="环评审批信息-批量删除", notes="环评审批信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		//删除
		companyEnvTrialService.remove(new QueryWrapper<CompanyEnvTrial>().lambda()
				.eq(CompanyEnvTrial::getStatus,Constant.status.TEMPORARY)
				.in(CompanyEnvTrial::getId,Arrays.asList(ids.split(","))) );

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
		 companyEnvTrialService.update(new UpdateWrapper<CompanyEnvTrial>().lambda()
				 .eq(CompanyEnvTrial::getStatus,Constant.status.TEMPORARY)
				 .in(CompanyEnvTrial::getId,Arrays.asList(ids.split(",")))
				 .set(CompanyEnvTrial::getStatus,Constant.status.PEND));

		 //修改申报记录
		 companyApplyService.update(new UpdateWrapper<CompanyApply>().lambda()
				 .eq(CompanyApply::getStatus,Constant.status.TEMPORARY)
				 .in(CompanyApply::getNewId,Arrays.asList(ids.split(",")))
				 .set(CompanyApply::getStatus,Constant.status.PEND));
		 return Result.ok("申报成功!");
	 }
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "环评审批信息-通过id查询")
	@ApiOperation(value="环评审批信息-通过id查询", notes="环评审批信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyEnvTrial companyEnvTrial = companyEnvTrialService.getById(id);
		if(companyEnvTrial==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyEnvTrial);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyEnvTrial
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyEnvTrial companyEnvTrial) {
        return super.exportXls(request, companyEnvTrial, CompanyEnvTrial.class, "环评审批信息");
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
        return super.importExcel(request, response, CompanyEnvTrial.class);
    }
	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "查询环评审批信息附件")
	 @ApiOperation(value="查询环评审批信息附件", notes="查询环评审批信息附件")
	 @GetMapping(value = "/queryFiles")
	 public Result<?> queryFiles(@RequestParam(name="id",required=true) String id) {
		 List<CompanyFile> files = companyFileService.list(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.ENVTRIAL)
				 .eq(CompanyFile::getTableId,id));
		 List<Map<String,String>> result = new ArrayList<>();
		 for(CompanyFile companyFile:files){
		 	Map<String,String> temp = new HashMap<>();
			 temp.put("filePath",companyFile.getFilepath()+companyFile.getFilename());
			 temp.put("fileName",companyFile.getFilename().substring(0,companyFile.getFilename().lastIndexOf("_"))+companyFile.getFilename().substring(companyFile.getFilename().lastIndexOf(".")));
			 result.add(temp);
		 }
		 return Result.ok(result);
	 }

}
