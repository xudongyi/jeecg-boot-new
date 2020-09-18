package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.annotation.ExcelSelf;

@Data
public class OverStandardRate {

    String mn;
    @ExcelSelf(name = "企业名称", width = 30,orderNum = 1)
    String companyName;
    @ExcelSelf(name = "监测点名称", width = 20,orderNum = 2)
    String siteName;

    @ExcelSelf(name = "时间", width = 30,orderNum = 3)
    String period;
    @ExcelSelf(name = "总有效数据条数", width = 15,orderNum = 4)
    Integer totalCount;
    @ExcelSelf(name = "超标数据条数", width = 15,orderNum = 5)
    Integer overCount;
    @ExcelSelf(name = "超标率", width = 15,orderNum = 6)
    String overRate;
}
