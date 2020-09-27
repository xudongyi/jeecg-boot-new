package org.jeecg.modules.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.entity.SourceDirectory;
import org.jeecg.modules.business.service.ISourceDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @Description: 污染源名录库
* @Author: jeecg-boot
* @Date:   2020-09-15
* @Version: V1.0
*/
@Api(tags="污染源名录库")
@RestController
@RequestMapping("/sourceDirectory")
@Slf4j
public class SourceDirectoryController extends JeecgController<SourceDirectory, ISourceDirectoryService> {
   @Autowired
   private ISourceDirectoryService sourceDirectoryService;

   /**
    * 通过id查询
    *
    * @return
    */
   @AutoLog(value = "污染源信息-通过id查询")
   @ApiOperation(value="污染源信息-通过id查询", notes="污染源信息-通过id查询")
   @GetMapping(value = "/queryByCompanyId")
   public Result<?> queryByCompanyId(@RequestParam(name="companyId",required=true) String companyId) {
       SourceDirectory sourceDirectory = sourceDirectoryService.getOne(new QueryWrapper<SourceDirectory>().lambda().eq(SourceDirectory::getCompanyId,companyId));
       return Result.ok(sourceDirectory);
   }

    /**
     *   新增
     *
     * @return
     */
    @AutoLog(value = "污染源信息-添加")
    @ApiOperation(value="污染源信息-添加", notes="污染源信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SourceDirectory sourceDirectory) {
        sourceDirectoryService.save(sourceDirectory);
        return Result.ok("添加成功！");
    }

    /**
     *  编辑
     *
     * @param sourceDirectory
     * @return
     */
    @AutoLog(value = "污染源信息-编辑")
    @ApiOperation(value="污染源信息-编辑", notes="污染源信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SourceDirectory sourceDirectory) {
        sourceDirectoryService.updateById(sourceDirectory);
        return Result.ok("编辑成功!");
    }

}
