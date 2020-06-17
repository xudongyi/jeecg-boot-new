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
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="清洁生产报告名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['reportName', validatorRules.reportName]" placeholder="请输入清洁生产报告名称" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="报告时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择报告时间" v-decorator="['reportTime']" :trigger-change="true" style="width: 100%" :disabled="disableSubmit"/>
        </a-form-item>
        <a-form-item label="清洁生成报告及专家意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['opinionFiles']" :trigger-change="true" :disabled="disableSubmit"></j-upload>
        </a-form-item>
        <a-form-item label="落实情况简要描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['conditionDescribe']" placeholder="请输入落实情况简要描述" :disabled="disableSubmit"></a-textarea>
        </a-form-item>
        <a-form-item label="落实情况附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-image-upload isMultiple v-decorator="['describeFiles']" :disabled="disableSubmit"></j-image-upload>
        </a-form-item>

      </a-form>
    </a-spin>
    <audit-footer ref="auditFooter" @success ="success"></audit-footer>
  </j-modal>
</template>



<script>

  import pick from "lodash.pick";
  import AuditFooter from "./AuditFooter";
  import {queryAduitBase, queryUserByName} from "../../../requestAction/request";
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'
  import JImageUpload from '@/components/jeecg/JImageUpload'
  export default {
        name: "CleanProductAudit",
      components:{
        AuditFooter,
        JDate,
        JUpload,
        JImageUpload
      },data(){
      return{
        companyId:'',
        confirmLoading:true,
        disableSubmit:true,
        visible: false,
        width:1200,
        title:"基础信息审核-清洁生产信息",
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          reportName: {
            rules: [
              { required: true, message: '请输入清洁生产报告名称!'},
            ]
          },
        },
        url: {
        }

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
            that.form.resetFields();
            that.model = Object.assign({}, res.result.info);
            that.visible = true;
            that.$nextTick(() => {
              that.form.setFieldsValue(pick(this.model,'reportName','reportTime','opinionFiles','conditionDescribe','describeFiles'))
            })


            //判断修改处的  后面需要处理一下
            if(res.cueColor === ''){

            }
          }
        });
        that.$nextTick(() => {
          that.$refs.auditFooter.applyer = this.applyInfo.createBy;
          that.$refs.auditFooter.applyTime = this.applyInfo.createTime;
        })
        this.confirmLoading = false;
      },
      modalFormOk(){

      },
    },created() {

    }
    }
</script>

<style scoped>

</style>