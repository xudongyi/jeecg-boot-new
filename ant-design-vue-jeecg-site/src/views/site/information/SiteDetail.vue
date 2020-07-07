<template>
  <a-layout id="components-layout-demo-top-side">
    <a-layout-content style="padding: 0 0">
      <a-layout style="padding: 24px 0;  background: #fff">
        <a-layout-sider width="200" style="background: #fff">
          <business-menu  ref = 'leftmenu' :item-list="leftMenus" :menu-style="leftStyle"  mode="inline"  @clickHandle = "leftHandle"></business-menu>
        </a-layout-sider>
        <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
          <siteMonitorPoint-modal ref="modalForm" @ok="modalFormOk" v-if="leftActive==1"></siteMonitorPoint-modal>
    </a-layout-content>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>



<script>
    import BusinessMenu from "../../business/component/BusinessMenu";
    import {getDetailMenus} from '../../requestAction/request';
    import SiteMonitorPointModal from "./modules/SiteMonitorPointModal";
    export default {
      name: "SiteDetail",
      components: {
        BusinessMenu,
        SiteMonitorPointModal
      },
      props:{
        id:''
      },
      data(){
        return {
          leftActive:1,
          leftMenus:[],
          menus : [],
          leftStyle :{
            top: '0px',
            left:'0px'
          },
        }
      },
      methods:{
        leftHandle(key){
          this.leftActive = key;
        }
      },
      created() {
        let _this = this;
        debugger
        if(_this.id) {
          //发送请求，查找
          getDetailMenus().then((res) => {
            if (res.success) {
              _this.menus = res.result.menus;
              _this.leftMenus = this.menus;
            } else {
              console.log(res.message);
            }
          });
        }

      }

    }
</script>

<style scoped>

</style>