<template>
   <div>

        <company-apply-list :company-id="companyId"
                            :from-table="fromTable" @applyDetail = "applyDetail"  @toDetail="latestDetail" @toApply="apply"
                            ref = "applyList"
                            ></company-apply-list>

    <jmodal-base-info ref="baseInfoForm" @submitOk="OK"></jmodal-base-info>
     <companyApply-modal ref="applyInfoForm" ></companyApply-modal>
   </div>
</template>

<script>

  import CompanyApplyList from "./modules/CompanyApplyList"
  import { queryLatestArchivedData} from "../../requestAction/request"
  import JmodalBaseInfo from "./modules/childModules/JmodalBaseInfo";
  import CompanyApplyModal from "./modules/childModules/CompanyApplyModal";
    export default {
        name: "BasicInfoApply",
      components:{
        JmodalBaseInfo,
        CompanyApplyList,
        CompanyApplyModal
      },
        data(){
          console.log(this.$store.getters.userInfo.companyIds[0])
          return {
            fromTable:"company_baseinfo",

            companyId:this.$store.getters.userInfo.companyIds[0],

          }
        },


      methods:{
          //详情
          latestDetail(){
            //查询详情数据
            this.$refs.baseInfoForm.title="详情";
            this.$refs.baseInfoForm.disableSubmit = false;
            this.$refs.baseInfoForm.visible = true;
            this.$refs.baseInfoForm.confirmLoading = false;

          },
          //新增申请
          apply(){

            this.$refs.baseInfoForm.title="申请";
            this.$refs.baseInfoForm.disableSubmit = true;
            this.$refs.baseInfoForm.visible = true;
            this.$refs.baseInfoForm.confirmLoading = false;


          },
          applyDetail(record){

            //查询详情数据
            this.$refs.applyInfoForm.detail(record);
            //单个表比较
            this.$refs.applyInfoForm.compareDetail(this.fromTable);
          },
        OK(){
          this.$refs.applyList.reInit();
        }


      },

    }
</script>

<style scoped>

</style>