<template>
    <j-modal
      ref="modal"
      :title="title"
      :width="width"
      :visible="visible"
      :confirmLoading="confirmLoading"
      switchFullscreen
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭"
      v-if="visible"
    >
      <base-info ref="baseModal"  :ftitle="title"  @OK="modalFormOk"></base-info>

      <audit-footer ref="auditFooter"></audit-footer>

    </j-modal>
</template>



<script>
  import BaseInfo from "../../oneCompayOneRecord/routeView/BaseInfo";
  import {queryAduitBase,queryUserByName} from "../../../requestAction/request"
  import AuditFooter from "../modules/AuditFooter";
  import store from '@/store/'
  export default {
        name: "BasicInfoAudit",
      components:{BaseInfo
        ,AuditFooter
      },data(){
      return{
        applyId:'',
        confirmLoading:true,
        visible: false,
        width:1200,
        title:"基本信息申报审核-基础信息"
      }

    },
    methods:{
      handleCancel () {
        this.visible = false;
        // this.close()
      },
      handleOk(){
        this.$refs.baseModal.handleOk();

        this.visible = false;

      },
      auditModal(record){
        this.applyId = record.id;
        let that = this;
        //查询对应的待审批数据  并查询出对应的对比数据
        queryAduitBase({applyId:record.id}).then((res)=>{
          if(res.success){
            that.$refs.baseModal.edit(res.result.baseInfo);



            //判断修改处的
            if(res.cueColor === ''){

            }
          }
        });

        //申请人的真实名字
        queryUserByName({userName:record.createBy}).then((res)=> {
          if(res.success){
            that.$refs.auditFooter.applyer = res.result.realname;
            that.$refs.auditFooter.applyTime = record.createTime;

          }
        })
        this.confirmLoading = false;
      },
      modalFormOk(){

      }

    },created() {
      console.log("Jmodal",this.title)
    }
  }
</script>

<style scoped>

</style>