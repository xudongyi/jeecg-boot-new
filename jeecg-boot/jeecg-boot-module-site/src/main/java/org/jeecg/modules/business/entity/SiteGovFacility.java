package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

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
 * @Description: 监测站治理设施
 * @Author: jeecg-boot
 * @Date:   2020-07-08
 * @Version: V1.0
 */
@Data
@TableName("site_gov_facility")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="site_gov_facility对象", description="监测站治理设施")
public class SiteGovFacility implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**监测点*/
	@Excel(name = "监测点", width = 15)
    @ApiModelProperty(value = "监测点")
    private java.lang.String monitorId;
	/**治理设施名称*/
	@Excel(name = "治理设施名称", width = 15)
    @ApiModelProperty(value = "治理设施名称")
    private java.lang.String govName;
	/**治理设施编号*/
	@Excel(name = "治理设施编号", width = 15)
    @ApiModelProperty(value = "治理设施编号")
    private java.lang.String govCode;
	/**治理设施类型*/
	@Excel(name = "治理设施类型", width = 15, dicCode = "govType")
	@Dict(dicCode = "govType")
    @ApiModelProperty(value = "治理设施类型")
    private java.lang.String govType;
	/**投入使用日期*/
	@Excel(name = "投入使用日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "投入使用日期")
    private java.util.Date useDate;
	/**设计处理能力*/
	@Excel(name = "设计处理能力", width = 15)
    @ApiModelProperty(value = "设计处理能力")
    private java.lang.String designAbility;
	/**设计月处理效率*/
	@Excel(name = "设计月处理效率", width = 15)
    @ApiModelProperty(value = "设计月处理效率")
    private java.lang.String designMonth;
	/**实际月处理效率*/
	@Excel(name = "实际月处理效率", width = 15)
    @ApiModelProperty(value = "实际月处理效率")
    private java.lang.String actualMonth;
	/**监测通道号*/
	@Excel(name = "监测通道号", width = 15)
    @ApiModelProperty(value = "监测通道号")
    private java.lang.String channelNumber;
	/**处理方法*/
	@Excel(name = "处理方法", width = 15, dicCode = "handlMethod")
	@Dict(dicCode = "handlMethod")
    @ApiModelProperty(value = "处理方法")
    private java.lang.String handlMethod;
	/**设施生产商*/
	@Excel(name = "设施生产商", width = 15)
    @ApiModelProperty(value = "设施生产商")
    private java.lang.String facilityProducer;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private java.lang.String concatUser;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String concatMobile;
	/**治理工艺*/
	@Excel(name = "治理工艺", width = 15)
    @ApiModelProperty(value = "治理工艺")
    private java.lang.String govCraft;
	/**治理方法*/
	@Excel(name = "治理方法", width = 15)
    @ApiModelProperty(value = "治理方法")
    private java.lang.String govMethod;
	/**治理因子信息*/
	@Excel(name = "治理因子信息", width = 15)
    @ApiModelProperty(value = "治理因子信息")
    private java.lang.String govFactorInfo;
	/**备注*/
	@Excel(name = "备注", width = 15)
    private transient java.lang.String remarkString;

    private byte[] remark;

    public byte[] getRemark(){
        if(remarkString==null){
            return null;
        }
        try {
            return remarkString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRemarkString(){
        if(remark==null || remark.length==0){
            return "";
        }
        try {
            return new String(remark,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
