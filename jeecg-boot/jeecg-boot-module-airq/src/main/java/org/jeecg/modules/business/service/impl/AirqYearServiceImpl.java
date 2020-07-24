package org.jeecg.modules.business.service.impl;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.business.entity.AirqYear;
import org.jeecg.modules.business.mapper.AirqYearMapper;
import org.jeecg.modules.business.service.IAirqYearService;
import org.jeecg.modules.business.vo.AirqVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: airq_year
 * @Author: jeecg-boot
 * @Date:   2020-07-16
 * @Version: V1.0
 */
@Service
public class AirqYearServiceImpl extends ServiceImpl<AirqYearMapper, AirqYear> implements IAirqYearService {

    @Resource
    private AirqYearMapper airqYearMapper;

    @Override
    public List<AirqVO> findEvaluate(String searchTime, List<String> mns) {
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
        return airqYearMapper.findEvaluate(StringUtils.join(mnsTemp.toArray(), ","),timeStart,timeEnd);
    }
}
