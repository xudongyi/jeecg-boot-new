package org.jeecg.modules.business.view;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.business.annotation.ExcelSelf;
import org.jeecg.modules.business.constant.SelfExcelConstants;
import org.jeecg.modules.business.entity.MergeColumn;
import org.jeecg.modules.business.service.ISysDictService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.view.MiniAbstractExcelView;
import org.springframework.beans.factory.annotation.Autowired;
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
@Controller(NormalExcelConstants.JEECG_ENTITY_EXCEL_VIEW)
public class SelfEntityExcelView extends MiniAbstractExcelView {

    public SelfEntityExcelView(ISysDictService sysDictService, RedisUtil redisUtil) {
        super();
        this.sysDictService = sysDictService;
        this.redisUtil = redisUtil;
    }

     final ISysDictService sysDictService;
     final RedisUtil redisUtil;

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sheetName  = model.get(SelfExcelConstants.SHEET_NAME).toString();

        Class<?> clazz = (Class<?>)model.get(SelfExcelConstants.CLAZZ);
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        List<Field> excelSelves = new ArrayList<>();
        Class<?> tmpClass = clazz.getSuperclass();
        while (tmpClass !=null && !tmpClass.getName().toLowerCase().equals("java.lang.object") )
        {
            fields.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
            tmpClass = tmpClass.getSuperclass();
        }
        //表头列数
        int titleRows = 0;
        //遍历
        for(Field field:fields){
            ExcelSelf excelSelf =  field.getAnnotation(ExcelSelf.class);
            if(excelSelf!=null){
                excelSelves.add(field);
                if(excelSelf.father().length>titleRows)
                    titleRows = excelSelf.father().length;
            }
        }
        //还有name一列
        titleRows = titleRows+1;

