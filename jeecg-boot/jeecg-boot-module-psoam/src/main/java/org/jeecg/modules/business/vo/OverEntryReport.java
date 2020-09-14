package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.annotation.ExcelSelf;
import java.sql.Timestamp;

@Data
public class OverEntryReport {
    String id;
    @ExcelSelf(name = "企业名称", width = 30,orderNum = 1)
    String companyName;
    @ExcelSelf(name = "监控点名称", width = 20,orderNum = 2)
    String siteName;
    @ExcelSelf(name = "超标因子", width = 15,orderNum = 3)
    String meaning;
    @ExcelSelf(name = "超标开始时间", width = 20, format = "yyyy:MM:dd HH:mm:ss",orderNum = 4)
    Timestamp beginTime;
    @ExcelSelf(name = "超标结束时间", width = 20, format = "yyyy:MM:dd HH:mm:ss",orderNum = 5)
    Timestamp endTime;
    @ExcelSelf(name = "最大超标值", width = 15,orderNum = 6)
    Double value;
    @ExcelSelf(name = "标准值", width = 15,orderNum = 7)
    Double standardValue;
    @ExcelSelf(name = "单位", width = 15,orderNum = 8)
    String chromaUnit;
    @ExcelSelf(name = "超标倍数", width = 15,orderNum = 9)
    Double multiple;
}
