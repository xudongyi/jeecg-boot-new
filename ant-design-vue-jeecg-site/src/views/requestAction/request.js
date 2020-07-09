import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'
import Vue from 'vue'

//查询用户所属的企业名称
const queryCompanyName = (params)=>getAction("/site/siteMonitorPoint/queryCompanyName",params);
const getDetailMenus = (params)=>getAction("/site/siteMonitorPoint/menus",params);
const getDataCollection = (params)=>getAction("/collection/siteDataCollection/queryByMonitorId",params);
const queryFiles = (params,url)=>getAction(url,params);
const queryPollution = (params)=>getAction("/device/siteMonitorDevice/queryPollution",params);
export {
  queryCompanyName,
  getDetailMenus,
  getDataCollection,
  queryFiles,
  queryPollution
}