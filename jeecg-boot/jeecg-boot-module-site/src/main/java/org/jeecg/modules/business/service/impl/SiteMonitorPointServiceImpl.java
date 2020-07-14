package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.SiteMonitorPoint;
import org.jeecg.modules.business.mapper.SiteMonitorPointMapper;
import org.jeecg.modules.business.service.ISiteMonitorPointService;
import org.jeecg.modules.business.vo.SiteMonitorPointVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 监测站点表
 * @Author: jeecg-boot
 * @Date:   2020-07-02
 * @Version: V1.0
 */
@Service
public class SiteMonitorPointServiceImpl extends ServiceImpl<SiteMonitorPointMapper, SiteMonitorPoint> implements ISiteMonitorPointService {

    @Resource
    private SiteMonitorPointMapper siteMonitorPointMapper;


    public List<Map<String,String>> getMenus(){
        List<Map<String,String>> menus = new ArrayList<>();
        addElements("1"," 基本信息",0,menus);
        addElements("2"," 数采仪信息",0,menus);
        addElements("3"," 监测设施",0,menus);
        addElements("4"," 治理设施",0,menus);
        return menus;
    }

    @Override
    public Page<SiteMonitorPointVO> getSiteMonitorPointList(Page<SiteMonitorPointVO> page,String siteType,String siteState,String siteName,String companyId,String siteLevel,String area,String mn) {
        return page.setRecords(siteMonitorPointMapper.getSiteMonitorPointList(page,siteType,siteState,siteName,companyId,siteLevel,area,mn));
    }

    private void addElements(String key,String value,Integer counts,List<Map<String,String>> basicInfoMenus){
        Map<String,String> param = new HashMap<>(2);
        param.put("key",key);
        param.put("text",value);
        param.put("point",Integer.toString(counts));
        basicInfoMenus.add(param);
    }

}
