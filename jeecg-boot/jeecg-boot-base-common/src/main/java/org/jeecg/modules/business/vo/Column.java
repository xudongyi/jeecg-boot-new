package org.jeecg.modules.business.vo;

import lombok.Data;

import java.util.List;

@Data
public class Column {

    private String title;

    private String align="center";

    private String dataIndex;

    private Integer width;

    private List<Column> children;

    public boolean hasChildren(){
        return children!=null&&!children.isEmpty();
    }
    public Column(){}
    public Column(String title,String dataIndex){
        this.title = title;
        this.dataIndex = dataIndex;
    }
}
