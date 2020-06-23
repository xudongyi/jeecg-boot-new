package org.jeecg.modules.business.service.impl;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.CompanyFile;
import org.jeecg.modules.business.mapper.CompanyFileMapper;
import org.jeecg.modules.business.service.ICompanyFileService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 企业附件表
 * @Author: jeecg-boot
 * @Date:   2020-06-08
 * @Version: V1.0
 */
@Service
public class CompanyFileServiceImpl extends ServiceImpl<CompanyFileMapper, CompanyFile> implements ICompanyFileService {

    /**
     * 保存文件 路径
     * @param files  文件路径
     * @param fileType 文件类型
     * @param fromTable 关联表
     * @param tableId  关联表ID
     * @return  是否成功
     */
    @Override
    public boolean saveFiles(String files,String fileType,String fromTable,String tableId){
        if(files.equals("[]"))
            return true;
        List<CompanyFile> companyFiles = new ArrayList<>();
        if(!StrUtil.isEmpty(files)) {
            // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            for (String file:files.split(",")) {
                //新增的
                CompanyFile companyFile = new CompanyFile();
                // 得到 每个对象中的属性值
                String[] array = file.split("/");
                companyFile.setFilename(array[array.length - 1]);
                array[array.length - 1] = "";
                companyFile.setFilepath(String.join("/", array));
                companyFile.setFiletype(fileType);//图片类型
                companyFile.setFromTable(fromTable);
                companyFile.setTableId(tableId);
                companyFiles.add(companyFile);
            }
        }
        return this.saveBatch(companyFiles);
    }


    /**
     * 查询文件列表
     * @param id  关联表ID
     * @param fromTable 关联表名
     * @return
     */
    @Override
    public List<Map<String, String>> getFileMaps(String id, String fromTable) {
        List<CompanyFile> files = this.list(new QueryWrapper<CompanyFile>().lambda().eq(CompanyFile::getFromTable, fromTable)
                .eq(CompanyFile::getTableId,id));
        List<Map<String,String>> result = new ArrayList<>();
        for(CompanyFile companyFile:files){
            Map<String,String> temp = new HashMap<>();
            temp.put("filePath",companyFile.getFilepath()+companyFile.getFilename());
            temp.put("fileName",companyFile.getFilename().substring(0,companyFile.getFilename().lastIndexOf("_"))+companyFile.getFilename().substring(companyFile.getFilename().lastIndexOf(".")));
            result.add(temp);
        }
        return result;
    }
}
