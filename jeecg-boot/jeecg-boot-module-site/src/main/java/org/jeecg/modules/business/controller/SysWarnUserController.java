package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SysWarnUser;
import org.jeecg.modules.business.service.ISysWarnUserService;

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
 * @Description: 报警短信接收人
 * @Author: jeecg-boot
 * @Date:   2020-07-09
 * @Version: V1.0
 */
@Api(tags="报警短信接收人")
@RestController
@RequestMapping("/swu/sysWarnUser")
@Slf4j
public class SysWarnUserController extends JeecgController<SysWarnUser, ISysWarnUserService> {
	@Autowired
	private ISysWarnUserService sysWarnUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysWarnUser
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "报警短信接收人-分页列表查询")
	@ApiOperation(value="报警短信接收人-分页列表查询", notes="报警短信接收人-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysWarnUser sysWarnUser,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysWarnUser> queryWrapper = QueryGenerator.initQueryWrapper(sysWarnUser, req.getParameterMap());
		Page<SysWarnUser> page = new Page<SysWarnUser>(pageNo, pageSize);
		IPage<SysWarnUser> pageList = sysWarnUserService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysWarnUser
	 * @return
	 */
	@AutoLog(value = "报警短信接收人-添加")
	@ApiOperation(value="报警短信接收人-添加", notes="报警短信接收人-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysWarnUser sysWarnUser) {
		sysWarnUserService.save(sysWarnUser);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysWarnUser
	 * @return
	 */
	@AutoLog(value = "报警短信接收人-编辑")
	@ApiOperation(value="报警短信接收人-编辑", notes="报警短信接收人-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysWarnUser sysWarnUser) {
		sysWarnUserService.updateById(sysWarnUser);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报警短信接收人-通过id删除")
	@ApiOperation(value="报警短信接收人-通过id删除", notes="报警短信接收人-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysWarnUserService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "报警短信接收人-批量删除")
	@ApiOperation(value="报警短信接收人-批量删除", notes="报警短信接收人-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysWarnUserService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报警短信接收人-通过id查询")
	@ApiOperation(value="报警短信接收人-通过id查询", notes="报警短信接收人-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysWarnUser sysWarnUser = sysWarnUserService.getById(id);
		if(sysWarnUser==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sysWarnUser);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysWarnUser
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysWarnUser sysWarnUser) {
        return super.exportXls(request, sysWarnUser, SysWarnUser.class, "报警短信接收人");
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
        return super.importExcel(request, response, SysWarnUser.class);
    }

}
