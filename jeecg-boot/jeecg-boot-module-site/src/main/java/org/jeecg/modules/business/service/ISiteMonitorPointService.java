package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.SiteMonitorPointVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 监测站点表
 * @Author: jeecg-boot
 * @Date:   2020-07-02
 * @Version: V1.0
 */
public interface ISiteMonitorPointService extends IService<SiteMonitorPoint> {

    List<Map<String, String>> getMenus();

    IPage<SiteMonitorPointVO> getSiteMonitorPointList(Page<SiteMonitorPointVO> page);
}
