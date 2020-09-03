package org.jeecg.modules.business.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface ICompanyService {

    JSONObject queryCompanyInfos(List<String> companyIds);


}
