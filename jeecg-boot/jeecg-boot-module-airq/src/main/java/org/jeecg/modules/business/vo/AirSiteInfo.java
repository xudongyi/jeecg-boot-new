package org.jeecg.modules.business.vo;

import lombok.Data;


@Data
public class AirSiteInfo extends AirHourInfo{
    /**
     * 经度
     */
    private String siteLongitude;
    /**
     * 纬度
     */
    private String siteLatitude;
    /**
     * 在线状态
     * 0：离线
     * 1：在线
     */
    private String onlineStatus;
    /**
     * 设备状态
     * 0：故障
     * 1：维修
     * 2：停运
     */
    private String deviceStatus;
    /**
     *温度
     */
    private Double a01001Avg;
    /**
     *湿度
     */
    private Double a01002Avg;
    /**
     * 风速平均值
     */
    private Double a01007Avg;
    /**
     *气压
     */
    private Double a01006Avg;
}
