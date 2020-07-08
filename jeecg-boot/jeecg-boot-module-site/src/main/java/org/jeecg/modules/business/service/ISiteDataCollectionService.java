package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.SiteDataCollection;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 数采仪
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
public interface ISiteDataCollectionService extends IService<SiteDataCollection> {

    SiteDataCollection findByMnCode(String mnCode);

    SiteDataCollection getByMonitorId(String monitorId);
}
