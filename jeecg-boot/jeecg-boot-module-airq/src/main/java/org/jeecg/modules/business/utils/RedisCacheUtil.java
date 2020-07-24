package org.jeecg.modules.business.utils;

import cn.hutool.core.util.StrUtil;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheUtil {

    @Autowired
    private RedisUtil redisUtil;

    //获取
    public String transformCode(String codes){
        if(StrUtil.isEmpty(codes))
            return codes;
        String[] codeArr  = codes.split(",");
        String tmp = null;
        StringBuilder meanings = new StringBuilder();
        for(String code : codeArr){
            tmp = redisUtil.hget("pullution_code_cache",code).toString();
            if (!StrUtil.isEmpty(tmp)) {
                if (meanings.length()>0) {
                    meanings.append(",");
                }
                meanings.append(tmp);
            }
        }
        return  meanings.toString();
    }
}