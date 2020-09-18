package org.jeecg.modules.business.service;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.vo.CompanySiteInfo;
import org.jeecg.modules.business.vo.OverStandardRate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ICompanyService {

    JSONObject queryCompanyInfos(List<String> companyIds);
    Map<String,Object> queryRealTime(String mn,String tableName,String field);
    List<Map<String, Object>> queryCodeAndStatus( String mn);

    List<OverStandardRate>  queryoverStandardRate(HttpServletRequest req);
    List<CompanySiteInfo> queryCompanySite(HttpServletRequest req);
}
