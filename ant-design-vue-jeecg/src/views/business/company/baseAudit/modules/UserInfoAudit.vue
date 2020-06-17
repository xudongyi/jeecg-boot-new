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
        <a-form-item label="姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" >
          <a-input v-decorator="['name']" placeholder="请输入姓名" :disabled="disable"></a-input>
        </a-form-item>
        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag  type="radio" dictCode="sex" v-decorator="['sex']" :disabled="disable"  :trigger-change="true" ></j-dict-select-tag>

        </a-form-item>
        <a-form-item label="学历" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['education']" placeholder="请输入学历" :disabled="disable"></a-input>
        </a-form-item>
        <a-form-item label="专业" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['profession']" placeholder="请输入专业" :disabled="disable"></a-input>
        </a-form-item>
        <a-form-item label="职称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['jobTitle']" placeholder="请输入职称" :disabled="disable"></a-input>
        </a-form-item>
        <a-form-item label="部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['department']" placeholder="请输入部门" :disabled="disable"></a-input>
        </a-form-item>
        <a-form-item label="岗位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['post']" placeholder="请输入岗位" :disabled="disable"></a-input>
        </a-form-item>
        <a-form-item label="身份证号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['idCard']" placeholder="请输入身份证号码" :disabled="disable"></a-input>
        </a-form-item>
        <a-form-item label="手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['pbone']" placeholder="请输入手机号码" :disabled="disable"></a-input>
        </a-form-item>
        <a-form-item label="出生日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请输入出生日期" v-decorator="['birthDate']" :trigger-change="true" style="width: 100%" :disabled="disable" />
        </a-form-item>
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
        name: "UserInfoAudit",
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
              that.form.setFieldsValue(pick(res.result.info,'name','sex','education','profession','jobTitle','department','post','idCard','pbone','birthDate'))
            })

            //判断修改处的  后面需要处理一下
            if(res.cueColor === ''){

            }
          }
        });

        this.$nextTick(() => {
          that.$refs.auditFooter.applyer = this.applyInfo.createBy;
          that.$refs.auditFooter.applyTime = this.applyInfo.createTime;
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