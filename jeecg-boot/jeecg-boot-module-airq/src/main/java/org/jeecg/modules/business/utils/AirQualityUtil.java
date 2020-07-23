package org.jeecg.modules.business.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.business.mapper.AirQualityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

@Component
@Slf4j
public class AirQualityUtil {
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private AirQualityMapper airQualityMapper;

    @PostConstruct
    public void initial() {
        this.initialAQI();
        this.initialLevel();
    }

    public void initialAQI() {
        Map<String, List<AirqAQIBean>> result = new HashMap();
        List<Map<String, Object>> data = airQualityMapper.getAIRQ_AQI();
        if (data != null) {
            for(int i = 0; i < data.size(); ++i) {
                try {
                    AirqAQIBean v = new AirqAQIBean();
                    Map<String, Object> map = data.get(i);
                    v.setCode((String)map.get("factor_code"));
                    if (map.get("li_aqi") != null) {
                        v.setLiAqi((Double)map.get("li_aqi"));
                    }

                    if (map.get("hi_aqi") != null) {
                        v.setHiAqi((Double)map.get("hi_aqi"));
                    }

                    if (map.get("l_value") != null) {
                        v.setLowValue((Double)map.get("l_value"));
                    }

                    if (map.get("h_value") != null) {
                        v.setHighValue((Double)map.get("h_value"));
                    }

                    if (map.get("type") != null) {
                        v.setType((Integer)map.get("type"));
                    }

                    String key = v.getCode().toUpperCase() + "-" + v.getType();
                    if (result.containsKey(key)) {
                        ((List)result.get(key)).add(v);
                    } else {
                        List<AirqAQIBean> list = new ArrayList();
                        list.add(v);
                        result.put(key, list);
                    }
                } catch (Exception e) {
                    this.log.info("初始化空气质量分指数计算标准出错:" + e.getMessage());
                }
            }
        }

        if (!result.isEmpty()) {
            Iterator e = result.keySet().iterator();

            while(e.hasNext()) {
                String key = (String)e.next();
                this.redisUtil.hset("airq_aqi_map", key,  CommonsUtil.toJsonStr(result.get(key)));
            }
        } else {
            this.log.info("初始化空气质量分指数计算标准失败:未取到值");
        }

    }


    public void initialLevel() {
        List<Map<String, Object>> data = airQualityMapper.getAIRQ_LEVEL();
        if (data != null && data.size() > 0) {
            for(int i = 0; i < data.size(); ++i) {
                try {
                    this.redisUtil.hset("airq_level_map",data.get(i).get("level")+"",  CommonsUtil.toJsonStr(data.get(i)));
                } catch (Exception e) {
                    this.log.debug("Redis提示[初始化空气质量等级]出错:" + e.getMessage());
                }
            }
        } else {
            this.log.debug("Redis提示[初始化空气质量等级]:未取到值");
        }

    }

    /**
     * 获取AQI
     * @param factorCode 污染因子编码
     * @param type 污染因子类型
     * @param avg 污染因子值
     * @return
     */
    public double getAQI(String factorCode, int type, double avg) {
        double aqi = -1.0D;
        Object result = this.redisUtil.hget("airq_aqi_map", factorCode + "-" + type);
        if (StringUtils.isNotEmpty(result.toString())) {
            List<AirqAQIBean> list = (List) CommonsUtil.toJsonObject(result.toString(), AirqAQIBean.class);
            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    if (list.get(i).getHighValue() >= avg && (list.get(i)).getLowValue() <= avg) {
                        aqi = this.calcuteAQI((list.get(i)).getHiAqi(), (list.get(i)).getLiAqi(), (list.get(i)).getHighValue(), (list.get(i)).getLowValue(), avg);
                        break;
                    }
                }

                AirqAQIBean maxbean = list.get(list.size() - 1);
                if (aqi == -1.0D && avg > maxbean.getHighValue()) {
                    aqi = this.calcuteAQI(maxbean.getHiAqi(), maxbean.getLiAqi(), maxbean.getHighValue(), maxbean.getLowValue(), avg);
                }
            }
        } else {
            this.log.debug("Redis提示[获取空气质量分指数计算标准" + factorCode + "-" + type + "]:未取到值");
        }

        return aqi;
    }

    /**
     * 根据污染因子获取污染因子等级
     * @param aqi
     * @return
     */
    public String getLevel(double aqi) {
        aqi = CommonsUtil.numberFormat(aqi, 0);
        String result = "7";
        Map<Object, Object> map = this.redisUtil.hmget("airq_level_map");
        if (map != null && !map.isEmpty()) {
            Iterator iterator = map.keySet().iterator();

            while (iterator.hasNext()) {
                String level = (String) iterator.next();

                try {
                    Map<String, Object> data = (Map) CommonsUtil.toJsonObject((String) map.get(level), (Class) null);
                    if (data != null) {
                        double aqi_h = 0.0D;
                        if (data.get("aqi_h") != null) {
                            aqi_h = Double.valueOf(String.valueOf(data.get("aqi_h")));
                        }

                        double aqi_l = 0.0D;
                        if (data.get("aqi_l") != null) {
                            aqi_l = Double.valueOf(String.valueOf(data.get("aqi_l")));
                        }

                        if (aqi_h >= aqi && aqi_l <= aqi) {
                            result = data.get("level") + "";
                            break;
                        }
                    }
                } catch (Exception var12) {
                    this.log.error("Redis错误[获取空气质量等级]:" + var12.getMessage());
                }
            }
        } else {
            this.log.error("Redis提示[获取空气质量等级]:未取到值");
            result = "";
        }

        return result;
    }


    private double calcuteAQI(double HiAQI, double LiAQI, double HValue, double LValue, double value) {
        double IAQI = 0.0D;
        IAQI = (HiAQI - LiAQI) / (HValue - LValue) * (value - LValue) + LiAQI;
        IAQI = CommonsUtil.numberFormat(IAQI, 0);
        return IAQI;
    }
}
@Data
class AirqAQIBean {
    private String code;
    private double lowValue;
    private double highValue;
    private double liAqi;
    private double hiAqi;
    private int type;

}
