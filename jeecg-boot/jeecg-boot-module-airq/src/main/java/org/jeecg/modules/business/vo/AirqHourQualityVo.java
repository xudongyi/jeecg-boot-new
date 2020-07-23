package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.annotation.ExcelSelf;

@Data
public class AirqHourQualityVo extends AirHourInfo{
    /**
     * SO2 二氧化硫 分指数
     */
    @ExcelSelf(name = "分指数",orderNum = 9,father = {"污染物浓度及空气质量分指数（IAQI）","二氧化硫（SO2）1小时平均"})
    Double a21026Iaqi;


    /**
     * NO2 二氧化氮 分指数
     */
    @ExcelSelf(name = "分指数",orderNum = 11,father = {"污染物浓度及空气质量分指数（IAQI）","二氧化氮（NO2）1小时平均"})
    Double a21004Iaqi;

    /**
     * PM10 1小时平均值 分指数
     */
    @ExcelSelf(name = "分指数",orderNum = 13,father = {"污染物浓度及空气质量分指数（IAQI）","颗粒物（粒径小于等于10μm）1小时平均"})

    Double a3400201Iaqi;

    /**
     * PM10 24小时平均值 分指数
     */
    @ExcelSelf(name = "分指数",orderNum = 15,father = {"污染物浓度及空气质量分指数（IAQI）","颗粒物（粒径小于等于10μm）24小时平均"})
    Double a3400224Iaqi;
    /**
     *CO 一氧化碳 分指数
     */
    @ExcelSelf(name = "分指数",orderNum = 17,father = {"污染物浓度及空气质量分指数（IAQI）","一氧化碳（CO）1小时平均"})

    Double a21005Iaqi;
    /**
     *O3 臭氧1小时 分指数
     */
    @ExcelSelf(name = "分指数",orderNum = 19,father = {"污染物浓度及空气质量分指数（IAQI）","臭氧（O3）1小时平均"})
    Double a0502401Iaqi;
    /**
     *O3 臭氧8小时 分指数
     */
    @ExcelSelf(name = "分指数",orderNum = 21,father = {"污染物浓度及空气质量分指数（IAQI）","臭氧（O3）8小时平均"})

    Double a0502408Iaqi;

    /**
     * PM2.5 1小时平均值
     */
    @ExcelSelf(name = "分指数",orderNum = 23,father = {"污染物浓度及空气质量分指数（IAQI）","颗粒物（粒径小于等于2.5μm）1小时平均"})

    Double a3400401Iaqi;
    /**
     * PM2.5 24小时平均值
     */
    @ExcelSelf(name = "分指数",orderNum = 25,father = {"污染物浓度及空气质量分指数（IAQI）","颗粒物（粒径小于等于2.5μm）24小时平均"})

    Double a3400424Iaqi;
}
