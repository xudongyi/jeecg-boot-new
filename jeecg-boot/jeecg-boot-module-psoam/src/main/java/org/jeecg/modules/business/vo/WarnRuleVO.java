package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.business.entity.WarnRule;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class WarnRuleVO {
    private String id;
    private String mn;
    private String ruleId;
    private String isUsed;
    private java.lang.String ruleType;
    /**监测类型*/
    private java.lang.String siteType;
    /**污染因子*/
    private java.lang.String code;
    /**报警级别*/
    private java.lang.String ruleLevel;
    /**阈值上限*/
    private java.lang.Double warnValueUp;
    /**阈值下限*/
    private java.lang.Double warnValueDown;
    /**是否留样*/
    private java.lang.String isSample;
    /**是否关阀*/
    private java.lang.String isCloseTap;
    /**是否发送短信*/
    private java.lang.String isSendMsg;
    /**是否同步短信联系人*/
    private java.lang.String isSynchronize;
    /**短信频率*/
    private java.lang.Integer msgRate;
    /**短信发送开始时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "HH:mm:ss")
    @DateTimeFormat(pattern="HH:mm:ss",iso = DateTimeFormat.ISO.TIME)
    @ApiModelProperty(value = "发送短信开始时间")
    private java.sql.Time msgStartTime;
    /**短信发送结束时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "HH:mm:ss")
    @DateTimeFormat(pattern="HH:mm:ss",iso = DateTimeFormat.ISO.TIME)
    @ApiModelProperty(value = "发送短信结束时间")
    private java.sql.Time msgEndTime;
    /**策略说明*/
    private java.lang.String content;
    /**重复数据条数*/
    private java.lang.Integer repeatDataCount;
}
