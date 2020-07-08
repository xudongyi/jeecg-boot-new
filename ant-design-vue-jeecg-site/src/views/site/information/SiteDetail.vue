<template>
  <a-layout id="components-layout-demo-top-side">
    <a-layout-content style="padding: 0 0">
      <a-layout style="padding: 24px 0;  background: #fff">
        <a-layout-sider width="200" style="background: #fff">
          <business-menu :item-list="leftMenus" :menu-style="leftStyle"  mode="inline"  @clickHandle = "leftHandle"></business-menu>
        </a-layout-sider>
        <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
          <siteMonitorPoint-modal ref="modalForm" v-show="leftActive==1" :siteType="siteType" :disable="disable"></siteMonitorPoint-modal>
          <site-data-collection-modal ref="dataForm"  v-show="leftActive==2"></site-data-collection-modal>
    </a-layout-content>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>



<script>
    import BusinessMenu from "../component/BusinessMenu";
    import {getDetailMenus,getDataCollection} from '../../requestAction/request';
    import SiteMonitorPointModal from "./modules/SiteMonitorPointModal";
    import SiteDataCollectionModal from "./modules/SiteDataCollectionModal";
    export default {
      name: "SiteDetail",
      components: {
        SiteDataCollectionModal,
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
          siteType:'',
          disable:false
        }
      },
      methods:{
        leftHandle(key){
          this.leftActive = key;
        },
        editMonitor(record){
          this.siteType=record.siteType;
          this.$refs.modalForm.edit(record);
        },editData(){

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
          getDataCollection({id:this.id}).then((res)=>{
            if (res.success) {
              debugger
              this.$refs.dataForm.edit(res.result);
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