package org.jeecg.modules.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AirqDayVO {
    @ApiModelProperty(value = "每个级别下的数量")
    private Integer value;
    @ApiModelProperty(value = "级别")
    private String name;
}
