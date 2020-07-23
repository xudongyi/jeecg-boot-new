package org.jeecg.modules.business.entity;


public class MergeColumn {

    private String columnNum ;
    private int mergeRows ; //处理列合并的行合并
    private int sameNums ; //处理列合并的行合并

    public MergeColumn(){
         columnNum = "";
         mergeRows = 0; //处理列合并的行合并
         sameNums = 0; //处理列合并的行合并
    }
    public void init(){
        columnNum = "";
        mergeRows = 0;
        sameNums = 0;
    }
    public void firstColumn(String name,int rows){
        columnNum = name;
        mergeRows = rows;
        sameNums = 1;
    }
    public boolean isSame(String name,int rows){
        return columnNum.equals(name) && mergeRows==rows;
    }
    public boolean needFirst(){
        return sameNums==0; //处理列合并的行合并
    }
    public boolean needMerge(){
        return sameNums>1;
    }
    public void sameColumn(){
        sameNums ++; //处理列合并的行合并
    }

    public int getSameNums() {
        return sameNums;
    }

    public int getMergeRows() {
        return mergeRows;
    }
}