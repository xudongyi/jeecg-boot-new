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
 * @Description: water_current_tr_2009
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
@Data
@TableName("water_current_tr_2009")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="water_current_tr_2009对象", description="water_current_tr_2009")
public class WaterCurrentTr_2009 implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**dataTime*/
	@Excel(name = "dataTime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "dataTime")
    private java.util.Date dataTime;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;
	/**mn*/
	@Excel(name = "mn", width = 15)
    @ApiModelProperty(value = "mn")
    private java.lang.String mn;
	/**state*/
	@Excel(name = "state", width = 15)
    @ApiModelProperty(value = "state")
    private java.lang.Integer state;
	/**w00000Today*/
	@Excel(name = "w00000Today", width = 15)
    @ApiModelProperty(value = "w00000Today")
    private java.lang.Double w00000Today;
	/**w00000TodayState*/
	@Excel(name = "w00000TodayState", width = 15)
    @ApiModelProperty(value = "w00000TodayState")
    private java.lang.Integer w00000TodayState;
	/**w00000Total*/
	@Excel(name = "w00000Total", width = 15)
    @ApiModelProperty(value = "w00000Total")
    private java.lang.Double w00000Total;
	/**w00000TotalState*/
	@Excel(name = "w00000TotalState", width = 15)
    @ApiModelProperty(value = "w00000TotalState")
    private java.lang.Integer w00000TotalState;
	/**w00000Surplus*/
	@Excel(name = "w00000Surplus", width = 15)
    @ApiModelProperty(value = "w00000Surplus")
    private java.lang.Double w00000Surplus;
	/**w00000SurplusState*/
	@Excel(name = "w00000SurplusState", width = 15)
    @ApiModelProperty(value = "w00000SurplusState")
    private java.lang.Integer w00000SurplusState;
	/**w00000Rtd*/
	@Excel(name = "w00000Rtd", width = 15)
    @ApiModelProperty(value = "w00000Rtd")
    private java.lang.Double w00000Rtd;
	/**w00000State*/
	@Excel(name = "w00000State", width = 15)
    @ApiModelProperty(value = "w00000State")
    private java.lang.Integer w00000State;
	/**w00000Flag*/
	@Excel(name = "w00000Flag", width = 15)
    @ApiModelProperty(value = "w00000Flag")
    private java.lang.String w00000Flag;
	/**w00000Sampletime*/
	@Excel(name = "w00000Sampletime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "w00000Sampletime")
    private java.util.Date w00000Sampletime;
	/**w21003Rtd*/
	@Excel(name = "w21003Rtd", width = 15)
    @ApiModelProperty(value = "w21003Rtd")
    private java.lang.Double w21003Rtd;
	/**w21003Flag*/
	@Excel(name = "w21003Flag", width = 15)
    @ApiModelProperty(value = "w21003Flag")
    private java.lang.String w21003Flag;
	/**w21003State*/
	@Excel(name = "w21003State", width = 15)
    @ApiModelProperty(value = "w21003State")
    private java.lang.Integer w21003State;
	/**w21003Sampletime*/
	@Excel(name = "w21003Sampletime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "w21003Sampletime")
    private java.util.Date w21003Sampletime;
	/**w01001Rtd*/
	@Excel(name = "w01001Rtd", width = 15)
    @ApiModelProperty(value = "w01001Rtd")
    private java.lang.Double w01001Rtd;
	/**w01001Flag*/
	@Excel(name = "w01001Flag", width = 15)
    @ApiModelProperty(value = "w01001Flag")
    private java.lang.String w01001Flag;
	/**w01001State*/
	@Excel(name = "w01001State", width = 15)
    @ApiModelProperty(value = "w01001State")
    private java.lang.Integer w01001State;
	/**w01001Sampletime*/
	@Excel(name = "w01001Sampletime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "w01001Sampletime")
    private java.util.Date w01001Sampletime;
	/**w21011Rtd*/
	@Excel(name = "w21011Rtd", width = 15)
    @ApiModelProperty(value = "w21011Rtd")
    private java.lang.Double w21011Rtd;
	/**w21011Flag*/
	@Excel(name = "w21011Flag", width = 15)
    @ApiModelProperty(value = "w21011Flag")
    private java.lang.String w21011Flag;
	/**w21011State*/
	@Excel(name = "w21011State", width = 15)
    @ApiModelProperty(value = "w21011State")
    private java.lang.Integer w21011State;
	/**w21011Sampletime*/
	@Excel(name = "w21011Sampletime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "w21011Sampletime")
    private java.util.Date w21011Sampletime;
	/**w01018Rtd*/
	@Excel(name = "w01018Rtd", width = 15)
    @ApiModelProperty(value = "w01018Rtd")
    private java.lang.Double w01018Rtd;
	/**w01018Flag*/
	@Excel(name = "w01018Flag", width = 15)
    @ApiModelProperty(value = "w01018Flag")
    private java.lang.String w01018Flag;
	/**w01018State*/
	@Excel(name = "w01018State", width = 15)
    @ApiModelProperty(value = "w01018State")
    private java.lang.Integer w01018State;
	/**w01018Sampletime*/
	@Excel(name = "w01018Sampletime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "w01018Sampletime")
    private java.util.Date w01018Sampletime;
}
