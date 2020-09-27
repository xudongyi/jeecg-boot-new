<template>
  <a-layout id="components-layout-demo-top-side">
    <a-layout-header class="header">
      <business-menu ref = 'headmenu' :item-list="headMenus" :menu-style="headStyle"  mode="horizontal"  @clickHandle = "headHandle" ></business-menu>

    </a-layout-header>
    <a-layout-content style="padding: 0 0">

      <a-layout style="padding: 24px 0;  background: #fff">
        <a-layout-sider width="200" style="background: #fff">

          <business-menu  ref = 'leftmenu' :item-list="leftMenus" :menu-style="leftStyle"  mode="inline"  @clickHandle = "leftHandle"></business-menu>
        </a-layout-sider>
        <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
          <base-info v-if="leftActive==1 && topActive==1"  :companyId="companyId"/>
          <qualification v-if="leftActive==2 && topActive==1"  :companyId="companyId"/>
          <userinfo-list v-if="leftActive==3 && topActive==1" :companyId="companyId"/>
          <product-material-list v-if="leftActive==4 && topActive==1" :companyId="companyId"/>
          <env-trial-list v-if="leftActive==5 && topActive==1" :companyId="companyId"/>
          <company-acceptance-list v-if="leftActive==6 && topActive==1" :companyId="companyId" :listType="2" :operation-show="false"/>
          <prevention v-if="leftActive==7 && topActive==1" :companyId="companyId" :listType="2" :operation-show="false"/>
          <company-dirty-allow-list v-if="leftActive==8 && topActive==1" :companyId="companyId" :listType="2" :operation-show="false"/>
          <company-risk-waste-list v-if="leftActive==9 && topActive==1" :companyId="companyId" :listType="2" :operation-show="false"/>
          <company-solid-waste-list v-if="leftActive==10 && topActive==1" :companyId="companyId" :listType="2" :operation-show="false"/>
          <company-radiate-waste-list v-if="leftActive==11 && topActive==1" :companyId="companyId" :listType="2" :operation-show="false"/>
          <company-env-tax-list v-if="leftActive==12 && topActive==1" :companyId="companyId" listType="2" :operation-show="false"/>
          <company-clean-product-list v-if="leftActive==13 && topActive==1" :companyId="companyId" listType="2" :operation-show="false"/>
          <company-online-info-list v-if="leftActive==14 && topActive==1" :companyId="companyId" listType="2" :operation-show="false"/>
          <source-directory-modal  v-if="leftActive==15 && topActive==1" :companyId="companyId" listType="2" :operation-show="true"/>
          <site-monitor-point-list v-if="leftActive==16 && topActive==1" :companyId="companyId" listType="2" :operation-show="true"/>

          <company-dynamic-supervision-list v-if="leftActive==1 && topActive==2" :companyId="companyId" listType="0" role="view"/>
          <company-admin-penalties-list v-if="leftActive==2 && topActive==2" :companyId="companyId" listType="0" role="view"/>
          <company-supervisory-monitor-list v-if="leftActive==3 && topActive==2" :companyId="companyId" listType="0" role="view"/>
          <company-complaint-letter-list v-if="leftActive==4 && topActive==2" :companyId="companyId" listType="0" role="view"/>

        </a-layout-content>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>



<script>
    import BusinessMenu from "../../component/BusinessMenu";

    import {getDetailMenus} from "../../requestAction/request"
    import Qualification from "./routeView/Qualification";
    import CompanyAcceptanceList from "./routeView/CompanyAcceptanceList";
    import BaseInfo from "./routeView/BaseInfo";
    import CompanyDynamicSupervisionList from './routeView/CompanyDynamicSupervisionList'
    import UserinfoList from "./routeView/UserinfoList"
    import ProductMaterialList from "./routeView/ProductMaterialList";
    import Prevention from "./routeView/Prevention";
    import CompanyDirtyAllowList from "./routeView/CompanyDirtyAllowList";
    import CompanyAdminPenaltiesList from "./CompanyAdminPenaltiesList";
    import CompanySupervisoryMonitorList from "./CompanySupervisoryMonitorList";
    import CompanyEnvTaxList from "./routeView/CompanyEnvTaxList";
    import CompanyComplaintLetterList from "./routeView/CompanyComplaintLetterList";
    import CompanyCleanProductList from "./routeView/CompanyCleanProductList";
    import CompanyOnlineInfoList from "./routeView/CompanyOnlineInfoList";
    import SourceDirectoryModal from "./routeView/SourceDirectoryModal";
    import SiteMonitorPointList from "./routeView/SiteMonitorPointList";

    import CompanyRadiateWasteList from "./routeView/CompanyRadiateWasteList";
    import CompanySolidWasteList from "./routeView/CompanySolidWasteList";
    import CompanyRiskWasteList from "./routeView/CompanyRiskWasteList";
    import EnvTrialList from "./routeView/EnvTrialList";
    export default {
      name: "CompanyDetail",
      components: {
        CompanyAcceptanceList,
        BusinessMenu,BaseInfo,Qualification,Prevention,CompanyDirtyAllowList,CompanyDynamicSupervisionList, CompanyAdminPenaltiesList,
        CompanySupervisoryMonitorList,CompanyComplaintLetterList,
        UserinfoList,
        ProductMaterialList,
        CompanyEnvTaxList,
        CompanyCleanProductList,
        CompanyOnlineInfoList,
        SourceDirectoryModal,
        EnvTrialList,
        CompanyRadiateWasteList,
        CompanySolidWasteList,
        CompanyRiskWasteList,
        SiteMonitorPointList
      },
      props:{

      },
      data(){
        return {
          companyId:this.$route.query.companyId,
          topActive:1,
          leftActive:1,
          leftMenus:[],
          basicInfoMenus : [],
          superviseMenus:[],
          leftStyle :{
            top: '0px',
            left:'0px'
          },
          headMenus : [
            {"key":"1","text":"基础信息"},
            {"key":"2","text":"监督检查"},
          ],
          headStyle :{
            lineHeight: '64px'
          }
        }
      },
      methods:{
        headHandle(key){
          this.topActive = key;
          //key1为 基础信息  2为监督检查
          if(key==="1"){
            this.leftMenus = this.basicInfoMenus;
          }else{
            this.leftMenus = this.superviseMenus;
          }
          this.leftActive=1;
        },
        leftHandle(key){

          this.leftActive = key;
        }
      },
      watch:{
        "$route": {
          handler(route) {
            this.companyId = route.query.companyId;
            if(route.query.companyId)
            {
              let _this = this;
              //发送请求，查找
              getDetailMenus({companyId: route.query.companyId}).then((res) => {
                if (res.success) {
                  _this.basicInfoMenus = res.result.basicInfoMenus;
                  _this.superviseMenus = res.result.superviseMenus;
                  _this.leftMenus = this.basicInfoMenus;
                  _this.topActive=1;
                  _this.leftActive=1;
                } else {
                  console.log(res.message);
                }
              });
            }
          },

        },
        topActive:{
          handler(active) {
            this.$refs.headmenu.assign(active);
          }
        },
        leftActive:{
          handler(active) {
            this.$refs.leftmenu.assign(active);
          }
        }
      },
      created() {

        let _this = this;
        if(this.companyId) {
          //发送请求，查找
          getDetailMenus({companyId: this.companyId}).then((res) => {
            if (res.success) {
              _this.basicInfoMenus = res.result.basicInfoMenus;
              _this.superviseMenus = res.result.superviseMenus;
              _this.leftMenus = this.basicInfoMenus;

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