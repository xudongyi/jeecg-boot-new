package org.jeecg.modules.business.mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.VocCurrentOverproof;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.OverEntry;
import org.jeecg.modules.business.vo.OverEntryReport;

/**
 * @Description: voc_current_overproof
 * @Author: jeecg-boot
 * @Date:   2020-09-02
 * @Version: V1.0
 */
public interface VocCurrentOverproofMapper extends BaseMapper<VocCurrentOverproof> {
    List<OverEntry>  queryOverVoc(@Param("companyIds") List<String>companyIds, String area, String code,
                                    String mn, Timestamp end, Timestamp begin);
    List<OverEntry> queryOverVoc(Page<OverEntry> page, @Param("companyIds") List<String>companyIds, String area, String code,
                                   String mn, Timestamp end, Timestamp begin);
    List<OverEntryReport>  queryOverVocReport(@Param("companyIds") List<String>companyIds, String area, String code,
                                                String mn, Timestamp end, Timestamp begin);
    List<OverEntryReport> queryOverVocReport(Page<OverEntryReport> page, @Param("companyIds") List<String>companyIds, String area, String code,
                                               String mn, Timestamp end, Timestamp begin);
}
