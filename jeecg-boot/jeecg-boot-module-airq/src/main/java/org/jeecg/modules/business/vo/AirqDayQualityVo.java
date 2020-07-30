package org.jeecg.modules.business.vo;


import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.annotation.ExcelSelf;
import org.jeecg.modules.business.constant.SelfExcelConstants;

import java.util.Date;

@Data
public class AirqDayQualityVo {

    String mn;
    /**
     * 所属企业
     */
    String companyId;
    /**
     *地址
     */
    @ExcelSelf(name = "行政区域",orderNum = 1,dictType = SelfExcelConstants.ANNOTATION_TABLE,
            dicCode = "SYS_AREA",dicText = {"NAME","CODE"})
    String area;

    /**
     * 站点名称
     */
    @ExcelSelf(name = "监测点位名称",orderNum = 2,width = 20)

    String siteName;
    /**
     * 创建时间
     */
    @ExcelSelf(name = "数据时间",orderNum = 3,width = 20)
    Date dataTime;
    /**
     * aqi
     */
    @ExcelSelf(name = "空气质量指数（AQI）",orderNum = 4)
    Double aqi;
    /**
     * 首要污染物
     */
    String firstCode;
    /**
     * first——code 的解释
     *
     */
    @ExcelSelf(name = "首要污染物",orderNum = 5)
    String meaning;
    /**
     * 等级
     * 1 优
     * 2 良
     * 3 轻度污染
     * 4 中度污染
     * 5 重度污染
     * 6 严重污染
     */
    @ExcelSelf(name = "空气质量指数级别",orderNum = 6,dictType = SelfExcelConstants.ANNOTATION_DICT,
    dicCode = "airLevel")
    @Dict(dicCode = "Level")
    String level;

    /**
     * SO2 二氧化硫
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 8,father = {"污染物浓度及空气质量分指数（IAQI）","二氧化硫（SO2）24小时平均"})
    Double a21026Avg;

    /**
     * SO2 二氧化硫
     */
    @ExcelSelf(name = "分指数",orderNum = 9,father = {"污染物浓度及空气质量分指数（IAQI）","二氧化硫（SO2）24小时平均"})
    Double a21026Iaqi;
    /**
     * NO2 二氧化氮
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 10,father = {"污染物浓度及空气质量分指数（IAQI）","二氧化氮（NO2）24小时平均"})
    Double a21004Avg;
    /**
     * NO2 二氧化氮
     */
    @ExcelSelf(name = "分指数",orderNum = 11,father = {"污染物浓度及空气质量分指数（IAQI）","二氧化氮（NO2）24小时平均"})
    Double a21004Iaqi;
    /**
     *  PM10
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 12,father = {"污染物浓度及空气质量分指数（IAQI）","PM10 24小时平均"})
    Double a3400224Avg;
    /**
     *  PM10
     */
    @ExcelSelf(name = "分指数",orderNum = 13,father = {"污染物浓度及空气质量分指数（IAQI）","PM10 24小时平均"})
    Double a3400224Iaqi;
    /**
     * 一氧化碳（CO）
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 14,father = {"污染物浓度及空气质量分指数（IAQI）","一氧化碳（CO）24小时平均"})
    Double a21005Avg;
    /**
     * 一氧化碳（CO）
     */
    @ExcelSelf(name = "分指数",orderNum = 15,father = {"污染物浓度及空气质量分指数（IAQI）","一氧化碳（CO）24小时平均"})
    Double a21005Iaqi;
    /**
     * 臭氧（O3）
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 16,father = {"污染物浓度及空气质量分指数（IAQI）","臭氧（O3）最大1小时平均"})
    Double a0502401Avg;
    /**
     * 臭氧（O3）
     */
    @ExcelSelf(name = "分指数",orderNum = 17,father = {"污染物浓度及空气质量分指数（IAQI）","臭氧（O3）最大1小时平均"})
    Double a0502401Iaqi;
    /**
     * 臭氧（O3）最大8小时滑动平均
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 18,father = {"污染物浓度及空气质量分指数（IAQI）","臭氧（O3）最大8小时滑动平均"})
    Double a0502408Avg;
    /**
     * 臭氧（O3）最大8小时滑动分指数
     */
    @ExcelSelf(name = "分指数",orderNum = 19,father = {"污染物浓度及空气质量分指数（IAQI）","臭氧（O3）最大8小时滑动平均"})
    Double a0502408Iaqi;
    /**
     * 臭氧（O3）最大8小时滑动平均
     */
    @ExcelSelf(name = "浓度(μg/m3)",orderNum = 20,father = {"污染物浓度及空气质量分指数（IAQI）","颗粒物（粒径小于等于2.5μm）24小时平均"})
    Double a3400424Avg;

    /**
     * PM2.5 1小时平均值
     */
    @ExcelSelf(name = "分指数",orderNum = 21,father = {"污染物浓度及空气质量分指数（IAQI）","颗粒物（粒径小于等于2.5μm）24小时平均"})

    Double a3400424Iaqi;


}