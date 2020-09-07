package org.jeecg.modules.business.constant;

/**
 * 正常导出Excel
 *
 * @Author MYP on 20-7-23. 静态常量
 */
public interface SelfExcelConstants {
    public static final String TITLE = "title";	//标题
    public static final String FOOTER = "footer";	//注脚

    public static final String SHEET_NAME = "sheetName";	//sheetName

    public static final String DATA_LIST = "dataList";//数据

    //-------table  查询数据库  redis查询redis缓存  dict数据字典------------//
    public static final String ANNOTATION_DICT = "dict";
    public static final String ANNOTATION_REDIS = "redis";
    public static final String ANNOTATION_TABLE = "table";
    //-------Map  execl
    public static final String COLUMNS = "columns";//表头  map
    public static final String TIMEFORMAT = "time_format";	//类class
    //-------class  execl
    public static final String CLAZZ = "clazz";	//类class

}
