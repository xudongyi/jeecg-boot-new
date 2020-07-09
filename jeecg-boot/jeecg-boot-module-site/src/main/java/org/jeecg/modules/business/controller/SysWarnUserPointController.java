package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SysWarnUserPoint;
import org.jeecg.modules.business.service.ISysWarnUserPointService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.SysWarnUserPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 站点报警短信接收人配置
 * @Author: jeecg-boot
 * @Date:   2020-07-08
 * @Version: V1.0
 */
@Api(tags="站点报警短信接收人配置")
@RestController
@RequestMapping("/swup/sysWarnUserPoint")
@Slf4j
public class SysWarnUserPointController extends JeecgController<SysWarnUserPoint, ISysWarnUserPointService> {
	@Autowired
	private ISysWarnUserPointService sysWarnUserPointService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysWarnUserPoint
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "站点报警短信接收人配置-分页列表查询")
	@ApiOperation(value="站点报警短信接收人配置-分页列表查询", notes="站点报警短信接收人配置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysWarnUserPoint sysWarnUserPoint,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String name = req.getParameter("name");
		String mobile = req.getParameter("mobile");
		Page<SysWarnUserPointVO> page = new Page<SysWarnUserPointVO>(pageNo, pageSize);
		IPage<SysWarnUserPointVO> pageList = sysWarnUserPointService.getSysWarnUserPoint(page, name,mobile);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysWarnUserPoint
	 * @return
	 */
	@AutoLog(value = "站点报警短信接收人配置-添加")
	@ApiOperation(value="站点报警短信接收人配置-添加", notes="站点报警短信接收人配置-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysWarnUserPoint sysWarnUserPoint) {
		sysWarnUserPointService.save(sysWarnUserPoint);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysWarnUserPoint
	 * @return
	 */
	@AutoLog(value = "站点报警短信接收人配置-编辑")
	@ApiOperation(value="站点报警短信接收人配置-编辑", notes="站点报警短信接收人配置-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysWarnUserPoint sysWarnUserPoint) {
		sysWarnUserPointService.updateById(sysWarnUserPoint);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "站点报警短信接收人配置-通过id删除")
	@ApiOperation(value="站点报警短信接收人配置-通过id删除", notes="站点报警短信接收人配置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysWarnUserPointService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "站点报警短信接收人配置-批量删除")
	@ApiOperation(value="站点报警短信接收人配置-批量删除", notes="站点报警短信接收人配置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysWarnUserPointService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "站点报警短信接收人配置-通过id查询")
	@ApiOperation(value="站点报警短信接收人配置-通过id查询", notes="站点报警短信接收人配置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysWarnUserPoint sysWarnUserPoint = sysWarnUserPointService.getById(id);
		if(sysWarnUserPoint==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sysWarnUserPoint);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysWarnUserPoint
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysWarnUserPoint sysWarnUserPoint) {
        return super.exportXls(request, sysWarnUserPoint, SysWarnUserPoint.class, "站点报警短信接收人配置");
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
        return super.importExcel(request, response, SysWarnUserPoint.class);
    }

}
