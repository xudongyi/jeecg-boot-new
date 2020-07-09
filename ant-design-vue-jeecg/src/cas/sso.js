import Vue from 'vue'
import { ACCESS_TOKEN } from "@/store/mutation-types"
import store from '@/store'
import Cookies from 'js-cookie'
/**
 * 单点登录
 */
const init = (callback) => {
  debugger
  console.log("-------单点登录开始-------");
  let token = Vue.ls.get(ACCESS_TOKEN);
  let sevice = "http://"+window.location.host+"/";
  let cookie = Cookies.get('_ticket_uid');
  if(token){
    loginSuccess(callback);
  }else{
    if(cookie){
      store.dispatch('CookieLogin',{tc:cookie}).then(res => {
        //this.departConfirm(res)
        if(res.success){
          loginSuccess(callback);
        }else{
          callback()
        }
      }).catch((err) => {
        console.log(err);
        //that.requestFailed(err);
      });
    }else{
      callback()
    }
  }
  console.log("-------单点登录结束-------");
};
const SSO = {
  init: init
};

function getUrlParam(paraName) {
  let url = document.location.toString();
  let arrObj = url.split("?");

  if (arrObj.length > 1) {
    let arrPara = arrObj[1].split("&");
    let arr;

    for (let i = 0; i < arrPara.length; i++) {
      arr = arrPara[i].split("=");

      if (arr != null && arr[0] == paraName) {
        return arr[1];
      }
    }
    return "";
  }
  else {
    return "";
  }
}

function validateSt(ticket,service,callback){
  let params = {
    ticket: ticket,
    service:service
  };
  store.dispatch('ValidateLogin',params).then(res => {
    //this.departConfirm(res)
    if(res.success){
      loginSuccess(callback);
    }else{
      let sevice = "http://"+window.location.host+"/";
      let serviceUrl = encodeURIComponent(sevice);
      window.location.href = window._CONFIG['casPrefixUrl']+"/login?service="+serviceUrl;
    }
  }).catch((err) => {
    console.log(err);
    //that.requestFailed(err);
  });
}

function loginSuccess (callback) {
  callback();
}
export default SSO;