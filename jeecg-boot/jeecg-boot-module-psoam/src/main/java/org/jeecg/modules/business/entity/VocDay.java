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
 * @Description: voc_day
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
@Data
@TableName("voc_day")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="voc_day对象", description="voc_day")
public class VocDay implements Serializable {
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
	/**二甲苯Min*/
	@Excel(name = "二甲苯Min", width = 15)
    @ApiModelProperty(value = "二甲苯Min")
    private java.lang.Double a25005Min;
	/**二甲苯MinState*/
	@Excel(name = "二甲苯MinState", width = 15)
    @ApiModelProperty(value = "二甲苯MinState")
    private java.lang.Integer a25005MinState;
	/**二甲苯Max*/
	@Excel(name = "二甲苯Max", width = 15)
    @ApiModelProperty(value = "二甲苯Max")
    private java.lang.Double a25005Max;
	/**二甲苯MaxState*/
	@Excel(name = "二甲苯MaxState", width = 15)
    @ApiModelProperty(value = "二甲苯MaxState")
    private java.lang.Integer a25005MaxState;
	/**二甲苯Avg*/
	@Excel(name = "二甲苯Avg", width = 15)
    @ApiModelProperty(value = "二甲苯Avg")
    private java.lang.Double a25005Avg;
	/**二甲苯AvgState*/
	@Excel(name = "二甲苯AvgState", width = 15)
    @ApiModelProperty(value = "二甲苯AvgState")
    private java.lang.Integer a25005AvgState;
	/**二甲苯Cou*/
	@Excel(name = "二甲苯Cou", width = 15)
    @ApiModelProperty(value = "二甲苯Cou")
    private java.lang.Double a25005Cou;
	/**二甲苯CouState*/
	@Excel(name = "二甲苯CouState", width = 15)
    @ApiModelProperty(value = "二甲苯CouState")
    private java.lang.Integer a25005CouState;
	/**二甲苯Flag*/
	@Excel(name = "二甲苯Flag", width = 15)
    @ApiModelProperty(value = "二甲苯Flag")
    private java.lang.String a25005Flag;
	/**二甲苯Zsmin*/
	@Excel(name = "二甲苯Zsmin", width = 15)
    @ApiModelProperty(value = "二甲苯Zsmin")
    private java.lang.Double a25005Zsmin;
	/**二甲苯ZsminState*/
	@Excel(name = "二甲苯ZsminState", width = 15)
    @ApiModelProperty(value = "二甲苯ZsminState")
    private java.lang.Integer a25005ZsminState;
	/**二甲苯Zsmax*/
	@Excel(name = "二甲苯Zsmax", width = 15)
    @ApiModelProperty(value = "二甲苯Zsmax")
    private java.lang.Double a25005Zsmax;
	/**二甲苯ZsmaxState*/
	@Excel(name = "二甲苯ZsmaxState", width = 15)
    @ApiModelProperty(value = "二甲苯ZsmaxState")
    private java.lang.Integer a25005ZsmaxState;
	/**二甲苯Zsavg*/
	@Excel(name = "二甲苯Zsavg", width = 15)
    @ApiModelProperty(value = "二甲苯Zsavg")
    private java.lang.Double a25005Zsavg;
	/**二甲苯ZsavgState*/
	@Excel(name = "二甲苯ZsavgState", width = 15)
    @ApiModelProperty(value = "二甲苯ZsavgState")
    private java.lang.Integer a25005ZsavgState;
	/**二甲苯Zscou*/
	@Excel(name = "二甲苯Zscou", width = 15)
    @ApiModelProperty(value = "二甲苯Zscou")
    private java.lang.Double a25005Zscou;
	/**二甲苯ZscouState*/
	@Excel(name = "二甲苯ZscouState", width = 15)
    @ApiModelProperty(value = "二甲苯ZscouState")
    private java.lang.Integer a25005ZscouState;
	/**苯Min*/
	@Excel(name = "苯Min", width = 15)
    @ApiModelProperty(value = "苯Min")
    private java.lang.Double a25002Min;
	/**苯MinState*/
	@Excel(name = "苯MinState", width = 15)
    @ApiModelProperty(value = "苯MinState")
    private java.lang.Integer a25002MinState;
	/**苯Max*/
	@Excel(name = "苯Max", width = 15)
    @ApiModelProperty(value = "苯Max")
    private java.lang.Double a25002Max;
	/**苯MaxState*/
	@Excel(name = "苯MaxState", width = 15)
    @ApiModelProperty(value = "苯MaxState")
    private java.lang.Integer a25002MaxState;
	/**苯Avg*/
	@Excel(name = "苯Avg", width = 15)
    @ApiModelProperty(value = "苯Avg")
    private java.lang.Double a25002Avg;
	/**苯AvgState*/
	@Excel(name = "苯AvgState", width = 15)
    @ApiModelProperty(value = "苯AvgState")
    private java.lang.Integer a25002AvgState;
	/**苯Cou*/
	@Excel(name = "苯Cou", width = 15)
    @ApiModelProperty(value = "苯Cou")
    private java.lang.Double a25002Cou;
	/**苯CouState*/
	@Excel(name = "苯CouState", width = 15)
    @ApiModelProperty(value = "苯CouState")
    private java.lang.Integer a25002CouState;
	/**苯Flag*/
	@Excel(name = "苯Flag", width = 15)
    @ApiModelProperty(value = "苯Flag")
    private java.lang.String a25002Flag;
	/**苯Zsmin*/
	@Excel(name = "苯Zsmin", width = 15)
    @ApiModelProperty(value = "苯Zsmin")
    private java.lang.Double a25002Zsmin;
	/**苯ZsminState*/
	@Excel(name = "苯ZsminState", width = 15)
    @ApiModelProperty(value = "苯ZsminState")
    private java.lang.Integer a25002ZsminState;
	/**苯Zsmax*/
	@Excel(name = "苯Zsmax", width = 15)
    @ApiModelProperty(value = "苯Zsmax")
    private java.lang.Double a25002Zsmax;
	/**苯ZsmaxState*/
	@Excel(name = "苯ZsmaxState", width = 15)
    @ApiModelProperty(value = "苯ZsmaxState")
    private java.lang.Integer a25002ZsmaxState;
	/**苯Zsavg*/
	@Excel(name = "苯Zsavg", width = 15)
    @ApiModelProperty(value = "苯Zsavg")
    private java.lang.Double a25002Zsavg;
	/**苯ZsavgState*/
	@Excel(name = "苯ZsavgState", width = 15)
    @ApiModelProperty(value = "苯ZsavgState")
    private java.lang.Integer a25002ZsavgState;
	/**苯Zscou*/
	@Excel(name = "苯Zscou", width = 15)
    @ApiModelProperty(value = "苯Zscou")
    private java.lang.Double a25002Zscou;
	/**苯ZscouState*/
	@Excel(name = "苯ZscouState", width = 15)
    @ApiModelProperty(value = "苯ZscouState")
    private java.lang.Integer a25002ZscouState;
	/**废气Min*/
	@Excel(name = "废气Min", width = 15)
    @ApiModelProperty(value = "废气Min")
    private java.lang.Double a00000Min;
	/**废气MinState*/
	@Excel(name = "废气MinState", width = 15)
    @ApiModelProperty(value = "废气MinState")
    private java.lang.Integer a00000MinState;
	/**废气Max*/
	@Excel(name = "废气Max", width = 15)
    @ApiModelProperty(value = "废气Max")
    private java.lang.Double a00000Max;
	/**废气MaxState*/
	@Excel(name = "废气MaxState", width = 15)
    @ApiModelProperty(value = "废气MaxState")
    private java.lang.Integer a00000MaxState;
	/**废气Avg*/
	@Excel(name = "废气Avg", width = 15)
    @ApiModelProperty(value = "废气Avg")
    private java.lang.Double a00000Avg;
	/**废气AvgState*/
	@Excel(name = "废气AvgState", width = 15)
    @ApiModelProperty(value = "废气AvgState")
    private java.lang.Integer a00000AvgState;
	/**废气Cou*/
	@Excel(name = "废气Cou", width = 15)
    @ApiModelProperty(value = "废气Cou")
    private java.lang.Double a00000Cou;
	/**废气CouState*/
	@Excel(name = "废气CouState", width = 15)
    @ApiModelProperty(value = "废气CouState")
    private java.lang.Integer a00000CouState;
	/**废气Flag*/
	@Excel(name = "废气Flag", width = 15)
    @ApiModelProperty(value = "废气Flag")
    private java.lang.String a00000Flag;
	/**废气Zsmin*/
	@Excel(name = "废气Zsmin", width = 15)
    @ApiModelProperty(value = "废气Zsmin")
    private java.lang.Double a00000Zsmin;
	/**废气ZsminState*/
	@Excel(name = "废气ZsminState", width = 15)
    @ApiModelProperty(value = "废气ZsminState")
    private java.lang.Integer a00000ZsminState;
	/**废气Zsmax*/
	@Excel(name = "废气Zsmax", width = 15)
    @ApiModelProperty(value = "废气Zsmax")
    private java.lang.Double a00000Zsmax;
	/**废气ZsmaxState*/
	@Excel(name = "废气ZsmaxState", width = 15)
    @ApiModelProperty(value = "废气ZsmaxState")
    private java.lang.Integer a00000ZsmaxState;
	/**废气Zsavg*/
	@Excel(name = "废气Zsavg", width = 15)
    @ApiModelProperty(value = "废气Zsavg")
    private java.lang.Double a00000Zsavg;
	/**废气ZsavgState*/
	@Excel(name = "废气ZsavgState", width = 15)
    @ApiModelProperty(value = "废气ZsavgState")
    private java.lang.Integer a00000ZsavgState;
	/**废气Zscou*/
	@Excel(name = "废气Zscou", width = 15)
    @ApiModelProperty(value = "废气Zscou")
    private java.lang.Double a00000Zscou;
	/**废气ZscouState*/
	@Excel(name = "废气ZscouState", width = 15)
    @ApiModelProperty(value = "废气ZscouState")
    private java.lang.Integer a00000ZscouState;
	/**烟尘Min*/
	@Excel(name = "烟尘Min", width = 15)
    @ApiModelProperty(value = "烟尘Min")
    private java.lang.Double a34013Min;
	/**烟尘MinState*/
	@Excel(name = "烟尘MinState", width = 15)
    @ApiModelProperty(value = "烟尘MinState")
    private java.lang.Integer a34013MinState;
	/**烟尘Max*/
	@Excel(name = "烟尘Max", width = 15)
    @ApiModelProperty(value = "烟尘Max")
    private java.lang.Double a34013Max;
	/**烟尘MaxState*/
	@Excel(name = "烟尘MaxState", width = 15)
    @ApiModelProperty(value = "烟尘MaxState")
    private java.lang.Integer a34013MaxState;
	/**烟尘Avg*/
	@Excel(name = "烟尘Avg", width = 15)
    @ApiModelProperty(value = "烟尘Avg")
    private java.lang.Double a34013Avg;
	/**烟尘AvgState*/
	@Excel(name = "烟尘AvgState", width = 15)
    @ApiModelProperty(value = "烟尘AvgState")
    private java.lang.Integer a34013AvgState;
	/**烟尘Cou*/
	@Excel(name = "烟尘Cou", width = 15)
    @ApiModelProperty(value = "烟尘Cou")
    private java.lang.Double a34013Cou;
	/**烟尘CouState*/
	@Excel(name = "烟尘CouState", width = 15)
    @ApiModelProperty(value = "烟尘CouState")
    private java.lang.Integer a34013CouState;
	/**烟尘Flag*/
	@Excel(name = "烟尘Flag", width = 15)
    @ApiModelProperty(value = "烟尘Flag")
    private java.lang.String a34013Flag;
	/**烟尘Zsmin*/
	@Excel(name = "烟尘Zsmin", width = 15)
    @ApiModelProperty(value = "烟尘Zsmin")
    private java.lang.Double a34013Zsmin;
	/**烟尘ZsminState*/
	@Excel(name = "烟尘ZsminState", width = 15)
    @ApiModelProperty(value = "烟尘ZsminState")
    private java.lang.Integer a34013ZsminState;
	/**烟尘Zsmax*/
	@Excel(name = "烟尘Zsmax", width = 15)
    @ApiModelProperty(value = "烟尘Zsmax")
    private java.lang.Double a34013Zsmax;
	/**烟尘ZsmaxState*/
	@Excel(name = "烟尘ZsmaxState", width = 15)
    @ApiModelProperty(value = "烟尘ZsmaxState")
    private java.lang.Integer a34013ZsmaxState;
	/**烟尘Zsavg*/
	@Excel(name = "烟尘Zsavg", width = 15)
    @ApiModelProperty(value = "烟尘Zsavg")
    private java.lang.Double a34013Zsavg;
	/**烟尘ZsavgState*/
	@Excel(name = "烟尘ZsavgState", width = 15)
    @ApiModelProperty(value = "烟尘ZsavgState")
    private java.lang.Integer a34013ZsavgState;
	/**烟尘Zscou*/
	@Excel(name = "烟尘Zscou", width = 15)
    @ApiModelProperty(value = "烟尘Zscou")
    private java.lang.Double a34013Zscou;
	/**烟尘ZscouState*/
	@Excel(name = "烟尘ZscouState", width = 15)
    @ApiModelProperty(value = "烟尘ZscouState")
    private java.lang.Integer a34013ZscouState;
	/**烟气流速Min*/
	@Excel(name = "烟气流速Min", width = 15)
    @ApiModelProperty(value = "烟气流速Min")
    private java.lang.Double a01011Min;
	/**烟气流速MinState*/
	@Excel(name = "烟气流速MinState", width = 15)
    @ApiModelProperty(value = "烟气流速MinState")
    private java.lang.Integer a01011MinState;
	/**烟气流速Max*/
	@Excel(name = "烟气流速Max", width = 15)
    @ApiModelProperty(value = "烟气流速Max")
    private java.lang.Double a01011Max;
	/**烟气流速MaxState*/
	@Excel(name = "烟气流速MaxState", width = 15)
    @ApiModelProperty(value = "烟气流速MaxState")
    private java.lang.Integer a01011MaxState;
	/**烟气流速Avg*/
	@Excel(name = "烟气流速Avg", width = 15)
    @ApiModelProperty(value = "烟气流速Avg")
    private java.lang.Double a01011Avg;
	/**烟气流速AvgState*/
	@Excel(name = "烟气流速AvgState", width = 15)
    @ApiModelProperty(value = "烟气流速AvgState")
    private java.lang.Integer a01011AvgState;
	/**烟气流速Cou*/
	@Excel(name = "烟气流速Cou", width = 15)
    @ApiModelProperty(value = "烟气流速Cou")
    private java.lang.Double a01011Cou;
	/**烟气流速CouState*/
	@Excel(name = "烟气流速CouState", width = 15)
    @ApiModelProperty(value = "烟气流速CouState")
    private java.lang.Integer a01011CouState;
	/**烟气流速Flag*/
	@Excel(name = "烟气流速Flag", width = 15)
    @ApiModelProperty(value = "烟气流速Flag")
    private java.lang.String a01011Flag;
	/**烟气流速Zsmin*/
	@Excel(name = "烟气流速Zsmin", width = 15)
    @ApiModelProperty(value = "烟气流速Zsmin")
    private java.lang.Double a01011Zsmin;
	/**烟气流速ZsminState*/
	@Excel(name = "烟气流速ZsminState", width = 15)
    @ApiModelProperty(value = "烟气流速ZsminState")
    private java.lang.Integer a01011ZsminState;
	/**烟气流速Zsmax*/
	@Excel(name = "烟气流速Zsmax", width = 15)
    @ApiModelProperty(value = "烟气流速Zsmax")
    private java.lang.Double a01011Zsmax;
	/**烟气流速ZsmaxState*/
	@Excel(name = "烟气流速ZsmaxState", width = 15)
    @ApiModelProperty(value = "烟气流速ZsmaxState")
    private java.lang.Integer a01011ZsmaxState;
	/**烟气流速Zsavg*/
	@Excel(name = "烟气流速Zsavg", width = 15)
    @ApiModelProperty(value = "烟气流速Zsavg")
    private java.lang.Double a01011Zsavg;
	/**烟气流速ZsavgState*/
	@Excel(name = "烟气流速ZsavgState", width = 15)
    @ApiModelProperty(value = "烟气流速ZsavgState")
    private java.lang.Integer a01011ZsavgState;
	/**烟气流速Zscou*/
	@Excel(name = "烟气流速Zscou", width = 15)
    @ApiModelProperty(value = "烟气流速Zscou")
    private java.lang.Double a01011Zscou;
	/**烟气流速ZscouState*/
	@Excel(name = "烟气流速ZscouState", width = 15)
    @ApiModelProperty(value = "烟气流速ZscouState")
    private java.lang.Integer a01011ZscouState;
	/**烟气温度Min*/
	@Excel(name = "烟气温度Min", width = 15)
    @ApiModelProperty(value = "烟气温度Min")
    private java.lang.Double a01012Min;
	/**烟气温度MinState*/
	@Excel(name = "烟气温度MinState", width = 15)
    @ApiModelProperty(value = "烟气温度MinState")
    private java.lang.Integer a01012MinState;
	/**烟气温度Max*/
	@Excel(name = "烟气温度Max", width = 15)
    @ApiModelProperty(value = "烟气温度Max")
    private java.lang.Double a01012Max;
	/**烟气温度MaxState*/
	@Excel(name = "烟气温度MaxState", width = 15)
    @ApiModelProperty(value = "烟气温度MaxState")
    private java.lang.Integer a01012MaxState;
	/**烟气温度Avg*/
	@Excel(name = "烟气温度Avg", width = 15)
    @ApiModelProperty(value = "烟气温度Avg")
    private java.lang.Double a01012Avg;
	/**烟气温度AvgState*/
	@Excel(name = "烟气温度AvgState", width = 15)
    @ApiModelProperty(value = "烟气温度AvgState")
    private java.lang.Integer a01012AvgState;
	/**烟气温度Cou*/
	@Excel(name = "烟气温度Cou", width = 15)
    @ApiModelProperty(value = "烟气温度Cou")
    private java.lang.Double a01012Cou;
	/**烟气温度CouState*/
	@Excel(name = "烟气温度CouState", width = 15)
    @ApiModelProperty(value = "烟气温度CouState")
    private java.lang.Integer a01012CouState;
	/**烟气温度Flag*/
	@Excel(name = "烟气温度Flag", width = 15)
    @ApiModelProperty(value = "烟气温度Flag")
    private java.lang.String a01012Flag;
	/**烟气温度Zsmin*/
	@Excel(name = "烟气温度Zsmin", width = 15)
    @ApiModelProperty(value = "烟气温度Zsmin")
    private java.lang.Double a01012Zsmin;
	/**烟气温度ZsminState*/
	@Excel(name = "烟气温度ZsminState", width = 15)
    @ApiModelProperty(value = "烟气温度ZsminState")
    private java.lang.Integer a01012ZsminState;
	/**烟气温度Zsmax*/
	@Excel(name = "烟气温度Zsmax", width = 15)
    @ApiModelProperty(value = "烟气温度Zsmax")
    private java.lang.Double a01012Zsmax;
	/**烟气温度ZsmaxState*/
	@Excel(name = "烟气温度ZsmaxState", width = 15)
    @ApiModelProperty(value = "烟气温度ZsmaxState")
    private java.lang.Integer a01012ZsmaxState;
	/**烟气温度Zsavg*/
	@Excel(name = "烟气温度Zsavg", width = 15)
    @ApiModelProperty(value = "烟气温度Zsavg")
    private java.lang.Double a01012Zsavg;
	/**烟气温度ZsavgState*/
	@Excel(name = "烟气温度ZsavgState", width = 15)
    @ApiModelProperty(value = "烟气温度ZsavgState")
    private java.lang.Integer a01012ZsavgState;
	/**烟气温度Zscou*/
	@Excel(name = "烟气温度Zscou", width = 15)
    @ApiModelProperty(value = "烟气温度Zscou")
    private java.lang.Double a01012Zscou;
	/**烟气温度ZscouState*/
	@Excel(name = "烟气温度ZscouState", width = 15)
    @ApiModelProperty(value = "烟气温度ZscouState")
    private java.lang.Integer a01012ZscouState;
	/**非甲烷总烃Min*/
	@Excel(name = "非甲烷总烃Min", width = 15)
    @ApiModelProperty(value = "非甲烷总烃Min")
    private java.lang.Double a24088Min;
	/**非甲烷总烃MinState*/
	@Excel(name = "非甲烷总烃MinState", width = 15)
    @ApiModelProperty(value = "非甲烷总烃MinState")
    private java.lang.Integer a24088MinState;
	/**非甲烷总烃Max*/
	@Excel(name = "非甲烷总烃Max", width = 15)
    @ApiModelProperty(value = "非甲烷总烃Max")
    private java.lang.Double a24088Max;
	/**非甲烷总烃MaxState*/
	@Excel(name = "非甲烷总烃MaxState", width = 15)
    @ApiModelProperty(value = "非甲烷总烃MaxState")
    private java.lang.Integer a24088MaxState;
	/**非甲烷总烃Avg*/
	@Excel(name = "非甲烷总烃Avg", width = 15)
    @ApiModelProperty(value = "非甲烷总烃Avg")
    private java.lang.Double a24088Avg;
	/**非甲烷总烃AvgState*/
	@Excel(name = "非甲烷总烃AvgState", width = 15)
    @ApiModelProperty(value = "非甲烷总烃AvgState")
    private java.lang.Integer a24088AvgState;
	/**非甲烷总烃Cou*/
	@Excel(name = "非甲烷总烃Cou", width = 15)
    @ApiModelProperty(value = "非甲烷总烃Cou")
    private java.lang.Double a24088Cou;
	/**非甲烷总烃CouState*/
	@Excel(name = "非甲烷总烃CouState", width = 15)
    @ApiModelProperty(value = "非甲烷总烃CouState")
    private java.lang.Integer a24088CouState;
	/**非甲烷总烃Flag*/
	@Excel(name = "非甲烷总烃Flag", width = 15)
    @ApiModelProperty(value = "非甲烷总烃Flag")
    private java.lang.String a24088Flag;
	/**非甲烷总烃Zsmin*/
	@Excel(name = "非甲烷总烃Zsmin", width = 15)
    @ApiModelProperty(value = "非甲烷总烃Zsmin")
    private java.lang.Double a24088Zsmin;
	/**非甲烷总烃ZsminState*/
	@Excel(name = "非甲烷总烃ZsminState", width = 15)
    @ApiModelProperty(value = "非甲烷总烃ZsminState")
    private java.lang.Integer a24088ZsminState;
	/**非甲烷总烃Zsmax*/
	@Excel(name = "非甲烷总烃Zsmax", width = 15)
    @ApiModelProperty(value = "非甲烷总烃Zsmax")
    private java.lang.Double a24088Zsmax;
	/**非甲烷总烃ZsmaxState*/
	@Excel(name = "非甲烷总烃ZsmaxState", width = 15)
    @ApiModelProperty(value = "非甲烷总烃ZsmaxState")
    private java.lang.Integer a24088ZsmaxState;
	/**非甲烷总烃Zsavg*/
	@Excel(name = "非甲烷总烃Zsavg", width = 15)
    @ApiModelProperty(value = "非甲烷总烃Zsavg")
    private java.lang.Double a24088Zsavg;
	/**非甲烷总烃ZsavgState*/
	@Excel(name = "非甲烷总烃ZsavgState", width = 15)
    @ApiModelProperty(value = "非甲烷总烃ZsavgState")
    private java.lang.Integer a24088ZsavgState;
	/**非甲烷总烃Zscou*/
	@Excel(name = "非甲烷总烃Zscou", width = 15)
    @ApiModelProperty(value = "非甲烷总烃Zscou")
    private java.lang.Double a24088Zscou;
	/**非甲烷总烃ZscouState*/
	@Excel(name = "非甲烷总烃ZscouState", width = 15)
    @ApiModelProperty(value = "非甲烷总烃ZscouState")
    private java.lang.Integer a24088ZscouState;
	/**甲苯Min*/
	@Excel(name = "甲苯Min", width = 15)
    @ApiModelProperty(value = "甲苯Min")
    private java.lang.Double a25003Min;
	/**甲苯MinState*/
	@Excel(name = "甲苯MinState", width = 15)
    @ApiModelProperty(value = "甲苯MinState")
    private java.lang.Integer a25003MinState;
	/**甲苯Max*/
	@Excel(name = "甲苯Max", width = 15)
    @ApiModelProperty(value = "甲苯Max")
    private java.lang.Double a25003Max;
	/**甲苯MaxState*/
	@Excel(name = "甲苯MaxState", width = 15)
    @ApiModelProperty(value = "甲苯MaxState")
    private java.lang.Integer a25003MaxState;
	/**甲苯Avg*/
	@Excel(name = "甲苯Avg", width = 15)
    @ApiModelProperty(value = "甲苯Avg")
    private java.lang.Double a25003Avg;
	/**甲苯AvgState*/
	@Excel(name = "甲苯AvgState", width = 15)
    @ApiModelProperty(value = "甲苯AvgState")
    private java.lang.Integer a25003AvgState;
	/**甲苯Cou*/
	@Excel(name = "甲苯Cou", width = 15)
    @ApiModelProperty(value = "甲苯Cou")
    private java.lang.Double a25003Cou;
	/**甲苯CouState*/
	@Excel(name = "甲苯CouState", width = 15)
    @ApiModelProperty(value = "甲苯CouState")
    private java.lang.Integer a25003CouState;
	/**甲苯Flag*/
	@Excel(name = "甲苯Flag", width = 15)
    @ApiModelProperty(value = "甲苯Flag")
    private java.lang.String a25003Flag;
	/**甲苯Zsmin*/
	@Excel(name = "甲苯Zsmin", width = 15)
    @ApiModelProperty(value = "甲苯Zsmin")
    private java.lang.Double a25003Zsmin;
	/**甲苯ZsminState*/
	@Excel(name = "甲苯ZsminState", width = 15)
    @ApiModelProperty(value = "甲苯ZsminState")
    private java.lang.Integer a25003ZsminState;
	/**甲苯Zsmax*/
	@Excel(name = "甲苯Zsmax", width = 15)
    @ApiModelProperty(value = "甲苯Zsmax")
    private java.lang.Double a25003Zsmax;
	/**甲苯ZsmaxState*/
	@Excel(name = "甲苯ZsmaxState", width = 15)
    @ApiModelProperty(value = "甲苯ZsmaxState")
    private java.lang.Integer a25003ZsmaxState;
	/**甲苯Zsavg*/
	@Excel(name = "甲苯Zsavg", width = 15)
    @ApiModelProperty(value = "甲苯Zsavg")
    private java.lang.Double a25003Zsavg;
	/**甲苯ZsavgState*/
	@Excel(name = "甲苯ZsavgState", width = 15)
    @ApiModelProperty(value = "甲苯ZsavgState")
    private java.lang.Integer a25003ZsavgState;
	/**甲苯Zscou*/
	@Excel(name = "甲苯Zscou", width = 15)
    @ApiModelProperty(value = "甲苯Zscou")
    private java.lang.Double a25003Zscou;
	/**甲苯ZscouState*/
	@Excel(name = "甲苯ZscouState", width = 15)
    @ApiModelProperty(value = "甲苯ZscouState")
    private java.lang.Integer a25003ZscouState;
	/**烟气动压Min*/
	@Excel(name = "烟气动压Min", width = 15)
    @ApiModelProperty(value = "烟气动压Min")
    private java.lang.Double a01017Min;
	/**烟气动压MinState*/
	@Excel(name = "烟气动压MinState", width = 15)
    @ApiModelProperty(value = "烟气动压MinState")
    private java.lang.Integer a01017MinState;
	/**烟气动压Max*/
	@Excel(name = "烟气动压Max", width = 15)
    @ApiModelProperty(value = "烟气动压Max")
    private java.lang.Double a01017Max;
	/**烟气动压MaxState*/
	@Excel(name = "烟气动压MaxState", width = 15)
    @ApiModelProperty(value = "烟气动压MaxState")
    private java.lang.Integer a01017MaxState;
	/**烟气动压Avg*/
	@Excel(name = "烟气动压Avg", width = 15)
    @ApiModelProperty(value = "烟气动压Avg")
    private java.lang.Double a01017Avg;
	/**烟气动压AvgState*/
	@Excel(name = "烟气动压AvgState", width = 15)
    @ApiModelProperty(value = "烟气动压AvgState")
    private java.lang.Integer a01017AvgState;
	/**烟气动压Cou*/
	@Excel(name = "烟气动压Cou", width = 15)
    @ApiModelProperty(value = "烟气动压Cou")
    private java.lang.Double a01017Cou;
	/**烟气动压CouState*/
	@Excel(name = "烟气动压CouState", width = 15)
    @ApiModelProperty(value = "烟气动压CouState")
    private java.lang.Integer a01017CouState;
	/**烟气动压Flag*/
	@Excel(name = "烟气动压Flag", width = 15)
    @ApiModelProperty(value = "烟气动压Flag")
    private java.lang.String a01017Flag;
	/**烟气动压Zsmin*/
	@Excel(name = "烟气动压Zsmin", width = 15)
    @ApiModelProperty(value = "烟气动压Zsmin")
    private java.lang.Double a01017Zsmin;
	/**烟气动压ZsminState*/
	@Excel(name = "烟气动压ZsminState", width = 15)
    @ApiModelProperty(value = "烟气动压ZsminState")
    private java.lang.Integer a01017ZsminState;
	/**烟气动压Zsmax*/
	@Excel(name = "烟气动压Zsmax", width = 15)
    @ApiModelProperty(value = "烟气动压Zsmax")
    private java.lang.Double a01017Zsmax;
	/**烟气动压ZsmaxState*/
	@Excel(name = "烟气动压ZsmaxState", width = 15)
    @ApiModelProperty(value = "烟气动压ZsmaxState")
    private java.lang.Integer a01017ZsmaxState;
	/**烟气动压Zsavg*/
	@Excel(name = "烟气动压Zsavg", width = 15)
    @ApiModelProperty(value = "烟气动压Zsavg")
    private java.lang.Double a01017Zsavg;
	/**烟气动压ZsavgState*/
	@Excel(name = "烟气动压ZsavgState", width = 15)
    @ApiModelProperty(value = "烟气动压ZsavgState")
    private java.lang.Integer a01017ZsavgState;
	/**烟气动压Zscou*/
	@Excel(name = "烟气动压Zscou", width = 15)
    @ApiModelProperty(value = "烟气动压Zscou")
    private java.lang.Double a01017Zscou;
	/**烟气动压ZscouState*/
	@Excel(name = "烟气动压ZscouState", width = 15)
    @ApiModelProperty(value = "烟气动压ZscouState")
    private java.lang.Integer a01017ZscouState;
	/**NOxMin*/
	@Excel(name = "NOxMin", width = 15)
    @ApiModelProperty(value = "NOxMin")
    private java.lang.Double a21002Min;
	/**NOxMinState*/
	@Excel(name = "NOxMinState", width = 15)
    @ApiModelProperty(value = "NOxMinState")
    private java.lang.Integer a21002MinState;
	/**NOxMax*/
	@Excel(name = "NOxMax", width = 15)
    @ApiModelProperty(value = "NOxMax")
    private java.lang.Double a21002Max;
	/**NOxMaxState*/
	@Excel(name = "NOxMaxState", width = 15)
    @ApiModelProperty(value = "NOxMaxState")
    private java.lang.Integer a21002MaxState;
	/**NOxAvg*/
	@Excel(name = "NOxAvg", width = 15)
    @ApiModelProperty(value = "NOxAvg")
    private java.lang.Double a21002Avg;
	/**NOxAvgState*/
	@Excel(name = "NOxAvgState", width = 15)
    @ApiModelProperty(value = "NOxAvgState")
    private java.lang.Integer a21002AvgState;
	/**NOxCou*/
	@Excel(name = "NOxCou", width = 15)
    @ApiModelProperty(value = "NOxCou")
    private java.lang.Double a21002Cou;
	/**NOxCouState*/
	@Excel(name = "NOxCouState", width = 15)
    @ApiModelProperty(value = "NOxCouState")
    private java.lang.Integer a21002CouState;
	/**NOxFlag*/
	@Excel(name = "NOxFlag", width = 15)
    @ApiModelProperty(value = "NOxFlag")
    private java.lang.String a21002Flag;
	/**NOxZsmin*/
	@Excel(name = "NOxZsmin", width = 15)
    @ApiModelProperty(value = "NOxZsmin")
    private java.lang.Double a21002Zsmin;
	/**NOxZsminState*/
	@Excel(name = "NOxZsminState", width = 15)
    @ApiModelProperty(value = "NOxZsminState")
    private java.lang.Integer a21002ZsminState;
	/**NOxZsmax*/
	@Excel(name = "NOxZsmax", width = 15)
    @ApiModelProperty(value = "NOxZsmax")
    private java.lang.Double a21002Zsmax;
	/**NOxZsmaxState*/
	@Excel(name = "NOxZsmaxState", width = 15)
    @ApiModelProperty(value = "NOxZsmaxState")
    private java.lang.Integer a21002ZsmaxState;
	/**NOxZsavg*/
	@Excel(name = "NOxZsavg", width = 15)
    @ApiModelProperty(value = "NOxZsavg")
    private java.lang.Double a21002Zsavg;
	/**NOxZsavgState*/
	@Excel(name = "NOxZsavgState", width = 15)
    @ApiModelProperty(value = "NOxZsavgState")
    private java.lang.Integer a21002ZsavgState;
	/**NOxZscou*/
	@Excel(name = "NOxZscou", width = 15)
    @ApiModelProperty(value = "NOxZscou")
    private java.lang.Double a21002Zscou;
	/**NOxZscouState*/
	@Excel(name = "NOxZscouState", width = 15)
    @ApiModelProperty(value = "NOxZscouState")
    private java.lang.Integer a21002ZscouState;
	/**SO2Min*/
	@Excel(name = "SO2Min", width = 15)
    @ApiModelProperty(value = "SO2Min")
    private java.lang.Double a21026Min;
	/**SO2MinState*/
	@Excel(name = "SO2MinState", width = 15)
    @ApiModelProperty(value = "SO2MinState")
    private java.lang.Integer a21026MinState;
	/**SO2Max*/
	@Excel(name = "SO2Max", width = 15)
    @ApiModelProperty(value = "SO2Max")
    private java.lang.Double a21026Max;
	/**SO2MaxState*/
	@Excel(name = "SO2MaxState", width = 15)
    @ApiModelProperty(value = "SO2MaxState")
    private java.lang.Integer a21026MaxState;
	/**SO2Avg*/
	@Excel(name = "SO2Avg", width = 15)
    @ApiModelProperty(value = "SO2Avg")
    private java.lang.Double a21026Avg;
	/**SO2AvgState*/
	@Excel(name = "SO2AvgState", width = 15)
    @ApiModelProperty(value = "SO2AvgState")
    private java.lang.Integer a21026AvgState;
	/**SO2Cou*/
	@Excel(name = "SO2Cou", width = 15)
    @ApiModelProperty(value = "SO2Cou")
    private java.lang.Double a21026Cou;
	/**SO2CouState*/
	@Excel(name = "SO2CouState", width = 15)
    @ApiModelProperty(value = "SO2CouState")
    private java.lang.Integer a21026CouState;
	/**SO2Flag*/
	@Excel(name = "SO2Flag", width = 15)
    @ApiModelProperty(value = "SO2Flag")
    private java.lang.String a21026Flag;
	/**SO2Zsmin*/
	@Excel(name = "SO2Zsmin", width = 15)
    @ApiModelProperty(value = "SO2Zsmin")
    private java.lang.Double a21026Zsmin;
	/**SO2ZsminState*/
	@Excel(name = "SO2ZsminState", width = 15)
    @ApiModelProperty(value = "SO2ZsminState")
    private java.lang.Integer a21026ZsminState;
	/**SO2Zsmax*/
	@Excel(name = "SO2Zsmax", width = 15)
    @ApiModelProperty(value = "SO2Zsmax")
    private java.lang.Double a21026Zsmax;
	/**SO2ZsmaxState*/
	@Excel(name = "SO2ZsmaxState", width = 15)
    @ApiModelProperty(value = "SO2ZsmaxState")
    private java.lang.Integer a21026ZsmaxState;
	/**SO2Zsavg*/
	@Excel(name = "SO2Zsavg", width = 15)
    @ApiModelProperty(value = "SO2Zsavg")
    private java.lang.Double a21026Zsavg;
	/**SO2ZsavgState*/
	@Excel(name = "SO2ZsavgState", width = 15)
    @ApiModelProperty(value = "SO2ZsavgState")
    private java.lang.Integer a21026ZsavgState;
	/**SO2Zscou*/
	@Excel(name = "SO2Zscou", width = 15)
    @ApiModelProperty(value = "SO2Zscou")
    private java.lang.Double a21026Zscou;
	/**SO2ZscouState*/
	@Excel(name = "SO2ZscouState", width = 15)
    @ApiModelProperty(value = "SO2ZscouState")
    private java.lang.Integer a21026ZscouState;
	/**COMin*/
	@Excel(name = "COMin", width = 15)
    @ApiModelProperty(value = "COMin")
    private java.lang.Double a21005Min;
	/**COMinState*/
	@Excel(name = "COMinState", width = 15)
    @ApiModelProperty(value = "COMinState")
    private java.lang.Integer a21005MinState;
	/**COMax*/
	@Excel(name = "COMax", width = 15)
    @ApiModelProperty(value = "COMax")
    private java.lang.Double a21005Max;
	/**COMaxState*/
	@Excel(name = "COMaxState", width = 15)
    @ApiModelProperty(value = "COMaxState")
    private java.lang.Integer a21005MaxState;
	/**COAvg*/
	@Excel(name = "COAvg", width = 15)
    @ApiModelProperty(value = "COAvg")
    private java.lang.Double a21005Avg;
	/**COAvgState*/
	@Excel(name = "COAvgState", width = 15)
    @ApiModelProperty(value = "COAvgState")
    private java.lang.Integer a21005AvgState;
	/**COCou*/
	@Excel(name = "COCou", width = 15)
    @ApiModelProperty(value = "COCou")
    private java.lang.Double a21005Cou;
	/**COCouState*/
	@Excel(name = "COCouState", width = 15)
    @ApiModelProperty(value = "COCouState")
    private java.lang.Integer a21005CouState;
	/**COFlag*/
	@Excel(name = "COFlag", width = 15)
    @ApiModelProperty(value = "COFlag")
    private java.lang.String a21005Flag;
	/**COZsmin*/
	@Excel(name = "COZsmin", width = 15)
    @ApiModelProperty(value = "COZsmin")
    private java.lang.Double a21005Zsmin;
	/**COZsminState*/
	@Excel(name = "COZsminState", width = 15)
    @ApiModelProperty(value = "COZsminState")
    private java.lang.Integer a21005ZsminState;
	/**COZsmax*/
	@Excel(name = "COZsmax", width = 15)
    @ApiModelProperty(value = "COZsmax")
    private java.lang.Double a21005Zsmax;
	/**COZsmaxState*/
	@Excel(name = "COZsmaxState", width = 15)
    @ApiModelProperty(value = "COZsmaxState")
    private java.lang.Integer a21005ZsmaxState;
	/**COZsavg*/
	@Excel(name = "COZsavg", width = 15)
    @ApiModelProperty(value = "COZsavg")
    private java.lang.Double a21005Zsavg;
	/**COZsavgState*/
	@Excel(name = "COZsavgState", width = 15)
    @ApiModelProperty(value = "COZsavgState")
    private java.lang.Integer a21005ZsavgState;
	/**COZscou*/
	@Excel(name = "COZscou", width = 15)
    @ApiModelProperty(value = "COZscou")
    private java.lang.Double a21005Zscou;
	/**COZscouState*/
	@Excel(name = "COZscouState", width = 15)
    @ApiModelProperty(value = "COZscouState")
    private java.lang.Integer a21005ZscouState;
}
