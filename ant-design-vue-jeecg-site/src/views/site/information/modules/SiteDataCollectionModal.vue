<template>
  <a-spin :spinning="confirmLoading">
    <a-button type="primary" @click="handleOk">修改</a-button>
    <a-form :form="form">
      <a-row>
        <a-col span='12'>
          <a-form-item label="数采仪mn号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['mnCode', validatorRules.mnCode]" placeholder="请输入数采仪mn号"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="实时上传间隔（s）" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['currentInterval', validatorRules.currentInterval]"
                     placeholder="请输入实时上传间隔（s）"  addon-after="秒"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="分钟上传间隔（minute）" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['minuteInterval', validatorRules.minuteInterval]"
                     placeholder="请输入分钟上传间隔（minute）"  addon-after="分钟"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="数采仪编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['colletionNumber']" placeholder="请输入数采仪编号"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="生产厂家" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['factory']" placeholder="请输入生产厂家"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="生产日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-date placeholder="请选择生产日期" v-decorator="['prodcutDate']" :trigger-change="true" style="width: 100%"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="数采仪型号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['productModel']" placeholder="请输入数采仪型号"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="ip地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['ip']" placeholder="请输入ip地址"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="安装日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-date placeholder="请选择安装日期" v-decorator="['installDate']" :trigger-change="true" style="width: 100%"/>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['concatUser']" placeholder="请输入联系人"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['concatMobile', validatorRules.concatMobile]" placeholder="请输入联系电话"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="保质日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-date placeholder="请选择保质日期" v-decorator="['shelfLifeDate']" :trigger-change="true" style="width: 100%"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="运维单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['operationCompany']" placeholder="请输入运维单位"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="运维负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['operationUser']" placeholder="请输入运维负责人"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="运维负责人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['operationMobile', validatorRules.operationMobile]"
                     placeholder="请输入运维负责人电话"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="SIM卡号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['sim']" placeholder="请输入SIM卡号"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="使用协议" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['protocol']" placeholder="请输入使用协议"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="设备启用日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-date placeholder="请选择设备启用日期" v-decorator="['useDate']" :trigger-change="true" style="width: 100%"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='24'>
          <a-form-item label="备注" :labelCol="labelCols" :wrapperCol="wrapperCols">
            <a-textarea v-decorator="['content']" rows="4" placeholder="请输入备注"/>
          </a-form-item>
        </a-col>
      </a-row>

    </a-form>
  </a-spin>
</template>

<script>

  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'


  export default {
    name: "SiteDataCollectionModal",
    components: {
      JDate,
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: "操作",
        width: 800,
        visible: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        labelCols: {
          xs: {span: 24},
          sm: {span: 3},
        },
        wrapperCols: {
          xs: {
            span: 24
          },
          sm: {
            span: 20

          }
        },
        confirmLoading: false,
        validatorRules: {
          mnCode: {
            rules: [
              {required: true, message: '请输入数采仪mn号!'},
              {validator: (rule, value, callback) => validateDuplicateValue('site_data_collection', 'mn_code', value, this.model.id, callback)},
            ]
          },
          currentInterval: {
            rules: [
              {pattern: /^-?\d+\.?\d*$/, message: '请输入数字!'},
            ]
          },
          minuteInterval: {
            rules: [
              {pattern: /^-?\d+\.?\d*$/, message: '请输入数字!'},
            ]
          },
          concatMobile: {
            rules: [
              {pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
            ]
          },
          operationMobile: {
            rules: [
              {pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
            ]
          },
        },
        url: {
          add: "/collection/siteDataCollection/add",
          edit: "/collection/siteDataCollection/edit",
        }
      }
    },
    created() {
    },
    methods: {
      add() {
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'mnCode', 'currentInterval', 'minuteInterval', 'colletionNumber', 'factory', 'prodcutDate', 'productModel', 'ip', 'installDate', 'concatUser', 'concatMobile', 'shelfLifeDate', 'operationCompany', 'operationUser', 'operationMobile', 'sim', 'protocol', 'useDate', 'content'))
        })
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
            } else {
              httpurl += this.url.edit;
              method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据", formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'mnCode', 'currentInterval', 'minuteInterval', 'colletionNumber', 'factory', 'prodcutDate', 'productModel', 'ip', 'installDate', 'concatUser', 'concatMobile', 'shelfLifeDate', 'operationCompany', 'operationUser', 'operationMobile', 'sim', 'protocol', 'useDate', 'content'))
      },


    }
  }
</script>