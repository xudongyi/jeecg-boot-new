package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.VocCurrentOverproof;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.OverEntry;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: voc_current_overproof
 * @Author: jeecg-boot
 * @Date:   2020-09-02
 * @Version: V1.0
 */
public interface IVocCurrentOverproofService extends IService<VocCurrentOverproof> {
    List<OverEntry> queryOverVoc(List<String>companyIds, String area, String code,
                                   String mn, Timestamp end, Timestamp begin);
    Page<OverEntry> queryOverVoc(Page<OverEntry> page, List<String>companyIds, String area, String code,
                                   String mn, Timestamp end, Timestamp begin);
}
