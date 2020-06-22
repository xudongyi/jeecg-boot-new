package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 固废许可证信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Data
@TableName("company_solid_waste")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_solid_waste对象", description="固废许可证信息")
public class CompanySolidWaste implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**数据状态*/
	@Excel(name = "数据状态", width = 15)
    @ApiModelProperty(value = "数据状态")
    @Dict(dicCode = "statue")
    private String status;
	/**企业id*/
	@Excel(name = "企业id", width = 15)
    @ApiModelProperty(value = "企业id")
    private String companyId;
	/**许可证编号*/
	@Excel(name = "许可证编号", width = 15)
    @ApiModelProperty(value = "许可证编号")
    private String licenceCode;
	/**有效开始时间*/
	@Excel(name = "有效开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效开始时间")
    private Date validStarttime;
	/**有效结束时间*/
	@Excel(name = "有效结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效结束时间")
    private Date validEndtime;
	/**发证日期*/
	@Excel(name = "发证日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发证日期")
    private Date certificateTime;
	/**发证机关*/
	@Excel(name = "发证机关", width = 15)
    @ApiModelProperty(value = "发证机关")
    private String certificateOffice;
	/**固废类别*/
	@Excel(name = "固废类别", width = 15)
    @ApiModelProperty(value = "固废类别")
    private String dirtyType;
}
