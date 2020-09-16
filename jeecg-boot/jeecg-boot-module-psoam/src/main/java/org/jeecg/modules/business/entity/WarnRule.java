package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: warn_rule
 * @Author: jeecg-boot
 * @Date:   2020-09-15
 * @Version: V1.0
 */
@Data
@TableName("warn_rule")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="warn_rule对象", description="warn_rule")
public class WarnRule implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**策略类型*/
	@Excel(name = "策略类型", width = 15)
    @ApiModelProperty(value = "策略类型")
    private java.lang.String ruleType;
	/**监测类型*/
	@Excel(name = "监测类型", width = 15)
    @ApiModelProperty(value = "监测类型")
    private java.lang.String siteType;
	/**污染因子*/
	@Excel(name = "污染因子", width = 15)
    @ApiModelProperty(value = "污染因子")
    private java.lang.String code;
	/**报警级别*/
	@Excel(name = "报警级别", width = 15)
    @ApiModelProperty(value = "报警级别")
    private java.lang.String ruleLevel;
	/**阈值上限*/
	@Excel(name = "阈值上限", width = 15)
    @ApiModelProperty(value = "阈值上限")
    private java.lang.Double warnValueUp;
	/**阈值下限*/
	@Excel(name = "阈值下限", width = 15)
    @ApiModelProperty(value = "阈值下限")
    private java.lang.Double warnValueDown;
	/**是否留样*/
	@Excel(name = "是否留样", width = 15)
    @ApiModelProperty(value = "是否留样")
    private java.lang.String isSample;
	/**是否关阀*/
	@Excel(name = "是否关阀", width = 15)
    @ApiModelProperty(value = "是否关阀")
    private java.lang.String isCloseTap;
	/**是否发送短信*/
	@Excel(name = "是否发送短信", width = 15)
    @ApiModelProperty(value = "是否发送短信")
    private java.lang.String isSendMsg;
	/**是否同步短信联系人*/
	@Excel(name = "是否同步短信联系人", width = 15)
    @ApiModelProperty(value = "是否同步短信联系人")
    private java.lang.String isSynchronize;
	/**短信频率*/
	@Excel(name = "短信频率", width = 15)
    @ApiModelProperty(value = "短信频率")
    private java.lang.Integer msgRate;
	/**短信发送开始时间*/
	@Excel(name = "短信发送开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "短信发送开始时间")
    private java.util.Date msgStartTime;
	/**短信发送结束时间*/
	@Excel(name = "短信发送结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "短信发送结束时间")
    private java.util.Date msgEndTime;
	/**策略说明*/
	@Excel(name = "策略说明", width = 15)
    @ApiModelProperty(value = "策略说明")
    private java.lang.String content;
	/**重复数据条数*/
	@Excel(name = "重复数据条数", width = 15)
    @ApiModelProperty(value = "重复数据条数")
    private java.lang.Integer repeatDataCount;
}
