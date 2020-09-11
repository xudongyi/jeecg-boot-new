package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class WarnCount {
    private String id;
    private String companyName;
    private String companyId;
    private String mn;
    private String siteName;
    //预警数量
    private Integer earlyWarnCount;
    //超标报警数量 0,1,2
    private Integer overWarnCount;
    //数据异常报警数量 7
    private Integer abnormalCount;
    //定值报警数量 6
    private Integer constantCount;
}
