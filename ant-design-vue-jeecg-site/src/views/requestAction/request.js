import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'
import Vue from 'vue'

//查询用户所属的企业名称
const queryCompanyName = (params)=>getAction("/site/siteMonitorPoint/queryCompanyName",params);

export {
  queryCompanyName
}