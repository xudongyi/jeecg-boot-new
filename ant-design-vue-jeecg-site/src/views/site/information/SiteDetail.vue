<template>
  <a-layout id="components-layout-demo-top-side">
    <a-layout-content style="padding: 0 0">
      <a-layout style="padding: 24px 0;  background: #fff">
        <a-layout-sider width="200" style="background: #fff">
          <business-menu :item-list="leftMenus" :menu-style="leftStyle"  mode="inline"  @clickHandle = "leftHandle"></business-menu>
        </a-layout-sider>
        <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
          <siteMonitorPoint-modal ref="modalForm" v-show="leftActive==1" :siteType="siteType" disable="false"></siteMonitorPoint-modal>
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
        id:""
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
          siteType:''
        }
      },
      methods:{
        leftHandle(key){
          this.leftActive = key;
        },
        edit(record){
          this.siteType=record.siteType;
          this.$refs.modalForm.edit(record);
        }
      },
      created() {
        let _this = this;
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