package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.AirqHour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.AirSiteInfo;
import org.jeecg.modules.business.vo.AirqHourInputVO;
import org.jeecg.modules.business.vo.AirqHourManInsertVO;
import org.jeecg.modules.business.vo.AirqHourMonitorVO;
import org.jeecg.modules.business.vo.AirqHourQualityVo;

import java.sql.Timestamp;
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
    List<AirqHourQualityVo> queryHourAirQuality(@Param("companyIds")List<String> companyIds, @Param("datatime")Timestamp datatime,@Param("datatime2")Timestamp datatime2,@Param("area")String area, @Param("mn")String mn);
    List<AirqHourMonitorVO> queryAirqHourMonitor(Page page,String area,String mn, Date dateBegin, Date dateEnd);

    List<AirqHourInputVO> queryAirqHourInput(Page page, String area, String mn, Date dateBegin, Date dateEnd);

    List<AirqHourManInsertVO> queryAirqHourManInsert(Page page, String area, String mn, Integer state,Date dateBegin, Date dateEnd);

}
