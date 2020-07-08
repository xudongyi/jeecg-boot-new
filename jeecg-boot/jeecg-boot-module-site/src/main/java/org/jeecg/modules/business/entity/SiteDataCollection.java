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
 * @Description: 数采仪
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
@Data
@TableName("site_data_collection")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="site_data_collection对象", description="数采仪")
public class SiteDataCollection implements Serializable {
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
	/**数采仪mn号*/
	@Excel(name = "数采仪mn号", width = 15)
    @ApiModelProperty(value = "数采仪mn号")
    private java.lang.String mnCode;
	/**实时上传间隔（s）*/
	@Excel(name = "实时上传间隔（s）", width = 15)
    @ApiModelProperty(value = "实时上传间隔（s）")
    private java.lang.String currentInterval;
	/**分钟上传间隔（minute）*/
	@Excel(name = "分钟上传间隔（minute）", width = 15)
    @ApiModelProperty(value = "分钟上传间隔（minute）")
    private java.lang.String minuteInterval;
	/**数采仪编号*/
	@Excel(name = "数采仪编号", width = 15)
    @ApiModelProperty(value = "数采仪编号")
    private java.lang.String colletionNumber;
	/**生产厂家*/
	@Excel(name = "生产厂家", width = 15)
    @ApiModelProperty(value = "生产厂家")
    private java.lang.String factory;
	/**生产日期*/
	@Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "生产日期")
    private java.util.Date prodcutDate;
	/**数采仪型号*/
	@Excel(name = "数采仪型号", width = 15)
    @ApiModelProperty(value = "数采仪型号")
    private java.lang.String productModel;
	/**ip地址*/
	@Excel(name = "ip地址", width = 15)
    @ApiModelProperty(value = "ip地址")
    private java.lang.String ip;
	/**安装日期*/
	@Excel(name = "安装日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "安装日期")
    private java.util.Date installDate;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private java.lang.String concatUser;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String concatMobile;
	/**保质日期*/
	@Excel(name = "保质日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "保质日期")
    private java.util.Date shelfLifeDate;
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
	/**SIM卡号*/
	@Excel(name = "SIM卡号", width = 15)
    @ApiModelProperty(value = "SIM卡号")
    private java.lang.String sim;
	/**使用协议*/
	@Excel(name = "使用协议", width = 15)
    @ApiModelProperty(value = "使用协议")
    private java.lang.String protocol;
	/**设备启用日期*/
	@Excel(name = "设备启用日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "设备启用日期")
    private java.util.Date useDate;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String content;
}
