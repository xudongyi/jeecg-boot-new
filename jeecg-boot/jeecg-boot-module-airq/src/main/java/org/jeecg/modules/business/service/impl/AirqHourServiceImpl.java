package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.AirqHour;
import org.jeecg.modules.business.mapper.AirqHourMapper;
import org.jeecg.modules.business.service.IAirqHourService;
import org.jeecg.modules.business.vo.AirSiteInfo;
import org.jeecg.modules.business.vo.AirqHourInputVO;
import org.jeecg.modules.business.vo.AirqHourManInsertVO;
import org.jeecg.modules.business.vo.AirqHourMonitorVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description: airq_hour
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Service
public class AirqHourServiceImpl extends ServiceImpl<AirqHourMapper, AirqHour> implements IAirqHourService {

    @Resource
    private AirqHourMapper airqHourMapper;

    @Override
    public List<AirSiteInfo> queryInfoByCompanyId(List<String> companyIds) {
        return airqHourMapper.queryInfoByCompanyId(companyIds);
    }

    @Override
    public Page<AirqHourMonitorVO> queryAirqHourMonitor(String companyIds,Page page, String area, String mn, Date dateBegin, Date dateEnd) {
        return page.setRecords(airqHourMapper.queryAirqHourMonitor(companyIds.split(","),page, area, mn, dateBegin, dateEnd));
    }

    @Override
    public Page<AirqHourInputVO> queryAirqHourInput(String companyIds,Page page, String area, String mn, Date dateBegin, Date dateEnd) {
        return page.setRecords(airqHourMapper.queryAirqHourInput(companyIds.split(","),page, area, mn, dateBegin, dateEnd));
    }

    @Override
    public Page<AirqHourManInsertVO> queryAirqHourManInsert(String companyIds,Page page, String area, String mn, Integer state, Date dateBegin, Date dateEnd) {
        return page.setRecords(airqHourMapper.queryAirqHourManInsert(companyIds.split(","),page, area, mn, state,dateBegin, dateEnd));
    }


}
