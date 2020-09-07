package org.jeecg.modules.business.view;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.business.annotation.ExcelSelf;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.MergeColumn;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecg.modules.business.utils.ExcelUtil;
import org.jeecg.modules.business.vo.Column;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.view.MiniAbstractExcelView;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Entity 实体数据对象导出
 * @Author MYP
 *
 */
@Controller
public class SelfMapExcelView extends MiniAbstractExcelView {



    @Override

    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sheetName  = model.get(SelfExcelConstants.SHEET_NAME).toString();

        List<Column> columns = (List<Column>)model.get(SelfExcelConstants.COLUMNS);
        String codedFileName = "临时文件";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);//创建一个sheet-test1
        //设置单元格风格，居中对齐.  title的样式
        HSSFCellStyle cs = ExcelUtil.getHssfCellStyle(workbook);
        //设置字体:
        cs.setFont(  ExcelUtil.titleFont(workbook));//标题要用到的字体格式
        //-------整理表头---------------//
        //字段的合集
        List<String> dataKeys = new ArrayList<>();
        //columnRows 表头行数
        int columnRows = ExcelUtil.columnStatistics(columns,dataKeys);

        //当前列数
        int rowNum  = 0;
        //-------标题---------------//
        if(model.get(SelfExcelConstants.TITLE)!=null) {
            ExcelUtil.writeTitle(model.get(SelfExcelConstants.TITLE).toString(), sheet, cs, dataKeys.size() - 1, rowNum);
            rowNum++;
        }
        //设置单元格风格，居中对齐.  表头的样式
        HSSFCellStyle cscolumn = ExcelUtil.getHssfCellStyle(workbook);
        //设置字体:
        HSSFFont fontcolumn = workbook.createFont();
        cscolumn.setFont(fontcolumn);//要用到的字体格式
        cscolumn.setWrapText(true);//设置字体超出宽度自动换行
        fontcolumn.setFontName("宋体");
        fontcolumn.setFontHeightInPoints((short) 11);//设置字体大小
        fontcolumn.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        cscolumn.setFont(fontcolumn);//要用到的字体格式
        HSSFRow row;
        //拼接表头
        for(int i=0;i<columnRows;i++){
            row = sheet.createRow(rowNum);
            row.setHeight((short)600);
            int cellNum = 0;//列数
            HSSFCell cell;
            for(Column column:columns){
                cell  = row.createCell(cellNum);
                cell.setCellStyle(cscolumn);
                //第一行表头
                if(i==0){
                    cell.setCellValue(column.getTitle());
                    if(column.hasChildren())
                    {
                        //需要计算 column  占用多少列
                        int occupy = ExcelUtil.calculeCell(column);
                        //横向合并
                        sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,cellNum,cellNum+occupy-1));
                        cellNum ++;
                        for(int j=1;j<occupy;j++){
                            cell  = row.createCell(cellNum);
                            cell.setCellStyle(cscolumn);
                            cellNum ++;
                        }
                    }
                    else
                    {

                        //纵向合并
                        sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+columnRows-i-1,cellNum,cellNum));
                        cellNum++;
                    }
                    sheet.setColumnWidth(cellNum,15000);
                }else{
                     //后面的行
                    for(Column item:ExcelUtil.getAllColum(column,i)){
                        cell  = row.createCell(cellNum);
                        sheet.setColumnWidth(cellNum,5000);

                        cell.setCellStyle(cscolumn);
                        if(item==null){
                            cellNum++;
                            //什么也不干
                        }else{
                            cell.setCellValue(item.getTitle());
                            if(item.hasChildren())
                            {
                                //需要计算 column  占用多少列
                                int occupy = ExcelUtil.calculeCell(item);
                                //横向合并
                                sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,cellNum,cellNum+occupy-1));
                                cellNum += occupy;
                            }
                            else
                            {
                                cellNum++;
                                //纵向合并
                                sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+columnRows-i-1,cellNum,cellNum));
                            }
                        }
                    }

                }
            }
            rowNum++;
        }


        //结果集
        List<Map<String,Object>> objs = (List)model.get(SelfExcelConstants.DATA_LIST);
        //设置单元格风格，居中对齐.  title的样式
        HSSFCellStyle contentcs = workbook.createCellStyle();
        contentcs.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //设置字体:
        HSSFFont contentFont = workbook.createFont();
        contentFont.setFontName("宋体");
        contentFont.setFontHeightInPoints((short) 11);//设置字体大小
        contentcs.setFont(contentFont);//要用到的字体格式
        //---------------时间格式------------------------//
        SimpleDateFormat formatter;
        if(model.get(SelfExcelConstants.TIMEFORMAT)!=null){
            formatter = new SimpleDateFormat(model.get(SelfExcelConstants.TIMEFORMAT).toString());
        }else{
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        //处理数据
        //---------------设置列值-内容------------------------//
        for (Map<String,Object> o:objs) {
            row = sheet.createRow(rowNum);
            for(int i=0;i<dataKeys.size();i++){
                HSSFCell cell = row.createCell( i);


                Object val = o.get(dataKeys.get(i));
                if(val==null||val.equals(""))
                    cell.setCellValue("NA");

                else if(val instanceof Double)
                    cell.setCellValue(Double.parseDouble(val.toString()));
                else if(val instanceof Date){
                    cell.setCellValue(formatter.format((Date) val));
                }
                else
                    cell.setCellValue(val.toString());
            }
            rowNum++;
        }
        //-----------------------注脚-----------------------------------//
        if(model.get(SelfExcelConstants.FOOTER)!=null){
             row = sheet.createRow(rowNum);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue(model.get(SelfExcelConstants.FOOTER).toString());
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,dataKeys.size()-1));//横向：合并第一行

        }

        //默认是 .xls
        codedFileName += HSSF;

        if (isIE(request)) {
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF8");
        } else {
            codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }




}
