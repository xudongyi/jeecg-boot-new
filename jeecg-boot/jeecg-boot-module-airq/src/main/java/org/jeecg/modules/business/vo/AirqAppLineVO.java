package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.annotation.ExcelSelf;

import java.util.Date;

@Data
public class AirqAppLineVO {
    /**
     * aqi
     */
    @ExcelSelf(name = "空气质量指数（AQI）",orderNum = 4)
    Double aqi;

    /**
     * 数据时间
     */
    @ExcelSelf(name = "数据时间",orderNum = 3,width = 20)
    Date dataTime;
}
