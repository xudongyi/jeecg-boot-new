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
const queryCalendarAirQuality = (params)=>getAction("/day/airqDay/queryCalendarAirQuality",params);
const queryChainCompare = (params)=>getAction("/statistic/queryChainCompare",params);
const querySiteNameAndId = (params)=>getAction("/warn/sysWarnLog/querySiteNameAndId",params);

const queryAlarmInfo = (params)=>getAction("/warn/sysWarnLog/queryAlarmInfo", params);
const queryAirHomeCalendar = (params)=>getAction("/day/airqDay/queryAirHomeCalendar",params);
//空气首页
const queryAirQuality =  (params)=>getAction("/hour/airqHour/queryAirQuality",params);
//报警天数
const querySiteState = (params)=>getAction("/warn/sysWarnLog/querySiteState",params);
//6个月优良天数
const queryFineDays = (params)=>getAction("/day/airqDay/queryFineDays",params);
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
  querySameCompare,
  queryCalendarAirQuality,
  queryChainCompare,
  querySiteNameAndId,
  queryAlarmInfo,
  queryAirHomeCalendar,
  queryAirQuality,
  querySiteState,
  queryFineDays
}