package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.annotation.ExcelSelf;
import org.jeecg.modules.business.constant.SelfExcelConstants;

import java.util.Date;

@Data
public class AirHourInfo {
    String mn;
    /**
     * 等级
     * 1 优
     * 2 良
     * 3 轻度污染
     * 4 中度污染
     * 5 重度污染
     * 6 严重污染
     */
    @ExcelSelf(name = "空气质量指数级别",orderNum = 6)
    @Dict(dicCode = "level")
    String level;
    /**
     * 所属企业
     */
    String companyId;
    /**
     * 站点名称
     */
    @ExcelSelf(name = "监测点位名称",orderNum = 2)

    String siteName;
    /**
     * aqi
     */
    @ExcelSelf(name = "空气质量指数（AQI）",orderNum = 4)
    Double aqi;
    /**
     * 创建时间
     */
    @ExcelSelf(name = "数据时间",orderNum = 3)
    Date dataTime;
    /**
     * 首要污染物
     */

    String firstCode;
    /**
     * PM2.5 1小时平均值
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 22,father = {"污染物浓度及空气质量分指数（IAQI）","颗粒物（粒径小于等于2.5μm）1小时平均"})

    Double a3400401Avg;
    /**
     * PM2.5 24小时平均值
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 24,father = {"污染物浓度及空气质量分指数（IAQI）","颗粒物（粒径小于等于2.5μm）24小时平均"})

    Double a3400424Avg;
    /**
     * PM10 1小时平均值
     */

    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 12,father = {"污染物浓度及空气质量分指数（IAQI）","颗粒物（粒径小于等于10μm）1小时平均"})

    Double a3400201Avg;
    /**
     * PM10 24小时平均值
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 14,father = {"污染物浓度及空气质量分指数（IAQI）","颗粒物（粒径小于等于10μm）24小时平均"})

    Double a3400224Avg;
    /**
     * SO2 二氧化硫
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 8,father = {"污染物浓度及空气质量分指数（IAQI）","二氧化硫（SO2）1小时平均"})
    Double a21026Avg;
    /**
     * NO2 二氧化氮
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 10,father = {"污染物浓度及空气质量分指数（IAQI）","二氧化氮（NO2）1小时平均"})
    Double a21004Avg;
    /**
     *O3 臭氧1小时平均值
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 18,father = {"污染物浓度及空气质量分指数（IAQI）","臭氧（O3）1小时平均"})

    Double a0502401Avg;
    /**
     *O3 臭氧8小时平均值
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 20,father = {"污染物浓度及空气质量分指数（IAQI）","臭氧（O3）8小时平均"})

    Double a0502408Avg;
    /**
     *CO 一氧化碳
     */

    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 16,father = {"污染物浓度及空气质量分指数（IAQI）","一氧化碳（CO）1小时平均"})

    Double a21005Avg;

    /**
     *地址
     */
    @ExcelSelf(name = "行政区域",orderNum = 1,dictType = SelfExcelConstants.ANNOTATION_TABLE,
    dicCode = "SYS_AREA",dicText = {"NAME","CODE"})
    String area;
    /**
     * first——code 的解释
     *
     */
    @ExcelSelf(name = "首要污染物",orderNum = 5)
    String meaning;

}
