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

        <div>
          <a-row>
            <a-col span="12">
              <a-form-item label="联系人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['name']" placeholder="请输入联系人姓名"></a-input>
              </a-form-item>
            </a-col>
            <a-col span="12">
              <a-form-item label="手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['mobile']" placeholder="请输入手机号码"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col span="12">
              <a-form-item label="所属单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['companyName']" placeholder="请输入所属单位"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <div>

        </div>


      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'


  export default {
    name: "SysWarnUserPointModal",
    components: { 
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:1000,
        visible: false,
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
          add: "/swup/sysWarnUserPoint/add",
          edit: "/swup/sysWarnUserPoint/edit",
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
          this.form.setFieldsValue(pick(this.model,'name','mobile','companyName'))
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
        this.form.setFieldsValue(pick(row,'warnUserid','monitorId'))
      },

      
    }
  }
</script>