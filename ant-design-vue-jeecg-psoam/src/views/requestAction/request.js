import {getAction} from '@/api/manage'
const queryGisInfos = (params)=>getAction("/psoam/companyInfo/queryGisInfos",params);
const querySiteNameAndMn = (params)=>getAction("/vocDay/vocDay/querySiteNameAndMn",params);
const queryCompanyName = (params)=>getAction("/vocDay/vocDay/queryCompanyName",params);
const queryConfig = (params)=>getAction("/psoamConfig/queryConfig",params);
const saveConfig = (params)=>getAction("/psoamConfig/saveConfig",params);
const queryPollutionCode = (params)=>getAction("/vocDay/vocDay/queryPollutionCode",params);

const queryWaterColumns = (params)=>getAction("/onlineMonitor/queryWaterColumns",params);
const queryAirColumns = (params)=>getAction("/onlineMonitor/queryAirColumns",params);
const queryVOCsColumns = (params)=>getAction("/onlineMonitor/queryVocsColumns",params);
export {
  queryGisInfos,
  querySiteNameAndMn,
  queryCompanyName,
  queryConfig,
  saveConfig,
  queryPollutionCode,
  queryWaterColumns,
  queryAirColumns,
  queryVOCsColumns
}