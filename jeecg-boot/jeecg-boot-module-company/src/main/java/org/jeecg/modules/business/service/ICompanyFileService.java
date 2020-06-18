package org.jeecg.modules.business.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.CompanyFile;

/**
 * @Description: 企业附件表
 * @Author: jeecg-boot
 * @Date:   2020-06-08
 * @Version: V1.0
 */
public interface ICompanyFileService extends IService<CompanyFile> {


    boolean saveFiles(String files, String fileType, String fromTable, String tableId);
}
