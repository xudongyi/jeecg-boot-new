import Vue from 'vue';
import App from './App.vue';
import './styles/index.scss';
import store from './store/'
import dataV from '@jiaminghi/data-view'
import 'ant-design-vue/dist/antd.css';
import echarts from 'echarts'
import  Antd  from 'ant-design-vue';
Vue.use( Antd );
Vue.use(dataV);
Vue.config.productionTip = false;


Vue.prototype.$original_height=1080;
Vue.prototype.$original_width=1920;

Vue.prototype.$echarts = echarts;//全局
new Vue({
    store,
    render: h => h(App)
}).$mount('#app');
