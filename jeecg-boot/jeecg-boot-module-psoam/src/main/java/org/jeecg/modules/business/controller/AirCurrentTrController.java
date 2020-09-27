package org.jeecg.modules.business.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.entity.AirCurrentTr;
import org.jeecg.modules.business.entity.SysPollutionCode;
import org.jeecg.modules.business.service.IAirCurrentTrService;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.business.service.ISysPollutionCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: air_current_tr
 * @Author: jeecg-boot
 * @Date: 2020-09-24
 * @Version: V1.0
 */
@Api(tags = "air_current_tr")
@RestController
@RequestMapping("/AirCurrentTr/airCurrentTr")
@Slf4j
public class AirCurrentTrController extends JeecgController<AirCurrentTr, IAirCurrentTrService> {

    @Autowired
    private IAirCurrentTrService airCurrentTrService;
    @Autowired
    private ISysPollutionCodeService sysPollutionCodeService;
    @Autowired
    private ISysDictService sysDictService;

    @AutoLog(value = "废气实时工况")
    @ApiOperation(value = "废气实时工况")
    @GetMapping(value = "/loadDetail")
    public Result<?> loadDetail(@RequestParam(name = "siteId", required = true) String siteId) {
        List<Map<String,String>> result = new ArrayList<>();
        //获取当前时间
        String dataTime = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm");
        //查询指定站点最新一条数据
        Map<String,Object> airCurrentTr = airCurrentTrService.findBySiteIdAndTime(siteId,dataTime);
        //解析成前端需要的格式
        result.add(this.getDetail(airCurrentTr,"A00000"));//废水
        result.add(this.getDetail(airCurrentTr,"A19001"));//氧气含量
        result.add(this.getDetail(airCurrentTr,"A01011"));//烟气流速
        result.add(this.getDetail(airCurrentTr,"A34013"));//烟尘
        result.add(this.getDetail(airCurrentTr,"A01012"));//烟气温度
        result.add(this.getDetail(airCurrentTr,"A01014"));//烟气湿度
        return Result.ok(result);
    }

    private Map<String,String> getDetail(Map<String,Object> airCurrentTr,String pollutionCOde){
        SysPollutionCode sysPollutionCode = sysPollutionCodeService.getOne(new QueryWrapper<SysPollutionCode>().lambda().eq(SysPollutionCode::getType, 1).eq(SysPollutionCode::getCode, pollutionCOde));
        Map<String,String> resultMap = new HashMap<>();
        if(airCurrentTr!=null){
            String rtd = StrUtil.toString(airCurrentTr.get(pollutionCOde+"_RTD"));
            String chromaUnit = sysDictService.queryDictTextByKey("allUnit", sysPollutionCode.getChromaUnit());
            rtd += chromaUnit;
            resultMap.put("rtd",rtd);
            String color = "";
            if(StrUtil.isNotEmpty(rtd)){
                int a00000_state = (Integer)airCurrentTr.get(pollutionCOde+"_STATE");
                color = this.getColorByState(a00000_state);
            }else{
                color = "#A1A1A1";
            }
            resultMap.put("color",color);
        }
        return resultMap;
    }

    private String getColorByState(int state){
        if(state==0){
            //正常
            return "#00D660";
        }else if(state==-1||state==1){
            //超标
            return "#FF2424";
        }else if((state>=2&&state<=5)||(state>=-5&&state<=-2)){
            //预警
            return "#FF9710";
        }else if(state==-6||state==6||state==9||state==-8||state==8||state==7){
            //异常、超过量程
            return  "#CD66C2";
        }
        return "A1A1A1";
    }
}
