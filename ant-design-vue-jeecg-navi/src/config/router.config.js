import { UserLayout, TabLayout, RouteView, BlankLayout, PageView } from '@/components/layouts'

/**
 * 走菜单，走权限控制
 * @type {[null,null]}
 */
// export const asyncRouterMap = [
//
//   {
//     path: '/',
//     name: 'dashboard',
//     component: TabLayout,
//     meta: { title: '首页' },
//     redirect: '/dashboard/analysis',
//     children: [
//       {
//         path:'/dashboard/analysis',
//         name:'dashboard-analysis',
//         component: () => {
//           console.log(import('@/views/dashboard/Analysis'))
//           return import('@/views/dashboard/Analysis')}
//       }
//     ]
//   },
//   {
//     path: '*', redirect: '/404', hidden: true
//   }
// ]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',//重定向到userlogin
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/RegisterResult')
      },
      {
        path: 'alteration',
        name: 'alteration',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Alteration')
      },
    ]
  },

  {
    path: '/',
    name: 'dashboard',
    component: TabLayout,
    meta: { title: '首页' },
    redirect: '/dashboard/analysis',
    children: [
      {
        path:'/dashboard/analysis',
        name:'dashboard-analysis',
        component: () => {
          console.log(import('@/views/dashboard/Analysis'))
          return import('@/views/dashboard/Analysis')}
      }
    ]
  },
  {
    path: '*',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  },
  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }
]
