<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="报警类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['ruleType']" :trigger-change="true" dictCode="rule_type" placeholder="请选择报警类型" :disabled="disableSubmit"/>
        </a-form-item>
        <a-form-item label="报警级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['ruleLevel']" :trigger-change="true" dictCode="rule_level" placeholder="请选择报警级别" :disabled="disableSubmit"/>
        </a-form-item>
        <a-form-item label="是否发送短信" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['isSendMsg']" placeholder="请输入是否发送短信" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="发送频率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['msgRate']" placeholder="请输入发送频率" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="发送短信开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择发送短信开始时间" v-decorator="['warnStarttime']" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" :disabled="disableSubmit"/>
        </a-form-item>
        <a-form-item label="发送短信结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择发送短信结束时间" v-decorator="['warnEndtime']" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" :disabled="disableSubmit"/>
        </a-form-item>
        <a-form-item label="策略说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['content']" placeholder="请输入策略说明" :rows="2" :disabled="disableSubmit"/>
        </a-form-item>
        <a-form-item label="策略状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['isUsed']" placeholder="请输入策略状态" :disabled="disableSubmit"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JDictSelectTag from "@/components/dict/JDictSelectTag"


  export default {
    name: "SysWarnRuleModal",
    components: { 
      JDate,
      JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        disableSubmit:'',
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
        },
        url: {
          add: "/swr/sysWarnRule/add",
          edit: "/swr/sysWarnRule/edit",
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'ruleType','ruleLevel','isSendMsg','msgRate','warnStarttime','warnEndtime','content','isUsed'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'ruleType','ruleLevel','isSendMsg','msgRate','warnStarttime','warnEndtime','content','isUsed'))
      },

      
    }
  }
</script>