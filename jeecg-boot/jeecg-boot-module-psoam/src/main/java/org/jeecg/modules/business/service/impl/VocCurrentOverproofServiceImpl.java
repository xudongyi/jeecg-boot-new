package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.VocCurrentOverproof;
import org.jeecg.modules.business.mapper.VocCurrentOverproofMapper;
import org.jeecg.modules.business.service.IVocCurrentOverproofService;
import org.jeecg.modules.business.vo.OverEntry;
import org.jeecg.modules.business.vo.OverEntryReport;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: voc_current_overproof
 * @Author: jeecg-boot
 * @Date:   2020-09-02
 * @Version: V1.0
 */
@Service
public class VocCurrentOverproofServiceImpl extends ServiceImpl<VocCurrentOverproofMapper, VocCurrentOverproof> implements IVocCurrentOverproofService {

    @Resource
    VocCurrentOverproofMapper vocCurrentOverproofMapper;

    @Override
    public List<OverEntry> queryOverVoc(List<String> companyIds, String area, String code, String mn, Timestamp end, Timestamp begin) {
        return vocCurrentOverproofMapper.queryOverVoc(companyIds ,area ,code ,mn ,end ,begin);
    }

    @Override
    public Page<OverEntry> queryOverVoc(Page<OverEntry> page, List<String> companyIds, String area, String code, String mn, Timestamp end, Timestamp begin) {
        return page.setRecords(vocCurrentOverproofMapper.queryOverVoc(page, companyIds, area, code, mn, end, begin));
    }

    @Override
    public List<OverEntryReport> queryOverVocReport(List<String> companyIds, String area, String code, String mn, Timestamp end, Timestamp begin) {
        return vocCurrentOverproofMapper.queryOverVocReport(companyIds, area, code, mn, end, begin);
    }

    @Override
    public Page<OverEntryReport> queryOverVocReport(Page<OverEntryReport> page, List<String> companyIds, String area, String code, String mn, Timestamp end, Timestamp begin) {
        return page.setRecords(vocCurrentOverproofMapper.queryOverVocReport(page, companyIds, area, code, mn, end, begin));
    }
}
