package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.sql.Timestamp;

@Data
public class OverEntry {

    String id;
    @Excel(name = "企业名称", width = 30)

    String companyName;
    @Excel(name = "监控点名称", width = 20)
    String siteName;

    @Excel(name = "数据时间", width = 20, format = "yyyy:MM:dd HH:mm:ss")
    Timestamp dataTime;
    @Excel(name = "监测因子", width = 15)
    String meaning;
    @Excel(name = "监测值", width = 15)
    Double value;
    @Excel(name = "标准值", width = 15)
    Double standardValue;
    @Excel(name = "单位", width = 15)
    String amountUnit;
    @Excel(name = "超标倍数", width = 15)
    Double multiple;
}
