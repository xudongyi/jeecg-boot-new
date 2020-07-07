package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.entity.SysWarnPointRule;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class SysWarnPointListVO extends SysWarnPointRule {
    /**站点名称*/
    private String siteName;

    /**站点类型*/
    @Dict(dicCode= "siteType")
    private String siteType;

    /**所属单位*/
    private String companyName;

    /**站点级别*/
    @Dict(dicCode = "siteLevel")
    private String siteLevel;

    /**策略类型*/
    @Dict(dicCode = "rule_type")
    private java.lang.String ruleType;

    /**是否发送短信*/
    @Dict(dicCode = "is_send_msg")
    private java.lang.String isSendMsg;

    /**发送频率*/
    private java.lang.Integer msgRate;

    /**发送短信开始时间*/
    @Excel(name = "发送短信开始时间", width = 20, format = "HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "HH:mm:ss")
    @DateTimeFormat(pattern="HH:mm:ss",iso = DateTimeFormat.ISO.TIME)
    @ApiModelProperty(value = "发送短信开始时间")
    private java.sql.Time warnStarttime;

    /**发送短信结束时间*/
    @Excel(name = "发送短信结束时间", width = 20, format = "HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "HH:mm:ss")
    @DateTimeFormat(pattern="HH:mm:ss",iso = DateTimeFormat.ISO.TIME)
    @ApiModelProperty(value = "发送短信结束时间")
    private java.sql.Time warnEndtime;

    /**策略说明*/
    @Excel(name = "策略说明", width = 15)
    @ApiModelProperty(value = "策略说明")
    private java.lang.String content;

}
