import Vue from 'vue'
import {login, logout, phoneLogin, thirdLogin} from "@/api/login"
import {
  ACCESS_TOKEN,
  USER_NAME,
  USER_INFO,
  USER_AUTH,
  SYS_BUTTON_AUTH,
  UI_CACHE_DB_DICT_DATA
} from "@/store/mutation-types"
import {welcome} from "@/utils/util"
import {queryPermissionsByUser} from '@/api/api'
import {getAction} from '@/api/manage'
import Cookies from 'js-cookie'

const user = {
  state: {
    token: '',
    username: '',
    realname: '',
    welcome: '',
    avatar: '',
    permissionList: [],
    info: {}  //添加 company_id  数据
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, {username, realname, welcome}) => {
      state.username = username
      state.realname = realname
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_PERMISSIONLIST: (state, permissionList) => {
      state.permissionList = permissionList
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
  },

  actions: {
    // CAS验证登录  -- 暂时不用
    ValidateLogin({commit}, userInfo) {
      console.log("cas单点登录验证", userInfo);
      return new Promise((resolve, reject) => {
        getAction("/cas/client/validateLogin", userInfo).then(response => {
          console.log("----cas 登录--------", response);
          if (response.success) {
            const result = response.result
            const userInfo = result.userInfo
            Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_NAME, userInfo.username, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000)
            commit('SET_TOKEN', result.token)
            commit('SET_INFO', userInfo)
            commit('SET_NAME', {username: userInfo.username, realname: userInfo.realname, welcome: welcome()})
            commit('SET_AVATAR', userInfo.avatar)
            resolve(response)
          } else {
            resolve(response)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    CookieLogin({commit}, userInfo) {
      console.log("cas单点登录验证", userInfo);
      return new Promise((resolve, reject) => {
        getAction("/redis/client/validateLogin", userInfo).then(response => {
          console.log("----cas 登录--------", response);
          if (response.success) {
            const result = response.result
            const userInfo = result.userInfo
            Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_NAME, userInfo.username, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000)
            commit('SET_TOKEN', result.token)
            commit('SET_INFO', userInfo)
            commit('SET_NAME', {username: userInfo.username, realname: userInfo.realname, welcome: welcome()})
            commit('SET_AVATAR', userInfo.avatar)
            Cookies.set('_ticket_uid' ,result.tickit, { expires: 7, path: '/' });//更新一下  保存7天
            resolve(response)
          } else {
            resolve(response)
          }
        }).catch(error => {
          resolve(error)
        })
      })
    },
    // 登录
    Login({commit}, userInfo) {

      console.log("登录", userInfo);
      return new Promise((resolve, reject) => {
        login(userInfo).then(response => {
          if (response.code == '200') {
            const result = response.result
            const userInfo = result.userInfo
            Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_NAME, userInfo.username, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(UI_CACHE_DB_DICT_DATA, result.sysAllDictItems, 7 * 24 * 60 * 60 * 1000)
            commit('SET_TOKEN', result.token)
            commit('SET_INFO', userInfo)
            commit('SET_NAME', {username: userInfo.username, realname: userInfo.realname, welcome: welcome()})
            commit('SET_AVATAR', userInfo.avatar)
            Cookies.set('_ticket_uid' ,result.tickit, { expires: 7, path: '/' });//更新一下  保存7天
            console.log( Cookies.get('_ticket_uid'))

            resolve(response)
          } else {
            reject(response)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    //手机号登录
    PhoneLogin({commit}, userInfo) {
      return new Promise((resolve, reject) => {
        phoneLogin(userInfo).then(response => {
          if (response.code == '200') {
            const result = response.result
            const userInfo = result.userInfo
            Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_NAME, userInfo.username, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(UI_CACHE_DB_DICT_DATA, result.sysAllDictItems, 7 * 24 * 60 * 60 * 1000)
            commit('SET_TOKEN', result.token)
            commit('SET_INFO', userInfo)
            commit('SET_NAME', {username: userInfo.username, realname: userInfo.realname, welcome: welcome()})
            commit('SET_AVATAR', userInfo.avatar)
            resolve(response)
          } else {
            reject(response)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 获取用户信息
    GetPermissionList({commit}) {
      console.log("MYP: user GetPermissionList");
      return new Promise((resolve, reject) => {
        let v_token = Vue.ls.get(ACCESS_TOKEN);
        let params = {token: v_token, systemId: window._CONFIG['systemId']};
        queryPermissionsByUser(params).then(response => {
          const menuData = response.result.menu;
          const authData = response.result.auth;
          const allAuthData = response.result.allAuth;
          //Vue.ls.set(USER_AUTH,authData);
          sessionStorage.setItem(USER_AUTH, JSON.stringify(authData));
          sessionStorage.setItem(SYS_BUTTON_AUTH, JSON.stringify(allAuthData));
          if (menuData && menuData.length > 0) {
            //update--begin--autor:qinfeng-----date:20200109------for：JEECG-63 一级菜单的子菜单全部是隐藏路由，则一级菜单不显示------
            menuData.forEach((item, index) => {
              if (item["children"]) {
                let hasChildrenMenu = item["children"].filter((i) => {
                  return !i.hidden || i.hidden == false
                })
                if (hasChildrenMenu == null || hasChildrenMenu.length == 0) {
                  item["hidden"] = true
                }
              }
            })
            console.log(" menu show json ", menuData)
            //update--end--autor:qinfeng-----date:20200109------for：JEECG-63 一级菜单的子菜单全部是隐藏路由，则一级菜单不显示------
            commit('SET_PERMISSIONLIST', menuData)
          } else {
            reject('getPermissionList: permissions must be a non-null array !')
          }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout({commit, state}) {
      return new Promise((resolve) => {
        let logoutToken = state.token;
        commit('SET_TOKEN', '')
        commit('SET_PERMISSIONLIST', [])
        Vue.ls.remove(ACCESS_TOKEN)
        Vue.ls.remove(UI_CACHE_DB_DICT_DATA)
        //console.log('logoutToken: '+ logoutToken)
        logout(logoutToken).then(() => {
          // var sevice = "http://"+window.location.host+"/";
          // var serviceUrl = encodeURIComponent(sevice);
          // window.location.href = window._CONFIG['casPrefixUrl']+"/logout?service="+serviceUrl;
          Cookies.remove('_ticket_uid');

          resolve()
        }).catch(() => {
          resolve()
        })
      })
    },
    // 第三方登录
    ThirdLogin({commit}, token) {
      return new Promise((resolve, reject) => {
        thirdLogin(token).then(response => {
          if (response.code == '200') {
            const result = response.result
            const userInfo = result.userInfo
            Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_NAME, userInfo.username, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000)
            commit('SET_TOKEN', result.token)
            commit('SET_INFO', userInfo)
            commit('SET_NAME', {username: userInfo.username, realname: userInfo.realname, welcome: welcome()})
            commit('SET_AVATAR', userInfo.avatar)
            resolve(response)
          } else {
            reject(response)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },

  }
}

export default user