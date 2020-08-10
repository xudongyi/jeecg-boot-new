import Vue from "vue";
import { getAction} from '@/api/manage'

const loadAreaDate= async  function (){
  //先从缓存中获取
  let key  ='sys_areas';

  //加载系统列表
  //优先从缓存中读取字典配置
  if( Vue.ls.get(key)){
    return Vue.ls.get(key)
  }
  let result = {};
  //根据字典Code, 初始化字典数组
  await getAction("/sys/sysArea/list",{active:'1'}).then((res) => {
    if (res.success) {

      Vue.ls.set(key, res.result, 24 * 60 * 60 * 1000)
      result = res.result;
    }
  })
  return result;
}
export {loadAreaDate}