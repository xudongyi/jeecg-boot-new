package org.jeecg.modules.business.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.mapper.CompanyMapper;
import org.jeecg.modules.business.service.ICompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CompanyService implements ICompanyService {
    @Resource
    CompanyMapper companyMapper;


   public  JSONObject queryCompanyInfos(List<String> companyIds){
       List<Map<String,Object>> queryResult = companyMapper.queryCompanyInfos(companyIds);
       JSONObject json = new JSONObject();
       for(Map<String,Object> map:queryResult){
           if(!json.containsKey(map.get("company_id").toString())){
               JSONObject company = new JSONObject();
               company.put("company_id",map.get("company_id"));
               company.put("company_name",map.get("company_name"));
               company.put("short_name",map.get("short_name"));
               company.put("longitude",map.get("longitude"));
               company.put("dimension",map.get("dimension"));
               company.put("address",map.get("address"));
               company.put("industry",map.get("industry"));
               company.put("env_protect_contact",map.get("env_protect_contact"));
               company.put("administrative_region",map.get("administrative_region"));
               company.put("contact_phone",map.get("contact_phone"));
               company.put("points",new JSONArray());
               json.put(map.get("company_id").toString(),company);
           }

           JSONObject point = new JSONObject();
           point.put("mn",map.get("mn"));
           point.put("site_type",map.get("site_type"));
           point.put("site_level",map.get("site_level"));
           point.put("location",map.get("location"));
           point.put("imorex",map.get("imorex"));
           point.put("direction",map.get("direction"));
           point.put("data_status",map.get("data_status"));
           point.put("device_status",map.get("device_status"));
           point.put("online_status",map.get("online_status"));
           point.put("site_name",map.get("site_name"));
           json.getJSONObject(map.get("company_id").toString()).getJSONArray("points").add(point);

       }

       return json;
    }

    @Override
    public Map<String, Object> queryRealTime(String mn,String tableName,String field) {
        return companyMapper.queryMaxRealTime(mn,tableName,field);
    }

    @Override
    public List<Map<String, Object>> queryCodeAndStatus(String mn) {
        return companyMapper.queryCodeAndStatus(mn);
    }

}
