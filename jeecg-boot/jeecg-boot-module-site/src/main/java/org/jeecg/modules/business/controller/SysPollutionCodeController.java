package org.jeecg.modules.business.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SysPollutionCode;
import org.jeecg.modules.business.service.ISysPollutionCodeService;

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
 * @Description: 污染因子表
 * @Author: jeecg-boot
 * @Date:   2020-07-01
 * @Version: V1.0
 */
@Api(tags="污染因子表")
@RestController
@RequestMapping("/spc/sysPollutionCode")
@Slf4j
public class SysPollutionCodeController extends JeecgController<SysPollutionCode, ISysPollutionCodeService> {
	@Autowired
	private ISysPollutionCodeService sysPollutionCodeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysPollutionCode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "污染因子表-分页列表查询")
	@ApiOperation(value="污染因子表-分页列表查询", notes="污染因子表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysPollutionCode sysPollutionCode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysPollutionCode> queryWrapper = QueryGenerator.initQueryWrapper(sysPollutionCode, req.getParameterMap());
		Page<SysPollutionCode> page = new Page<SysPollutionCode>(pageNo, pageSize);
		IPage<SysPollutionCode> pageList = sysPollutionCodeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysPollutionCode
	 * @return
	 */
	@AutoLog(value = "污染因子表-添加")
	@ApiOperation(value="污染因子表-添加", notes="污染因子表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysPollutionCode sysPollutionCode) {
		String code = sysPollutionCode.getCode();
		String type = sysPollutionCode.getType();
		List<SysPollutionCode> list = sysPollutionCodeService.list(new QueryWrapper<SysPollutionCode>().lambda().eq(SysPollutionCode::getCode,code).eq(SysPollutionCode::getType,type));
		//int count =sysPollutionCodeService.count(new QueryWrapper<SysPollutionCode>().lambda().eq(SysPollutionCode::getCode,code).eq(SysPollutionCode::getType,type));
		if(!list.isEmpty()) {
			return Result.error("该污染因子已存在,请勿重复添加");
		}else {
			sysPollutionCodeService.save(sysPollutionCode);
			return Result.ok("添加成功！");
		}
	}
	
	/**
	 *  编辑
	 *
	 * @param sysPollutionCode
	 * @return
	 */
	@AutoLog(value = "污染因子表-编辑")
	@ApiOperation(value="污染因子表-编辑", notes="污染因子表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysPollutionCode sysPollutionCode) {
		String code = sysPollutionCode.getCode();
		String type = sysPollutionCode.getType();
		List<SysPollutionCode> list = sysPollutionCodeService.list(new QueryWrapper<SysPollutionCode>().lambda().eq(SysPollutionCode::getCode,code).eq(SysPollutionCode::getType,type));
		if(!list.isEmpty()) {
			return Result.error("该污染因子已存在");
		}else {
			sysPollutionCodeService.updateById(sysPollutionCode);
			return Result.ok("编辑成功!");
		}
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "污染因子表-通过id删除")
	@ApiOperation(value="污染因子表-通过id删除", notes="污染因子表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysPollutionCodeService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "污染因子表-批量删除")
	@ApiOperation(value="污染因子表-批量删除", notes="污染因子表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysPollutionCodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "污染因子表-通过id查询")
	@ApiOperation(value="污染因子表-通过id查询", notes="污染因子表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysPollutionCode sysPollutionCode = sysPollutionCodeService.getById(id);
		if(sysPollutionCode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sysPollutionCode);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysPollutionCode
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysPollutionCode sysPollutionCode) {
        return super.exportXls(request, sysPollutionCode, SysPollutionCode.class, "污染因子表");
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
        return super.importExcel(request, response, SysPollutionCode.class);
    }

}
