package org.jeecg.modules.business.vo;

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
import org.jeecg.modules.business.entity.SiteMonitorPoint;
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
public class SiteMonitorPointVO{

	/**id*/
    private String id;
	/**站点名称*/
    private String siteName;
	/**站点编号*/
    private String siteCode;
	/**站点类型*/
	@Dict(dicCode = "siteType")
    private String siteType;
	/**所属单位*/
    private String companyName;
	private String companyId;
	/**站点级别*/
	@Dict(dicCode = "siteLevel")
    private String siteLevel;
	/**所属区域*/
    private String area;
	/**站点位置*/
    private String location;
	/**站点经度*/
    private String siteLongitude;
	/**站点纬度*/
    private String siteLatitude;
	/**是否联网*/
	@Dict(dicCode = "isNet")
    private String isNet;
	/**站点状态*/
	@Dict(dicCode = "siteState")
    private String siteState;
	/**进出口*/
	@Dict(dicCode = "imorex")
    private String imorex;
	/**排放去向*/
    private String direction;
	/**排放规律*/
	@Dict(dicCode = "letLaw")
    private String letLaw;
	/**水域功能区类别*/
	@Dict(dicCode = "waterType")
    private String waterType;
	/**出口高度*/
    private String exportHeight;
	/**出口内径*/
    private String exportBore;
	/**最大流量*/
    private String maxFlow;
	/**出口截面积*/
    private String exportCross;
	/**废气排风口类型*/
	@Dict(dicCode = "exitType")
    private String exitType;
	/**(地表水)地表水环境功能区类别*/
	@Dict(dicCode = "waterType")
    private String surfaceType;
	/**(噪音)声音环境功能区类别*/
	@Dict(dicCode = "noiseType")
    private String noiseType;
	/**联系人*/
    private String linkman;
	/**联系电话*/
    private String phone;
	/**标志牌安装方式*/
	@Dict(dicCode = "signType")
    private String signType;
	/**数采仪MN号*/
    private String mn;
}
