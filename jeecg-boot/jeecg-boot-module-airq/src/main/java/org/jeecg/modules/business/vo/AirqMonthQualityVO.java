package org.jeecg.modules.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.annotation.ExcelSelf;

@Data
public class AirqMonthQualityVO {
    /*
    行政区域
     */
    @ExcelSelf(name = "行政区域",orderNum = 1)
    private String area;
    /**mn号*/

    @ApiModelProperty(value = "mn号")
    private String mn;
    /*
    监测点位名称
     */
    @ExcelSelf(name = "监测点位名称",orderNum = 2)
    private String siteName;
    /**年份*/
    @ExcelSelf(name = "数据时间", width = 15,orderNum = 3)
    @ApiModelProperty(value = "month")
    private String month;
    /**综合指数*/
    @ExcelSelf(name = "综合指数", width = 15,orderNum = 4)
    @ApiModelProperty(value = "totalI")
    private Double totalI;
    /**首要污染物*/
    @ApiModelProperty(value = "首要污染物")
    private String firstCode;
    /*
    污染因子名称
     */
    @ExcelSelf(name = "污染因子名称", width = 15,orderNum = 5)
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
    private String level;
    /**SO2*/
    @ExcelSelf(name = "二氧化硫(SO2)年平均浓度(μg/m3)", width = 15,orderNum = 8)
    @ApiModelProperty(value = "SO2")
    private Double a21026Avg;
    /**NO2*/
    @ExcelSelf(name = "二氧化氮(NO2)年平均浓度(μg/m3)", width = 15,orderNum = 9)
    @ApiModelProperty(value = "NO2")
    private Double a21004Avg;
    /**PM10*/
    @ExcelSelf(name = "PM10年平均浓度(μg/m3)", width = 15,orderNum = 11)
    @ApiModelProperty(value = "PM10")
    private Double a34002Avg;
    /**CO*/
    @ExcelSelf(name = "一氧化碳(CO)(μg/m3)", width = 15,orderNum = 12)
    @ApiModelProperty(value = "CO")
    private Double a21005Avg;
    /**O3*/
    @ExcelSelf(name = "臭氧(O3)年平均浓度(μg/m3)", width = 15,orderNum = 13)
    @ApiModelProperty(value = "O3")
    private Double a05024Avg;
    /**PM2.5*/
    @ExcelSelf(name = "PM2.5年平均浓度(μg/m3)", width = 15,orderNum = 14)
    @ApiModelProperty(value = "PM2.5")
    private Double a34004Avg;
}
