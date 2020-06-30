<template>
  <div>
    <company-apply-list v-show="!listshow" :company-id="companyId" :from-table="fromTable"
                         @toDetail="detail" @toApply="apply"
                        @applyDetail="viewApply" ref="applyList"></company-apply-list>
    <company-radiate-waste-list v-if="listshow" :company-id="companyId" :operationShow="operationShow" :listType="listType" @reloadApply="reload"
    ></company-radiate-waste-list>
    <companyApply-modal ref="applyInfoForm"></companyApply-modal>
  </div>
</template>

<script>

  import CompanyApplyList from "./modules/CompanyApplyList"
  import CompanyRadiateWasteList from "../oneCompayOneRecord/routeView/CompanyRadiateWasteList";
  import {queryComparisonData, queryLatestArchivedData} from "../../requestAction/request"
  import CompanyApplyModal from "./modules/childModules/CompanyApplyModal";

  export default {
    name: "RadiateWasteApply",
    components: {
      CompanyApplyList,
      queryLatestArchivedData,
      CompanyRadiateWasteList,
      CompanyApplyModal
    },
    data() {
      return {
        listshow: false,
        fromTable: "company_radiate_waste",
        hoverable: true,
        //最新归档信息数据

        companyId: this.$store.getters.userInfo.companyIds[0],
        operationShow: false,
        listType: "0"
      }
    },
    //计算属性
    computed: {},
    methods: {
      //详情
      detail() {
        this.title = "详情";
        this.listshow = true;
        this.operationShow = false;
        this.listType = "1";
      },
      //申报
      apply() {
        this.title = "申报";
        this.listshow = true;
        this.operationShow = true;
        this.listType = "0";
      },
      //查看
      viewApply(record) {

        console.log(record)
        //查询详情数据
        this.$refs.applyInfoForm.detail(record);
        //单个表比较
        this.$refs.applyInfoForm.compareDetail(this.fromTable);
      },
      reload(){
        this.$refs.applyList.reInit();
        this.listshow=false;
      }


    },

  }
</script>

<style scoped>

</style>