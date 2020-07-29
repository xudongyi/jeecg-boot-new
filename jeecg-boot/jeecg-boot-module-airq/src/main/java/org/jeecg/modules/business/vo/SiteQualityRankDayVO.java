package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.annotation.ExcelSelf;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class SiteQualityRankDayVO {
    /*
    行政区域
     */
    @ExcelSelf(name = "行政区域",orderNum = 1)
    private String area;
    /**mn号*/

    @ApiModelProperty(value = "mn号")
    private java.lang.String mn;
    /*
    监测点位名称
     */
    @ExcelSelf(name = "监测点位名称",orderNum = 2)
    private String siteName;
    /**数据时间*/
    @ExcelSelf(name = "发布时间", width = 20,orderNum = 3)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据时间")
    private java.util.Date dataTime;
    /**空气质量指数*/
    @ExcelSelf(name = "空气质量指数(AQI)", width = 15,orderNum = 4)
    @ApiModelProperty(value = "空气质量指数")
    private java.lang.Double aqi;
    /**首要污染物*/
    @ApiModelProperty(value = "首要污染物")
    private java.lang.String firstCode;
    /*
    污染因子名称
     */
    @ExcelSelf(name = "首要污染物", width = 15,orderNum = 5)
    private String meaning;
    /*
    等级1-6级
     */
    @ExcelSelf(name = "空气质量指数级别", width = 15,dicCode = "airLevel",orderNum = 6)
    private String airLevel;
    /**空气质量级别*/
    @ExcelSelf(name = "空气质量指数类别", width = 15,dicCode = "level",orderNum = 7)
    @Dict(dicCode = "level")
    @ApiModelProperty(value = "空气质量级别")
    private java.lang.String level;
    /*
    排名
     */
    @ExcelSelf(name = "排名", width = 15,orderNum = 8)
    private String rank;
    /**SO2*/
    @ExcelSelf(name = "SO2(1h)μg/m3", width = 15,orderNum = 9)
    @ApiModelProperty(value = "SO2")
    private java.lang.Double a21026Avg;
    /**NO2*/
    @ExcelSelf(name = "NO2(1h)μg/m3", width = 15,orderNum = 10)
    @ApiModelProperty(value = "NO2")
    private java.lang.Double a21004Avg;
    /**PM10(1h)*/
    @ExcelSelf(name = "PM10(1h)μg/m3", width = 15,orderNum = 11)
    @ApiModelProperty(value = "PM10(1h)")
    private java.lang.Double a3400201Avg;
    /**PM10(24h)*/
    @ExcelSelf(name = "PM10(24h)μg/m3", width = 15,orderNum = 12)
    @ApiModelProperty(value = "PM10(24h)")
    private java.lang.Double a3400224Avg;
    /**CO*/
    @ExcelSelf(name = "CO(1h)μg/m3", width = 15,orderNum = 13)
    @ApiModelProperty(value = "CO")
    private java.lang.Double a21005Avg;
    /**O3(1h)*/
    @ExcelSelf(name = "O3(1h)μg/m3", width = 15,orderNum = 14)
    @ApiModelProperty(value = "O3(1h)")
    private java.lang.Double a0502401Avg;
    /**O3(8h)*/
    @ExcelSelf(name = "O3(8h)μg/m3", width = 15,orderNum = 15)
    @ApiModelProperty(value = "O3(8h)")
    private java.lang.Double a0502408Avg;
    /**PM2.5(1h)*/
    @ExcelSelf(name = "PM2.5(1h)μg/m3", width = 15,orderNum = 16)
    @ApiModelProperty(value = "PM2.5(1h)")
    private java.lang.Double a3400401Avg;
    /**PM2.5(24h)*/
    @ExcelSelf(name = "PM2.5(24h)μg/m3", width = 15,orderNum = 17)
    @ApiModelProperty(value = "PM2.5(24h)")
    private java.lang.Double a3400424Avg;
}
