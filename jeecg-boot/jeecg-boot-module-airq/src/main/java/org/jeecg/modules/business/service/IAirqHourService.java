package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.AirqHour;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.AirSiteInfo;
import org.jeecg.modules.business.vo.AirqHourInputVO;
import org.jeecg.modules.business.vo.AirqHourManInsertVO;
import org.jeecg.modules.business.vo.AirqHourMonitorVO;
import org.jeecg.modules.business.vo.AirqHourQualityVo;

import java.util.Date;
import java.util.List;

/**
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
public interface IAirqHourService extends IService<AirqHour> {

    List<AirSiteInfo> queryInfoByCompanyId(List<String> companyIds);
    List<AirqHourQualityVo> queryHourAirQuality(List<String> companyIds, String datatime,String datatime2,String area,String mn);

    Page<AirqHourMonitorVO> queryAirqHourMonitor(String companyIds,Page page, String area, String mn, Date dateBegin, Date dateEnd);

    Page<AirqHourInputVO> queryAirqHourInput(String companyIds,Page page, String area, String mn, Date dateBegin, Date dateEnd);

    Page<AirqHourManInsertVO> queryAirqHourManInsert(String companyIds,Page page, String area, String mn,Integer state, Date dateBegin, Date dateEnd);

    IPage<AirqHourManInsertVO> queryAirqHourManInsert(Page<AirqHourManInsertVO> page, String area, String mn, Integer state, Date dateBegin, Date dateEnd);
}
