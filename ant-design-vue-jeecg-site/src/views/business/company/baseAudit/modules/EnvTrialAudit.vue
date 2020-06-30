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
  >
  <a-form :form="form">

    <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" :validate-status="modalStatus.projectName">
      <a-input v-decorator="['projectName']" placeholder="请输入项目名称" :disabled="disable"></a-input>
    </a-form-item>
    <a-form-item label="批复文件号" :labelCol="labelCol" :wrapperCol="wrapperCol" :validate-status="modalStatus.approveFilenum">
      <a-input v-decorator="['approveFilenum']" placeholder="请输入批复文件号" :disabled="disable"></a-input>
    </a-form-item>
    <a-form-item label="审批单位" :labelCol="labelCol" :wrapperCol="wrapperCol" :validate-status="modalStatus.approveUnit">
      <a-input v-decorator="['approveUnit']" placeholder="请输入审批单位" :disabled="disable"></a-input>
    </a-form-item>
    <a-form-item label="审批时间" :labelCol="labelCol" :wrapperCol="wrapperCol" :validate-status="modalStatus.approveDate">
      <j-date placeholder="请选择审批时间" v-decorator="['approveDate']" :trigger-change="true" style="width: 100%" :disabled="disable"/>
    </a-form-item>
    <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol" >
      <j-upload ref="uploadRef" :disabled="disable" fileType="file" bizPath="envtrial"></j-upload>
    </a-form-item>
    <!--加一个表格显示-->

  </a-form>

    <audit-footer ref="auditFooter" @success ="success" :disable="applyInfo.isView"></audit-footer>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel" >关闭</a-button>
      <a-button type="primary" @click="handleOk" v-show="!applyInfo.isView">确认</a-button>
    </template>
  </j-modal>
</template>

<script>
  import {queryAduitBase, queryenvTrialFiles, queryFiles} from "../../../requestAction/request"
  import AuditFooter from "../modules/AuditFooter";
  import pick from "lodash.pick";
  import JDictSelectTag from  '@/components/dict/JDictSelectTag.vue'
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'

  export default {
        name: "EnvTrialAudit",
      components:{
        AuditFooter,JDictSelectTag,JDate,JUpload
      },data(){
        return{
          applyInfo:{},
          confirmLoading:true,
          visible: false,
          width:1200,
          title:"基本信息申报审核-环评审批信息",
          disable:true,
          form: this.$form.createForm(this),
          labelCol: {
            xs: { span: 24 },
            sm: { span: 5 },
          },
          wrapperCol: {
            xs: { span: 24 },
            sm: { span: 16 },
          },
          modalStatus:{},
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
              that.$nextTick(() => {
                this.form.setFieldsValue(pick(res.result.info,'projectName','approveFilenum','approveUnit','approveDate','annex'));

              });

              //全部
              if (res.result.cueColor === 'ALL') {
                for (let i = 0, len = res.result.cueField.length; i < len; i++) {
                  that.modalStatus[res.result.cueField[i]] = "warning";
                }

              } else {
                for (let i = 0, len = res.result.cueField.length; i < len; i++) {
                  that.modalStatus[res.result.cueField[i]] = "error";
                }
              }
            }
          });
          queryFiles({id:record.newId},"/company/envTrial/queryFiles").then((res)=>{
            that.$nextTick(() => {
              that.$refs.uploadRef.initFileListArr(res.result);
            });

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
      }
    }
</script>

<style scoped>

</style>