package org.jeecg.modules.business.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OverEntry {

    String id;
    String companyId;
    String siteName;
    Timestamp dataTime;
    String meaning;
    Double value;
    Double standardValue;
    String amountUnit;
    Double multiple;
}
