package org.jeecg.modules.business.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.SysArea;
import org.jeecg.modules.business.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 系统区域表
 * @Author: jeecg-boot
 * @Date:   2020-07-03
 * @Version: V1.0
 */
@Api(tags="系统区域表")
@RestController
@RequestMapping("/sys/sysArea")
@Slf4j
public class SysAreaController extends JeecgController<SysArea, ISysAreaService> {
	@Autowired
	private ISysAreaService sysAreaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param req
	 * @return
	 */
	@AutoLog(value = "系统区域表-分页列表查询")
	@ApiOperation(value="系统区域表-分页列表查询", notes="系统区域表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(
			HttpServletRequest req) {
		//可以为空  为空查询所有
		String active = req.getParameter("active");
		LambdaQueryWrapper<SysArea> queryWrapper= new QueryWrapper<SysArea>().lambda();
		if(!StrUtil.isEmpty(active))
			queryWrapper.eq(SysArea::getActive,active);

		List<SysArea> sysAreas = sysAreaService.list(queryWrapper);
		//对数据做分级处理
		Map<String,Map<String,String>> result = new HashMap<>();
		for(SysArea sysArea:sysAreas){
			result.putIfAbsent(sysArea.getParentCode(),new HashMap<>());
			result.get(sysArea.getParentCode()).put(sysArea.getCode(),sysArea.getName());
		}
		return Result.ok(result);
	}

	/**
	 *   添加
	 *
	 * @param sysArea
	 * @return
	 */
	@AutoLog(value = "系统区域表-添加")
	@ApiOperation(value="系统区域表-添加", notes="系统区域表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysArea sysArea) {
		sysAreaService.save(sysArea);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param requset
	 * @return
	 */
	@AutoLog(value = "系统区域表-active更新")
	@ApiOperation(value="系统区域表-active更新", notes="系统区域表-active更新")
	@PostMapping(value = "/change")
	public Result<?> edit(@RequestBody JSONObject requset) {

		sysAreaService.update(new UpdateWrapper<SysArea>().lambda().set(SysArea::getActive,"0"));
		sysAreaService.update(new UpdateWrapper<SysArea>().lambda().in(SysArea::getCode,requset.getJSONArray("keys").toJavaList(String.class))
				.set(SysArea::getActive,"1"));
		return Result.ok("系统区域表更新成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "系统区域表-通过id删除")
	@ApiOperation(value="系统区域表-通过id删除", notes="系统区域表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysAreaService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "系统区域表-批量删除")
	@ApiOperation(value="系统区域表-批量删除", notes="系统区域表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysAreaService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "系统区域表-通过id查询")
	@ApiOperation(value="系统区域表-通过id查询", notes="系统区域表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysArea sysArea = sysAreaService.getById(id);
		if(sysArea==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sysArea);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysArea
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysArea sysArea) {
        return super.exportXls(request, sysArea, SysArea.class, "系统区域表");
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
        return super.importExcel(request, response, SysArea.class);
    }

}
