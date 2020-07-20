package org.jeecg.modules.business.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class AirQualityUtil {
    @Autowired
    private RedisUtil redisUtil;

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
