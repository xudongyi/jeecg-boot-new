package org.jeecg.modules.cas.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.business.service.ICompanySysuserService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.util.LoginUtil;
import org.jeecg.modules.system.vo.SysUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/redis/client")
public class RedisClientController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    ISysDictService sysDictService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    ICompanySysuserService companySysuserService;
    @GetMapping("/validateLogin")
    public Object validateLogin(@RequestParam(name="tc") String tc,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        Result<JSONObject> result = new Result<JSONObject>();
        log.info("redis api login.");
        Object o =  redisUtil.get(tc);
        if(o==null){
            result.error500("免密登录失败");
            return result;
        }
        redisUtil.expire(tc,0);
        SysUserVo sysUserVo =  JSONObject.parseObject(o.toString(),SysUserVo.class);
        LoginUtil.userInfo(sysUserVo, result,redisUtil,sysDepartService,sysUserService,sysDictService);

        return new HttpEntity<>(result);
    }


}
