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
 * @Description: 报警日志表
 * @Author: jeecg-boot
 * @Date:   2020-07-30
 * @Version: V1.0
 */
@Data
@TableName("sys_warn_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_warn_log对象", description="报警日志表")
public class SysWarnLog implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**报警内容*/
	@Excel(name = "报警内容", width = 15)
    @ApiModelProperty(value = "报警内容")
    private java.lang.String content;
	/**报警时间*/
	@Excel(name = "报警时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "报警时间")
    private java.util.Date warnTime;
	/**报文id*/
	@Excel(name = "报文id", width = 15)
    @ApiModelProperty(value = "报文id")
    private java.lang.String messageId;
	/**监控点id*/
	@Excel(name = "监控点id", width = 15)
    @ApiModelProperty(value = "监控点id")
    private java.lang.String monitorId;
	/**污染因子*/
	@Excel(name = "污染因子", width = 15)
    @ApiModelProperty(value = "污染因子")
    private java.lang.String code;
	/**数据值*/
	@Excel(name = "数据值", width = 15)
    @ApiModelProperty(value = "数据值")
    private java.lang.String value;
	/**是否已发送短信*/
	@Excel(name = "是否已发送短信", width = 15)
    @ApiModelProperty(value = "是否已发送短信")
    private java.lang.String isSendMessage;
	/**是否已发送邮箱*/
	@Excel(name = "是否已发送邮箱", width = 15)
    @ApiModelProperty(value = "是否已发送邮箱")
    private java.lang.String isSendMail;
	/**报警级别*/
	@Excel(name = "报警级别", width = 15, dicCode = "warnFlag")
	@Dict(dicCode = "warnFlag")
    @ApiModelProperty(value = "报警级别")
    private java.lang.String flag;
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
