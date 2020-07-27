package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class SiteQualityEvaluateVO {
    /*
    行政区域
     */
    private String area;
    /**mn号*/
    @Excel(name = "mn号", width = 15)
    @ApiModelProperty(value = "mn号")
    private java.lang.String mn;
    /*
    监测点位名称
     */
    private String siteName;
    /**数据时间*/
    @Excel(name = "数据时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date dataTime;
    /**空气质量指数*/
    @Excel(name = "空气质量指数", width = 15)
    @ApiModelProperty(value = "空气质量指数")
    private java.lang.Double aqi;
    /**首要污染物*/
    @Excel(name = "首要污染物", width = 15)
    @ApiModelProperty(value = "首要污染物")
    private java.lang.String firstCode;
    /*
    污染因子名称
     */
    private String meaning;
    /*
    等级1-6级
     */
    private String airLevel;
    /**空气质量级别*/
    @Excel(name = "空气质量级别", width = 15,dicCode = "level")
    @Dict(dicCode = "level")
    @ApiModelProperty(value = "空气质量级别")
    private java.lang.String level;
    /**小时数据平台状态*/
    @Excel(name = "小时数据平台状态", width = 15, dicCode = "airDataStatus")
    @Dict(dicCode = "airDataStatus")
    @ApiModelProperty(value = "小时数据平台状态")
    private java.lang.Integer state;
    private String advice;
    private String levelContent;
}
