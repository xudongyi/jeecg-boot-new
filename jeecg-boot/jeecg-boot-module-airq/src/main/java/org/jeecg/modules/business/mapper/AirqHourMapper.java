package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.AirqHour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.AirSiteInfo;
import org.jeecg.modules.business.vo.AirqHourMonitorVO;

import java.util.Date;
import java.util.List;

/**
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface AirqHourMapper extends BaseMapper<AirqHour> {

    List<AirSiteInfo> queryInfoByCompanyId(@Param("companyIds")List<String> companyIds);

    List<AirqHourMonitorVO> queryAirqHourMonitor(Page page,String area,String siteName, Date dateBegin, Date dateEnd);

}
