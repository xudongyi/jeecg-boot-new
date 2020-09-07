package org.jeecg.modules.business.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.MergeColumn;
import org.jeecg.modules.business.vo.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static void mergeColumn(MergeColumn mergeColumn, String name, int rownum, int row, int column, HSSFSheet sheet){

        //合并行
        if(mergeColumn.needFirst()){
            mergeColumn.firstColumn(name,rownum);
        }else if(mergeColumn.isSame(name,rownum)){
            mergeColumn.sameColumn();
        }else if(mergeColumn.needMerge()){
            sheet.addMergedRegion(new CellRangeAddress(row,row+rownum-1,column-mergeColumn.getSameNums(),column-1));
            mergeColumn.init();
            mergeColumn.firstColumn(name,rownum);
        }else{
            mergeColumn.init();
        }
    }

    public static Integer columnStatistics(List<Column> columns, List<String> dataIndexs){
        int rows = 1;
        for(Column column:columns){
            int result = 1;
            if(column.hasChildren()){
                result += ExcelUtil.columnStatistics(column.getChildren(),dataIndexs);

            }else{
                dataIndexs.add(column.getDataIndex());
            }
            rows = Math.max(result,rows);
        }

        return rows;
    }

    /**
     *  通用样式
     * @param workbook
     * @return
     */
    public static HSSFCellStyle getHssfCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cs = workbook.createCellStyle();
        cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        cs.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cs.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cs.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        return cs;
    }

    /**
     *  标题字体
     * @param workbook
     */
    public static HSSFFont titleFont(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);//设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        return font;
    }
    /**
     *   写标题
     * @param title
     * @param sheet
     * @param cs
     * @param lastCol
     * @param rowNum
     */
    public static  void writeTitle(String title, HSSFSheet sheet, HSSFCellStyle cs, int lastCol, int rowNum) {

            HSSFRow row = sheet.createRow(rowNum);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue(title);
            cell.setCellStyle(cs);
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,lastCol));//横向：合并第一行
            row.setHeight((short)800);
    }

    /**
     * 计算有多少列
     * @param column
     * @return
     */
    public static  int calculeCell(Column column) {
        int cell = 0;
        if(column.hasChildren()){
           for(Column child:column.getChildren()){
               cell+=calculeCell(child);
           }
        }else{
            cell++;
        }
        return cell;
    }

    /**
     *  查询改行的  所有列
     * @param column
     * @param row
     * @return
     */
    public static  List<Column> getAllColum(Column column, int row) {

        List<Column> result = new ArrayList<>();
        result.add(column);
        for(int i=0;i<row;i++){

            result = getColumns(result);

        }

        return result;
    }

    private static List<Column> getColumns( List<Column> columns) {
        List<Column> result = new ArrayList<>();
        for(Column column :columns){
            if(column!=null&&column.hasChildren()){
                result.addAll(column.getChildren());
            }
            else
                 result.add(null);
        }
       return result;
    }
}
