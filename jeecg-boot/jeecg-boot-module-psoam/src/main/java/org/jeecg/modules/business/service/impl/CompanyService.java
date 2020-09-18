package org.jeecg.modules.business.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.business.constant.PollutionSource;
import org.jeecg.modules.business.mapper.CompanyMapper;
import org.jeecg.modules.business.service.ICompanyService;
import org.jeecg.modules.business.vo.CompanySiteInfo;
import org.jeecg.modules.business.vo.OverStandardRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CompanyService implements ICompanyService {
    @Resource
    CompanyMapper companyMapper;
    @Autowired
    CommonService commonService;


    private final static Logger logger= LoggerFactory.getLogger(CompanyService.class);



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

    @Override
    public List<OverStandardRate> queryoverStandardRate(HttpServletRequest req) {

        //请求参数  取出来
        String dateType = req.getParameter("dateType");
        String dataTime_begin = req.getParameter("dataTime_begin");
        String dataTime_end = req.getParameter("dataTime_end");

        String area = req.getParameter("area");
        List<String> companyIds = Arrays.asList(req.getParameter("companyId").split(","));
        String mn = req.getParameter("mn");
        List<String> moniterTypes = Arrays.asList(req.getParameter("moniterType").split(","));

        //先根据company_info company_base site_monitor_point 查询出所有的站点信息

        List<Map<String,Object>> siteInfos = companyMapper.querySiteInfos(companyIds,area,moniterTypes,mn);
        List<String> mns = new ArrayList<>();
        Map<String,Map<String,Object>> mnsiteInfos = new HashMap<>();
        siteInfos.forEach(siteInfo->{
            mns.add(siteInfo.get("mn").toString());
            mnsiteInfos.putIfAbsent(siteInfo.get("mn").toString(),siteInfo);
        });
        //--------根据起始时间  查询总有效数据条数-------------
        DateTime begin = DateUtil.parse(dataTime_begin,getDateFormat(dateType));
        DateTime end = DateUtil.parse(dataTime_end,getDateFormat(dateType)).offset(DateField.DAY_OF_MONTH,1);
        if(PollutionSource.ReportDateType.MONTH.equals(dateType)){
            end = DateUtil.endOfMonth(DateUtil.parse(dataTime_end,getDateFormat(dateType))).offset(DateField.DAY_OF_MONTH,1);
        }else if(PollutionSource.ReportDateType.QUARTERLY.equals(dateType)){
            int quarterly = Integer.parseInt(req.getParameter("quarterly"));
            begin = DateUtil.offsetMonth(begin,quarterly*3-3);
            end =DateUtil.offsetMonth(begin,3);
        }else if(PollutionSource.ReportDateType.YEAR.equals(dateType)){
            end = DateUtil.endOfYear(DateUtil.parse(dataTime_end,getDateFormat(dateType))).offset(DateField.DAY_OF_MONTH,1);
        }
        List<Map<String,Object>> mnTotals = new ArrayList<>();
        DateTime tmp = begin;
        //获取表名
        while(true){
            String yymm = tmp.toDateStr().substring(2,4)+tmp.toDateStr().substring(5,7);
            //查询
            for(String siteType:moniterTypes){
                long startTime = System.currentTimeMillis();

                String tableName = getTablePrefix(siteType) +"_current_tr_"+yymm;
                if(commonService.checkTableExists(tableName))
                    mnTotals.addAll(companyMapper.countCurbyMn(tableName
                            ,mns,begin.toTimestamp(),end.toTimestamp()));
                //结束之间
                long endTime = System.currentTimeMillis();
                //程序块运行时间
               logger.info(tableName+"运行时间为：" + (endTime - startTime) + "毫秒");

            }
            if(begin.month()==end.month()&&begin.year()==end.year()){
                break;
            }
            tmp = tmp.offset(DateField.MONTH,1);
        }
        List<Map<String,Object>> mnOvers = new ArrayList<>();
        //查询超标数据条数
        for(String siteType:moniterTypes){
            mnOvers.addAll(companyMapper.countCurbyMn(getTablePrefix(siteType)
                    +"_current_overproof",mns,begin.toTimestamp(),end.toTimestamp()));
        }
        Map<String,Integer> totalCounts = new HashMap<>();
        //汇总计算超标率
        for(Map<String,Object> param:mnTotals){
            totalCounts.putIfAbsent(param.get("mn").toString(),0);
            totalCounts.put( param.get("mn").toString(),totalCounts.get(param.get("mn").toString())+Integer.parseInt(param.get("num").toString()));
        }
        Map<String,Integer> overCounts = new HashMap<>();
        mnOvers.forEach(mnOver->{
            overCounts.put(mnOver.get("mn").toString(),Integer.parseInt(mnOver.get("num").toString()));
        });

        List<OverStandardRate> overStandardRates = new ArrayList<>();
        String date = begin.toDateStr()+"~"+end.offset(DateField.DAY_OF_MONTH,-1).toDateStr();
        for(Map.Entry<String,Integer> totalcount:totalCounts.entrySet()){
            OverStandardRate overStandardRate = new OverStandardRate();
            overStandardRate.setMn(totalcount.getKey());
            overStandardRate.setCompanyName(mnsiteInfos.get(totalcount.getKey()).get("company_name").toString());
            overStandardRate.setSiteName(mnsiteInfos.get(totalcount.getKey()).get("site_name").toString());
            overStandardRate.setPeriod(date);
            overStandardRate.setTotalCount(totalcount.getValue());
            overStandardRate.setOverCount(overCounts.get(totalcount.getKey())==null?0:overCounts.get(totalcount.getKey()));
            if(overStandardRate.getOverCount()!=null &&overStandardRate.getTotalCount()!=null){
                overStandardRate.setOverRate(
                        new BigDecimal(overStandardRate.getOverCount()).divide(new BigDecimal(overStandardRate.getTotalCount()),4,BigDecimal.ROUND_HALF_UP)
                        .multiply( new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()+"%"
                );
            }
            overStandardRates.add(overStandardRate);

        }
        logger.info(""+overStandardRates.size());
        return overStandardRates;
    }

    @Override
    public List<CompanySiteInfo> queryCompanySite(HttpServletRequest req) {
        List<String> companyIds = Arrays.asList(req.getParameter("companyIds").split(","));
        List<String> siteType =  Arrays.asList(req.getParameter("siteType").split(","));
        List<Map<String,Object>> siteInfos = companyMapper.querySiteInfos(companyIds,null,siteType,null);
        List<CompanySiteInfo> companySiteInfos = new ArrayList<>();
        siteInfos.forEach(siteInfo->{
            CompanySiteInfo companySiteInfo = new CompanySiteInfo();
//            停运 >离线 > 故障 > 超标 > 正常
            companySiteInfo.setMn(siteInfo.get("mn").toString());
            companySiteInfo.setSiteName(siteInfo.get("site_name").toString());
            companySiteInfo.setArea(siteInfo.get("area").toString());

            companySiteInfo.setCompanyId(siteInfo.get("company_id").toString());
            companySiteInfo.setCompanyName(siteInfo.get("company_name").toString());
            String device_status = siteInfo.get("device_status")==null?"-1":siteInfo.get("device_status").toString();
            String online_status = siteInfo.get("online_status").toString();
            String data_status = siteInfo.get("data_status").toString();


            if("2".equals(device_status)){
                companySiteInfo.setStatus("outOfService");//停运
            }
            else if("0".equals(online_status)){
                companySiteInfo.setStatus("offline");//离线
            }
            else if("0".equals(device_status)){
                companySiteInfo.setStatus("error");//故障
            }
            else if("0".equals(data_status)){
                companySiteInfo.setStatus("normal");//正常
            }
            else
                companySiteInfo.setStatus("abnormal");//异常
            /**
             * device_status	设备状态		"0：故障
             * 1：维修
             * 2：停运"
             * online_status	在线状态		"0：离线
             * 1：在线"
             */
            companySiteInfos.add(companySiteInfo);
        });


        return companySiteInfos;
    }


    private String getDateFormat(String key){
       if(PollutionSource.ReportDateType.DATE.equals(key))
           return "yyyy-MM-dd";
       if(PollutionSource.ReportDateType.MONTH.equals(key))
           return "yyyy-MM";
        else
           return "yyyy";
    }

    private String getTablePrefix(String key){
        if("0".equals(key))
            return "water";
        if("1".equals(key))
            return "air";
        else
            return "voc";
    }
}
