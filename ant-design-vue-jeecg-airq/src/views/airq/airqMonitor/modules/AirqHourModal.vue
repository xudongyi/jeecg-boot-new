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


        <a-row>
          <a-col span="12">
            <a-form-item label="采集时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请输入采集时间" v-decorator="['dataTime']" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="SO2(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a21026Avg']" placeholder="请输入SO2" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="NO2(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a21004Avg']" placeholder="请输入NO2" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="PM10(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a3400201Avg']" placeholder="请输入PM10(1h)" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="PM10(24h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a3400224Avg']" placeholder="请输入PM10(24h)" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="CO(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a21005Avg']" placeholder="请输入CO" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="O3(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a0502401Avg']" placeholder="请输入O3(1h)" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="O3(8h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a0502408Avg']" placeholder="请输入O3(8h)" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="PM2.5(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a3400401Avg']" placeholder="请输入PM2.5(1h)" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="PM2.5(24h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a3400424Avg']" placeholder="请输入PM2.5(24h)" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="温度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a01001Avg']" placeholder="请输入温度" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="湿度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a01002Avg']" placeholder="请输入湿度" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="风速" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a01007Avg']" placeholder="请输入风速" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="风向" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['a01008Avg']" :trigger-change="true" dictCode="wind" placeholder="请输入风向"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="小时数据平台状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['state']" :trigger-change="true" dictCode="airDataStatus" placeholder="请选择小时数据平台状态"/>
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
    name: "AirqHourModal",
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
          add: "/hourInsert/airqHour/add",
          edit: "/hourInsert/airqHour/edit",
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
          this.form.setFieldsValue(pick(this.model,'dataTime','createTime','mn','state','level','firstCode','aqi','a01006Avg','a01006Iaqi','a21005Avg','a21005Iaqi','a3400201Avg','a3400201Iaqi','a01007Avg','a01007Iaqi','a21004Avg','a21004Iaqi','a3400424Avg','a3400424Iaqi','a01001Avg','a01001Iaqi','a21002Avg','a21002Iaqi','a0502408Avg','a0502408Iaqi','a3400401Avg','a3400401Iaqi','a0502401Avg','a0502401Iaqi','a01002Avg','a01002Iaqi','a21026Avg','a21026Iaqi','a3400224Avg','a3400224Iaqi','a21003Avg','a21003Iaqi'))
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
        this.form.setFieldsValue(pick(row,'dataTime','createTime','mn','state','level','firstCode','aqi','a01006Avg','a01006Iaqi','a21005Avg','a21005Iaqi','a3400201Avg','a3400201Iaqi','a01007Avg','a01007Iaqi','a21004Avg','a21004Iaqi','a3400424Avg','a3400424Iaqi','a01001Avg','a01001Iaqi','a21002Avg','a21002Iaqi','a0502408Avg','a0502408Iaqi','a3400401Avg','a3400401Iaqi','a0502401Avg','a0502401Iaqi','a01002Avg','a01002Iaqi','a21026Avg','a21026Iaqi','a3400224Avg','a3400224Iaqi','a21003Avg','a21003Iaqi'))
      },

      
    }
  }
</script>