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
 * @Description: air_current_tr
 * @Author: jeecg-boot
 * @Date:   2020-09-07
 * @Version: V1.0
 */
@Data
@TableName("air_current_tr")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="air_current_tr对象", description="air_current_tr")
public class AirCurrentTr implements Serializable {
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
	/**a00000Rtd*/
	@Excel(name = "a00000Rtd", width = 15)
    @ApiModelProperty(value = "a00000Rtd")
    private java.lang.Double a00000Rtd;
	/**a00000State*/
	@Excel(name = "a00000State", width = 15)
    @ApiModelProperty(value = "a00000State")
    private java.lang.Integer a00000State;
	/**a00000Zsrtd*/
	@Excel(name = "a00000Zsrtd", width = 15)
    @ApiModelProperty(value = "a00000Zsrtd")
    private java.lang.Double a00000Zsrtd;
	/**a00000Zsstate*/
	@Excel(name = "a00000Zsstate", width = 15)
    @ApiModelProperty(value = "a00000Zsstate")
    private java.lang.Integer a00000Zsstate;
	/**a00000Flag*/
	@Excel(name = "a00000Flag", width = 15)
    @ApiModelProperty(value = "a00000Flag")
    private java.lang.String a00000Flag;
}
