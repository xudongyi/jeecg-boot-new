package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.annotation.ExcelSelf;

@Data
public class WarnCount {
    private String id;

    @ExcelSelf(name = "企业名称", width = 30,orderNum = 1)
    private String companyName;

    private String companyId;

    private String mn;

    @ExcelSelf(name = "监控点名称", width = 15,orderNum = 2)
    private String siteName;
    //预警数量
    @ExcelSelf(name = "预警数量", width = 15,orderNum = 3)
    private Integer earlyWarnCount;
    //超标报警数量 0,1,2
    @ExcelSelf(name = "超标报警数量", width = 15,orderNum = 4)
    private Integer overWarnCount;
    //数据异常报警数量 7
    @ExcelSelf(name = "数据异常报警数量", width = 15,orderNum = 5)
    private Integer abnormalCount;
    //定值报警数量 6
    @ExcelSelf(name = "定值报警数量", width = 15,orderNum = 6)
    private Integer constantCount;
}
