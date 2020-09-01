import {getAction} from '@/api/manage'

const queryConfig = (params)=>getAction("/psoamConfig/queryConfig",params);
const saveConfig = (params)=>getAction("/psoamConfig/saveConfig",params);
const queryColumns = (params)=>getAction("/onlineMonitor/queryColumns",params);

export {
  queryConfig,
  saveConfig,
  queryColumns
}