<template>
  <div>


      <company-apply-list :company-id="companyId"
                        :from-table="fromTable" @applyDetail = "applyDetail"  @toDetail="latestDetail" @toApply="apply"
                         v-if="!showDetail"></company-apply-list>

    <userinfo-apply-list :isApply="isApply" :company-id="companyId" v-if="showDetail" ></userinfo-apply-list>
    <companyApply-modal ref="applyInfoForm" ></companyApply-modal>
  </div>
</template>

<script>

  import CompanyApplyList from "./modules/CompanyApplyList"
  import {queryLatestArchivedData} from "../../requestAction/request"
  import CompanyApplyModal from "./modules/childModules/CompanyApplyModal";
  import UserinfoApplyList from "./modules/UserinfoApplyList";

  export default {
        name: "UserInfoApply",
      components:{
        CompanyApplyList,
        queryLatestArchivedData,
        CompanyApplyModal,
        UserinfoApplyList
      },
      data(){
        return {
          fromTable:"company_userinfo",
          //最新归档信息数据
          companyId:this.$store.getters.userInfo.companyIds[0],
          showDetail:false,
          isApply:false
        }
      },//计算属性

      methods:{
        //详情
        latestDetail(){
          //查询详情数据
          this.showDetail = true;
          this.isApply = false;
        },
        //新增申请
        apply(){
          //查询详情数据
          this.showDetail = true;
          this.isApply = true;

        },
        applyDetail(record){
          //查询详情数据
          this.$refs.applyInfoForm.detail(record);
          //单个表比较
          this.$refs.applyInfoForm.compareDetail(this.fromTable);
        },


      },

    }
</script>

<style scoped>

</style>