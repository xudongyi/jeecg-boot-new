<template>
  <div>
  <company-apply-list :company-id="companyId" :hoverable="latestArchived"
                      :from-table="fromTable" @applyDetail = "applyDetail"  @toDetail="latestDetail" @toApply="apply"
                      v-if="showDetail ==='list'"></company-apply-list>
    <qualification ref="qModal" :company-id="companyId" v-else-if="showDetail ==='view'" ></qualification>
    <qualification-apply-detail ref="qDetail" :company-id="companyId" v-else = "showDetail ==='apply'" @success="applyOk"></qualification-apply-detail>
    <qualification-apply-modal ref="applyInfoForm"></qualification-apply-modal>
  </div>
</template>

<script>
  import CompanyApplyList from "./modules/CompanyApplyList"
  import {queryLatestArchivedData} from "../../requestAction/request";
  import Qualification from "../oneCompayOneRecord/routeView/Qualification";
  import QualificationApplyModal from "./modules/childModules/QualificationApplyModal";
  import QualificationApplyDetail from "./modules/QualificationApplyDetail";
    export default {
        name: "QualificationApply",
      components:{
        QualificationApplyModal,
        CompanyApplyList,Qualification,
        QualificationApplyDetail
      },
      data(){
          return {
            fromTable:"company_qualification",
            companyId:this.$store.getters.userInfo.companyIds[0],
            showDetail:'list',
            latestArchived:false
          }
      },
      created() {

        let that = this;
        //查询最新归档信息
        queryLatestArchivedData({companyId:this.companyId,fromTable:this.fromTable}).then((res)=>{
          if(res.success){
            that.latestArchived = res.result;
          }else{
            that.$message.warning(res.message);
          }

        })


      },
      methods:{
        latestDetail(){
          this.showDetail = 'view';
          let that = this;
          this.$nextTick(() => {
            that.$refs.qModal.isApply = false;
          });

        },
        apply(){
          this.showDetail = 'apply';
          // let that = this;
          // this.$nextTick(() => {
          //   that.$refs.qModal.isApply = true;
          // });
        },
        applyOk(){
          this.showDetail = 'list';
        },
        applyDetail(record){
          //查询详情数据
          this.$refs.applyInfoForm.detail(record);
          }
        }

    }
</script>

<style scoped>

</style>