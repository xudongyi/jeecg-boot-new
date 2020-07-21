package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class AirqHourManInsertVO {
    /*
    行政区域
     */
    private String area;
    /**小时数据平台状态*/
    @Excel(name = "小时数据平台状态", width = 15, dicCode = "airDataStatus")
    @Dict(dicCode = "airDataStatus")
    @ApiModelProperty(value = "小时数据平台状态")
    private java.lang.Integer state;
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
    /**SO2*/
    @Excel(name = "SO2", width = 15)
    @ApiModelProperty(value = "SO2")
    private java.lang.Double a21026Avg;
    /**NO2*/
    @Excel(name = "NO2", width = 15)
    @ApiModelProperty(value = "NO2")
    private java.lang.Double a21004Avg;
    /**PM10(1h)*/
    @Excel(name = "PM10(1h)", width = 15)
    @ApiModelProperty(value = "PM10(1h)")
    private java.lang.Double a3400201Avg;
    /**PM10(24h)*/
    @Excel(name = "PM10(24h)", width = 15)
    @ApiModelProperty(value = "PM10(24h)")
    private java.lang.Double a3400224Avg;
    /**CO*/
    @Excel(name = "CO", width = 15)
    @ApiModelProperty(value = "CO")
    private java.lang.Double a21005Avg;
    /**O3(1h)*/
    @Excel(name = "O3(1h)", width = 15)
    @ApiModelProperty(value = "O3(1h)")
    private java.lang.Double a0502401Avg;
    /**O3(8h)*/
    @Excel(name = "O3(8h)", width = 15)
    @ApiModelProperty(value = "O3(8h)")
    private java.lang.Double a0502408Avg;
    /**PM2.5(1h)*/
    @Excel(name = "PM2.5(1h)", width = 15)
    @ApiModelProperty(value = "PM2.5(1h)")
    private java.lang.Double a3400401Avg;
    /**PM2.5(24h)*/
    @Excel(name = "PM2.5(24h)", width = 15)
    @ApiModelProperty(value = "PM2.5(24h)")
    private java.lang.Double a3400424Avg;
    /**温度*/
    @Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private java.lang.Double a01001Avg;
    /**湿度*/
    @Excel(name = "湿度", width = 15)
    @ApiModelProperty(value = "湿度")
    private java.lang.Double a01002Avg;
    /**风速*/
    @Excel(name = "风速", width = 15)
    @ApiModelProperty(value = "风速")
    private java.lang.Double a01007Avg;
    /**气压*/
    @Excel(name = "气压", width = 15)
    @ApiModelProperty(value = "气压")
    private java.lang.Double a01006Avg;
    /**风向*/
    @Excel(name = "风向", width = 15)
    @ApiModelProperty(value = "风向")
    private java.lang.Double a01008Avg;
}
