package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.business.entity.AirqDay;
import org.jeecg.modules.business.mapper.AirqDayMapper;
import org.jeecg.modules.business.service.IAirqDayService;
import org.jeecg.modules.business.vo.AirqDayQualityVo;
import org.jeecg.modules.business.vo.AirqDayVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: airq_day
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Service
public class AirqDayServiceImpl extends ServiceImpl<AirqDayMapper, AirqDay> implements IAirqDayService {
    @Resource
    private AirqDayMapper airqDayMapper;

    @Override
    public List<AirqDayVO> findEvaluate(String searchTime, List<String> mns) {
        String[] times = null;
        String timeStart = null;
        String timeEnd = null;
        if(StrUtil.isNotEmpty(searchTime)){
            times = searchTime.split(",");
            timeStart = times[0];
            timeEnd = times[1];
        }
        List<String> mnsTemp = new ArrayList<>();
        for(int i = 0 ;i< mns.size();i++){
            mnsTemp.add(i,"'"+mns.get(i)+"'");
        }
       return airqDayMapper.findEvaluate(StringUtils.join(mnsTemp.toArray(), ","),timeStart,timeEnd);
    }

    @Override
    public List<AirqDayQualityVo> queryDayAirQuality(List<String> companyIds, String datatime, String datatime2, String area, String mn) {
        Timestamp ts = DateUtil.parse(datatime, "yyyy-MM-dd").toTimestamp();
        Timestamp ts2 = DateUtil.parse(datatime2, "yyyy-MM-dd").toTimestamp();

        return airqDayMapper.queryDayAirQuality(companyIds,ts,ts2,area,mn);
    }
}
