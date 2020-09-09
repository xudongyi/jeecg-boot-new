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
 * @Description: warn_log
 * @Author: jeecg-boot
 * @Date:   2020-09-08
 * @Version: V1.0
 */
@Data
@TableName("warn_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="warn_log对象", description="warn_log")
public class WarnLog implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**type*/
	@Excel(name = "type", width = 15)
    @ApiModelProperty(value = "type")
    private java.lang.Integer type;
	/**companyId*/
	@Excel(name = "companyId", width = 15)
    @ApiModelProperty(value = "companyId")
    private java.lang.String companyId;
	/**mn*/
	@Excel(name = "mn", width = 15)
    @ApiModelProperty(value = "mn")
    private java.lang.String mn;
	/**warnType*/
	@Excel(name = "warnType", width = 15)
    @ApiModelProperty(value = "warnType")
    private java.lang.Integer warnType;
	/**code*/
	@Excel(name = "code", width = 15)
    @ApiModelProperty(value = "code")
    private java.lang.String code;
	/**warnLevel*/
	@Excel(name = "warnLevel", width = 15)
    @ApiModelProperty(value = "warnLevel")
    private java.lang.Integer warnLevel;
	/**messageStatus*/
	@Excel(name = "messageStatus", width = 15)
    @ApiModelProperty(value = "messageStatus")
    private java.lang.Integer messageStatus;
	/**content*/
	@Excel(name = "content", width = 15)
    @ApiModelProperty(value = "content")
    private java.lang.String content;
	/**warnTime*/
	@Excel(name = "warnTime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "warnTime")
    private java.util.Date warnTime;
	/**任务受理状态:1-未受理；2-已受理；3-已生成任务*/
	@Excel(name = "任务受理状态:1-未受理；2-已受理；3-已生成任务", width = 15)
    @ApiModelProperty(value = "任务受理状态:1-未受理；2-已受理；3-已生成任务")
    private java.lang.Integer taskStatus;
}
