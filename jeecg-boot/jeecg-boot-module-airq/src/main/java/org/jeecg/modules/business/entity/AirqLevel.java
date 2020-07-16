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
 * @Description: airq_level
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Data
@TableName("airq_level")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="airq_level对象", description="airq_level")
public class AirqLevel implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**级别*/
	@Excel(name = "级别", width = 15)
    @ApiModelProperty(value = "级别")
    private java.lang.Integer level;
	/**评分 优，良，轻度污染，中度污染，重度污染，严重污染，爆表*/
	@Excel(name = "评分 优，良，轻度污染，中度污染，重度污染，严重污染，爆表", width = 15)
    @ApiModelProperty(value = "评分 优，良，轻度污染，中度污染，重度污染，严重污染，爆表")
    private java.lang.String levelGrade;
	/**空气质量AQI低值*/
	@Excel(name = "空气质量AQI低值", width = 15)
    @ApiModelProperty(value = "空气质量AQI低值")
    private java.lang.Double aqiL;
	/**空气质量AQI高值*/
	@Excel(name = "空气质量AQI高值", width = 15)
    @ApiModelProperty(value = "空气质量AQI高值")
    private java.lang.Double aqiH;
	/**对健康影响情况*/
	@Excel(name = "对健康影响情况", width = 15)
    @ApiModelProperty(value = "对健康影响情况")
    private java.lang.String levelContent;
	/**颜色rgb值*/
	@Excel(name = "颜色rgb值", width = 15)
    @ApiModelProperty(value = "颜色rgb值")
    private java.lang.String levelRgb;
	/**建议采取的措施*/
	@Excel(name = "建议采取的措施", width = 15)
    @ApiModelProperty(value = "建议采取的措施")
    private java.lang.String advice;
}
