package org.jeecg.modules.business.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.entity.CompanyAdminPenalties;
import org.jeecg.modules.business.entity.CompanyDynamicSupervision;
import org.jeecg.modules.business.entity.CompanyFile;
import org.jeecg.modules.business.service.ICompanyAdminPenaltiesService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.business.utils.Constant;
import org.jeecg.modules.business.vo.CompanyAdminPenaltiesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import sun.java2d.pipe.SpanShapeRenderer;

/**
 * @Description: 行政处罚信息
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Api(tags="行政处罚信息")
@RestController
@RequestMapping("/cap/companyAdminPenalties")
@Slf4j
public class CompanyAdminPenaltiesController extends JeecgController<CompanyAdminPenalties, ICompanyAdminPenaltiesService> {
	@Autowired
	private ICompanyAdminPenaltiesService companyAdminPenaltiesService;

	@Autowired
	private ICompanyFileService companyFileService;
	/**
	 * 分页列表查询
	 *
	 * @param companyAdminPenalties
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-分页列表查询")
	@ApiOperation(value="行政处罚信息-分页列表查询", notes="行政处罚信息-分页列表查询")
	@GetMapping(value = "/list/{listType}")
	public Result<?> queryPageList( CompanyAdminPenalties companyAdminPenalties,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req,@PathVariable int listType) throws ParseException {
		String companyIds = req.getParameter("companyIds");
		String status = req.getParameter("status");
		String companyId = req.getParameter("companyId");
		String reportDateBegin = req.getParameter("reportDate_begin");
		String reportDateEnd = req.getParameter("reportDate_end");
		Date dateBegin;
		Date dateEnd;
		if(!StrUtil.isEmpty(companyId)) {
			companyIds= companyId;
		}
		if(StrUtil.isEmpty(reportDateBegin) && StrUtil.isEmpty(reportDateEnd)) {
			dateBegin = null;
			dateEnd = null;
		}else{
			dateBegin = new SimpleDateFormat("yyyy-MM-dd").parse(reportDateBegin);
			dateEnd = new SimpleDateFormat("yyyy-MM-dd").parse(reportDateEnd);
		}
//		QueryWrapper<CompanyAdminPenalties> queryWrapper = QueryGenerator.initQueryWrapper(companyAdminPenalties, parameterMap);
		Page<CompanyAdminPenaltiesVO> page = new Page<CompanyAdminPenaltiesVO>(pageNo, pageSize);
		IPage<CompanyAdminPenaltiesVO> pageList = companyAdminPenaltiesService.getCompanyAdminPenalties(page, companyIds,status,dateBegin,dateEnd,listType);
		return Result.ok(pageList);
	}

	/**
	 * 分页列表查询
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-申报")
	@ApiOperation(value="行政处罚信息-申报", notes="行政处罚信息-申报")
	@PutMapping(value = "/declare")
	public Result<?> declare(@RequestBody JSONObject jsonObject) {
		CompanyAdminPenalties companyAdminPenalties = getCompanyAdminPenalties(jsonObject);
		companyAdminPenalties.setStatus(Constant.status.PEND);
		//判断是新增还是编辑
		if(!StrUtil.isEmpty(companyAdminPenalties.getId())){
			//编辑
			//查询修改之前的对象
			CompanyAdminPenalties oldCompanyAdminPenalties = companyAdminPenaltiesService.getById(companyAdminPenalties.getId());
			//状态为正常
			if(Constant.status.NORMAL.equals(oldCompanyAdminPenalties.getStatus())){
				//修改老数据状态为过期
				oldCompanyAdminPenalties.setStatus(Constant.status.EXPIRED);
				companyAdminPenaltiesService.updateById(oldCompanyAdminPenalties);
				//新增修改后的为新数据
				companyAdminPenalties.setId("");
				companyAdminPenalties.setUpdateBy("");
				companyAdminPenalties.setUpdateTime(null);
				companyAdminPenalties.setContent("");
				companyAdminPenaltiesService.save(companyAdminPenalties);
			}else if(Constant.status.NOPASS.equals(oldCompanyAdminPenalties.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyAdminPenalties.getStatus())){
				companyAdminPenalties.setUpdateBy("");
				companyAdminPenalties.setUpdateTime(null);
				companyAdminPenalties.setContent("");
				companyAdminPenaltiesService.updateById(companyAdminPenalties);
				companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.ADMINPENALTIES)
						.eq(CompanyFile::getTableId,companyAdminPenalties.getId()));
			}
		}else {
			companyAdminPenaltiesService.save(companyAdminPenalties);
		}
		companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.ADMINPENALTIES,companyAdminPenalties.getId());
		return Result.ok("申报成功！");
	}

	private CompanyAdminPenalties getCompanyAdminPenalties(@RequestBody JSONObject jsonObject) {
		CompanyAdminPenalties companyAdminPenalties = new CompanyAdminPenalties();
		companyAdminPenalties.setId(jsonObject.getString("id"));
		companyAdminPenalties.setCompanyId(jsonObject.getString("companyId"));
		companyAdminPenalties.setStatus(jsonObject.getString("status"));
		companyAdminPenalties.setReportDate(jsonObject.getDate("reportDate"));
		companyAdminPenalties.setDocumentName(jsonObject.getString("documentName"));
		companyAdminPenalties.setDocumentNo(jsonObject.getString("documentNo"));
		companyAdminPenalties.setContent(jsonObject.getString("content"));
		companyAdminPenalties.setCreateBy(jsonObject.getString("createBy"));
		companyAdminPenalties.setCreateTime(jsonObject.getDate("createTime"));
		companyAdminPenalties.setUpdateBy(jsonObject.getString("updateBy"));
		companyAdminPenalties.setUpdateTime(jsonObject.getDate("updateTime"));
		return companyAdminPenalties;
	}

	/**
	 *   添加
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-添加")
	@ApiOperation(value="行政处罚信息-添加", notes="行政处罚信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JSONObject jsonObject) {
		CompanyAdminPenalties companyAdminPenalties = getCompanyAdminPenalties(jsonObject);
		companyAdminPenalties.setStatus(Constant.status.TEMPORARY);
		companyAdminPenaltiesService.save(companyAdminPenalties);
		companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.ADMINPENALTIES,companyAdminPenalties.getId());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-编辑")
	@ApiOperation(value="行政处罚信息-编辑", notes="行政处罚信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JSONObject jsonObject) {
		CompanyAdminPenalties companyAdminPenalties = getCompanyAdminPenalties(jsonObject);
		CompanyAdminPenalties oldCompanyAdminPenalties = companyAdminPenaltiesService.getById(companyAdminPenalties.getId());
		//查询数据状态
		if(Constant.status.NORMAL.equals(companyAdminPenalties.getStatus())) {
			companyAdminPenalties.setStatus(Constant.status.TEMPORARY);
			oldCompanyAdminPenalties.setStatus(Constant.status.EXPIRED);
			companyAdminPenaltiesService.updateById(oldCompanyAdminPenalties);
			//新增修改后的为新数据
			companyAdminPenalties.setId("");
			companyAdminPenalties.setUpdateBy("");
			companyAdminPenalties.setUpdateTime(null);
			companyAdminPenalties.setContent("");
			companyAdminPenaltiesService.save(companyAdminPenalties);
		}else if (Constant.status.TEMPORARY.equals(oldCompanyAdminPenalties.getStatus()) || Constant.status.NOPASS.equals(oldCompanyAdminPenalties.getStatus())) {
			//状态为暂存和未通过的
			companyAdminPenalties.setStatus(Constant.status.TEMPORARY);
			companyAdminPenalties.setUpdateBy("");
			companyAdminPenalties.setUpdateTime(null);
			companyAdminPenalties.setContent("");
			companyAdminPenaltiesService.updateById(companyAdminPenalties);
			companyFileService.remove(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable,Constant.tables.ADMINPENALTIES)
					.eq(CompanyFile::getTableId,companyAdminPenalties.getId()));
		}
		companyFileService.saveFiles(jsonObject.getString("fileList"),Constant.fileType.FILE,Constant.tables.ADMINPENALTIES,companyAdminPenalties.getId());
		return Result.ok("编辑成功!");
	}

	/**
	 *  审核
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-审核")
	@ApiOperation(value="行政处罚信息-审核", notes="行政处罚信息-审核")
	@PutMapping(value = "/audit")
	public Result<?> audit(@RequestBody JSONObject jsonObject) {
		CompanyAdminPenalties companyAdminPenalties = getCompanyAdminPenalties(jsonObject);
		companyAdminPenaltiesService.updateById(companyAdminPenalties);
		return Result.ok("审核成功!");
	}

	/**
	 * 批量通过
	 *
	 * @param jsonObject
	 */
	@PostMapping(value = "/batchPass")
	@AutoLog(value = "批量通过")
	@ApiOperation(value = "批量通过", notes = "批量通过")
	public Result<?> batchPass(@RequestBody JSONObject jsonObject) {
		String[]  ids = jsonObject.getString("ids").split(",");
		if(ids.length>0){
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

			LambdaUpdateWrapper updateWrapper = new UpdateWrapper<CompanyAdminPenalties>().lambda().in(CompanyAdminPenalties::getId,Arrays.asList(ids))
					.set(CompanyAdminPenalties::getStatus,Constant.status.NORMAL).set(CompanyAdminPenalties::getUpdateTime,new Date())
					.set(CompanyAdminPenalties::getUpdateBy,sysUser.getId());
			companyAdminPenaltiesService.update(updateWrapper);
		}
		return Result.ok();
	}

	/**
	 * 批量不通过
	 *
	 * @param jsonObject
	 */
	@PostMapping(value = "/batchFail")
	@AutoLog(value = "批量不通过")
	@ApiOperation(value = "批量不通过", notes = "批量不通过")
	public Result<?> batchFail(@RequestBody JSONObject jsonObject) {
		String[]  ids = jsonObject.getString("ids").split(",");
		if(ids.length>0){
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

			LambdaUpdateWrapper updateWrapper = new UpdateWrapper<CompanyAdminPenalties>().lambda().in(CompanyAdminPenalties::getId,Arrays.asList(ids))
					.set(CompanyAdminPenalties::getStatus,Constant.status.NOPASS).set(CompanyAdminPenalties::getUpdateTime,new Date())
					.set(CompanyAdminPenalties::getUpdateBy,sysUser.getId());
			companyAdminPenaltiesService.update(updateWrapper);
		}
		return Result.ok();
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-通过id删除")
	@ApiOperation(value="行政处罚信息-通过id删除", notes="行政处罚信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyAdminPenaltiesService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-批量删除")
	@ApiOperation(value="行政处罚信息-批量删除", notes="行政处罚信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		//删除申报暂存记录
		companyAdminPenaltiesService.remove(new QueryWrapper<CompanyAdminPenalties>().lambda()
				.eq(CompanyAdminPenalties::getStatus, Constant.status.TEMPORARY)
				.in(CompanyAdminPenalties::getId, Arrays.asList(ids.split(","))));
		return Result.ok("批量删除暂存记录成功!");
	}

	/**
	 *  批量申报
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-批量申报")
	@ApiOperation(value="行政处罚信息-批量申报", notes="行政处罚信息-批量申报")
	@GetMapping(value = "/batchDeclare")
	public Result<?> batchDeclare(@RequestParam(name="ids",required=true) String ids) {
		//修改
		companyAdminPenaltiesService.update(new UpdateWrapper<CompanyAdminPenalties>().lambda()
				.eq(CompanyAdminPenalties::getStatus,Constant.status.TEMPORARY)
				.in(CompanyAdminPenalties::getId,Arrays.asList(ids.split(",")))
				.set(CompanyAdminPenalties::getStatus,Constant.status.PEND));
		return Result.ok("批量申报暂存记录成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-通过id查询")
	@ApiOperation(value="行政处罚信息-通过id查询", notes="行政处罚信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyAdminPenalties companyAdminPenalties = companyAdminPenaltiesService.getById(id);
		if(companyAdminPenalties==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyAdminPenalties);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyAdminPenalties
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyAdminPenalties companyAdminPenalties) {
        return super.exportXls(request, companyAdminPenalties, CompanyAdminPenalties.class, "行政处罚信息");
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
        return super.importExcel(request, response, CompanyAdminPenalties.class);
    }

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "查询行政处罚信息附件")
	@ApiOperation(value="查询行政处罚信息附件", notes="查询行政处罚信息附件")
	@GetMapping(value = "/queryFiles")
	public Result<?> queryFiles(@RequestParam(name="id",required=true) String id) {
		List<Map<String, String>> result = companyFileService.getFileMaps(id,Constant.tables.ADMINPENALTIES);
		return Result.ok(result);
	}
}
