package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecg.modules.business.annotation.ExcelSelf;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.sql.Timestamp;

@Data
public class OverEntry {

    String id;
    @ExcelSelf(name = "企业名称", width = 30,orderNum = 1)
    String companyName;
    @ExcelSelf(name = "监控点名称", width = 20,orderNum = 2)
    String siteName;

    @ExcelSelf(name = "数据时间", width = 20, format = "yyyy:MM:dd HH:mm:ss",orderNum = 3)
    Timestamp dataTime;
    @ExcelSelf(name = "监测因子", width = 15,orderNum = 4)
    String meaning;
    @ExcelSelf(name = "监测值", width = 15,orderNum = 5)
    Double value;
    @ExcelSelf(name = "标准值", width = 15,orderNum = 6)
    Double standardValue;
    @ExcelSelf(name = "单位", width = 15,orderNum = 7)
    String amountUnit;
    @ExcelSelf(name = "超标倍数", width = 15,orderNum = 8)
    Double multiple;
}
