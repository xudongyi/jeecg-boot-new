package org.jeecg.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: air_minute
 * @Author: jeecg-boot
 * @Date:   2020-09-07
 * @Version: V1.0
 */
@Data
@TableName("air_minute")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="air_minute对象", description="air_minute")
public class AirMinute implements Serializable {
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
	/**a00000Min*/
	@Excel(name = "a00000Min", width = 15)
    @ApiModelProperty(value = "a00000Min")
    private java.lang.Double a00000Min;
	/**a00000MinState*/
	@Excel(name = "a00000MinState", width = 15)
    @ApiModelProperty(value = "a00000MinState")
    private java.lang.Integer a00000MinState;
	/**a00000Zsmin*/
	@Excel(name = "a00000Zsmin", width = 15)
    @ApiModelProperty(value = "a00000Zsmin")
    private java.lang.Double a00000Zsmin;
	/**a00000ZsminState*/
	@Excel(name = "a00000ZsminState", width = 15)
    @ApiModelProperty(value = "a00000ZsminState")
    private java.lang.Integer a00000ZsminState;
	/**a00000Max*/
	@Excel(name = "a00000Max", width = 15)
    @ApiModelProperty(value = "a00000Max")
    private java.lang.Double a00000Max;
	/**a00000MaxState*/
	@Excel(name = "a00000MaxState", width = 15)
    @ApiModelProperty(value = "a00000MaxState")
    private java.lang.Integer a00000MaxState;
	/**a00000Zsmax*/
	@Excel(name = "a00000Zsmax", width = 15)
    @ApiModelProperty(value = "a00000Zsmax")
    private java.lang.Double a00000Zsmax;
	/**a00000ZsmaxState*/
	@Excel(name = "a00000ZsmaxState", width = 15)
    @ApiModelProperty(value = "a00000ZsmaxState")
    private java.lang.Integer a00000ZsmaxState;
	/**a00000Avg*/
	@Excel(name = "a00000Avg", width = 15)
    @ApiModelProperty(value = "a00000Avg")
    private java.lang.Double a00000Avg;
	/**a00000AvgState*/
	@Excel(name = "a00000AvgState", width = 15)
    @ApiModelProperty(value = "a00000AvgState")
    private java.lang.Integer a00000AvgState;
	/**a00000Zsavg*/
	@Excel(name = "a00000Zsavg", width = 15)
    @ApiModelProperty(value = "a00000Zsavg")
    private java.lang.Double a00000Zsavg;
	/**a00000ZsavgState*/
	@Excel(name = "a00000ZsavgState", width = 15)
    @ApiModelProperty(value = "a00000ZsavgState")
    private java.lang.Integer a00000ZsavgState;
	/**a00000Cou*/
	@Excel(name = "a00000Cou", width = 15)
    @ApiModelProperty(value = "a00000Cou")
    private java.lang.Double a00000Cou;
	/**a00000CouState*/
	@Excel(name = "a00000CouState", width = 15)
    @ApiModelProperty(value = "a00000CouState")
    private java.lang.Integer a00000CouState;
	/**a00000Zscou*/
	@Excel(name = "a00000Zscou", width = 15)
    @ApiModelProperty(value = "a00000Zscou")
    private java.lang.Double a00000Zscou;
	/**a00000ZscouState*/
	@Excel(name = "a00000ZscouState", width = 15)
    @ApiModelProperty(value = "a00000ZscouState")
    private java.lang.Integer a00000ZscouState;
	/**a00000Flag*/
	@Excel(name = "a00000Flag", width = 15)
    @ApiModelProperty(value = "a00000Flag")
    private java.lang.String a00000Flag;
}
