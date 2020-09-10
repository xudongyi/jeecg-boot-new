package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.annotation.ExcelSelf;


import java.sql.Timestamp;

@Data
public class RealTimeWarn {
    /**id*/
    private java.lang.String id;
    /**companyId*/
    @ExcelSelf(name = "企业名称", width = 30,orderNum = 1)
    private java.lang.String companyName;
    /**mn*/
    @ExcelSelf(name = "监控点名称", width = 15,orderNum = 2)
    private java.lang.String siteName;
    /**warnType*/
    @ExcelSelf(name = "报警类型", width = 15,orderNum = 3)
    private java.lang.String warnType;
    /**code*/
    @ExcelSelf(name = "污染因子", width = 15,orderNum = 4)
    private java.lang.String meaning;
    /**warnLevel*/
    @ExcelSelf(name = "报警级别", width = 15,orderNum = 5)
    private java.lang.String warnLevel;
    /**content*/
    @ExcelSelf(name = "报警内容", width = 15,orderNum = 6)
    private java.lang.String content;
    /**warnTime*/
    @ExcelSelf(name = "报警时间", width = 20, format = "yyyy-MM-dd HH:mm:ss",orderNum = 7)
    Timestamp warnTime;
}
