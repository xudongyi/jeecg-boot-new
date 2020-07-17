package org.jeecg.modules.business.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AirSiteInfo {
    String mn;
    /**
     * 在线状态
     * 0：离线
     * 1：在线
     */
    String onlineStatus;
    /**
     * 设备状态
     * 0：故障
     * 1：维修
     * 2：停运
     */
    String deviceStatus;
    /**
     * 等级
     * 1 优
     * 2 良
     * 3 轻度污染
     * 4 中度污染
     * 5 重度污染
     * 6 严重污染
     */
    String level;
    /**
     * 经度
     */
    String siteLongitude;
    /**
     * 纬度
     */
    String siteLatitude;
    /**
     * 所属企业
     */
    String companyId;
    /**
     * 站点名称
     */
    String siteName;
    /**
     * aqi
     */
    Double aqi;
    /**
     * 创建时间
     */
    Date createTime;
    /**
     * 首要污染物
     */
    String firstCode;
    /**
     * PM2.5 1小时平均值
     */
    Double a3400401Avg;
    /**
     * PM2.5 24小时平均值
     */
    Double a3400424Avg;
    /**
     * PM10 1小时平均值
     */
    Double a3400201Avg;
    /**
     * PM10 24小时平均值
     */
    Double a3400224Avg;
    /**
     * SO2 二氧化硫
     */
    Double a21026Avg;
    /**
     * NO2 二氧化氮
     */
    Double a21004Avg;
    /**
     *O3 臭氧1小时平均值
     */
    Double a0502401Avg;
    /**
     *O3 臭氧8小时平均值
     */
    Double a0502408Avg;
    /**
     *CO 一氧化碳
     */
    Double a21005Avg;
    /**
     *温度
     */
    Double a01001Avg;
    /**
     *湿度
     */
    Double a01002Avg;
    /**
     * 风速平均值
     */
    Double a01007Avg;
    /**
     *气压
     */
    Double a01006Avg;
    /**
     *地址
     */
    String area;
    /**
     * first——code 的解释
     *
     */
    String meaning;
}
