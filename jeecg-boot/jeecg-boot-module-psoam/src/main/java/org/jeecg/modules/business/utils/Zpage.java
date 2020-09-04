package org.jeecg.modules.business.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.jeecg.modules.business.vo.Column;

import java.util.List;

@Data
public class Zpage<T>{
    private List<T> records;
    private long total;
    private long size;
    private long current;
    private List<OrderItem> orders;
    private boolean optimizeCountSql;
    private boolean isSearchCount;
    List<Column>  columns;
    public Zpage(Page<T> page){
        this.records = page.getRecords();
        this.total= page.getTotal();
        this.size= page.getTotal();
        this.current= page.getCurrent();
        this.orders= page.getOrders();
    }

}
