package org.jeecg.modules.business.vo;

import lombok.Data;

import java.util.List;

@Data
public class Column {

    private String title;

    private String align="center";

    private String dataIndex;

    private List<Column> childrenColumn;
}
