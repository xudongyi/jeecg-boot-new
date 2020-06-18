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

    <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input v-decorator="['projectName']" placeholder="请输入项目名称" :disabled="disable"></a-input>
    </a-form-item>
    <a-form-item label="批复文件号" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input v-decorator="['approveFilenum']" placeholder="请输入批复文件号" :disabled="disable"></a-input>
    </a-form-item>
    <a-form-item label="审批单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input v-decorator="['approveUnit']" placeholder="请输入审批单位" :disabled="disable"></a-input>
    </a-form-item>
    <a-form-item label="审批时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <j-date placeholder="请选择审批时间" v-decorator="['approveDate']" :trigger-change="true" style="width: 100%" :disabled="disable"/>
    </a-form-item>
    <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <j-upload ></j-upload>
    </a-form-item>
    <!--加一个表格显示-->

  </a-form>

    <audit-footer ref="auditFooter" @success ="success"></audit-footer>

  </j-modal>
</template>

<script>
  import {queryAduitBase} from "../../../requestAction/request"
  import AuditFooter from "../modules/AuditFooter";
  import pick from "lodash.pick";
  import JDictSelectTag from  '@/components/dict/JDictSelectTag.vue'
  import JDate from '@/components/jeecg/JDate'
    export default {
        name: "EnvTrialAudit",
      components:{
        AuditFooter,JDictSelectTag,JDate
      },data(){
        return{
          applyInfo:'',
          confirmLoading:true,
          visible: false,
          width:1200,
          title:"基本信息申报审核-员工信息",
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
                this.form.setFieldsValue(pick(res.result.info,'outputType','outputName','yield','maxStore','cas','storeType','hazardousChemicalsCategory','mainRisk','supervision','toxic','precursorChemicals','status','certified','rawMaterials','proEquipment','remake'))

              });

              //判断修改处的  后面需要处理一下
              if(res.cueColor === ''){

              }
            }
          });

          this.$nextTick(() => {
            that.$refs.auditFooter.applyer = this.applyInfo.createBy;
            that.$refs.auditFooter.applyTime = this.applyInfo.createTime;
          });

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