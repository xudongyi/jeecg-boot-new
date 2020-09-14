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
 * @Description: water_month
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@TableName("water_month")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="water_month对象", description="water_month")
public class WaterMonth implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**dataTime*/
	@Excel(name = "dataTime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "dataTime")
    private Date dataTime;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private Date createTime;
	/**staticTime*/
	@Excel(name = "staticTime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "staticTime")
    private Date staticTime;
	/**mn*/
	@Excel(name = "mn", width = 15)
    @ApiModelProperty(value = "mn")
    private String mn;
	/**times*/
	@Excel(name = "times", width = 15)
    @ApiModelProperty(value = "times")
    private Integer times;
	/**w00000Min*/
	@Excel(name = "w00000Min", width = 15)
    @ApiModelProperty(value = "w00000Min")
    private Double w00000Min;
	/**w00000MinState*/
	@Excel(name = "w00000MinState", width = 15)
    @ApiModelProperty(value = "w00000MinState")
    private Integer w00000MinState;
	/**w00000Max*/
	@Excel(name = "w00000Max", width = 15)
    @ApiModelProperty(value = "w00000Max")
    private Double w00000Max;
	/**w00000MaxState*/
	@Excel(name = "w00000MaxState", width = 15)
    @ApiModelProperty(value = "w00000MaxState")
    private Integer w00000MaxState;
	/**w00000Avg*/
	@Excel(name = "w00000Avg", width = 15)
    @ApiModelProperty(value = "w00000Avg")
    private Double w00000Avg;
	/**w00000AvgState*/
	@Excel(name = "w00000AvgState", width = 15)
    @ApiModelProperty(value = "w00000AvgState")
    private Integer w00000AvgState;
	/**w00000Cou*/
	@Excel(name = "w00000Cou", width = 15)
    @ApiModelProperty(value = "w00000Cou")
    private Double w00000Cou;
	/**w00000CouState*/
	@Excel(name = "w00000CouState", width = 15)
    @ApiModelProperty(value = "w00000CouState")
    private Integer w00000CouState;
	/**w21003Min*/
	@Excel(name = "w21003Min", width = 15)
    @ApiModelProperty(value = "w21003Min")
    private Double w21003Min;
	/**w21003MinState*/
	@Excel(name = "w21003MinState", width = 15)
    @ApiModelProperty(value = "w21003MinState")
    private Integer w21003MinState;
	/**w21003Max*/
	@Excel(name = "w21003Max", width = 15)
    @ApiModelProperty(value = "w21003Max")
    private Double w21003Max;
	/**w21003MaxState*/
	@Excel(name = "w21003MaxState", width = 15)
    @ApiModelProperty(value = "w21003MaxState")
    private Integer w21003MaxState;
	/**w21003Avg*/
	@Excel(name = "w21003Avg", width = 15)
    @ApiModelProperty(value = "w21003Avg")
    private Double w21003Avg;
	/**w21003AvgState*/
	@Excel(name = "w21003AvgState", width = 15)
    @ApiModelProperty(value = "w21003AvgState")
    private Integer w21003AvgState;
	/**w21003Cou*/
	@Excel(name = "w21003Cou", width = 15)
    @ApiModelProperty(value = "w21003Cou")
    private Double w21003Cou;
	/**w21003CouState*/
	@Excel(name = "w21003CouState", width = 15)
    @ApiModelProperty(value = "w21003CouState")
    private Integer w21003CouState;
	/**w01001Min*/
	@Excel(name = "w01001Min", width = 15)
    @ApiModelProperty(value = "w01001Min")
    private Double w01001Min;
	/**w01001MinState*/
	@Excel(name = "w01001MinState", width = 15)
    @ApiModelProperty(value = "w01001MinState")
    private Integer w01001MinState;
	/**w01001Max*/
	@Excel(name = "w01001Max", width = 15)
    @ApiModelProperty(value = "w01001Max")
    private Double w01001Max;
	/**w01001MaxState*/
	@Excel(name = "w01001MaxState", width = 15)
    @ApiModelProperty(value = "w01001MaxState")
    private Integer w01001MaxState;
	/**w01001Avg*/
	@Excel(name = "w01001Avg", width = 15)
    @ApiModelProperty(value = "w01001Avg")
    private Double w01001Avg;
	/**w01001AvgState*/
	@Excel(name = "w01001AvgState", width = 15)
    @ApiModelProperty(value = "w01001AvgState")
    private Integer w01001AvgState;
	/**w01001Cou*/
	@Excel(name = "w01001Cou", width = 15)
    @ApiModelProperty(value = "w01001Cou")
    private Double w01001Cou;
	/**w01001CouState*/
	@Excel(name = "w01001CouState", width = 15)
    @ApiModelProperty(value = "w01001CouState")
    private Integer w01001CouState;
	/**w21011Min*/
	@Excel(name = "w21011Min", width = 15)
    @ApiModelProperty(value = "w21011Min")
    private Double w21011Min;
	/**w21011MinState*/
	@Excel(name = "w21011MinState", width = 15)
    @ApiModelProperty(value = "w21011MinState")
    private Integer w21011MinState;
	/**w21011Max*/
	@Excel(name = "w21011Max", width = 15)
    @ApiModelProperty(value = "w21011Max")
    private Double w21011Max;
	/**w21011MaxState*/
	@Excel(name = "w21011MaxState", width = 15)
    @ApiModelProperty(value = "w21011MaxState")
    private Integer w21011MaxState;
	/**w21011Avg*/
	@Excel(name = "w21011Avg", width = 15)
    @ApiModelProperty(value = "w21011Avg")
    private Double w21011Avg;
	/**w21011AvgState*/
	@Excel(name = "w21011AvgState", width = 15)
    @ApiModelProperty(value = "w21011AvgState")
    private Integer w21011AvgState;
	/**w21011Cou*/
	@Excel(name = "w21011Cou", width = 15)
    @ApiModelProperty(value = "w21011Cou")
    private Double w21011Cou;
	/**w21011CouState*/
	@Excel(name = "w21011CouState", width = 15)
    @ApiModelProperty(value = "w21011CouState")
    private Integer w21011CouState;
	/**w01018Min*/
	@Excel(name = "w01018Min", width = 15)
    @ApiModelProperty(value = "w01018Min")
    private Double w01018Min;
	/**w01018MinState*/
	@Excel(name = "w01018MinState", width = 15)
    @ApiModelProperty(value = "w01018MinState")
    private Integer w01018MinState;
	/**w01018Max*/
	@Excel(name = "w01018Max", width = 15)
    @ApiModelProperty(value = "w01018Max")
    private Double w01018Max;
	/**w01018MaxState*/
	@Excel(name = "w01018MaxState", width = 15)
    @ApiModelProperty(value = "w01018MaxState")
    private Integer w01018MaxState;
	/**w01018Avg*/
	@Excel(name = "w01018Avg", width = 15)
    @ApiModelProperty(value = "w01018Avg")
    private Double w01018Avg;
	/**w01018AvgState*/
	@Excel(name = "w01018AvgState", width = 15)
    @ApiModelProperty(value = "w01018AvgState")
    private Integer w01018AvgState;
	/**w01018Cou*/
	@Excel(name = "w01018Cou", width = 15)
    @ApiModelProperty(value = "w01018Cou")
    private Double w01018Cou;
	/**w01018CouState*/
	@Excel(name = "w01018CouState", width = 15)
    @ApiModelProperty(value = "w01018CouState")
    private Integer w01018CouState;
}
