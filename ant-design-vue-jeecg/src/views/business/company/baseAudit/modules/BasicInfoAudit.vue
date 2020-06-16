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

      <audit-footer ref="auditFooter" @success ="success"></audit-footer>

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
        applyInfo:'',
        confirmLoading:true,
        visible: false,
        width:1200,
        title:"基本信息申报审核-基础信息"
      }

    },
    methods:{
      handleCancel () {
        this.visible = false;
      },
      handleOk(){
        this.confirmLoading=true;

        //提交数据，做数据处理
        //调用
        this.$refs.auditFooter.submit(this.applyInfo);

      },
      //关闭窗口
      success(){
        this.visible = false;
      },
      auditModal(record){
        this.applyInfo = record;
        let that = this;
        //查询对应的待审批数据  并查询出对应的对比数据
        queryAduitBase({applyId:record.id}).then((res)=>{
          if(res.success){
            that.$refs.baseModal.edit(res.result.info);

            //判断修改处的  后面需要处理一下
            if(res.cueColor === ''){

            }
          }
        });


          that.$refs.auditFooter.applyer = this.applyInfo.createBy;
          that.$refs.auditFooter.applyTime = this.applyInfo.createTime;

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