        String codedFileName = "临时文件";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);//创建一个sheet-test1
        //设置单元格风格，居中对齐.  title的样式
        HSSFCellStyle cs = workbook.createCellStyle();
        cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        cs.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cs.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cs.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        //设置字体:
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);//设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        cs.setFont(font);//要用到的字体格式


        //当前列数
        int rowNum  = 0;
        //标题
        if(model.get(SelfExcelConstants.TITLE)!=null){
            HSSFRow row = sheet.createRow(rowNum);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue(model.get(SelfExcelConstants.TITLE).toString());
            cell.setCellStyle(cs);
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,excelSelves.size()-1));//横向：合并第一行
            row.setHeight((short)800);
            rowNum++;
        }
        //对  表头排序
        Collections.sort(excelSelves, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                //从小到大排序
                return o1.getAnnotation(ExcelSelf.class).orderNum()-o2.getAnnotation(ExcelSelf.class).orderNum();
            }
        });

        //设置单元格风格，居中对齐.  title的样式
        HSSFCellStyle cscolumn = workbook.createCellStyle();
        cscolumn.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        cscolumn.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cscolumn.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cscolumn.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cscolumn.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
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
        for(int i = 0;i<titleRows;i++){
            row = sheet.createRow(rowNum);
            MergeColumn mergeColumn = new MergeColumn();
            row.setHeight((short)600);

            for(int j = 0;j<excelSelves.size();j++){
                ExcelSelf excelSelf = excelSelves.get(j).getAnnotation(ExcelSelf.class);
                HSSFCell cell  = row.createCell(j);
                cell.setCellStyle(cscolumn);
                //第一行特殊处理
                if(i==0){
                    //不足合并
                    if(excelSelf.father().length<(titleRows-i-1)){
                        if(excelSelf.father().length==0)
                        {
                            cell.setCellValue(excelSelf.name());
                            //竖向合并
                            sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+titleRows-1,j,j));

                            mergeColumn(mergeColumn,excelSelf.name(),titleRows-1,rowNum,j,sheet);
                        }else{
                            cell.setCellValue(excelSelf.father()[i]);
                            //竖向合并
                            sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+titleRows-1-excelSelf.father().length,j,j));

                            mergeColumn(mergeColumn,excelSelf.father()[i],rowNum+titleRows-1-excelSelf.father().length ,rowNum,j,sheet);

                        }

                    }else{
                        //正常的则
                        if(excelSelf.father().length==0)//没有需要合并 的
                        {

                            cell.setCellValue(excelSelf.name());
                            mergeColumn(mergeColumn,excelSelf.name(),1 ,rowNum,j,sheet);
                        }else{

                            cell.setCellValue(excelSelf.father()[i]);
                            mergeColumn(mergeColumn,excelSelf.father()[i],1 ,rowNum,j,sheet);
                        }


                    }

                }else{
                    //合并后的正常处理  都不需要列合并
                    if(excelSelf.father().length == (titleRows-1)){
                        if(i<titleRows-1)
                        {
                            //正常的则
                            cell.setCellValue(excelSelf.father()[i]);
                            mergeColumn(mergeColumn,excelSelf.father()[i],1 ,rowNum,j,sheet);

                        }
                        else
                        {
                            //正常的则
                            cell.setCellValue(excelSelf.name());
                            mergeColumn(mergeColumn,excelSelf.name(),1 ,rowNum,j,sheet);

                        }

                    }else{
                        mergeColumn.init();
                    }
                }
                sheet.setColumnWidth(j,excelSelf.width()*300);
            }

            if(mergeColumn.needMerge()){
                sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum+mergeColumn.getMergeRows()-1,excelSelves.size()-mergeColumn.getSameNums(),excelSelves.size()-1));
                mergeColumn.init();
            }
            rowNum++;
        }

        //结果集
        List<Object> objs = (List)model.get(SelfExcelConstants.DATA_LIST);
        //设置单元格风格，居中对齐.  title的样式
        HSSFCellStyle contentcs = workbook.createCellStyle();
        contentcs.setAlignment(HSSFCellStyle.ALIGN_CENTER);


        //设置字体:
        HSSFFont contentFont = workbook.createFont();
        contentFont.setFontName("宋体");
        contentFont.setFontHeightInPoints((short) 11);//设置字体大小
        contentcs.setFont(contentFont);//要用到的字体格式
        //处理数据
        //设置列值-内容
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Object o:objs) {
            row = sheet.createRow(rowNum);
            for(int i=0;i<excelSelves.size();i++){
                excelSelves.get(i).setAccessible(true);
                ExcelSelf excelSelf = excelSelves.get(i).getAnnotation(ExcelSelf.class);
                HSSFCell cell = row.createCell( i);
                Object val = excelSelves.get(i).get(o);
                if(val==null||val.equals(""))
                    cell.setCellValue("NA");
                else if(!StrUtil.isEmpty(excelSelf.dictType())){
                        //字典
                    cell.setCellValue(queryDictTextByKey(excelSelf.dictType(),excelSelf.dicCode(), excelSelf.dicText(),val));
                }
                else if(val instanceof Double)
                    cell.setCellValue(Double.parseDouble(val.toString()));
                else if(val instanceof Date){
                    if(!StrUtil.isEmpty(excelSelf.format()))
                        formatter = new SimpleDateFormat(excelSelf.format());
                    cell.setCellValue(formatter.format((Date) val));
                }
                else
                    cell.setCellValue(val.toString());

                //为了  特殊处理
                if(excelSelf.bgColor()){
                    //设置单元格风格，居中对齐.  title的样式
                    HSSFCellStyle bgcs = workbook.createCellStyle();
                    bgcs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    bgcs.setFont(contentFont);//要用到的字体格式
                    //拿到palette颜色板
                    HSSFPalette palette = workbook.getCustomPalette();
                    //这个是重点，具体的就是把之前的颜色 HSSFColor.LIME.index
                    //替换为  RGB(51,204,204) 宝石蓝这种颜色
                    //你可以改为 RGB(0,255,127)

                    switch (Integer.parseInt(val.toString())){
                        case 1:   palette.setColorAtIndex(HSSFColor.LIME.index, (byte) 0, (byte) 228, (byte) 0);break;
                        case 2:   palette.setColorAtIndex(HSSFColor.LIME.index, (byte) 250, (byte) 241, (byte) 0);break;
                        case 3:   palette.setColorAtIndex(HSSFColor.LIME.index, (byte) 255, (byte) 126, (byte) 0);break;
                        case 4:   palette.setColorAtIndex(HSSFColor.LIME.index, (byte) 255, (byte) 0, (byte) 0);break;
                        case 5:   palette.setColorAtIndex(HSSFColor.LIME.index, (byte) 153, (byte) 0, (byte) 76);break;
                        case 6:   palette.setColorAtIndex(HSSFColor.LIME.index, (byte) 126, (byte) 0, (byte) 35);break;
                    }
                    bgcs.setFillForegroundColor(palette.getColor(HSSFColor.LIME.index).getIndex());
                    bgcs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

                    cell.setCellStyle(bgcs);
                    bgcs.setFont(contentFont);//要用到的字体格式
                }else{
                    cell.setCellStyle(contentcs);
                }

            }
            rowNum++;
        }
        //注脚
        if(model.get(SelfExcelConstants.FOOTER)!=null){
             row = sheet.createRow(rowNum);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue(model.get(SelfExcelConstants.FOOTER).toString());
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,excelSelves.size()-1));//横向：合并第一行

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

    /**
     *  字典
     */
    private String  queryDictTextByKey(String type,String code,String[] text,Object val){
        StringBuilder textValue=new StringBuilder();

        for(String key:val.toString().split(",")){
            String tmp = null;
            if(type.equals(SelfExcelConstants.ANNOTATION_DICT))
                tmp =   sysDictService.queryDictTextByKey(code,key.trim());
            else if (type.equals(SelfExcelConstants.ANNOTATION_REDIS))
                tmp =   redisUtil.hget(code,key).toString();
            else{
                tmp = sysDictService.queryTableDictTextByKey(code,text[0],text[1],key.trim());
            }

            if (tmp != null) {
                if (textValue.length()>0) {
                    textValue.append(",");
                }
                textValue.append(tmp);
            }
        }

        return textValue.toString();
    }

    private void mergeColumn(MergeColumn mergeColumn,String name, int rownum,int row,int column,HSSFSheet sheet){

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
}
