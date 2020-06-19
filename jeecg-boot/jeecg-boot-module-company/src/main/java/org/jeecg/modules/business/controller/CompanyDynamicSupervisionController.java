package org.jeecg.modules.business.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyDynamicSupervision;
import org.jeecg.modules.business.entity.CompanySupervisoryMonitor;
import org.jeecg.modules.business.service.ICompanyDynamicSupervisionService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ICompanySysuserService;
import org.jeecg.modules.business.utils.Constant;
import org.jeecg.modules.business.vo.CompanyDynamicSupervisionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 企业年度动态监管
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
@Api(tags="企业年度动态监管")
@RestController
@RequestMapping("/cds/companyDynamicSupervision")
@Slf4j
public class CompanyDynamicSupervisionController extends JeecgController<CompanyDynamicSupervision, ICompanyDynamicSupervisionService> {
	@Autowired
	private ICompanyDynamicSupervisionService companyDynamicSupervisionService;

	 /**
	  * 分页列表查询
	  *
	  * @param companyDynamicSupervision
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
//	 @AutoLog(value = "企业年度动态监管-分页列表查询")
//	 @ApiOperation(value="企业年度动态监管-分页列表查询", notes="企业年度动态监管-分页列表查询")
//	 @GetMapping(value = "/list2/xxx")
//	 public Result<?> queryPageListVO(@RequestParam(name = "companyId",required = true) String companyId,@RequestParam(name = "status") String status ,CompanyDynamicSupervisionVO companyDynamicSupervision,
//									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
//									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
//									HttpServletRequest req) {
//		 Map<String, String[]> parameterMap = new HashMap(req.getParameterMap());
//		 parameterMap.put("companyId_MultiString",new String[]{String.join(",", companyId)});
//		 parameterMap.put("status",new String[]{String.join(",",status)});
//		 QueryWrapper<CompanyDynamicSupervisionVO> queryWrapper = QueryGenerator.initQueryWrapper(companyDynamicSupervision, parameterMap);
//		 Page<CompanyDynamicSupervisionVO> page = new Page<CompanyDynamicSupervisionVO>(pageNo, pageSize);
//		 IPage<CompanyDynamicSupervisionVO> pageList = companyDynamicSupervisionService.getCompanyDynamicSupervision(page, String.join(",", companyId),String.join(",",status));
//		 return Result.ok(pageList);
//	 }

	 /**
	  * 分页列表查询
	  *
	  * @param companyDynamicSupervision
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "企业年度动态监管-分页列表查询")
	 @ApiOperation(value="企业年度动态监管-分页列表查询", notes="企业年度动态监管-分页列表查询")
	 @GetMapping(value = "/list/{listType}")
	 public Result<?> queryPageList(CompanyDynamicSupervisionVO companyDynamicSupervision,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req,@PathVariable int listType) {
		 String companyIds = req.getParameter("companyIds");
		 String status = req.getParameter("status");
		 String companyId = req.getParameter("companyId");
		 String reportYear = req.getParameter("reportYear");
		 if(!StrUtil.isEmpty(companyId)) {
			 companyIds= companyId;
		 }
//		 QueryWrapper<CompanyDynamicSupervisionVO> queryWrapper = QueryGenerator.initQueryWrapper(companyDynamicSupervision,req.getParameterMap());
		 Page<CompanyDynamicSupervisionVO> page = new Page<>(pageNo, pageSize);
		 IPage<CompanyDynamicSupervisionVO> pageList = companyDynamicSupervisionService.getCompanyDynamicSupervision(page,companyIds,status,reportYear,listType);
		 return Result.ok(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param companyDynamicSupervision
	  * @return
	  */
	 @AutoLog(value = "企业年度动态监管-申报")
	 @ApiOperation(value="企业年度动态监管-申报", notes="企业年度动态监管-申报")
	 @PutMapping(value = "/declare")
	 public Result<?> declare(@RequestBody CompanyDynamicSupervision companyDynamicSupervision) {
	 	companyDynamicSupervision.setStatus(Constant.status.PEND);
//	 	companyDynamicSupervision.setUpdateBy("");
//	 	companyDynamicSupervision.setUpdateTime(null);
		 //判断是新增还是编辑
	 	if(!StrUtil.isEmpty(companyDynamicSupervision.getId())){
			//编辑
			//查询修改之前的对象
			CompanyDynamicSupervision oldCompanyDynamicSupervision = companyDynamicSupervisionService.getById(companyDynamicSupervision.getId());
			//状态为正常
			if(Constant.status.NORMAL.equals(oldCompanyDynamicSupervision.getStatus())){
				//修改老数据状态为过期
				oldCompanyDynamicSupervision.setStatus(Constant.status.EXPIRED);
				companyDynamicSupervisionService.updateById(oldCompanyDynamicSupervision);
				//新增修改后的为新数据
				companyDynamicSupervision.setId("");
				companyDynamicSupervisionService.save(companyDynamicSupervision);
			}else if(Constant.status.NOPASS.equals(oldCompanyDynamicSupervision.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyDynamicSupervision.getStatus())){
				companyDynamicSupervisionService.updateById(companyDynamicSupervision);
			}
		}else{
	 		companyDynamicSupervisionService.save(companyDynamicSupervision);
		}
		 return Result.ok("申报成功！");
	 }

