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
 * @Description: airq_year
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Data
@TableName("airq_year")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="airq_year对象", description="airq_year")
public class AirqYear implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**year*/
	@Excel(name = "year", width = 15)
    @ApiModelProperty(value = "year")
    private java.lang.String year;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;
	/**mn*/
	@Excel(name = "mn", width = 15)
    @ApiModelProperty(value = "mn")
    private java.lang.String mn;
    /**level*/
    @Excel(name = "level", width = 15)
    @ApiModelProperty(value = "level")
    private java.lang.String level;
	/**fineDays*/
	@Excel(name = "fineDays", width = 15)
    @ApiModelProperty(value = "fineDays")
    private java.lang.Integer fineDays;
	/**totalI*/
	@Excel(name = "totalI", width = 15)
    @ApiModelProperty(value = "totalI")
    private java.lang.Double totalI;
	/**a21026Avg*/
	@Excel(name = "a21026Avg", width = 15)
    @ApiModelProperty(value = "a21026Avg")
    private java.lang.Double a21026Avg;
	/**a21004Avg*/
	@Excel(name = "a21004Avg", width = 15)
    @ApiModelProperty(value = "a21004Avg")
    private java.lang.Double a21004Avg;
	/**a34002Avg*/
	@Excel(name = "a34002Avg", width = 15)
    @ApiModelProperty(value = "a34002Avg")
    private java.lang.Double a34002Avg;
	/**a34004Avg*/
	@Excel(name = "a34004Avg", width = 15)
    @ApiModelProperty(value = "a34004Avg")
    private java.lang.Double a34004Avg;
	/**a2100595*/
	@Excel(name = "a2100595", width = 15)
    @ApiModelProperty(value = "a2100595")
    private java.lang.Double a2100595;
	/**a0502490*/
	@Excel(name = "a0502490", width = 15)
    @ApiModelProperty(value = "a0502490")
    private java.lang.Double a0502490;
	/**a21026S*/
	@Excel(name = "a21026S", width = 15)
    @ApiModelProperty(value = "a21026S")
    private java.lang.Double a21026S;
	/**a21004S*/
	@Excel(name = "a21004S", width = 15)
    @ApiModelProperty(value = "a21004S")
    private java.lang.Double a21004S;
	/**a34002S*/
	@Excel(name = "a34002S", width = 15)
    @ApiModelProperty(value = "a34002S")
    private java.lang.Double a34002S;
	/**a34004S*/
	@Excel(name = "a34004S", width = 15)
    @ApiModelProperty(value = "a34004S")
    private java.lang.Double a34004S;
	/**a21005S*/
	@Excel(name = "a21005S", width = 15)
    @ApiModelProperty(value = "a21005S")
    private java.lang.Double a21005S;
	/**a05024S*/
	@Excel(name = "a05024S", width = 15)
    @ApiModelProperty(value = "a05024S")
    private java.lang.Double a05024S;
	/**a21026I*/
	@Excel(name = "a21026I", width = 15)
    @ApiModelProperty(value = "a21026I")
    private java.lang.Double a21026I;
	/**a21004I*/
	@Excel(name = "a21004I", width = 15)
    @ApiModelProperty(value = "a21004I")
    private java.lang.Double a21004I;
	/**a34002I*/
	@Excel(name = "a34002I", width = 15)
    @ApiModelProperty(value = "a34002I")
    private java.lang.Double a34002I;
	/**a34004I*/
	@Excel(name = "a34004I", width = 15)
    @ApiModelProperty(value = "a34004I")
    private java.lang.Double a34004I;
	/**a21005I*/
	@Excel(name = "a21005I", width = 15)
    @ApiModelProperty(value = "a21005I")
    private java.lang.Double a21005I;
	/**a05024I*/
	@Excel(name = "a05024I", width = 15)
    @ApiModelProperty(value = "a05024I")
    private java.lang.Double a05024I;
}
