package org.jeecg.modules.business.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.VocCurrentOverproof;
import org.jeecg.modules.business.service.IVocCurrentOverproofService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: voc_current_overproof
 * @Author: jeecg-boot
 * @Date:   2020-09-02
 * @Version: V1.0
 */
@Api(tags="voc_current_overproof")
@RestController
@RequestMapping("/vocOver/vocCurrentOverproof")
@Slf4j
public class VocCurrentOverproofController extends JeecgController<VocCurrentOverproof, IVocCurrentOverproofService> {
	@Autowired
	private IVocCurrentOverproofService vocCurrentOverproofService;
	
	/**
	 * 分页列表查询
	 *
	 * @param vocCurrentOverproof
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "voc_current_overproof-分页列表查询")
	@ApiOperation(value="voc_current_overproof-分页列表查询", notes="voc_current_overproof-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(VocCurrentOverproof vocCurrentOverproof,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<VocCurrentOverproof> queryWrapper = QueryGenerator.initQueryWrapper(vocCurrentOverproof, req.getParameterMap());
		Page<VocCurrentOverproof> page = new Page<VocCurrentOverproof>(pageNo, pageSize);
		IPage<VocCurrentOverproof> pageList = vocCurrentOverproofService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param vocCurrentOverproof
	 * @return
	 */
	@AutoLog(value = "voc_current_overproof-添加")
	@ApiOperation(value="voc_current_overproof-添加", notes="voc_current_overproof-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody VocCurrentOverproof vocCurrentOverproof) {
		vocCurrentOverproofService.save(vocCurrentOverproof);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param vocCurrentOverproof
	 * @return
	 */
	@AutoLog(value = "voc_current_overproof-编辑")
	@ApiOperation(value="voc_current_overproof-编辑", notes="voc_current_overproof-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody VocCurrentOverproof vocCurrentOverproof) {
		vocCurrentOverproofService.updateById(vocCurrentOverproof);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "voc_current_overproof-通过id删除")
	@ApiOperation(value="voc_current_overproof-通过id删除", notes="voc_current_overproof-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		vocCurrentOverproofService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "voc_current_overproof-批量删除")
	@ApiOperation(value="voc_current_overproof-批量删除", notes="voc_current_overproof-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.vocCurrentOverproofService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "voc_current_overproof-通过id查询")
	@ApiOperation(value="voc_current_overproof-通过id查询", notes="voc_current_overproof-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		VocCurrentOverproof vocCurrentOverproof = vocCurrentOverproofService.getById(id);
		if(vocCurrentOverproof==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(vocCurrentOverproof);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param vocCurrentOverproof
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, VocCurrentOverproof vocCurrentOverproof) {
        return super.exportXls(request, vocCurrentOverproof, VocCurrentOverproof.class, "voc_current_overproof");
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
        return super.importExcel(request, response, VocCurrentOverproof.class);
    }

}
