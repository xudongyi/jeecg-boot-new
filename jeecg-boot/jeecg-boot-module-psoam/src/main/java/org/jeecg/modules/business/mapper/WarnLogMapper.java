package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.WarnLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.RealTimeWarn;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: warn_log
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface WarnLogMapper extends BaseMapper<WarnLog> {
    List<RealTimeWarn> queryWarn(Page<RealTimeWarn> page, @Param("companyIds") List<String>companyIds, String area, String type, String warnType,
                              String mn, Timestamp end, Timestamp begin);

    List<RealTimeWarn> queryWarn(@Param("companyIds") List<String>companyIds, String area, String type, String warnType,
                                 String mn, Timestamp end, Timestamp begin);
}
