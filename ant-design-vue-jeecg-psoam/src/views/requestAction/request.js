import {getAction} from '@/api/manage'

const querySiteNameAndMn = (params)=>getAction("/vocDay/vocDay/querySiteNameAndMn",params);
const queryCompanyName = (params)=>getAction("/vocDay/vocDay/queryCompanyName",params);
const queryConfig = (params)=>getAction("/psoamConfig/queryConfig",params);
const saveConfig = (params)=>getAction("/psoamConfig/saveConfig",params);
const queryWaterColumns = (params)=>getAction("/onlineMonitor/queryWaterColumns",params);
const queryAirColumns = (params)=>getAction("/onlineMonitor/queryAirColumns",params);
const queryVocsColumns = (params)=>getAction("/onlineMonitor/queryVocsColumns",params);
export {
  querySiteNameAndMn,
  queryCompanyName,
  queryConfig,
  saveConfig,
  queryWaterColumns,
  queryAirColumns,
  queryVocsColumns
}