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
 * @Description: voc_current_tr_2009
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
@Data
@TableName("voc_current_tr_2009")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="voc_current_tr_2009对象", description="voc_current_tr_2009")
public class VocCurrentTr_2009 implements Serializable {
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
	/**a25005Rtd*/
	@Excel(name = "a25005Rtd", width = 15)
    @ApiModelProperty(value = "a25005Rtd")
    private java.lang.Double a25005Rtd;
	/**a25005Flag*/
	@Excel(name = "a25005Flag", width = 15)
    @ApiModelProperty(value = "a25005Flag")
    private java.lang.String a25005Flag;
	/**a25005State*/
	@Excel(name = "a25005State", width = 15)
    @ApiModelProperty(value = "a25005State")
    private java.lang.Integer a25005State;
	/**a25005Zsrtd*/
	@Excel(name = "a25005Zsrtd", width = 15)
    @ApiModelProperty(value = "a25005Zsrtd")
    private java.lang.Double a25005Zsrtd;
	/**a25005Zsstate*/
	@Excel(name = "a25005Zsstate", width = 15)
    @ApiModelProperty(value = "a25005Zsstate")
    private java.lang.Integer a25005Zsstate;
	/**a25002Rtd*/
	@Excel(name = "a25002Rtd", width = 15)
    @ApiModelProperty(value = "a25002Rtd")
    private java.lang.Double a25002Rtd;
	/**a25002Flag*/
	@Excel(name = "a25002Flag", width = 15)
    @ApiModelProperty(value = "a25002Flag")
    private java.lang.String a25002Flag;
	/**a25002State*/
	@Excel(name = "a25002State", width = 15)
    @ApiModelProperty(value = "a25002State")
    private java.lang.Integer a25002State;
	/**a25002Zsrtd*/
	@Excel(name = "a25002Zsrtd", width = 15)
    @ApiModelProperty(value = "a25002Zsrtd")
    private java.lang.Double a25002Zsrtd;
	/**a25002Zsstate*/
	@Excel(name = "a25002Zsstate", width = 15)
    @ApiModelProperty(value = "a25002Zsstate")
    private java.lang.Integer a25002Zsstate;
	/**a00000Rtd*/
	@Excel(name = "a00000Rtd", width = 15)
    @ApiModelProperty(value = "a00000Rtd")
    private java.lang.Double a00000Rtd;
	/**a00000Flag*/
	@Excel(name = "a00000Flag", width = 15)
    @ApiModelProperty(value = "a00000Flag")
    private java.lang.String a00000Flag;
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
	/**a34013Rtd*/
	@Excel(name = "a34013Rtd", width = 15)
    @ApiModelProperty(value = "a34013Rtd")
    private java.lang.Double a34013Rtd;
	/**a34013Flag*/
	@Excel(name = "a34013Flag", width = 15)
    @ApiModelProperty(value = "a34013Flag")
    private java.lang.String a34013Flag;
	/**a34013State*/
	@Excel(name = "a34013State", width = 15)
    @ApiModelProperty(value = "a34013State")
    private java.lang.Integer a34013State;
	/**a34013Zsrtd*/
	@Excel(name = "a34013Zsrtd", width = 15)
    @ApiModelProperty(value = "a34013Zsrtd")
    private java.lang.Double a34013Zsrtd;
	/**a34013Zsstate*/
	@Excel(name = "a34013Zsstate", width = 15)
    @ApiModelProperty(value = "a34013Zsstate")
    private java.lang.Integer a34013Zsstate;
	/**a01011Rtd*/
	@Excel(name = "a01011Rtd", width = 15)
    @ApiModelProperty(value = "a01011Rtd")
    private java.lang.Double a01011Rtd;
	/**a01011Flag*/
	@Excel(name = "a01011Flag", width = 15)
    @ApiModelProperty(value = "a01011Flag")
    private java.lang.String a01011Flag;
	/**a01011State*/
	@Excel(name = "a01011State", width = 15)
    @ApiModelProperty(value = "a01011State")
    private java.lang.Integer a01011State;
	/**a01011Zsrtd*/
	@Excel(name = "a01011Zsrtd", width = 15)
    @ApiModelProperty(value = "a01011Zsrtd")
    private java.lang.Double a01011Zsrtd;
	/**a01011Zsstate*/
	@Excel(name = "a01011Zsstate", width = 15)
    @ApiModelProperty(value = "a01011Zsstate")
    private java.lang.Integer a01011Zsstate;
	/**a01012Rtd*/
	@Excel(name = "a01012Rtd", width = 15)
    @ApiModelProperty(value = "a01012Rtd")
    private java.lang.Double a01012Rtd;
	/**a01012Flag*/
	@Excel(name = "a01012Flag", width = 15)
    @ApiModelProperty(value = "a01012Flag")
    private java.lang.String a01012Flag;
	/**a01012State*/
	@Excel(name = "a01012State", width = 15)
    @ApiModelProperty(value = "a01012State")
    private java.lang.Integer a01012State;
	/**a01012Zsrtd*/
	@Excel(name = "a01012Zsrtd", width = 15)
    @ApiModelProperty(value = "a01012Zsrtd")
    private java.lang.Double a01012Zsrtd;
	/**a01012Zsstate*/
	@Excel(name = "a01012Zsstate", width = 15)
    @ApiModelProperty(value = "a01012Zsstate")
    private java.lang.Integer a01012Zsstate;
	/**a24088Rtd*/
	@Excel(name = "a24088Rtd", width = 15)
    @ApiModelProperty(value = "a24088Rtd")
    private java.lang.Double a24088Rtd;
	/**a24088Flag*/
	@Excel(name = "a24088Flag", width = 15)
    @ApiModelProperty(value = "a24088Flag")
    private java.lang.String a24088Flag;
	/**a24088State*/
	@Excel(name = "a24088State", width = 15)
    @ApiModelProperty(value = "a24088State")
    private java.lang.Integer a24088State;
	/**a24088Zsrtd*/
	@Excel(name = "a24088Zsrtd", width = 15)
    @ApiModelProperty(value = "a24088Zsrtd")
    private java.lang.Double a24088Zsrtd;
	/**a24088Zsstate*/
	@Excel(name = "a24088Zsstate", width = 15)
    @ApiModelProperty(value = "a24088Zsstate")
    private java.lang.Integer a24088Zsstate;
	/**a25003Rtd*/
	@Excel(name = "a25003Rtd", width = 15)
    @ApiModelProperty(value = "a25003Rtd")
    private java.lang.Double a25003Rtd;
	/**a25003Flag*/
	@Excel(name = "a25003Flag", width = 15)
    @ApiModelProperty(value = "a25003Flag")
    private java.lang.String a25003Flag;
	/**a25003State*/
	@Excel(name = "a25003State", width = 15)
    @ApiModelProperty(value = "a25003State")
    private java.lang.Integer a25003State;
	/**a25003Zsrtd*/
	@Excel(name = "a25003Zsrtd", width = 15)
    @ApiModelProperty(value = "a25003Zsrtd")
    private java.lang.Double a25003Zsrtd;
	/**a25003Zsstate*/
	@Excel(name = "a25003Zsstate", width = 15)
    @ApiModelProperty(value = "a25003Zsstate")
    private java.lang.Integer a25003Zsstate;
	/**a01017Rtd*/
	@Excel(name = "a01017Rtd", width = 15)
    @ApiModelProperty(value = "a01017Rtd")
    private java.lang.Double a01017Rtd;
	/**a01017Flag*/
	@Excel(name = "a01017Flag", width = 15)
    @ApiModelProperty(value = "a01017Flag")
    private java.lang.String a01017Flag;
	/**a01017State*/
	@Excel(name = "a01017State", width = 15)
    @ApiModelProperty(value = "a01017State")
    private java.lang.Integer a01017State;
	/**a01017Zsrtd*/
	@Excel(name = "a01017Zsrtd", width = 15)
    @ApiModelProperty(value = "a01017Zsrtd")
    private java.lang.Double a01017Zsrtd;
	/**a01017Zsstate*/
	@Excel(name = "a01017Zsstate", width = 15)
    @ApiModelProperty(value = "a01017Zsstate")
    private java.lang.Integer a01017Zsstate;
	/**a21002Rtd*/
	@Excel(name = "a21002Rtd", width = 15)
    @ApiModelProperty(value = "a21002Rtd")
    private java.lang.Double a21002Rtd;
	/**a21002Flag*/
	@Excel(name = "a21002Flag", width = 15)
    @ApiModelProperty(value = "a21002Flag")
    private java.lang.String a21002Flag;
	/**a21002State*/
	@Excel(name = "a21002State", width = 15)
    @ApiModelProperty(value = "a21002State")
    private java.lang.Integer a21002State;
	/**a21002Zsrtd*/
	@Excel(name = "a21002Zsrtd", width = 15)
    @ApiModelProperty(value = "a21002Zsrtd")
    private java.lang.Double a21002Zsrtd;
	/**a21002Zsstate*/
	@Excel(name = "a21002Zsstate", width = 15)
    @ApiModelProperty(value = "a21002Zsstate")
    private java.lang.Integer a21002Zsstate;
	/**a21026Rtd*/
	@Excel(name = "a21026Rtd", width = 15)
    @ApiModelProperty(value = "a21026Rtd")
    private java.lang.Double a21026Rtd;
	/**a21026Flag*/
	@Excel(name = "a21026Flag", width = 15)
    @ApiModelProperty(value = "a21026Flag")
    private java.lang.String a21026Flag;
	/**a21026State*/
	@Excel(name = "a21026State", width = 15)
    @ApiModelProperty(value = "a21026State")
    private java.lang.Integer a21026State;
	/**a21026Zsrtd*/
	@Excel(name = "a21026Zsrtd", width = 15)
    @ApiModelProperty(value = "a21026Zsrtd")
    private java.lang.Double a21026Zsrtd;
	/**a21026Zsstate*/
	@Excel(name = "a21026Zsstate", width = 15)
    @ApiModelProperty(value = "a21026Zsstate")
    private java.lang.Integer a21026Zsstate;
	/**a21005Rtd*/
	@Excel(name = "a21005Rtd", width = 15)
    @ApiModelProperty(value = "a21005Rtd")
    private java.lang.Double a21005Rtd;
	/**a21005Flag*/
	@Excel(name = "a21005Flag", width = 15)
    @ApiModelProperty(value = "a21005Flag")
    private java.lang.String a21005Flag;
	/**a21005State*/
	@Excel(name = "a21005State", width = 15)
    @ApiModelProperty(value = "a21005State")
    private java.lang.Integer a21005State;
	/**a21005Zsrtd*/
	@Excel(name = "a21005Zsrtd", width = 15)
    @ApiModelProperty(value = "a21005Zsrtd")
    private java.lang.Double a21005Zsrtd;
	/**a21005Zsstate*/
	@Excel(name = "a21005Zsstate", width = 15)
    @ApiModelProperty(value = "a21005Zsstate")
    private java.lang.Integer a21005Zsstate;
}
