package org.jeecg.modules.business.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

public class Util {

    public static String getTableName(String tableName,String dateTime){
        if(!StrUtil.isEmpty(dateTime))
            tableName = tableName+ dateTime.substring(2,4)+dateTime.substring(5,7);
        else
            tableName = tableName+ ( DateUtil.date().year()-2000)+ (DateUtil.date().month()<9?"0":"")+(DateUtil.date().month()+1);
        return tableName;
    }

}
