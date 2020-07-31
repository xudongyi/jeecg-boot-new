package org.jeecg.modules.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.annotation.ExcelSelf;
import org.jeecg.modules.business.constant.SelfExcelConstants;

@Data
public class SiteQualityRankYearVO {
    /*
    行政区域
     */
    @ExcelSelf(name = "行政区域",orderNum = 1,dictType = SelfExcelConstants.ANNOTATION_TABLE,dicCode = "SYS_AREA",dicText = {"NAME","CODE"})
    private String area;
    /**mn号*/

    @ApiModelProperty(value = "mn号")
    private java.lang.String mn;
    /*
    监测点位名称
     */
    @ExcelSelf(name = "监测点位名称",orderNum = 2)
    private String siteName;
    /**年份*/
    @ExcelSelf(name = "年份", width = 15,orderNum = 3)
    @ApiModelProperty(value = "year")
    private java.lang.String year;
    /**综合指数*/
    @ExcelSelf(name = "综合指数", width = 15,orderNum = 4)
    @ApiModelProperty(value = "totalI")
    private java.lang.Double totalI;
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
    @ExcelSelf(name = "空气质量指数级别", width = 15,dicCode = "airLevel",orderNum = 6,dictType = SelfExcelConstants.ANNOTATION_DICT)
    private String airLevel;
    /**空气质量级别*/
    @ExcelSelf(name = "空气质量指数类别", width = 15,dicCode = "level",orderNum = 7,bgColor=true,dictType = SelfExcelConstants.ANNOTATION_DICT)
    @Dict(dicCode = "level")
    @ApiModelProperty(value = "空气质量级别")
    private java.lang.String level;
    @ExcelSelf(name = "排名", width = 15,orderNum = 8)
    private String rank;
    /**SO2*/
    @ExcelSelf(name = "二氧化硫(SO2)年平均浓度(μg/m3)", width = 15,orderNum = 9)
    @ApiModelProperty(value = "SO2")
    private java.lang.Double a21026Avg;
    /**NO2*/
    @ExcelSelf(name = "二氧化氮(NO2)年平均浓度(μg/m3)", width = 15,orderNum = 10)
    @ApiModelProperty(value = "NO2")
    private java.lang.Double a21004Avg;
    /**PM10*/
    @ExcelSelf(name = "PM10年平均浓度(μg/m3)", width = 15,orderNum = 11)
    @ApiModelProperty(value = "PM10")
    private java.lang.Double a34002Avg;
    /**CO*/
    @ExcelSelf(name = "一氧化碳(CO)(μg/m3)", width = 15,orderNum = 12)
    @ApiModelProperty(value = "CO")
    private java.lang.Double a21005Avg;
    /**O3*/
    @ExcelSelf(name = "臭氧(O3)年平均浓度(μg/m3)", width = 15,orderNum = 13)
    @ApiModelProperty(value = "O3")
    private java.lang.Double a05024Avg;
    /**PM2.5*/
    @ExcelSelf(name = "PM2.5年平均浓度(μg/m3)", width = 15,orderNum = 14)
    @ApiModelProperty(value = "PM2.5")
    private java.lang.Double a34004Avg;

}
