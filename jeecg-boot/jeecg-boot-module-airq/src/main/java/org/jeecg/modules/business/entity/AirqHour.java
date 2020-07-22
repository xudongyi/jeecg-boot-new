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
	/**数据时间*/
	@Excel(name = "数据时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date dataTime;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;
    /**审核时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;
	/**mn号*/
	@Excel(name = "mn号", width = 15)
    @ApiModelProperty(value = "mn号")
    private java.lang.String mn;
	/**小时数据平台状态*/
	@Excel(name = "小时数据平台状态", width = 15, dicCode = "airDataStatus")
    @Dict(dicCode = "airDataStatus")
    @ApiModelProperty(value = "小时数据平台状态")
    private java.lang.Integer state;
	/**空气质量级别*/
	@Excel(name = "空气质量级别", width = 15,dicCode = "level")
    @Dict(dicCode = "level")
    @ApiModelProperty(value = "空气质量级别")
    private java.lang.String level;
	/**首要污染物*/
	@Excel(name = "首要污染物", width = 15)
    @ApiModelProperty(value = "首要污染物")
    private java.lang.String firstCode;
	/**空气质量指数*/
	@Excel(name = "空气质量指数", width = 15)
    @ApiModelProperty(value = "空气质量指数")
    private java.lang.Double aqi;
	/**气压*/
	@Excel(name = "气压", width = 15)
    @ApiModelProperty(value = "气压")
    private java.lang.Double a01006Avg;
	/**气压Iaqi*/
	@Excel(name = "气压Iaqi", width = 15)
    @ApiModelProperty(value = "气压Iaqi")
    private java.lang.Double a01006Iaqi;
	/**CO*/
	@Excel(name = "CO", width = 15)
    @ApiModelProperty(value = "CO")
    private java.lang.Double a21005Avg;
	/**COIaqi*/
	@Excel(name = "COIaqi", width = 15)
    @ApiModelProperty(value = "COIaqi")
    private java.lang.Double a21005Iaqi;
	/**PM10(1h)*/
	@Excel(name = "PM10(1h)", width = 15)
    @ApiModelProperty(value = "PM10(1h)")
    private java.lang.Double a3400201Avg;
	/**PM10(1h)Iaqi*/
	@Excel(name = "PM10(1h)Iaqi", width = 15)
    @ApiModelProperty(value = "PM10(1h)Iaqi")
    private java.lang.Double a3400201Iaqi;
	/**风速*/
	@Excel(name = "风速", width = 15)
    @ApiModelProperty(value = "风速")
    private java.lang.Double a01007Avg;
	/**风速Iaqi*/
	@Excel(name = "风速Iaqi", width = 15)
    @ApiModelProperty(value = "风速Iaqi")
    private java.lang.Double a01007Iaqi;
    /**风向*/
    @Excel(name = "风向", width = 15)
    @ApiModelProperty(value = "风向")
    private java.lang.Double a01008Avg;
    /**风速Iaqi*/
    @Excel(name = "风向Iaqi", width = 15)
    @ApiModelProperty(value = "风向Iaqi")
    private java.lang.Double a01008Iaqi;
	/**NO2*/
	@Excel(name = "NO2", width = 15)
    @ApiModelProperty(value = "NO2")
    private java.lang.Double a21004Avg;
	/**NO2Iaqi*/
	@Excel(name = "NO2Iaqi", width = 15)
    @ApiModelProperty(value = "NO2Iaqi")
    private java.lang.Double a21004Iaqi;
	/**PM2.5(24h)*/
	@Excel(name = "PM2.5(24h)", width = 15)
    @ApiModelProperty(value = "PM2.5(24h)")
    private java.lang.Double a3400424Avg;
	/**PM2.5(24h)Iaqi*/
	@Excel(name = "PM2.5(24h)Iaqi", width = 15)
    @ApiModelProperty(value = "PM2.5(24h)Iaqi")
    private java.lang.Double a3400424Iaqi;
	/**pH值*/
	@Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private java.lang.Double a01001Avg;
	/**pH值Iaqi*/
	@Excel(name = "温度Iaqi", width = 15)
    @ApiModelProperty(value = "温度Iaqi")
    private java.lang.Double a01001Iaqi;
	/**NOx*/
	@Excel(name = "NOx", width = 15)
    @ApiModelProperty(value = "NOx")
    private java.lang.Double a21002Avg;
	/**NOxIaqi*/
	@Excel(name = "NOxIaqi", width = 15)
    @ApiModelProperty(value = "NOxIaqi")
    private java.lang.Double a21002Iaqi;
	/**O3(8h)*/
	@Excel(name = "O3(8h)", width = 15)
    @ApiModelProperty(value = "O3(8h)")
    private java.lang.Double a0502408Avg;
	/**O3(8h)Iaqi*/
	@Excel(name = "O3(8h)Iaqi", width = 15)
    @ApiModelProperty(value = "O3(8h)Iaqi")
    private java.lang.Double a0502408Iaqi;
	/**PM2.5(1h)*/
	@Excel(name = "PM2.5(1h)", width = 15)
    @ApiModelProperty(value = "PM2.5(1h)")
    private java.lang.Double a3400401Avg;
	/**PM2.5(1h)Iaqi*/
	@Excel(name = "PM2.5(1h)Iaqi", width = 15)
    @ApiModelProperty(value = "PM2.5(1h)Iaqi")
    private java.lang.Double a3400401Iaqi;
	/**O3(1h)*/
	@Excel(name = "O3(1h)", width = 15)
    @ApiModelProperty(value = "O3(1h)")
    private java.lang.Double a0502401Avg;
	/**O3(1h)Iaqi*/
	@Excel(name = "O3(1h)Iaqi", width = 15)
    @ApiModelProperty(value = "O3(1h)Iaqi")
    private java.lang.Double a0502401Iaqi;
	/**湿度*/
	@Excel(name = "湿度", width = 15)
    @ApiModelProperty(value = "湿度")
    private java.lang.Double a01002Avg;
	/**湿度Iaqi*/
	@Excel(name = "湿度Iaqi", width = 15)
    @ApiModelProperty(value = "湿度Iaqi")
    private java.lang.Double a01002Iaqi;
	/**SO2*/
	@Excel(name = "SO2", width = 15)
    @ApiModelProperty(value = "SO2")
    private java.lang.Double a21026Avg;
	/**SO2Iaqi*/
	@Excel(name = "SO2Iaqi", width = 15)
    @ApiModelProperty(value = "SO2Iaqi")
    private java.lang.Double a21026Iaqi;
	/**PM10(24h)*/
	@Excel(name = "PM10(24h)", width = 15)
    @ApiModelProperty(value = "PM10(24h)")
    private java.lang.Double a3400224Avg;
	/**PM10(24h)Iaqi*/
	@Excel(name = "PM10(24h)Iaqi", width = 15)
    @ApiModelProperty(value = "PM10(24h)Iaqi")
    private java.lang.Double a3400224Iaqi;
	/**NO*/
	@Excel(name = "NO", width = 15)
    @ApiModelProperty(value = "NO")
    private java.lang.Double a21003Avg;
	/**NOIaqi*/
	@Excel(name = "NOIaqi", width = 15)
    @ApiModelProperty(value = "NOIaqi")
    private java.lang.Double a21003Iaqi;
}
