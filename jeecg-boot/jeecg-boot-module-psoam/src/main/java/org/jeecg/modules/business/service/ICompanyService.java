package org.jeecg.modules.business.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface ICompanyService {

    JSONObject queryCompanyInfos(List<String> companyIds);

    Map<String,Object> queryRealTime(String mn,String tableName,String field);
    List<Map<String, Object>> queryCodeAndStatus( String mn);

}
