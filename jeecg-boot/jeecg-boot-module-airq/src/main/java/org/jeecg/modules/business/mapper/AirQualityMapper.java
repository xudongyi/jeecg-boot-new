package org.jeecg.modules.business.mapper;

import java.util.List;
import java.util.Map;

/**
 * @program: hj-springboot
 * @description: 空气质量相关mapper
 * @author: xudy
 * @create: 2020-06-29 18:30
 **/
public interface AirQualityMapper {

    List<Map<String,Object>> getAIRQ_AQI();

    List<Map<String,Object>> getAIRQ_LEVEL();
}
