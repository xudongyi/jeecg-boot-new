import {getAction} from '@/api/manage'

const queryConfig = (params)=>getAction("/psoamConfig/queryConfig",params);
const saveConfig = (params)=>getAction("/psoamConfig/saveConfig",params);

export {
  queryConfig,
  saveConfig
}