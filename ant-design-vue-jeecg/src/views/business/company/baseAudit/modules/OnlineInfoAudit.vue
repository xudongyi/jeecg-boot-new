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

        <a-form-item label="在线设备名称/型号" :labelCol="labelCol" :wrapperCol="wrapperCol" :validate-status="modalStatus.equipmentName">
          <a-input v-decorator="['equipmentName', validatorRules.equipmentName]" placeholder="请输入在线设备名称/型号" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="设备生产厂家" :labelCol="labelCol" :wrapperCol="wrapperCol" :validate-status="modalStatus.equipmentManufacturers">
          <a-input v-decorator="['equipmentManufacturers']" placeholder="请输入设备生产厂家" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="运维单位" :labelCol="labelCol" :wrapperCol="wrapperCol" :validate-status="modalStatus.operationalUnit">
          <a-input v-decorator="['operationalUnit']" placeholder="请输入运维单位" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="投入使用日期" :labelCol="labelCol" :wrapperCol="wrapperCol" :validate-status="modalStatus.usedTime">
          <j-date placeholder="请选择投入使用日期" v-decorator="['usedTime']" :trigger-change="true" style="width: 100%" :disabled="disableSubmit"/>
        </a-form-item>
        <a-form-item label="安装位置" :labelCol="labelCol" :wrapperCol="wrapperCol" :validate-status="modalStatus.installLocation">
          <a-input v-decorator="['installLocation']" placeholder="请输入安装位置" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="在线监控验收材料" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['files']" :trigger-change="true" :disabled="disableSubmit"></j-upload>
        </a-form-item>

      </a-form>
    </a-spin>
    <audit-footer ref="auditFooter" @success ="success" :disable="applyInfo.isView"></audit-footer>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel" >关闭</a-button>
      <a-button type="primary" @click="handleOk" v-show="!applyInfo.isView">确认</a-button>
    </template>  </j-modal>
</template>



<script>

  import pick from "lodash.pick";
  import AuditFooter from "./AuditFooter";
  import {queryAduitBase, queryUserByName} from "../../../requestAction/request";
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'
  export default {
        name: "OnlineInfoAudit",
      components:{
        AuditFooter,
        JDate,
        JUpload,
      },data(){
      return{
        applyInfo:{},
        companyId:'',
        confirmLoading:true,
        disableSubmit:true,
        visible: false,
        width:1200,
        title:"基础信息审核-竣工验收信息",
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
        validatorRules: {
          equipmentName: {
            rules: [
              { required: true, message: '请输入在线设备名称/型号!'},
            ]
          },
        },
        url: {
        },
        modalStatus:{

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
              that.form.setFieldsValue(pick(this.model,'equipmentName','equipmentManufacturers','operationalUnit','usedTime','installLocation'))
            })
            //全部
            if(res.result.cueColor === 'ALL'){
              for(let i = 0, len = res.result.cueField.length; i < len; i++){
                that.modalStatus[res.result.cueField[i]] = "warning";
              }
            }else {
              for(let i = 0, len = res.result.cueField.length; i < len; i++){
                that.modalStatus[res.result.cueField[i]] = "error";
              }
            }
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

      },
    },created() {

    }
    }
</script>

<style scoped>

</style>