package org.jeecg.modules.business.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SysPollutionCode;
import org.jeecg.modules.business.entity.WaterCurrentTr;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.business.service.ISysPollutionCodeService;
import org.jeecg.modules.business.service.IWaterCurrentTrService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: water_current_tr
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
@Api(tags="water_current_tr")
@RestController
@RequestMapping("/onlineMonitor")
@Slf4j
public class WaterCurrentTrController extends JeecgController<WaterCurrentTr, IWaterCurrentTrService> {
	@Autowired
	private IWaterCurrentTrService waterCurrentTrService;
	@Autowired
	private ISysPollutionCodeService sysPollutionCodeService;
	@Autowired
	private ISysDictService sysDictService;

	 @AutoLog(value = "查询表头")
	 @ApiOperation(value="查询表头", notes="查询表头")
	 @GetMapping(value = "/queryColumns")
	 public Result<?> queryColumns(@RequestParam(name="type") Integer type) {
		 List<SysPollutionCode> sysPollutionCodes = sysPollutionCodeService.list(new QueryWrapper<SysPollutionCode>().lambda().eq(SysPollutionCode::getType, type).eq(SysPollutionCode::getIsUse, "Y"));
		 List<Column> columns =null;
		 if(CollectionUtil.isNotEmpty(sysPollutionCodes)){
			 columns = new ArrayList<>();
			 for (SysPollutionCode sysPollutionCode : sysPollutionCodes) {
				 Column column = new Column();
			 	if("w00000".equalsIgnoreCase(sysPollutionCode.getCode())){
					Column columnq = new Column();
					String amountUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getAmountUnit());
					columnq.setTitle("污水排放量("+amountUnit+")");
					columnq.setDataIndex("w00000Total");
					columns.add(columnq);
					String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
					column.setTitle(sysPollutionCode.getMeaning()+"("+chromaUnit+")");
					column.setDataIndex(sysPollutionCode.getCode()+"Rtd");
					columns.add(column);
				}else{
					String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
					column.setTitle(sysPollutionCode.getMeaning()+"("+chromaUnit+")");
					column.setDataIndex(sysPollutionCode.getCode()+"Rtd");
					columns.add(column);
			 	}

			 }
		 }
		 return Result.ok(columns);
	 }
	
	/**
	 * 分页列表查询
	 *
	 * @param waterCurrentTr
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "water_current_tr-分页列表查询")
	@ApiOperation(value="water_current_tr-分页列表查询", notes="water_current_tr-分页列表查询")
	@GetMapping(value = "/waterCurrentTr/list")
	public Result<?> queryPageList(WaterCurrentTr waterCurrentTr,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WaterCurrentTr> queryWrapper = QueryGenerator.initQueryWrapper(waterCurrentTr, req.getParameterMap());
		Page<WaterCurrentTr> page = new Page<WaterCurrentTr>(pageNo, pageSize);
		IPage<WaterCurrentTr> pageList = waterCurrentTrService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param waterCurrentTr
	 * @return
	 */
	@AutoLog(value = "water_current_tr-添加")
	@ApiOperation(value="water_current_tr-添加", notes="water_current_tr-添加")
	@PostMapping(value = "/waterCurrentTr/add")
	public Result<?> add(@RequestBody WaterCurrentTr waterCurrentTr) {
		waterCurrentTrService.save(waterCurrentTr);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param waterCurrentTr
	 * @return
	 */
	@AutoLog(value = "water_current_tr-编辑")
	@ApiOperation(value="water_current_tr-编辑", notes="water_current_tr-编辑")
	@PutMapping(value = "/waterCurrentTr/edit")
	public Result<?> edit(@RequestBody WaterCurrentTr waterCurrentTr) {
		waterCurrentTrService.updateById(waterCurrentTr);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "water_current_tr-通过id删除")
	@ApiOperation(value="water_current_tr-通过id删除", notes="water_current_tr-通过id删除")
	@DeleteMapping(value = "/waterCurrentTr/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		waterCurrentTrService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "water_current_tr-批量删除")
	@ApiOperation(value="water_current_tr-批量删除", notes="water_current_tr-批量删除")
	@DeleteMapping(value = "/waterCurrentTr/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.waterCurrentTrService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "water_current_tr-通过id查询")
	@ApiOperation(value="water_current_tr-通过id查询", notes="water_current_tr-通过id查询")
	@GetMapping(value = "/waterCurrentTr/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WaterCurrentTr waterCurrentTr = waterCurrentTrService.getById(id);
		if(waterCurrentTr==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(waterCurrentTr);
	}

}
