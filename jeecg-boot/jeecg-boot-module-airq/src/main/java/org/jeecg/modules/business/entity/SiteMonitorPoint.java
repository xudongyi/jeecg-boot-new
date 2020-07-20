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

/**
 * @Description: 监测站点表
 * @Author: jeecg-boot
 * @Date:   2020-07-02
 * @Version: V1.0
 */
@Data
@TableName("site_monitor_point")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="site_monitor_point对象", description="监测站点表")
public class SiteMonitorPoint implements Serializable {
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
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**站点名称*/
	@Excel(name = "站点名称", width = 15)
    @ApiModelProperty(value = "站点名称")
    private String siteName;
	/**站点编号*/
	@Excel(name = "站点编号", width = 15)
    @ApiModelProperty(value = "站点编号")
    private String siteCode;
	/**站点类型*/
	@Excel(name = "站点类型", width = 15, dicCode = "siteType")
	@Dict(dicCode = "siteType")
    @ApiModelProperty(value = "站点类型")
    private String siteType;
	/**所属单位*/
	@Excel(name = "所属单位", width = 15)
    @ApiModelProperty(value = "所属单位")
    private String companyId;
	/**站点级别*/
	@Excel(name = "站点级别", width = 15, dicCode = "siteLevel")
	@Dict(dicCode = "siteLevel")
    @ApiModelProperty(value = "站点级别")
    private String siteLevel;
	/**所属区域*/
	@Excel(name = "所属区域", width = 15)
    @ApiModelProperty(value = "所属区域")
    private String area;
	/**站点位置*/
	@Excel(name = "站点位置", width = 15)
    @ApiModelProperty(value = "站点位置")
    private String location;
	/**站点经度*/
	@Excel(name = "站点经度", width = 15)
    @ApiModelProperty(value = "站点经度")
    private String siteLongitude;
	/**站点纬度*/
	@Excel(name = "站点纬度", width = 15)
    @ApiModelProperty(value = "站点纬度")
    private String siteLatitude;
	/**是否联网*/
	@Excel(name = "是否联网", width = 15, dicCode = "isNet")
	@Dict(dicCode = "isNet")
    @ApiModelProperty(value = "是否联网")
    private String isNet;
	/**站点状态*/
	@Excel(name = "站点状态", width = 15, dicCode = "siteState")
	@Dict(dicCode = "siteState")
    @ApiModelProperty(value = "站点状态")
    private String siteState;
	/**进出口*/
	@Excel(name = "进出口", width = 15, dicCode = "imorex")
	@Dict(dicCode = "imorex")
    @ApiModelProperty(value = "进出口")
    private String imorex;
	/**排放去向*/
	@Excel(name = "排放去向", width = 15)
    @ApiModelProperty(value = "排放去向")
    private String direction;
	/**排放规律*/
	@Excel(name = "排放规律", width = 15)
	@Dict(dicCode = "letLaw")
    @ApiModelProperty(value = "排放规律")
    private String letLaw;
	/**水域功能区类别*/
	@Excel(name = "水域功能区类别", width = 15, dicCode = "waterType")
	@Dict(dicCode = "waterType")
    @ApiModelProperty(value = "水域功能区类别")
    private String waterType;
	/**出口高度*/
	@Excel(name = "出口高度", width = 15)
    @ApiModelProperty(value = "出口高度")
    private String exportHeight;
	/**出口内径*/
	@Excel(name = "出口内径", width = 15)
    @ApiModelProperty(value = "出口内径")
    private String exportBore;
	/**最大流量*/
	@Excel(name = "最大流量", width = 15)
    @ApiModelProperty(value = "最大流量")
    private String maxFlow;
	/**出口截面积*/
	@Excel(name = "出口截面积", width = 15)
    @ApiModelProperty(value = "出口截面积")
    private String exportCross;
	/**废气排风口类型*/
	@Excel(name = "废气排风口类型", width = 15)
	@Dict(dicCode = "exitType")
    @ApiModelProperty(value = "废气排风口类型")
    private String exitType;
	/**(地表水)地表水环境功能区类别*/
	@Excel(name = "(地表水)地表水环境功能区类别", width = 15, dicCode = "waterType")
	@Dict(dicCode = "waterType")
    @ApiModelProperty(value = "(地表水)地表水环境功能区类别")
    private String surfaceType;
	/**(噪音)声音环境功能区类别*/
	@Excel(name = "(噪音)声音环境功能区类别", width = 15, dicCode = "noiseType")
	@Dict(dicCode = "noiseType")
    @ApiModelProperty(value = "(噪音)声音环境功能区类别")
    private String noiseType;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private String linkman;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private String phone;
	/**标志牌安装方式*/
	@Excel(name = "标志牌安装方式", width = 15)
	@Dict(dicCode = "signType")
    @ApiModelProperty(value = "标志牌安装方式")
    private String signType;
	/**数采仪MN号*/
	@Excel(name = "数采仪MN号", width = 15)
    @ApiModelProperty(value = "数采仪MN号")
    private String mn;
}
