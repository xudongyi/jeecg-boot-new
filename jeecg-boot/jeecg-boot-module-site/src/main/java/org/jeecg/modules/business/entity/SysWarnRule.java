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
 * @Description: 报警策略表
 * @Author: jeecg-boot
 * @Date:   2020-07-01
 * @Version: V1.0
 */
@Data
@TableName("sys_warn_rule")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_warn_rule对象", description="报警策略表")
public class SysWarnRule implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**报警类型*/
	@Excel(name = "报警类型", width = 15, dicCode = "rule_type")
	@Dict(dicCode = "rule_type")
    @ApiModelProperty(value = "报警类型")
    private java.lang.String ruleType;
	/**报警级别*/
	@Excel(name = "报警级别", width = 15, dicCode = "rule_level")
	@Dict(dicCode = "rule_level")
    @ApiModelProperty(value = "报警级别")
    private java.lang.String ruleLevel;
	/**是否发送短信*/
	@Excel(name = "是否发送短信", width = 15, dicCode = "is_send_msg")
    @Dict(dicCode = "is_send_msg")
    @ApiModelProperty(value = "是否发送短信")
    private java.lang.String isSendMsg;
	/**发送频率*/
    @Excel(name = "发送频率", width = 15)
    @ApiModelProperty(value = "发送频率")
    private java.lang.Integer msgRate;
	/**发送短信开始时间*/
	@Excel(name = "发送短信开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发送短信开始时间")
    private java.util.Date warnStarttime;
	/**发送短信结束时间*/
	@Excel(name = "发送短信结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发送短信结束时间")
    private java.util.Date warnEndtime;
	/**策略说明*/
	@Excel(name = "策略说明", width = 15)
    @ApiModelProperty(value = "策略说明")
    private java.lang.String content;
	/**策略状态*/
	@Excel(name = "策略状态", width = 15, dicCode = "is_used")
    @Dict(dicCode = "is_used")
    @ApiModelProperty(value = "策略状态")
    private java.lang.String isUsed;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}
