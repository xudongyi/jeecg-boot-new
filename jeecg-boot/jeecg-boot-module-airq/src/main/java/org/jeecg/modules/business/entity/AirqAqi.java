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
 * @Description: airq_aqi
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Data
@TableName("airq_aqi")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="airq_aqi对象", description="airq_aqi")
public class AirqAqi implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**factorCode*/
	@Excel(name = "factorCode", width = 15)
    @ApiModelProperty(value = "factorCode")
    private java.lang.String factorCode;
	/**lValue*/
	@Excel(name = "lValue", width = 15)
    @ApiModelProperty(value = "lValue")
    private java.lang.Double lValue;
	/**hValue*/
	@Excel(name = "hValue", width = 15)
    @ApiModelProperty(value = "hValue")
    private java.lang.Double hValue;
	/**liAqi*/
	@Excel(name = "liAqi", width = 15)
    @ApiModelProperty(value = "liAqi")
    private java.lang.Double liAqi;
	/**hiAqi*/
	@Excel(name = "hiAqi", width = 15)
    @ApiModelProperty(value = "hiAqi")
    private java.lang.Double hiAqi;
	/**type*/
	@Excel(name = "type", width = 15)
    @ApiModelProperty(value = "type")
    private java.lang.Integer type;
}
