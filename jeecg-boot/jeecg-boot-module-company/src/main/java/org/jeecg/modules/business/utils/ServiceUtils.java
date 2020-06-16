package org.jeecg.modules.business.utils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.SpringContextUtils;

public class ServiceUtils {

    public static ServiceImpl getService( String fromTable) {
        String[]  arrs = fromTable.split("_");
        StringBuilder sb = new StringBuilder(arrs[0]);
        for(int i =1;i<arrs.length;i++) sb.append(toUpperFirstChar2(arrs[i]));

        //获取bean对象
        return (ServiceImpl) SpringContextUtils.getBean(sb.toString()+"ServiceImpl");
    }
    // 高效率  首字母转大写
    private static String toUpperFirstChar2(String string) {
        char[] chars = string.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] -= 32;
            return String.valueOf(chars);
        }
        return string;
    }
}
