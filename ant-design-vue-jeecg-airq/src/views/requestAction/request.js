import {getAction} from '@/api/manage'

//查询用户所属的企业名称
const queryCompanyName = (params)=>getAction("/site/siteMonitorPoint/queryCompanyName",params);
const getDetailMenus = (params)=>getAction("/site/siteMonitorPoint/menus",params);
const getDataCollection = (params)=>getAction("/collection/siteDataCollection/queryByMonitorId",params);
const querySiteName = (params)=>getAction("/statistic/querySiteName",params);
const queryFiles = (params,url)=>getAction(url,params);
const queryPollution = (params)=>getAction("/device/siteMonitorDevice/queryPollution",params);
const queryUnit = (params)=>getAction("/device/siteMonitorDevice/queryUnit",params);
const loadBaiduMap = (params)=>getAction("/site/siteMonitorPoint/loadBaiduMap",params);
const queryLastAirInfo = (params)=>getAction("/hour/airqHour/queryLastAirInfo",params);
const querySiteNameAndMn = (params)=>getAction("/hour/airqHour/querySiteNameAndMn",params);
const queryHourAirQuality = (params)=>getAction("/hour/airqHour/queryHourAirQuality",params);
const queryDayAirQuality = (params)=>getAction("/day/airqDay/queryDayAirQuality",params);

const queryStatistic = (params)=>getAction("/statistic/queryEvaluate",params);
const queryTrend = (params)=>getAction("/statistic/queryTrend",params);
const querySameCompare = (params)=>getAction("/statistic/querySameCompare",params);
export {
  queryCompanyName,
  querySiteName,
  queryFiles,
  queryPollution,
  queryUnit,
  loadBaiduMap,
  queryLastAirInfo,
  queryStatistic,
  querySiteNameAndMn,
  queryTrend,
  queryHourAirQuality,
  queryDayAirQuality,
  querySameCompare
}