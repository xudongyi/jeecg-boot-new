package org.jeecg.modules.business.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AirHourPlayVo {


    private Double aqi;
    private Double a21026Iaqi;
    private Double a21004Iaqi;
    private Double a3400201Iaqi;
    private Double a3400401Iaqi;
    private Double a21005Iaqi;
    private Double a0502401Iaqi;
    /**
     * 经度
     */
    private String siteLongitude;
    /**
     * 纬度
     */
    private String siteLatitude;
    private Date dataTime;
}