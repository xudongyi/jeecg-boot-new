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
 * @Description: air_month
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@TableName("air_month")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="air_month对象", description="air_month")
public class AirMonth implements Serializable {
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
	/**times*/
	@Excel(name = "times", width = 15)
    @ApiModelProperty(value = "times")
    private Integer times;
	/**mn*/
	@Excel(name = "mn", width = 15)
    @ApiModelProperty(value = "mn")
    private String mn;
	/**a00000Min*/
	@Excel(name = "a00000Min", width = 15)
    @ApiModelProperty(value = "a00000Min")
    private Double a00000Min;
	/**a00000MinState*/
	@Excel(name = "a00000MinState", width = 15)
    @ApiModelProperty(value = "a00000MinState")
    private Integer a00000MinState;
	/**a00000Zsmin*/
	@Excel(name = "a00000Zsmin", width = 15)
    @ApiModelProperty(value = "a00000Zsmin")
    private Double a00000Zsmin;
	/**a00000ZsminState*/
	@Excel(name = "a00000ZsminState", width = 15)
    @ApiModelProperty(value = "a00000ZsminState")
    private Integer a00000ZsminState;
	/**a00000Max*/
	@Excel(name = "a00000Max", width = 15)
    @ApiModelProperty(value = "a00000Max")
    private Double a00000Max;
	/**a00000MaxState*/
	@Excel(name = "a00000MaxState", width = 15)
    @ApiModelProperty(value = "a00000MaxState")
    private Integer a00000MaxState;
	/**a00000Zsmax*/
	@Excel(name = "a00000Zsmax", width = 15)
    @ApiModelProperty(value = "a00000Zsmax")
    private Double a00000Zsmax;
	/**a00000ZsmaxState*/
	@Excel(name = "a00000ZsmaxState", width = 15)
    @ApiModelProperty(value = "a00000ZsmaxState")
    private Integer a00000ZsmaxState;
	/**a00000Avg*/
	@Excel(name = "a00000Avg", width = 15)
    @ApiModelProperty(value = "a00000Avg")
    private Double a00000Avg;
	/**a00000AvgState*/
	@Excel(name = "a00000AvgState", width = 15)
    @ApiModelProperty(value = "a00000AvgState")
    private Integer a00000AvgState;
	/**a00000Zsavg*/
	@Excel(name = "a00000Zsavg", width = 15)
    @ApiModelProperty(value = "a00000Zsavg")
    private Double a00000Zsavg;
	/**a00000ZsavgState*/
	@Excel(name = "a00000ZsavgState", width = 15)
    @ApiModelProperty(value = "a00000ZsavgState")
    private Integer a00000ZsavgState;
	/**a00000Cou*/
	@Excel(name = "a00000Cou", width = 15)
    @ApiModelProperty(value = "a00000Cou")
    private Double a00000Cou;
	/**a00000CouState*/
	@Excel(name = "a00000CouState", width = 15)
    @ApiModelProperty(value = "a00000CouState")
    private Integer a00000CouState;
	/**a00000Zscou*/
	@Excel(name = "a00000Zscou", width = 15)
    @ApiModelProperty(value = "a00000Zscou")
    private Double a00000Zscou;
	/**a00000ZscouState*/
	@Excel(name = "a00000ZscouState", width = 15)
    @ApiModelProperty(value = "a00000ZscouState")
    private Integer a00000ZscouState;
	/**a21005Min*/
	@Excel(name = "a21005Min", width = 15)
    @ApiModelProperty(value = "a21005Min")
    private Double a21005Min;
	/**a21005MinState*/
	@Excel(name = "a21005MinState", width = 15)
    @ApiModelProperty(value = "a21005MinState")
    private Integer a21005MinState;
	/**a21005Max*/
	@Excel(name = "a21005Max", width = 15)
    @ApiModelProperty(value = "a21005Max")
    private Double a21005Max;
	/**a21005MaxState*/
	@Excel(name = "a21005MaxState", width = 15)
    @ApiModelProperty(value = "a21005MaxState")
    private Integer a21005MaxState;
	/**a21005Avg*/
	@Excel(name = "a21005Avg", width = 15)
    @ApiModelProperty(value = "a21005Avg")
    private Double a21005Avg;
	/**a21005AvgState*/
	@Excel(name = "a21005AvgState", width = 15)
    @ApiModelProperty(value = "a21005AvgState")
    private Integer a21005AvgState;
	/**a21005Cou*/
	@Excel(name = "a21005Cou", width = 15)
    @ApiModelProperty(value = "a21005Cou")
    private Double a21005Cou;
	/**a21005CouState*/
	@Excel(name = "a21005CouState", width = 15)
    @ApiModelProperty(value = "a21005CouState")
    private Integer a21005CouState;
	/**a21005Zsmin*/
	@Excel(name = "a21005Zsmin", width = 15)
    @ApiModelProperty(value = "a21005Zsmin")
    private Double a21005Zsmin;
	/**a21005ZsminState*/
	@Excel(name = "a21005ZsminState", width = 15)
    @ApiModelProperty(value = "a21005ZsminState")
    private Integer a21005ZsminState;
	/**a21005Zsmax*/
	@Excel(name = "a21005Zsmax", width = 15)
    @ApiModelProperty(value = "a21005Zsmax")
    private Double a21005Zsmax;
	/**a21005ZsmaxState*/
	@Excel(name = "a21005ZsmaxState", width = 15)
    @ApiModelProperty(value = "a21005ZsmaxState")
    private Integer a21005ZsmaxState;
	/**a21005Zsavg*/
	@Excel(name = "a21005Zsavg", width = 15)
    @ApiModelProperty(value = "a21005Zsavg")
    private Double a21005Zsavg;
	/**a21005ZsavgState*/
	@Excel(name = "a21005ZsavgState", width = 15)
    @ApiModelProperty(value = "a21005ZsavgState")
    private Integer a21005ZsavgState;
	/**a21005Zscou*/
	@Excel(name = "a21005Zscou", width = 15)
    @ApiModelProperty(value = "a21005Zscou")
    private Double a21005Zscou;
	/**a21005ZscouState*/
	@Excel(name = "a21005ZscouState", width = 15)
    @ApiModelProperty(value = "a21005ZscouState")
    private Integer a21005ZscouState;
	/**a34013Min*/
	@Excel(name = "a34013Min", width = 15)
    @ApiModelProperty(value = "a34013Min")
    private Double a34013Min;
	/**a34013MinState*/
	@Excel(name = "a34013MinState", width = 15)
    @ApiModelProperty(value = "a34013MinState")
    private Integer a34013MinState;
	/**a34013Max*/
	@Excel(name = "a34013Max", width = 15)
    @ApiModelProperty(value = "a34013Max")
    private Double a34013Max;
	/**a34013MaxState*/
	@Excel(name = "a34013MaxState", width = 15)
    @ApiModelProperty(value = "a34013MaxState")
    private Integer a34013MaxState;
	/**a34013Avg*/
	@Excel(name = "a34013Avg", width = 15)
    @ApiModelProperty(value = "a34013Avg")
    private Double a34013Avg;
	/**a34013AvgState*/
	@Excel(name = "a34013AvgState", width = 15)
    @ApiModelProperty(value = "a34013AvgState")
    private Integer a34013AvgState;
	/**a34013Cou*/
	@Excel(name = "a34013Cou", width = 15)
    @ApiModelProperty(value = "a34013Cou")
    private Double a34013Cou;
	/**a34013CouState*/
	@Excel(name = "a34013CouState", width = 15)
    @ApiModelProperty(value = "a34013CouState")
    private Integer a34013CouState;
	/**a34013Zsmin*/
	@Excel(name = "a34013Zsmin", width = 15)
    @ApiModelProperty(value = "a34013Zsmin")
    private Double a34013Zsmin;
	/**a34013ZsminState*/
	@Excel(name = "a34013ZsminState", width = 15)
    @ApiModelProperty(value = "a34013ZsminState")
    private Integer a34013ZsminState;
	/**a34013Zsmax*/
	@Excel(name = "a34013Zsmax", width = 15)
    @ApiModelProperty(value = "a34013Zsmax")
    private Double a34013Zsmax;
	/**a34013ZsmaxState*/
	@Excel(name = "a34013ZsmaxState", width = 15)
    @ApiModelProperty(value = "a34013ZsmaxState")
    private Integer a34013ZsmaxState;
	/**a34013Zsavg*/
	@Excel(name = "a34013Zsavg", width = 15)
    @ApiModelProperty(value = "a34013Zsavg")
    private Double a34013Zsavg;
	/**a34013ZsavgState*/
	@Excel(name = "a34013ZsavgState", width = 15)
    @ApiModelProperty(value = "a34013ZsavgState")
    private Integer a34013ZsavgState;
	/**a34013Zscou*/
	@Excel(name = "a34013Zscou", width = 15)
    @ApiModelProperty(value = "a34013Zscou")
    private Double a34013Zscou;
	/**a34013ZscouState*/
	@Excel(name = "a34013ZscouState", width = 15)
    @ApiModelProperty(value = "a34013ZscouState")
    private Integer a34013ZscouState;
	/**a21026Min*/
	@Excel(name = "a21026Min", width = 15)
    @ApiModelProperty(value = "a21026Min")
    private Double a21026Min;
	/**a21026MinState*/
	@Excel(name = "a21026MinState", width = 15)
    @ApiModelProperty(value = "a21026MinState")
    private Integer a21026MinState;
	/**a21026Max*/
	@Excel(name = "a21026Max", width = 15)
    @ApiModelProperty(value = "a21026Max")
    private Double a21026Max;
	/**a21026MaxState*/
	@Excel(name = "a21026MaxState", width = 15)
    @ApiModelProperty(value = "a21026MaxState")
    private Integer a21026MaxState;
	/**a21026Avg*/
	@Excel(name = "a21026Avg", width = 15)
    @ApiModelProperty(value = "a21026Avg")
    private Double a21026Avg;
	/**a21026AvgState*/
	@Excel(name = "a21026AvgState", width = 15)
    @ApiModelProperty(value = "a21026AvgState")
    private Integer a21026AvgState;
	/**a21026Cou*/
	@Excel(name = "a21026Cou", width = 15)
    @ApiModelProperty(value = "a21026Cou")
    private Double a21026Cou;
	/**a21026CouState*/
	@Excel(name = "a21026CouState", width = 15)
    @ApiModelProperty(value = "a21026CouState")
    private Integer a21026CouState;
	/**a21026Zsmin*/
	@Excel(name = "a21026Zsmin", width = 15)
    @ApiModelProperty(value = "a21026Zsmin")
    private Double a21026Zsmin;
	/**a21026ZsminState*/
	@Excel(name = "a21026ZsminState", width = 15)
    @ApiModelProperty(value = "a21026ZsminState")
    private Integer a21026ZsminState;
	/**a21026Zsmax*/
	@Excel(name = "a21026Zsmax", width = 15)
    @ApiModelProperty(value = "a21026Zsmax")
    private Double a21026Zsmax;
	/**a21026ZsmaxState*/
	@Excel(name = "a21026ZsmaxState", width = 15)
    @ApiModelProperty(value = "a21026ZsmaxState")
    private Integer a21026ZsmaxState;
	/**a21026Zsavg*/
	@Excel(name = "a21026Zsavg", width = 15)
    @ApiModelProperty(value = "a21026Zsavg")
    private Double a21026Zsavg;
	/**a21026ZsavgState*/
	@Excel(name = "a21026ZsavgState", width = 15)
    @ApiModelProperty(value = "a21026ZsavgState")
    private Integer a21026ZsavgState;
	/**a21026Zscou*/
	@Excel(name = "a21026Zscou", width = 15)
    @ApiModelProperty(value = "a21026Zscou")
    private Double a21026Zscou;
	/**a21026ZscouState*/
	@Excel(name = "a21026ZscouState", width = 15)
    @ApiModelProperty(value = "a21026ZscouState")
    private Integer a21026ZscouState;
	/**a21002Min*/
	@Excel(name = "a21002Min", width = 15)
    @ApiModelProperty(value = "a21002Min")
    private Double a21002Min;
	/**a21002MinState*/
	@Excel(name = "a21002MinState", width = 15)
    @ApiModelProperty(value = "a21002MinState")
    private Integer a21002MinState;
	/**a21002Max*/
	@Excel(name = "a21002Max", width = 15)
    @ApiModelProperty(value = "a21002Max")
    private Double a21002Max;
	/**a21002MaxState*/
	@Excel(name = "a21002MaxState", width = 15)
    @ApiModelProperty(value = "a21002MaxState")
    private Integer a21002MaxState;
	/**a21002Avg*/
	@Excel(name = "a21002Avg", width = 15)
    @ApiModelProperty(value = "a21002Avg")
    private Double a21002Avg;
	/**a21002AvgState*/
	@Excel(name = "a21002AvgState", width = 15)
    @ApiModelProperty(value = "a21002AvgState")
    private Integer a21002AvgState;
	/**a21002Cou*/
	@Excel(name = "a21002Cou", width = 15)
    @ApiModelProperty(value = "a21002Cou")
    private Double a21002Cou;
	/**a21002CouState*/
	@Excel(name = "a21002CouState", width = 15)
    @ApiModelProperty(value = "a21002CouState")
    private Integer a21002CouState;
	/**a21002Zsmin*/
	@Excel(name = "a21002Zsmin", width = 15)
    @ApiModelProperty(value = "a21002Zsmin")
    private Double a21002Zsmin;
	/**a21002ZsminState*/
	@Excel(name = "a21002ZsminState", width = 15)
    @ApiModelProperty(value = "a21002ZsminState")
    private Integer a21002ZsminState;
	/**a21002Zsmax*/
	@Excel(name = "a21002Zsmax", width = 15)
    @ApiModelProperty(value = "a21002Zsmax")
    private Double a21002Zsmax;
	/**a21002ZsmaxState*/
	@Excel(name = "a21002ZsmaxState", width = 15)
    @ApiModelProperty(value = "a21002ZsmaxState")
    private Integer a21002ZsmaxState;
	/**a21002Zsavg*/
	@Excel(name = "a21002Zsavg", width = 15)
    @ApiModelProperty(value = "a21002Zsavg")
    private Double a21002Zsavg;
	/**a21002ZsavgState*/
	@Excel(name = "a21002ZsavgState", width = 15)
    @ApiModelProperty(value = "a21002ZsavgState")
    private Integer a21002ZsavgState;
	/**a21002Zscou*/
	@Excel(name = "a21002Zscou", width = 15)
    @ApiModelProperty(value = "a21002Zscou")
    private Double a21002Zscou;
	/**a21002ZscouState*/
	@Excel(name = "a21002ZscouState", width = 15)
    @ApiModelProperty(value = "a21002ZscouState")
    private Integer a21002ZscouState;
}