	/**
	 *   添加
	 *
	 * @param companyDynamicSupervision
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-添加")
	@ApiOperation(value="企业年度动态监管-添加", notes="企业年度动态监管-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyDynamicSupervision companyDynamicSupervision) {
		companyDynamicSupervisionService.save(companyDynamicSupervision);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyDynamicSupervision
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-编辑")
	@ApiOperation(value="企业年度动态监管-编辑", notes="企业年度动态监管-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyDynamicSupervision companyDynamicSupervision) {
		companyDynamicSupervisionService.updateById(companyDynamicSupervision);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-通过id删除")
	@ApiOperation(value="企业年度动态监管-通过id删除", notes="企业年度动态监管-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyDynamicSupervisionService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-批量删除")
	@ApiOperation(value="企业年度动态监管-批量删除", notes="企业年度动态监管-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyDynamicSupervisionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	 /**
	  *  批量申报
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "企业年度动态监管-批量申报")
	 @ApiOperation(value="企业年度动态监管-批量申报", notes="企业年度动态监管-批量申报")
	 @GetMapping(value = "/batchDeclare")
	 public Result<?> batchDeclare(@RequestParam(name="ids",required=true) String ids) {
		 List<String> idList = Arrays.asList(ids.split(","));
		 if (CollectionUtil.isNotEmpty(idList)) {
			 for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
				 String id = iterator.next();
				 //查询
				 CompanyDynamicSupervision companyDynamicSupervision = companyDynamicSupervisionService.getById(id);
				 //判断申报的是否是暂存
				 if (!Constant.status.TEMPORARY.equals(companyDynamicSupervision.getStatus())) {
					 return Result.error("请选择暂存的信息申报！");
				 }
				 //修改状态为1：待审核状态
				 companyDynamicSupervision.setStatus(Constant.status.PEND);
				 companyDynamicSupervisionService.updateById(companyDynamicSupervision);

			 }
		 }
		 return Result.ok("批量申报成功!");
	 }

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-通过id查询")
	@ApiOperation(value="企业年度动态监管-通过id查询", notes="企业年度动态监管-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyDynamicSupervision companyDynamicSupervision = companyDynamicSupervisionService.getById(id);
		if(companyDynamicSupervision==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyDynamicSupervision);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyDynamicSupervision
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyDynamicSupervision companyDynamicSupervision) {
        return super.exportXls(request, companyDynamicSupervision, CompanyDynamicSupervision.class, "企业年度动态监管");
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
        return super.importExcel(request, response, CompanyDynamicSupervision.class);
    }

}
