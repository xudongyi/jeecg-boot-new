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
 * @Description: 污染因子表
 * @Author: jeecg-boot
 * @Date:   2020-07-01
 * @Version: V1.0
 */
@Data
@TableName("sys_pollution_code")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_pollution_code对象", description="污染因子表")
public class SysPollutionCode implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**新国标污染因子*/
	@Excel(name = "新国标污染因子", width = 15)
    @ApiModelProperty(value = "新国标污染因子")
    private java.lang.String code;
	/**污染因子名称*/
	@Excel(name = "污染因子名称", width = 15)
    @ApiModelProperty(value = "污染因子名称")
    private java.lang.String meaning;
	/**老国标污染因子*/
	@Excel(name = "老国标污染因子", width = 15)
    @ApiModelProperty(value = "老国标污染因子")
    private java.lang.String oldCode;
	/**浓度单位*/
	@Excel(name = "浓度单位", width = 15,dicCode = "allUnit")
	@Dict(dicCode = "allUnit")
    @ApiModelProperty(value = "浓度单位")
    private java.lang.String chromaUnit;
	/**计量单位-浓度-数学表达式*/
	@Excel(name = "计量单位-浓度-数学表达式", width = 15,dicCode = "allUnit")
	@Dict(dicCode = "allUnit")
    @ApiModelProperty(value = "计量单位-浓度-数学表达式")
    private java.lang.String chromaUnitMath;
	/**排放量单位*/
	@Excel(name = "排放量单位", width = 15,dicCode = "allUnit")
	@Dict(dicCode = "allUnit")
    @ApiModelProperty(value = "排放量单位")
    private java.lang.String amountUnit;
	/**计量单位-总量-数学表达式*/
	@Excel(name = "计量单位-总量-数学表达式", width = 15,dicCode = "allUnit")
	@Dict(dicCode = "allUnit")
    @ApiModelProperty(value = "计量单位-总量-数学表达式")
    private java.lang.String amountUnitMath;
	/**是否有折算值*/
	@Excel(name = "是否有折算值", width = 15, dicCode = "yes_or_no")
	@Dict(dicCode = "yes_or_no")
    @ApiModelProperty(value = "是否有折算值")
    private java.lang.String isZs;
	/**格式值*/
	@Excel(name = "格式值", width = 15)
    @ApiModelProperty(value = "格式值")
    private java.lang.String format;
	/**污染因子类型*/
	@Excel(name = "污染因子类型", width = 15, dicCode = "siteType")
	@Dict(dicCode = "siteType")
    @ApiModelProperty(value = "污染因子类型")
    private java.lang.String type;
	/**数据是否重复*/
	@Excel(name = "数据是否重复", width = 15, dicCode = "yes_or_no")
	@Dict(dicCode = "yes_or_no")
    @ApiModelProperty(value = "数据是否重复")
    private java.lang.String isRepeat;
	/**是否含有排放量*/
	@Excel(name = "是否含有排放量", width = 15, dicCode = "yes_or_no")
	@Dict(dicCode = "yes_or_no")
    @ApiModelProperty(value = "是否含有排放量")
    private java.lang.String isTotal;
	/**是否为主要污染物*/
	@Excel(name = "是否为主要污染物", width = 15, dicCode = "yes_or_no")
	@Dict(dicCode = "yes_or_no")
    @ApiModelProperty(value = "是否为主要污染物")
    private java.lang.String isImportant;
	/**异常值上限*/
	@Excel(name = "异常值上限", width = 15)
    @ApiModelProperty(value = "异常值上限")
    private java.lang.String errorMax;
	/**异常值下限*/
	@Excel(name = "异常值下限", width = 15)
    @ApiModelProperty(value = "异常值下限")
    private java.lang.String errorMin;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.String sort;
	/**是否启用*/
	@Excel(name = "是否启用", width = 15, dicCode = "yes_or_no")
	@Dict(dicCode = "yes_or_no")
    @ApiModelProperty(value = "是否启用")
    private java.lang.String isUse;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String content;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
}
