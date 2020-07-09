package org.jeecg.modules.business.entity;

import java.io.Serializable;

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
 * @Description: 监测点检测设备
 * @Author: jeecg-boot
 * @Date:   2020-07-08
 * @Version: V1.0
 */
@Data
@TableName("site_monitor_device")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="site_monitor_device对象", description="监测点检测设备")
public class SiteMonitorDevice implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**站点id*/
	@Excel(name = "站点id", width = 15)
    @ApiModelProperty(value = "站点id")
    private java.lang.String monitorId;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String deviceName;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String deviceNumber;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15, dicCode = "siteType")
	@Dict(dicCode = "siteType")
    @ApiModelProperty(value = "设备类型")
    private java.lang.String deviceType;
	/**污染因子*/
	@Excel(name = "污染因子", width = 15)
    @ApiModelProperty(value = "污染因子")
    private java.lang.String pollutionCode;
	/**做样周期*/
	@Excel(name = "做样周期", width = 15)
    @ApiModelProperty(value = "做样周期")
    private java.lang.String sampleCycle;
	/**监测仪器状态*/
	@Excel(name = "监测仪器状态", width = 15, dicCode = "siteState")
	@Dict(dicCode = "siteState")
    @ApiModelProperty(value = "监测仪器状态")
    private java.lang.String deviceState;
	/**检测仪器型号*/
	@Excel(name = "检测仪器型号", width = 15)
    @ApiModelProperty(value = "检测仪器型号")
    private java.lang.String deviceModel;
	/**监测仪器厂家*/
	@Excel(name = "监测仪器厂家", width = 15)
    @ApiModelProperty(value = "监测仪器厂家")
    private java.lang.String deviceFactory;
	/**量程上限*/
	@Excel(name = "量程上限", width = 15)
    @ApiModelProperty(value = "量程上限")
    private java.lang.String rangeMax;
	/**量程下限*/
	@Excel(name = "量程下限", width = 15)
    @ApiModelProperty(value = "量程下限")
    private java.lang.String rangeMin;
	/**检出限*/
	@Excel(name = "检出限", width = 15)
    @ApiModelProperty(value = "检出限")
    private java.lang.String checkoutMax;
	/**检出限单位*/
	@Excel(name = "检出限单位", width = 15)
    @ApiModelProperty(value = "检出限单位")
    private java.lang.String checkoutUnit;
	/**生产日期*/
	@Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "生产日期")
    private java.util.Date productDate;
	/**安装日期*/
	@Excel(name = "安装日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "安装日期")
    private java.util.Date installDate;
	/**保质日期*/
	@Excel(name = "保质日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "保质日期")
    private java.util.Date shelfLifeDate;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private java.lang.String deviceConcatUser;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String deviceConcatMobile;
	/**设备启用日期*/
	@Excel(name = "设备启用日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "设备启用日期")
    private java.util.Date useDate;
	/**运维单位*/
	@Excel(name = "运维单位", width = 15)
    @ApiModelProperty(value = "运维单位")
    private java.lang.String operationCompany;
	/**运维负责人*/
	@Excel(name = "运维负责人", width = 15)
    @ApiModelProperty(value = "运维负责人")
    private java.lang.String operationUser;
	/**运维负责人电话*/
	@Excel(name = "运维负责人电话", width = 15)
    @ApiModelProperty(value = "运维负责人电话")
    private java.lang.String operationMobile;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String content;
}
