package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.annotation.ExcelSelf;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class WarnLogVO {
    /*
    行政区域
     */
    @ExcelSelf(name = "行政区域",orderNum = 1,dictType = SelfExcelConstants.ANNOTATION_TABLE,dicCode = "SYS_AREA",dicText = {"NAME","CODE"})
    private String area;
    /**监控点id*/
    @ApiModelProperty(value = "监控点id")
    private java.lang.String monitorId;
    /*
    监测点位名称
     */
    @ExcelSelf(name = "监测点位名称",orderNum = 2)
    private String siteName;
    /**报警类型*/
    @ExcelSelf(name = "报警类型", width = 15, dicCode = "warnFlag",orderNum = 3,dictType = SelfExcelConstants.ANNOTATION_DICT)
    @Dict(dicCode = "warnFlag")
    @ApiModelProperty(value = "报警类型")
    private java.lang.String flag;
    /**报警内容*/
    @ExcelSelf(name = "报警内容", width = 15,orderNum = 4)
    @ApiModelProperty(value = "报警内容")
    private java.lang.String content;
    /**报警时间*/
    @ExcelSelf(name = "报警时间", width = 20,orderNum = 5)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "报警时间")
    private java.util.Date warnTime;


}
