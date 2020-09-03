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
 * @Description: voc_hour
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
@Data
@TableName("voc_hour")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="voc_hour对象", description="voc_hour")
public class VocHour implements Serializable {
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
	/**a25005Min*/
	@Excel(name = "a25005Min", width = 15)
    @ApiModelProperty(value = "a25005Min")
    private java.lang.Double a25005Min;
	/**a25005MinState*/
	@Excel(name = "a25005MinState", width = 15)
    @ApiModelProperty(value = "a25005MinState")
    private java.lang.Integer a25005MinState;
	/**a25005Max*/
	@Excel(name = "a25005Max", width = 15)
    @ApiModelProperty(value = "a25005Max")
    private java.lang.Double a25005Max;
	/**a25005MaxState*/
	@Excel(name = "a25005MaxState", width = 15)
    @ApiModelProperty(value = "a25005MaxState")
    private java.lang.Integer a25005MaxState;
	/**a25005Avg*/
	@Excel(name = "a25005Avg", width = 15)
    @ApiModelProperty(value = "a25005Avg")
    private java.lang.Double a25005Avg;
	/**a25005AvgState*/
	@Excel(name = "a25005AvgState", width = 15)
    @ApiModelProperty(value = "a25005AvgState")
    private java.lang.Integer a25005AvgState;
	/**a25005Cou*/
	@Excel(name = "a25005Cou", width = 15)
    @ApiModelProperty(value = "a25005Cou")
    private java.lang.Double a25005Cou;
	/**a25005CouState*/
	@Excel(name = "a25005CouState", width = 15)
    @ApiModelProperty(value = "a25005CouState")
    private java.lang.Integer a25005CouState;
	/**a25005Flag*/
	@Excel(name = "a25005Flag", width = 15)
    @ApiModelProperty(value = "a25005Flag")
    private java.lang.String a25005Flag;
	/**a25005Zsmin*/
	@Excel(name = "a25005Zsmin", width = 15)
    @ApiModelProperty(value = "a25005Zsmin")
    private java.lang.Double a25005Zsmin;
	/**a25005ZsminState*/
	@Excel(name = "a25005ZsminState", width = 15)
    @ApiModelProperty(value = "a25005ZsminState")
    private java.lang.Integer a25005ZsminState;
	/**a25005Zsmax*/
	@Excel(name = "a25005Zsmax", width = 15)
    @ApiModelProperty(value = "a25005Zsmax")
    private java.lang.Double a25005Zsmax;
	/**a25005ZsmaxState*/
	@Excel(name = "a25005ZsmaxState", width = 15)
    @ApiModelProperty(value = "a25005ZsmaxState")
    private java.lang.Integer a25005ZsmaxState;
	/**a25005Zsavg*/
	@Excel(name = "a25005Zsavg", width = 15)
    @ApiModelProperty(value = "a25005Zsavg")
    private java.lang.Double a25005Zsavg;
	/**a25005ZsavgState*/
	@Excel(name = "a25005ZsavgState", width = 15)
    @ApiModelProperty(value = "a25005ZsavgState")
    private java.lang.Integer a25005ZsavgState;
	/**a25005Zscou*/
	@Excel(name = "a25005Zscou", width = 15)
    @ApiModelProperty(value = "a25005Zscou")
    private java.lang.Double a25005Zscou;
	/**a25005ZscouState*/
	@Excel(name = "a25005ZscouState", width = 15)
    @ApiModelProperty(value = "a25005ZscouState")
    private java.lang.Integer a25005ZscouState;
	/**a25002Min*/
	@Excel(name = "a25002Min", width = 15)
    @ApiModelProperty(value = "a25002Min")
    private java.lang.Double a25002Min;
	/**a25002MinState*/
	@Excel(name = "a25002MinState", width = 15)
    @ApiModelProperty(value = "a25002MinState")
    private java.lang.Integer a25002MinState;
	/**a25002Max*/
	@Excel(name = "a25002Max", width = 15)
    @ApiModelProperty(value = "a25002Max")
    private java.lang.Double a25002Max;
	/**a25002MaxState*/
	@Excel(name = "a25002MaxState", width = 15)
    @ApiModelProperty(value = "a25002MaxState")
    private java.lang.Integer a25002MaxState;
	/**a25002Avg*/
	@Excel(name = "a25002Avg", width = 15)
    @ApiModelProperty(value = "a25002Avg")
    private java.lang.Double a25002Avg;
	/**a25002AvgState*/
	@Excel(name = "a25002AvgState", width = 15)
    @ApiModelProperty(value = "a25002AvgState")
    private java.lang.Integer a25002AvgState;
	/**a25002Cou*/
	@Excel(name = "a25002Cou", width = 15)
    @ApiModelProperty(value = "a25002Cou")
    private java.lang.Double a25002Cou;
	/**a25002CouState*/
	@Excel(name = "a25002CouState", width = 15)
    @ApiModelProperty(value = "a25002CouState")
    private java.lang.Integer a25002CouState;
	/**a25002Flag*/
	@Excel(name = "a25002Flag", width = 15)
    @ApiModelProperty(value = "a25002Flag")
    private java.lang.String a25002Flag;
	/**a25002Zsmin*/
	@Excel(name = "a25002Zsmin", width = 15)
    @ApiModelProperty(value = "a25002Zsmin")
    private java.lang.Double a25002Zsmin;
	/**a25002ZsminState*/
	@Excel(name = "a25002ZsminState", width = 15)
    @ApiModelProperty(value = "a25002ZsminState")
    private java.lang.Integer a25002ZsminState;
	/**a25002Zsmax*/
	@Excel(name = "a25002Zsmax", width = 15)
    @ApiModelProperty(value = "a25002Zsmax")
    private java.lang.Double a25002Zsmax;
	/**a25002ZsmaxState*/
	@Excel(name = "a25002ZsmaxState", width = 15)
    @ApiModelProperty(value = "a25002ZsmaxState")
    private java.lang.Integer a25002ZsmaxState;
	/**a25002Zsavg*/
	@Excel(name = "a25002Zsavg", width = 15)
    @ApiModelProperty(value = "a25002Zsavg")
    private java.lang.Double a25002Zsavg;
	/**a25002ZsavgState*/
	@Excel(name = "a25002ZsavgState", width = 15)
    @ApiModelProperty(value = "a25002ZsavgState")
    private java.lang.Integer a25002ZsavgState;
	/**a25002Zscou*/
	@Excel(name = "a25002Zscou", width = 15)
    @ApiModelProperty(value = "a25002Zscou")
    private java.lang.Double a25002Zscou;
	/**a25002ZscouState*/
	@Excel(name = "a25002ZscouState", width = 15)
    @ApiModelProperty(value = "a25002ZscouState")
    private java.lang.Integer a25002ZscouState;
	/**a00000Min*/
	@Excel(name = "a00000Min", width = 15)
    @ApiModelProperty(value = "a00000Min")
    private java.lang.Double a00000Min;
	/**a00000MinState*/
	@Excel(name = "a00000MinState", width = 15)
    @ApiModelProperty(value = "a00000MinState")
    private java.lang.Integer a00000MinState;
	/**a00000Max*/
	@Excel(name = "a00000Max", width = 15)
    @ApiModelProperty(value = "a00000Max")
    private java.lang.Double a00000Max;
	/**a00000MaxState*/
	@Excel(name = "a00000MaxState", width = 15)
    @ApiModelProperty(value = "a00000MaxState")
    private java.lang.Integer a00000MaxState;
	/**a00000Avg*/
	@Excel(name = "a00000Avg", width = 15)
    @ApiModelProperty(value = "a00000Avg")
    private java.lang.Double a00000Avg;
	/**a00000AvgState*/
	@Excel(name = "a00000AvgState", width = 15)
    @ApiModelProperty(value = "a00000AvgState")
    private java.lang.Integer a00000AvgState;
	/**a00000Cou*/
	@Excel(name = "a00000Cou", width = 15)
    @ApiModelProperty(value = "a00000Cou")
    private java.lang.Double a00000Cou;
	/**a00000CouState*/
	@Excel(name = "a00000CouState", width = 15)
    @ApiModelProperty(value = "a00000CouState")
    private java.lang.Integer a00000CouState;
	/**a00000Flag*/
	@Excel(name = "a00000Flag", width = 15)
    @ApiModelProperty(value = "a00000Flag")
    private java.lang.String a00000Flag;
	/**a00000Zsmin*/
	@Excel(name = "a00000Zsmin", width = 15)
    @ApiModelProperty(value = "a00000Zsmin")
    private java.lang.Double a00000Zsmin;
	/**a00000ZsminState*/
	@Excel(name = "a00000ZsminState", width = 15)
    @ApiModelProperty(value = "a00000ZsminState")
    private java.lang.Integer a00000ZsminState;
	/**a00000Zsmax*/
	@Excel(name = "a00000Zsmax", width = 15)
    @ApiModelProperty(value = "a00000Zsmax")
    private java.lang.Double a00000Zsmax;
	/**a00000ZsmaxState*/
	@Excel(name = "a00000ZsmaxState", width = 15)
    @ApiModelProperty(value = "a00000ZsmaxState")
    private java.lang.Integer a00000ZsmaxState;
	/**a00000Zsavg*/
	@Excel(name = "a00000Zsavg", width = 15)
    @ApiModelProperty(value = "a00000Zsavg")
    private java.lang.Double a00000Zsavg;
	/**a00000ZsavgState*/
	@Excel(name = "a00000ZsavgState", width = 15)
    @ApiModelProperty(value = "a00000ZsavgState")
    private java.lang.Integer a00000ZsavgState;
	/**a00000Zscou*/
	@Excel(name = "a00000Zscou", width = 15)
    @ApiModelProperty(value = "a00000Zscou")
    private java.lang.Double a00000Zscou;
	/**a00000ZscouState*/
	@Excel(name = "a00000ZscouState", width = 15)
    @ApiModelProperty(value = "a00000ZscouState")
    private java.lang.Integer a00000ZscouState;
	/**a34013Min*/
	@Excel(name = "a34013Min", width = 15)
    @ApiModelProperty(value = "a34013Min")
    private java.lang.Double a34013Min;
	/**a34013MinState*/
	@Excel(name = "a34013MinState", width = 15)
    @ApiModelProperty(value = "a34013MinState")
    private java.lang.Integer a34013MinState;
	/**a34013Max*/
	@Excel(name = "a34013Max", width = 15)
    @ApiModelProperty(value = "a34013Max")
    private java.lang.Double a34013Max;
	/**a34013MaxState*/
	@Excel(name = "a34013MaxState", width = 15)
    @ApiModelProperty(value = "a34013MaxState")
    private java.lang.Integer a34013MaxState;
	/**a34013Avg*/
	@Excel(name = "a34013Avg", width = 15)
    @ApiModelProperty(value = "a34013Avg")
    private java.lang.Double a34013Avg;
	/**a34013AvgState*/
	@Excel(name = "a34013AvgState", width = 15)
    @ApiModelProperty(value = "a34013AvgState")
    private java.lang.Integer a34013AvgState;
	/**a34013Cou*/
	@Excel(name = "a34013Cou", width = 15)
    @ApiModelProperty(value = "a34013Cou")
    private java.lang.Double a34013Cou;
	/**a34013CouState*/
	@Excel(name = "a34013CouState", width = 15)
    @ApiModelProperty(value = "a34013CouState")
    private java.lang.Integer a34013CouState;
	/**a34013Flag*/
	@Excel(name = "a34013Flag", width = 15)
    @ApiModelProperty(value = "a34013Flag")
    private java.lang.String a34013Flag;
	/**a34013Zsmin*/
	@Excel(name = "a34013Zsmin", width = 15)
    @ApiModelProperty(value = "a34013Zsmin")
    private java.lang.Double a34013Zsmin;
	/**a34013ZsminState*/
	@Excel(name = "a34013ZsminState", width = 15)
    @ApiModelProperty(value = "a34013ZsminState")
    private java.lang.Integer a34013ZsminState;
	/**a34013Zsmax*/
	@Excel(name = "a34013Zsmax", width = 15)
    @ApiModelProperty(value = "a34013Zsmax")
    private java.lang.Double a34013Zsmax;
	/**a34013ZsmaxState*/
	@Excel(name = "a34013ZsmaxState", width = 15)
    @ApiModelProperty(value = "a34013ZsmaxState")
    private java.lang.Integer a34013ZsmaxState;
	/**a34013Zsavg*/
	@Excel(name = "a34013Zsavg", width = 15)
    @ApiModelProperty(value = "a34013Zsavg")
    private java.lang.Double a34013Zsavg;
	/**a34013ZsavgState*/
	@Excel(name = "a34013ZsavgState", width = 15)
    @ApiModelProperty(value = "a34013ZsavgState")
    private java.lang.Integer a34013ZsavgState;
	/**a34013Zscou*/
	@Excel(name = "a34013Zscou", width = 15)
    @ApiModelProperty(value = "a34013Zscou")
    private java.lang.Double a34013Zscou;
	/**a34013ZscouState*/
	@Excel(name = "a34013ZscouState", width = 15)
    @ApiModelProperty(value = "a34013ZscouState")
    private java.lang.Integer a34013ZscouState;
	/**a01011Min*/
	@Excel(name = "a01011Min", width = 15)
    @ApiModelProperty(value = "a01011Min")
    private java.lang.Double a01011Min;
	/**a01011MinState*/
	@Excel(name = "a01011MinState", width = 15)
    @ApiModelProperty(value = "a01011MinState")
    private java.lang.Integer a01011MinState;
	/**a01011Max*/
	@Excel(name = "a01011Max", width = 15)
    @ApiModelProperty(value = "a01011Max")
    private java.lang.Double a01011Max;
	/**a01011MaxState*/
	@Excel(name = "a01011MaxState", width = 15)
    @ApiModelProperty(value = "a01011MaxState")
    private java.lang.Integer a01011MaxState;
	/**a01011Avg*/
	@Excel(name = "a01011Avg", width = 15)
    @ApiModelProperty(value = "a01011Avg")
    private java.lang.Double a01011Avg;
	/**a01011AvgState*/
	@Excel(name = "a01011AvgState", width = 15)
    @ApiModelProperty(value = "a01011AvgState")
    private java.lang.Integer a01011AvgState;
	/**a01011Cou*/
	@Excel(name = "a01011Cou", width = 15)
    @ApiModelProperty(value = "a01011Cou")
    private java.lang.Double a01011Cou;
	/**a01011CouState*/
	@Excel(name = "a01011CouState", width = 15)
    @ApiModelProperty(value = "a01011CouState")
    private java.lang.Integer a01011CouState;
	/**a01011Flag*/
	@Excel(name = "a01011Flag", width = 15)
    @ApiModelProperty(value = "a01011Flag")
    private java.lang.String a01011Flag;
	/**a01011Zsmin*/
	@Excel(name = "a01011Zsmin", width = 15)
    @ApiModelProperty(value = "a01011Zsmin")
    private java.lang.Double a01011Zsmin;
	/**a01011ZsminState*/
	@Excel(name = "a01011ZsminState", width = 15)
    @ApiModelProperty(value = "a01011ZsminState")
    private java.lang.Integer a01011ZsminState;
	/**a01011Zsmax*/
	@Excel(name = "a01011Zsmax", width = 15)
    @ApiModelProperty(value = "a01011Zsmax")
    private java.lang.Double a01011Zsmax;
	/**a01011ZsmaxState*/
	@Excel(name = "a01011ZsmaxState", width = 15)
    @ApiModelProperty(value = "a01011ZsmaxState")
    private java.lang.Integer a01011ZsmaxState;
	/**a01011Zsavg*/
	@Excel(name = "a01011Zsavg", width = 15)
    @ApiModelProperty(value = "a01011Zsavg")
    private java.lang.Double a01011Zsavg;
	/**a01011ZsavgState*/
	@Excel(name = "a01011ZsavgState", width = 15)
    @ApiModelProperty(value = "a01011ZsavgState")
    private java.lang.Integer a01011ZsavgState;
	/**a01011Zscou*/
	@Excel(name = "a01011Zscou", width = 15)
    @ApiModelProperty(value = "a01011Zscou")
    private java.lang.Double a01011Zscou;
	/**a01011ZscouState*/
	@Excel(name = "a01011ZscouState", width = 15)
    @ApiModelProperty(value = "a01011ZscouState")
    private java.lang.Integer a01011ZscouState;
	/**a01012Min*/
	@Excel(name = "a01012Min", width = 15)
    @ApiModelProperty(value = "a01012Min")
    private java.lang.Double a01012Min;
	/**a01012MinState*/
	@Excel(name = "a01012MinState", width = 15)
    @ApiModelProperty(value = "a01012MinState")
    private java.lang.Integer a01012MinState;
	/**a01012Max*/
	@Excel(name = "a01012Max", width = 15)
    @ApiModelProperty(value = "a01012Max")
    private java.lang.Double a01012Max;
	/**a01012MaxState*/
	@Excel(name = "a01012MaxState", width = 15)
    @ApiModelProperty(value = "a01012MaxState")
    private java.lang.Integer a01012MaxState;
	/**a01012Avg*/
	@Excel(name = "a01012Avg", width = 15)
    @ApiModelProperty(value = "a01012Avg")
    private java.lang.Double a01012Avg;
	/**a01012AvgState*/
	@Excel(name = "a01012AvgState", width = 15)
    @ApiModelProperty(value = "a01012AvgState")
    private java.lang.Integer a01012AvgState;
	/**a01012Cou*/
	@Excel(name = "a01012Cou", width = 15)
    @ApiModelProperty(value = "a01012Cou")
    private java.lang.Double a01012Cou;
	/**a01012CouState*/
	@Excel(name = "a01012CouState", width = 15)
    @ApiModelProperty(value = "a01012CouState")
    private java.lang.Integer a01012CouState;
	/**a01012Flag*/
	@Excel(name = "a01012Flag", width = 15)
    @ApiModelProperty(value = "a01012Flag")
    private java.lang.String a01012Flag;
	/**a01012Zsmin*/
	@Excel(name = "a01012Zsmin", width = 15)
    @ApiModelProperty(value = "a01012Zsmin")
    private java.lang.Double a01012Zsmin;
	/**a01012ZsminState*/
	@Excel(name = "a01012ZsminState", width = 15)
    @ApiModelProperty(value = "a01012ZsminState")
    private java.lang.Integer a01012ZsminState;
	/**a01012Zsmax*/
	@Excel(name = "a01012Zsmax", width = 15)
    @ApiModelProperty(value = "a01012Zsmax")
    private java.lang.Double a01012Zsmax;
	/**a01012ZsmaxState*/
	@Excel(name = "a01012ZsmaxState", width = 15)
    @ApiModelProperty(value = "a01012ZsmaxState")
    private java.lang.Integer a01012ZsmaxState;
	/**a01012Zsavg*/
	@Excel(name = "a01012Zsavg", width = 15)
    @ApiModelProperty(value = "a01012Zsavg")
    private java.lang.Double a01012Zsavg;
	/**a01012ZsavgState*/
	@Excel(name = "a01012ZsavgState", width = 15)
    @ApiModelProperty(value = "a01012ZsavgState")
    private java.lang.Integer a01012ZsavgState;
	/**a01012Zscou*/
	@Excel(name = "a01012Zscou", width = 15)
    @ApiModelProperty(value = "a01012Zscou")
    private java.lang.Double a01012Zscou;
	/**a01012ZscouState*/
	@Excel(name = "a01012ZscouState", width = 15)
    @ApiModelProperty(value = "a01012ZscouState")
    private java.lang.Integer a01012ZscouState;
	/**a24088Min*/
	@Excel(name = "a24088Min", width = 15)
    @ApiModelProperty(value = "a24088Min")
    private java.lang.Double a24088Min;
	/**a24088MinState*/
	@Excel(name = "a24088MinState", width = 15)
    @ApiModelProperty(value = "a24088MinState")
    private java.lang.Integer a24088MinState;
	/**a24088Max*/
	@Excel(name = "a24088Max", width = 15)
    @ApiModelProperty(value = "a24088Max")
    private java.lang.Double a24088Max;
	/**a24088MaxState*/
	@Excel(name = "a24088MaxState", width = 15)
    @ApiModelProperty(value = "a24088MaxState")
    private java.lang.Integer a24088MaxState;
	/**a24088Avg*/
	@Excel(name = "a24088Avg", width = 15)
    @ApiModelProperty(value = "a24088Avg")
    private java.lang.Double a24088Avg;
	/**a24088AvgState*/
	@Excel(name = "a24088AvgState", width = 15)
    @ApiModelProperty(value = "a24088AvgState")
    private java.lang.Integer a24088AvgState;
	/**a24088Cou*/
	@Excel(name = "a24088Cou", width = 15)
    @ApiModelProperty(value = "a24088Cou")
    private java.lang.Double a24088Cou;
	/**a24088CouState*/
	@Excel(name = "a24088CouState", width = 15)
    @ApiModelProperty(value = "a24088CouState")
    private java.lang.Integer a24088CouState;
	/**a24088Flag*/
	@Excel(name = "a24088Flag", width = 15)
    @ApiModelProperty(value = "a24088Flag")
    private java.lang.String a24088Flag;
	/**a24088Zsmin*/
	@Excel(name = "a24088Zsmin", width = 15)
    @ApiModelProperty(value = "a24088Zsmin")
    private java.lang.Double a24088Zsmin;
	/**a24088ZsminState*/
	@Excel(name = "a24088ZsminState", width = 15)
    @ApiModelProperty(value = "a24088ZsminState")
    private java.lang.Integer a24088ZsminState;
	/**a24088Zsmax*/
	@Excel(name = "a24088Zsmax", width = 15)
    @ApiModelProperty(value = "a24088Zsmax")
    private java.lang.Double a24088Zsmax;
	/**a24088ZsmaxState*/
	@Excel(name = "a24088ZsmaxState", width = 15)
    @ApiModelProperty(value = "a24088ZsmaxState")
    private java.lang.Integer a24088ZsmaxState;
	/**a24088Zsavg*/
	@Excel(name = "a24088Zsavg", width = 15)
    @ApiModelProperty(value = "a24088Zsavg")
    private java.lang.Double a24088Zsavg;
	/**a24088ZsavgState*/
	@Excel(name = "a24088ZsavgState", width = 15)
    @ApiModelProperty(value = "a24088ZsavgState")
    private java.lang.Integer a24088ZsavgState;
	/**a24088Zscou*/
	@Excel(name = "a24088Zscou", width = 15)
    @ApiModelProperty(value = "a24088Zscou")
    private java.lang.Double a24088Zscou;
	/**a24088ZscouState*/
	@Excel(name = "a24088ZscouState", width = 15)
    @ApiModelProperty(value = "a24088ZscouState")
    private java.lang.Integer a24088ZscouState;
	/**a25003Min*/
	@Excel(name = "a25003Min", width = 15)
    @ApiModelProperty(value = "a25003Min")
    private java.lang.Double a25003Min;
	/**a25003MinState*/
	@Excel(name = "a25003MinState", width = 15)
    @ApiModelProperty(value = "a25003MinState")
    private java.lang.Integer a25003MinState;
	/**a25003Max*/
	@Excel(name = "a25003Max", width = 15)
    @ApiModelProperty(value = "a25003Max")
    private java.lang.Double a25003Max;
	/**a25003MaxState*/
	@Excel(name = "a25003MaxState", width = 15)
    @ApiModelProperty(value = "a25003MaxState")
    private java.lang.Integer a25003MaxState;
	/**a25003Avg*/
	@Excel(name = "a25003Avg", width = 15)
    @ApiModelProperty(value = "a25003Avg")
    private java.lang.Double a25003Avg;
	/**a25003AvgState*/
	@Excel(name = "a25003AvgState", width = 15)
    @ApiModelProperty(value = "a25003AvgState")
    private java.lang.Integer a25003AvgState;
	/**a25003Cou*/
	@Excel(name = "a25003Cou", width = 15)
    @ApiModelProperty(value = "a25003Cou")
    private java.lang.Double a25003Cou;
	/**a25003CouState*/
	@Excel(name = "a25003CouState", width = 15)
    @ApiModelProperty(value = "a25003CouState")
    private java.lang.Integer a25003CouState;
	/**a25003Flag*/
	@Excel(name = "a25003Flag", width = 15)
    @ApiModelProperty(value = "a25003Flag")
    private java.lang.String a25003Flag;
	/**a25003Zsmin*/
	@Excel(name = "a25003Zsmin", width = 15)
    @ApiModelProperty(value = "a25003Zsmin")
    private java.lang.Double a25003Zsmin;
	/**a25003ZsminState*/
	@Excel(name = "a25003ZsminState", width = 15)
    @ApiModelProperty(value = "a25003ZsminState")
    private java.lang.Integer a25003ZsminState;
	/**a25003Zsmax*/
	@Excel(name = "a25003Zsmax", width = 15)
    @ApiModelProperty(value = "a25003Zsmax")
    private java.lang.Double a25003Zsmax;
	/**a25003ZsmaxState*/
	@Excel(name = "a25003ZsmaxState", width = 15)
    @ApiModelProperty(value = "a25003ZsmaxState")
    private java.lang.Integer a25003ZsmaxState;
	/**a25003Zsavg*/
	@Excel(name = "a25003Zsavg", width = 15)
    @ApiModelProperty(value = "a25003Zsavg")
    private java.lang.Double a25003Zsavg;
	/**a25003ZsavgState*/
	@Excel(name = "a25003ZsavgState", width = 15)
    @ApiModelProperty(value = "a25003ZsavgState")
    private java.lang.Integer a25003ZsavgState;
	/**a25003Zscou*/
	@Excel(name = "a25003Zscou", width = 15)
    @ApiModelProperty(value = "a25003Zscou")
    private java.lang.Double a25003Zscou;
	/**a25003ZscouState*/
	@Excel(name = "a25003ZscouState", width = 15)
    @ApiModelProperty(value = "a25003ZscouState")
    private java.lang.Integer a25003ZscouState;
	/**a01017Min*/
	@Excel(name = "a01017Min", width = 15)
    @ApiModelProperty(value = "a01017Min")
    private java.lang.Double a01017Min;
	/**a01017MinState*/
	@Excel(name = "a01017MinState", width = 15)
    @ApiModelProperty(value = "a01017MinState")
    private java.lang.Integer a01017MinState;
	/**a01017Max*/
	@Excel(name = "a01017Max", width = 15)
    @ApiModelProperty(value = "a01017Max")
    private java.lang.Double a01017Max;
	/**a01017MaxState*/
	@Excel(name = "a01017MaxState", width = 15)
    @ApiModelProperty(value = "a01017MaxState")
    private java.lang.Integer a01017MaxState;
	/**a01017Avg*/
	@Excel(name = "a01017Avg", width = 15)
    @ApiModelProperty(value = "a01017Avg")
    private java.lang.Double a01017Avg;
	/**a01017AvgState*/
	@Excel(name = "a01017AvgState", width = 15)
    @ApiModelProperty(value = "a01017AvgState")
    private java.lang.Integer a01017AvgState;
	/**a01017Cou*/
	@Excel(name = "a01017Cou", width = 15)
    @ApiModelProperty(value = "a01017Cou")
    private java.lang.Double a01017Cou;
	/**a01017CouState*/
	@Excel(name = "a01017CouState", width = 15)
    @ApiModelProperty(value = "a01017CouState")
    private java.lang.Integer a01017CouState;
	/**a01017Flag*/
	@Excel(name = "a01017Flag", width = 15)
    @ApiModelProperty(value = "a01017Flag")
    private java.lang.String a01017Flag;
	/**a01017Zsmin*/
	@Excel(name = "a01017Zsmin", width = 15)
    @ApiModelProperty(value = "a01017Zsmin")
    private java.lang.Double a01017Zsmin;
	/**a01017ZsminState*/
	@Excel(name = "a01017ZsminState", width = 15)
    @ApiModelProperty(value = "a01017ZsminState")
    private java.lang.Integer a01017ZsminState;
	/**a01017Zsmax*/
	@Excel(name = "a01017Zsmax", width = 15)
    @ApiModelProperty(value = "a01017Zsmax")
    private java.lang.Double a01017Zsmax;
	/**a01017ZsmaxState*/
	@Excel(name = "a01017ZsmaxState", width = 15)
    @ApiModelProperty(value = "a01017ZsmaxState")
    private java.lang.Integer a01017ZsmaxState;
	/**a01017Zsavg*/
	@Excel(name = "a01017Zsavg", width = 15)
    @ApiModelProperty(value = "a01017Zsavg")
    private java.lang.Double a01017Zsavg;
	/**a01017ZsavgState*/
	@Excel(name = "a01017ZsavgState", width = 15)
    @ApiModelProperty(value = "a01017ZsavgState")
    private java.lang.Integer a01017ZsavgState;
	/**a01017Zscou*/
	@Excel(name = "a01017Zscou", width = 15)
    @ApiModelProperty(value = "a01017Zscou")
    private java.lang.Double a01017Zscou;
	/**a01017ZscouState*/
	@Excel(name = "a01017ZscouState", width = 15)
    @ApiModelProperty(value = "a01017ZscouState")
    private java.lang.Integer a01017ZscouState;
	/**a21002Min*/
	@Excel(name = "a21002Min", width = 15)
    @ApiModelProperty(value = "a21002Min")
    private java.lang.Double a21002Min;
	/**a21002MinState*/
	@Excel(name = "a21002MinState", width = 15)
    @ApiModelProperty(value = "a21002MinState")
    private java.lang.Integer a21002MinState;
	/**a21002Max*/
	@Excel(name = "a21002Max", width = 15)
    @ApiModelProperty(value = "a21002Max")
    private java.lang.Double a21002Max;
	/**a21002MaxState*/
	@Excel(name = "a21002MaxState", width = 15)
    @ApiModelProperty(value = "a21002MaxState")
    private java.lang.Integer a21002MaxState;
	/**a21002Avg*/
	@Excel(name = "a21002Avg", width = 15)
    @ApiModelProperty(value = "a21002Avg")
    private java.lang.Double a21002Avg;
	/**a21002AvgState*/
	@Excel(name = "a21002AvgState", width = 15)
    @ApiModelProperty(value = "a21002AvgState")
    private java.lang.Integer a21002AvgState;
	/**a21002Cou*/
	@Excel(name = "a21002Cou", width = 15)
    @ApiModelProperty(value = "a21002Cou")
    private java.lang.Double a21002Cou;
	/**a21002CouState*/
	@Excel(name = "a21002CouState", width = 15)
    @ApiModelProperty(value = "a21002CouState")
    private java.lang.Integer a21002CouState;
	/**a21002Flag*/
	@Excel(name = "a21002Flag", width = 15)
    @ApiModelProperty(value = "a21002Flag")
    private java.lang.String a21002Flag;
	/**a21002Zsmin*/
	@Excel(name = "a21002Zsmin", width = 15)
    @ApiModelProperty(value = "a21002Zsmin")
    private java.lang.Double a21002Zsmin;
	/**a21002ZsminState*/
	@Excel(name = "a21002ZsminState", width = 15)
    @ApiModelProperty(value = "a21002ZsminState")
    private java.lang.Integer a21002ZsminState;
	/**a21002Zsmax*/
	@Excel(name = "a21002Zsmax", width = 15)
    @ApiModelProperty(value = "a21002Zsmax")
    private java.lang.Double a21002Zsmax;
	/**a21002ZsmaxState*/
	@Excel(name = "a21002ZsmaxState", width = 15)
    @ApiModelProperty(value = "a21002ZsmaxState")
    private java.lang.Integer a21002ZsmaxState;
	/**a21002Zsavg*/
	@Excel(name = "a21002Zsavg", width = 15)
    @ApiModelProperty(value = "a21002Zsavg")
    private java.lang.Double a21002Zsavg;
	/**a21002ZsavgState*/
	@Excel(name = "a21002ZsavgState", width = 15)
    @ApiModelProperty(value = "a21002ZsavgState")
    private java.lang.Integer a21002ZsavgState;
	/**a21002Zscou*/
	@Excel(name = "a21002Zscou", width = 15)
    @ApiModelProperty(value = "a21002Zscou")
    private java.lang.Double a21002Zscou;
	/**a21002ZscouState*/
	@Excel(name = "a21002ZscouState", width = 15)
    @ApiModelProperty(value = "a21002ZscouState")
    private java.lang.Integer a21002ZscouState;
	/**a21026Min*/
	@Excel(name = "a21026Min", width = 15)
    @ApiModelProperty(value = "a21026Min")
    private java.lang.Double a21026Min;
	/**a21026MinState*/
	@Excel(name = "a21026MinState", width = 15)
    @ApiModelProperty(value = "a21026MinState")
    private java.lang.Integer a21026MinState;
	/**a21026Max*/
	@Excel(name = "a21026Max", width = 15)
    @ApiModelProperty(value = "a21026Max")
    private java.lang.Double a21026Max;
	/**a21026MaxState*/
	@Excel(name = "a21026MaxState", width = 15)
    @ApiModelProperty(value = "a21026MaxState")
    private java.lang.Integer a21026MaxState;
	/**a21026Avg*/
	@Excel(name = "a21026Avg", width = 15)
    @ApiModelProperty(value = "a21026Avg")
    private java.lang.Double a21026Avg;
	/**a21026AvgState*/
	@Excel(name = "a21026AvgState", width = 15)
    @ApiModelProperty(value = "a21026AvgState")
    private java.lang.Integer a21026AvgState;
	/**a21026Cou*/
	@Excel(name = "a21026Cou", width = 15)
    @ApiModelProperty(value = "a21026Cou")
    private java.lang.Double a21026Cou;
	/**a21026CouState*/
	@Excel(name = "a21026CouState", width = 15)
    @ApiModelProperty(value = "a21026CouState")
    private java.lang.Integer a21026CouState;
	/**a21026Flag*/
	@Excel(name = "a21026Flag", width = 15)
    @ApiModelProperty(value = "a21026Flag")
    private java.lang.String a21026Flag;
	/**a21026Zsmin*/
	@Excel(name = "a21026Zsmin", width = 15)
    @ApiModelProperty(value = "a21026Zsmin")
    private java.lang.Double a21026Zsmin;
	/**a21026ZsminState*/
	@Excel(name = "a21026ZsminState", width = 15)
    @ApiModelProperty(value = "a21026ZsminState")
    private java.lang.Integer a21026ZsminState;
	/**a21026Zsmax*/
	@Excel(name = "a21026Zsmax", width = 15)
    @ApiModelProperty(value = "a21026Zsmax")
    private java.lang.Double a21026Zsmax;
	/**a21026ZsmaxState*/
	@Excel(name = "a21026ZsmaxState", width = 15)
    @ApiModelProperty(value = "a21026ZsmaxState")
    private java.lang.Integer a21026ZsmaxState;
	/**a21026Zsavg*/
	@Excel(name = "a21026Zsavg", width = 15)
    @ApiModelProperty(value = "a21026Zsavg")
    private java.lang.Double a21026Zsavg;
	/**a21026ZsavgState*/
	@Excel(name = "a21026ZsavgState", width = 15)
    @ApiModelProperty(value = "a21026ZsavgState")
    private java.lang.Integer a21026ZsavgState;
	/**a21026Zscou*/
	@Excel(name = "a21026Zscou", width = 15)
    @ApiModelProperty(value = "a21026Zscou")
    private java.lang.Double a21026Zscou;
	/**a21026ZscouState*/
	@Excel(name = "a21026ZscouState", width = 15)
    @ApiModelProperty(value = "a21026ZscouState")
    private java.lang.Integer a21026ZscouState;
	/**a21005Min*/
	@Excel(name = "a21005Min", width = 15)
    @ApiModelProperty(value = "a21005Min")
    private java.lang.Double a21005Min;
	/**a21005MinState*/
	@Excel(name = "a21005MinState", width = 15)
    @ApiModelProperty(value = "a21005MinState")
    private java.lang.Integer a21005MinState;
	/**a21005Max*/
	@Excel(name = "a21005Max", width = 15)
    @ApiModelProperty(value = "a21005Max")
    private java.lang.Double a21005Max;
	/**a21005MaxState*/
	@Excel(name = "a21005MaxState", width = 15)
    @ApiModelProperty(value = "a21005MaxState")
    private java.lang.Integer a21005MaxState;
	/**a21005Avg*/
	@Excel(name = "a21005Avg", width = 15)
    @ApiModelProperty(value = "a21005Avg")
    private java.lang.Double a21005Avg;
	/**a21005AvgState*/
	@Excel(name = "a21005AvgState", width = 15)
    @ApiModelProperty(value = "a21005AvgState")
    private java.lang.Integer a21005AvgState;
	/**a21005Cou*/
	@Excel(name = "a21005Cou", width = 15)
    @ApiModelProperty(value = "a21005Cou")
    private java.lang.Double a21005Cou;
	/**a21005CouState*/
	@Excel(name = "a21005CouState", width = 15)
    @ApiModelProperty(value = "a21005CouState")
    private java.lang.Integer a21005CouState;
	/**a21005Flag*/
	@Excel(name = "a21005Flag", width = 15)
    @ApiModelProperty(value = "a21005Flag")
    private java.lang.String a21005Flag;
	/**a21005Zsmin*/
	@Excel(name = "a21005Zsmin", width = 15)
    @ApiModelProperty(value = "a21005Zsmin")
    private java.lang.Double a21005Zsmin;
	/**a21005ZsminState*/
	@Excel(name = "a21005ZsminState", width = 15)
    @ApiModelProperty(value = "a21005ZsminState")
    private java.lang.Integer a21005ZsminState;
	/**a21005Zsmax*/
	@Excel(name = "a21005Zsmax", width = 15)
    @ApiModelProperty(value = "a21005Zsmax")
    private java.lang.Double a21005Zsmax;
	/**a21005ZsmaxState*/
	@Excel(name = "a21005ZsmaxState", width = 15)
    @ApiModelProperty(value = "a21005ZsmaxState")
    private java.lang.Integer a21005ZsmaxState;
	/**a21005Zsavg*/
	@Excel(name = "a21005Zsavg", width = 15)
    @ApiModelProperty(value = "a21005Zsavg")
    private java.lang.Double a21005Zsavg;
	/**a21005ZsavgState*/
	@Excel(name = "a21005ZsavgState", width = 15)
    @ApiModelProperty(value = "a21005ZsavgState")
    private java.lang.Integer a21005ZsavgState;
	/**a21005Zscou*/
	@Excel(name = "a21005Zscou", width = 15)
    @ApiModelProperty(value = "a21005Zscou")
    private java.lang.Double a21005Zscou;
	/**a21005ZscouState*/
	@Excel(name = "a21005ZscouState", width = 15)
    @ApiModelProperty(value = "a21005ZscouState")
    private java.lang.Integer a21005ZscouState;
}
