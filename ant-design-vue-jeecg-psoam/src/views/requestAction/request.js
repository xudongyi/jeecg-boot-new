import {getAction} from '@/api/manage'
const queryGisInfos = (params)=>getAction("/psoam/companyInfo/queryGisInfos",params);
const querySiteNameAndMn = (params)=>getAction("/vocDay/vocDay/querySiteNameAndMn",params);
const queryCompanyName = (params)=>getAction("/vocDay/vocDay/queryCompanyName",params);
const queryConfig = (params)=>getAction("/psoamConfig/queryConfig",params);
const saveConfig = (params)=>getAction("/psoamConfig/saveConfig",params);
const queryColumns = (params)=>getAction("/onlineMonitor/queryColumns",params);

export {
  queryGisInfos,
  querySiteNameAndMn,
  queryCompanyName,
  queryConfig,
  saveConfig,
  queryColumns
}