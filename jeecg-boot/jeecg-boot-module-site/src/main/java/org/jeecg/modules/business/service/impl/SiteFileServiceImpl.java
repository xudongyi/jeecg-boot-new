package org.jeecg.modules.business.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.SiteFile;
import org.jeecg.modules.business.mapper.SiteFileMapper;
import org.jeecg.modules.business.service.ISiteFileService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 站点附件表
 * @Author: jeecg-boot
 * @Date:   2020-06-08
 * @Version: V1.0
 */
@Service
public class SiteFileServiceImpl extends ServiceImpl<SiteFileMapper, SiteFile> implements ISiteFileService {

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
        List<SiteFile> companyFiles = new ArrayList<>();
        if(!StrUtil.isEmpty(files)) {
            // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            for (String file:files.split(",")) {
                //新增的
                SiteFile siteFile = new SiteFile();
                // 得到 每个对象中的属性值
                String[] array = file.split("/");
                siteFile.setFilename(array[array.length - 1]);
                array[array.length - 1] = "";
                siteFile.setFilepath(String.join("/", array));
                siteFile.setFiletype(fileType);//图片类型
                siteFile.setFromTable(fromTable);
                siteFile.setTableId(tableId);
                companyFiles.add(siteFile);
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
        List<SiteFile> files = this.list(new QueryWrapper<SiteFile>().lambda().eq(SiteFile::getFromTable, fromTable)
                .eq(SiteFile::getTableId,id));
        List<Map<String,String>> result = new ArrayList<>();
        for(SiteFile siteFile:files){
            Map<String,String> temp = new HashMap<>();
            temp.put("filePath",siteFile.getFilepath()+siteFile.getFilename());
            temp.put("fileName",siteFile.getFilename().substring(0,siteFile.getFilename().lastIndexOf("_"))+siteFile.getFilename().substring(siteFile.getFilename().lastIndexOf(".")));
            result.add(temp);
        }
        return result;
    }
}
