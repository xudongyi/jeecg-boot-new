package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class WarnOldCount {
    private String companyName;
    private String companyId;
    private String mn;
    private String siteName;
    private String warnType;
    private String warnLevel;
    private Integer num;
}
