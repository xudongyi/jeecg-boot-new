package org.jeecg.modules.business.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@Slf4j
public class RedisCacheUtil<T> {

    @Autowired
    private RedisUtil redisUtil;

    //获取
    public String transformCode(String codes){
        if(StrUtil.isEmpty(codes)||codes.equals("null"))
            return "";
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

    public T getAdviceAndContent(String level,Class<T> clazz) {
        Map<Object, Object> map = redisUtil.hmget("airq_level_map");
        if (map != null && !map.isEmpty()) {
            try {
               return  clazz.cast(CommonsUtil.toJsonObject((String) map.get(level),clazz));
            } catch (Exception var12) {
                this.log.error("Redis错误[获取空气质量等级]:" + var12.getMessage());
            }
        } else {
            this.log.error("Redis提示[获取空气质量等级]:未取到值");

        }
        return null;
    }
}