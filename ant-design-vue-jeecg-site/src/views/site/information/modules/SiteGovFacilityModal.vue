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
          <a-col span='12'>
            <a-form-item label="治理设施名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['govName', validatorRules.govName]" placeholder="请输入治理设施名称" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="治理设施编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['govCode', validatorRules.govCode]" placeholder="请输入治理设施编号" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="治理设施类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['govType']" :trigger-change="true" dictCode="govType"
                                 placeholder="请选择治理设施类型" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="投入使用日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择投入使用日期" v-decorator="['useDate']" :trigger-change="true" style="width: 100%" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="设计处理能力" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['designAbility']" placeholder="请输入设计处理能力" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="设计月处理效率" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['designMonth']" placeholder="请输入设计月处理效率" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="实际月处理效率" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['actualMonth']" placeholder="请输入实际月处理效率" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="监测通道号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['channelNumber']" placeholder="请输入监测通道号" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="处理方法" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['handlMethod']" :trigger-change="true" dictCode="handlMethod"
                                 placeholder="请选择处理方法" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="设施生产商" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['facilityProducer']" placeholder="请输入设施生产商" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['concatUser']" placeholder="请输入联系人" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['concatMobile', validatorRules.concatMobile]" placeholder="请输入联系电话" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="治理工艺" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-input v-decorator="['govCraft']" placeholder="请输入治理工艺" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="治理方法" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-input v-decorator="['govMethod']" placeholder="请输入治理方法" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="治理因子信息" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-input v-decorator="['govFactorInfo']" placeholder="请输入治理因子信息" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="备注" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-textarea v-decorator="['remark']" rows="4" placeholder="请输入备注" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
      <a-button type="primary" @click="handleOk"  v-if="!disableSubmit">保存</a-button>
    </template>
  </j-modal>
</template>

<script>

  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"


  export default {
    name: "SiteGovFacilityModal",
    components: {
      JDate,
      JDictSelectTag,
    },
    data() {
      return {
        form: this.$form.createForm(this),
        disableSubmit:false,
        title: "操作",
        width: 1200,
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
          govName: {
            rules: [
              {required: true, message: '请输入治理设施名称!'},
              {validator: (rule, value, callback) => validateDuplicateValue('site_gov_facility', 'gov_name', value, this.model.id, callback)},
            ]
          },
          govCode: {
            rules: [
              {validator: (rule, value, callback) => validateDuplicateValue('site_gov_facility', 'gov_code', value, this.model.id, callback)},
            ]
          },
          concatMobile: {
            rules: [
              {pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
            ]
          },
        },
        url: {
          add: "/facility/siteGovFacility/add",
          edit: "/facility/siteGovFacility/edit",
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
          this.form.setFieldsValue(pick(this.model, 'govName', 'govCode', 'govType', 'useDate', 'designAbility', 'designMonth', 'actualMonth', 'channelNumber', 'handlMethod', 'facilityProducer', 'concatUser', 'concatMobile', 'govCraft', 'govMethod', 'govFactorInfo', 'remarkString'))
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
            formData.monitorId = that.monitorId;
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
        this.form.setFieldsValue(pick(row, 'govName', 'govCode', 'govType', 'useDate', 'designAbility', 'designMonth', 'actualMonth', 'channelNumber', 'handlMethod', 'facilityProducer', 'concatUser', 'concatMobile', 'govCraft', 'govMethod', 'govFactorInfo', 'remarkString'))
      },
    }, props: {
      siteType: '',
      monitorId: ''
    }
  }
</script>