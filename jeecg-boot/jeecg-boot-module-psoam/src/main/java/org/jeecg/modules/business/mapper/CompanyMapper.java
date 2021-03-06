package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.PsoamConfig;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Description: 监测类型配置
 * @Author: jeecg-boot
 * @Date:   2020-08-31
 * @Version: V1.0
 */
public interface CompanyMapper {

    List<Map<String,Object>> queryCompanyInfos(@Param("companyIds") List<String> companyIds);
    Map<String, Object> queryMaxRealTime( String mn,String tableName,String field);
    List<Map<String, Object>> queryCodeAndStatus( String mn);
    List<Map<String, Object>> querySiteInfos(@Param("companyIds") List<String> companyIds,String area,@Param("siteTypes") List<String> siteTypes,
            String mn);
    List<Map<String, Object>> countCurbyMn(String tableName, @Param("mns") List<String> mns, Timestamp begin,Timestamp end);

}
