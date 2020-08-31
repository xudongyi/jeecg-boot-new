package org.jeecg.modules.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.entity.PsoamConfig;
import org.jeecg.modules.business.service.IPsoamConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 监测类型配置
 * @Author: jeecg-boot
 * @Date: 2020-08-31
 * @Version: V1.0
 */
@Api(tags = "监测类型配置")
@RestController
@RequestMapping("/psoamConfig")
@Slf4j
public class PsoamConfigController extends JeecgController<PsoamConfig, IPsoamConfigService> {
    @Autowired
    private IPsoamConfigService psoamConfigService;


    @AutoLog(value = "监测类型配置查询")
    @ApiOperation(value = "监测类型配置查询", notes = "监测类型配置查询")
    @GetMapping(value = "/queryConfig")
    public Result<?> queryConfig() {
        PsoamConfig psoamConfig = psoamConfigService.getOne(new QueryWrapper<PsoamConfig>().lambda().eq(PsoamConfig::getId, "1"));
        return Result.ok(psoamConfig);
    }


    /**
     * 保存
     *
     * @param
     * @return
     */
    @AutoLog(value = "监测类型配置-保存")
    @ApiOperation(value = "监测类型配置-保存", notes = "监测类型配置-保存")
    @GetMapping(value = "/saveConfig")
    public Result<?> saveConfig(@RequestParam(name = "waterChecked") boolean waterChecked, @RequestParam(name = "gasChecked") boolean gasChecked, @RequestParam(name = "vocsChecked") boolean vocsChecked) {
		PsoamConfig psoamConfig = psoamConfigService.getOne(new QueryWrapper<PsoamConfig>().lambda().eq(PsoamConfig::getId, "1"));
		psoamConfig.setGasChecked(gasChecked);
		psoamConfig.setWaterChecked(waterChecked);
		psoamConfig.setVocsChecked(vocsChecked);
		boolean b = psoamConfigService.updateById(psoamConfig);
		if(!b){
			return Result.ok(false);
		}
		return Result.ok(true);
    }

}
