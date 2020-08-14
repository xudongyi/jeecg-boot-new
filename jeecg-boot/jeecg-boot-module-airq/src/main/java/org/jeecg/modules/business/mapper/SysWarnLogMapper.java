package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.SysWarnLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.WarnLogVO;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 报警日志表
 * @Author: jeecg-boot
 * @Date:   2020-07-30
 * @Version: V1.0
 */
public interface SysWarnLogMapper extends BaseMapper<SysWarnLog> {
    List<WarnLogVO> queryWarnLogInfo(String[] companyIds, Page page, String area, String monitorId, Timestamp dateBegin, Timestamp dateEnd,String flag);
    List<WarnLogVO> exportWarnLogInfo(String[] companyIds, String area, String monitorId, Timestamp dateBegin, Timestamp dateEnd,String flag);

    List<Map<String,Object>> queryWarnInfo(@Param("companyIds")List<String> companyIds, @Param("monitorId")String monitorId,@Param("dateBegin")Timestamp dateBegin,
                                           @Param("dateEnd")Timestamp dateEnd,@Param("flag") String flag);
}
