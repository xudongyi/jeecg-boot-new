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

        <a-form-item label="sourceId" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['sourceId']" placeholder="请输入sourceId"></a-input>
        </a-form-item>
        <a-form-item label="mn" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['mn']" placeholder="请输入mn"></a-input>
        </a-form-item>
        <a-form-item label="code" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['code']" placeholder="请输入code"></a-input>
        </a-form-item>
        <a-form-item label="value" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['value']" placeholder="请输入value" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="standardValue" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['standardValue']" placeholder="请输入standardValue" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="status" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['status']" placeholder="请输入status" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="dataTime" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择dataTime" v-decorator="['dataTime']" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="beginTime" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择beginTime" v-decorator="['beginTime']" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="endTime" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择endTime" v-decorator="['endTime']" :trigger-change="true" style="width: 100%"/>
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


  export default {
    name: "AirCurrentOverproofModal",
    components: { 
      JDate,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
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
          add: "/airCurrentOverproof/airCurrentOverproof/add",
          edit: "/airCurrentOverproof/airCurrentOverproof/edit",
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
          this.form.setFieldsValue(pick(this.model,'sourceId','mn','code','value','standardValue','status','dataTime','beginTime','endTime'))
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
        this.form.setFieldsValue(pick(row,'sourceId','mn','code','value','standardValue','status','dataTime','beginTime','endTime'))
      },

      
    }
  }
</script>