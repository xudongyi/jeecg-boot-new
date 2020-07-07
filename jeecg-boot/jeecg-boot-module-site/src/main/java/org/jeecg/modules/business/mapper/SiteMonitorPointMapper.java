package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.SiteMonitorPointVO;

import java.util.List;

/**
 * @Description: 监测站点表
 * @Author: jeecg-boot
 * @Date:   2020-07-02
 * @Version: V1.0
 */
public interface SiteMonitorPointMapper extends BaseMapper<SiteMonitorPoint> {

    List<SiteMonitorPointVO> getSiteMonitorPointList(Page<SiteMonitorPointVO> page,String siteType);
}
