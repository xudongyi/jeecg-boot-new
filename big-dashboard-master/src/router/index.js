import Vue from 'vue'
import Router from 'vue-router'
import Dashboard from '../views/Dashboard'

Vue.use(Router)

const constantRouterMap =[
    {
        path:'/dashboard',
        component:Dashboard
    },
    {
        path:'/comprehensiveAnalysis',
        component:() => import( '../views/pages/comprehensiveAnalysis')
    },
];


export default new Router({
    routes:constantRouterMap
})
