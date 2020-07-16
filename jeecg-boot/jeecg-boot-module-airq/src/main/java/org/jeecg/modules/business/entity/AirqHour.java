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
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Data
@TableName("airq_hour")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="airq_hour对象", description="airq_hour")
public class AirqHour implements Serializable {
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
	/**level*/
	@Excel(name = "level", width = 15)
    @ApiModelProperty(value = "level")
    private java.lang.String level;
	/**firstCode*/
	@Excel(name = "firstCode", width = 15)
    @ApiModelProperty(value = "firstCode")
    private java.lang.String firstCode;
	/**aqi*/
	@Excel(name = "aqi", width = 15)
    @ApiModelProperty(value = "aqi")
    private java.lang.Double aqi;
	/**a01006Avg*/
	@Excel(name = "a01006Avg", width = 15)
    @ApiModelProperty(value = "a01006Avg")
    private java.lang.Double a01006Avg;
	/**a01006Iaqi*/
	@Excel(name = "a01006Iaqi", width = 15)
    @ApiModelProperty(value = "a01006Iaqi")
    private java.lang.Double a01006Iaqi;
	/**a21005Avg*/
	@Excel(name = "a21005Avg", width = 15)
    @ApiModelProperty(value = "a21005Avg")
    private java.lang.Double a21005Avg;
	/**a21005Iaqi*/
	@Excel(name = "a21005Iaqi", width = 15)
    @ApiModelProperty(value = "a21005Iaqi")
    private java.lang.Double a21005Iaqi;
	/**a3400201Avg*/
	@Excel(name = "a3400201Avg", width = 15)
    @ApiModelProperty(value = "a3400201Avg")
    private java.lang.Double a3400201Avg;
	/**a3400201Iaqi*/
	@Excel(name = "a3400201Iaqi", width = 15)
    @ApiModelProperty(value = "a3400201Iaqi")
    private java.lang.Double a3400201Iaqi;
	/**a01007Avg*/
	@Excel(name = "a01007Avg", width = 15)
    @ApiModelProperty(value = "a01007Avg")
    private java.lang.Double a01007Avg;
	/**a01007Iaqi*/
	@Excel(name = "a01007Iaqi", width = 15)
    @ApiModelProperty(value = "a01007Iaqi")
    private java.lang.Double a01007Iaqi;
	/**a21004Avg*/
	@Excel(name = "a21004Avg", width = 15)
    @ApiModelProperty(value = "a21004Avg")
    private java.lang.Double a21004Avg;
	/**a21004Iaqi*/
	@Excel(name = "a21004Iaqi", width = 15)
    @ApiModelProperty(value = "a21004Iaqi")
    private java.lang.Double a21004Iaqi;
	/**a3400424Avg*/
	@Excel(name = "a3400424Avg", width = 15)
    @ApiModelProperty(value = "a3400424Avg")
    private java.lang.Double a3400424Avg;
	/**a3400424Iaqi*/
	@Excel(name = "a3400424Iaqi", width = 15)
    @ApiModelProperty(value = "a3400424Iaqi")
    private java.lang.Double a3400424Iaqi;
	/**a01001Avg*/
	@Excel(name = "a01001Avg", width = 15)
    @ApiModelProperty(value = "a01001Avg")
    private java.lang.Double a01001Avg;
	/**a01001Iaqi*/
	@Excel(name = "a01001Iaqi", width = 15)
    @ApiModelProperty(value = "a01001Iaqi")
    private java.lang.Double a01001Iaqi;
	/**a21002Avg*/
	@Excel(name = "a21002Avg", width = 15)
    @ApiModelProperty(value = "a21002Avg")
    private java.lang.Double a21002Avg;
	/**a21002Iaqi*/
	@Excel(name = "a21002Iaqi", width = 15)
    @ApiModelProperty(value = "a21002Iaqi")
    private java.lang.Double a21002Iaqi;
	/**a0502408Avg*/
	@Excel(name = "a0502408Avg", width = 15)
    @ApiModelProperty(value = "a0502408Avg")
    private java.lang.Double a0502408Avg;
	/**a0502408Iaqi*/
	@Excel(name = "a0502408Iaqi", width = 15)
    @ApiModelProperty(value = "a0502408Iaqi")
    private java.lang.Double a0502408Iaqi;
	/**a3400401Avg*/
	@Excel(name = "a3400401Avg", width = 15)
    @ApiModelProperty(value = "a3400401Avg")
    private java.lang.Double a3400401Avg;
	/**a3400401Iaqi*/
	@Excel(name = "a3400401Iaqi", width = 15)
    @ApiModelProperty(value = "a3400401Iaqi")
    private java.lang.Double a3400401Iaqi;
	/**a0502401Avg*/
	@Excel(name = "a0502401Avg", width = 15)
    @ApiModelProperty(value = "a0502401Avg")
    private java.lang.Double a0502401Avg;
	/**a0502401Iaqi*/
	@Excel(name = "a0502401Iaqi", width = 15)
    @ApiModelProperty(value = "a0502401Iaqi")
    private java.lang.Double a0502401Iaqi;
	/**a01002Avg*/
	@Excel(name = "a01002Avg", width = 15)
    @ApiModelProperty(value = "a01002Avg")
    private java.lang.Double a01002Avg;
	/**a01002Iaqi*/
	@Excel(name = "a01002Iaqi", width = 15)
    @ApiModelProperty(value = "a01002Iaqi")
    private java.lang.Double a01002Iaqi;
	/**a21026Avg*/
	@Excel(name = "a21026Avg", width = 15)
    @ApiModelProperty(value = "a21026Avg")
    private java.lang.Double a21026Avg;
	/**a21026Iaqi*/
	@Excel(name = "a21026Iaqi", width = 15)
    @ApiModelProperty(value = "a21026Iaqi")
    private java.lang.Double a21026Iaqi;
	/**a3400224Avg*/
	@Excel(name = "a3400224Avg", width = 15)
    @ApiModelProperty(value = "a3400224Avg")
    private java.lang.Double a3400224Avg;
	/**a3400224Iaqi*/
	@Excel(name = "a3400224Iaqi", width = 15)
    @ApiModelProperty(value = "a3400224Iaqi")
    private java.lang.Double a3400224Iaqi;
	/**a21003Avg*/
	@Excel(name = "a21003Avg", width = 15)
    @ApiModelProperty(value = "a21003Avg")
    private java.lang.Double a21003Avg;
	/**a21003Iaqi*/
	@Excel(name = "a21003Iaqi", width = 15)
    @ApiModelProperty(value = "a21003Iaqi")
    private java.lang.Double a21003Iaqi;
}
