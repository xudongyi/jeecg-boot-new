package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class WarnPointRule {
    private String id;
    private String companyName;
    private String siteName;
    private String siteType;
    private String realTimeOver;
    private String hourOver;
    private String dayOver;
    private String offLine;
    private String deviceFail;
    private String constant;
    private String measureDistance;
    private String dataAbnormal;

}
