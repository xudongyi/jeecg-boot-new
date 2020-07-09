package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.SiteFile;

import java.util.List;
import java.util.Map;

/**
 * @Description: 企业附件表
 * @Author: jeecg-boot
 * @Date:   2020-06-08
 * @Version: V1.0
 */
public interface ISiteFileService extends IService<SiteFile> {


    boolean saveFiles(String files, String fileType, String fromTable, String tableId);
    List<Map<String, String>> getFileMaps(String id, String fromTable);
}
