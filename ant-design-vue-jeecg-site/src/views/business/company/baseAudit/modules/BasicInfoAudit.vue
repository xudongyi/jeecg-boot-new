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

      <audit-footer ref="auditFooter" @success ="success" :disable="applyInfo.isView"></audit-footer>
      <template slot="footer">
        <a-button type="primary" @click="handleCancel" >关闭</a-button>
        <a-button type="primary" @click="handleOk" v-show="!applyInfo.isView">确认</a-button>
      </template>
    </j-modal>
</template>



<script>
  import BaseInfo from "../../oneCompayOneRecord/routeView/BaseInfo";
  import {queryAduitBase} from "../../../requestAction/request"
  import AuditFooter from "../modules/AuditFooter";

  export default {
        name: "BasicInfoAudit",
      components:{BaseInfo
        ,AuditFooter
      },data(){
      return{
        applyInfo:{},
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

           let  modalStatus = {}
            //全部
            if(res.result.cueColor === 'ALL'){
              for(let i = 0, len = res.result.cueField.length; i < len; i++){
                modalStatus[res.result.cueField[i]] = "warning";
              }

            }else {
              for(let i = 0, len = res.result.cueField.length; i < len; i++){
                modalStatus[res.result.cueField[i]] = "error";
              }
            }
            that.$refs.baseModal.modalStatus = modalStatus;
          }
        });

        this.$nextTick(() => {
          that.$refs.auditFooter.edit( this.applyInfo) ;
        });
        if(this.applyInfo.isView) {
        }
        else {
          that.applyInfo.isView = false;
        }

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