package org.jeecg.modules.cache;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.business.entity.SysPollutionCode;
import org.jeecg.modules.business.service.ISysPollutionCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * redis缓存
 */
@Component
@Slf4j
public class PullutionCodeCache {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysPollutionCodeService sysPollutionCodeService;
    @PostConstruct
    public void initial() {
        this.initialCache();
    }
    public void initialCache(){
        sysPollutionCodeService.list().forEach(sysPollutionCode -> {
            redisUtil.hset("pullution_code_cache",sysPollutionCode.getCode(),sysPollutionCode.getMeaning());
        });


    }

}