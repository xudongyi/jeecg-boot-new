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
            <a-form-item label="报警类型" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <j-dict-select-tag type="list" v-decorator="['ruleType',validatorRules.ruleType]" @change="ruleLevelChange" :trigger-change="true" dictCode="rule_type" placeholder="请选择报警类型" style="width: 100%" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span="12" v-show="levelShow">
            <a-form-item label="报警级别" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <j-dict-select-tag type="list" v-decorator="['ruleLevel',validatorRules.ruleLevel]" :trigger-change="true" dictCode="rule_level" placeholder="请选择报警级别" style="width: 100%" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="24">
            <a-form-item label="是否发送短信：" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group v-model="resultMsg" @change="msgChange" :trigger-change="true" :disabled="disableSubmit">
                <a-radio value="0">
                  发送
                </a-radio>
                <a-radio value="1">
                  不发送
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="24" v-show="msgShow">
            <a-form-item label="发送频率(次/天)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['msgRate',validatorRules.msgRate]" placeholder="请输入发送频率" style="width: 100%" :disabled="disableSubmit" :max="10" :min="1"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="12" v-show="msgShow">
            <a-form-item label="短信发送时段" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-time-picker format="HH:mm" :minute-step="30" :second-step="60" v-decorator="['warnStarttime',validatorRules.warnStarttime]" style="width: 100%" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span="12" v-show="msgShow">
            <a-form-item label="至" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-time-picker format="HH:mm" :minute-step="30" :second-step="60" v-decorator="['warnEndtime',validatorRules.warnEndtime]" style="width: 100%" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="24">
            <a-form-item label="策略说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['content']" placeholder="请输入策略说明" :rows="2" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="24">
            <a-form-item label="策略状态：" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group v-model="resultUsed" :disabled="disableSubmit">
                <a-radio value="0">
                  启用
                </a-radio>
                <a-radio value="1">
                  停用
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>

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
  import moment from 'moment'
  import {duplicateCheck } from '@/api/api'

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
        levelShow:'',
        msgShow:'',
        disableSubmit:'',
        resultMsg:'0',
        resultUsed:'0',
        rates:[],
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 4 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 12 },
        },
        labelCols: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCols: {
          xs: { span: 24 },
          sm: { span: 14 },
        },
        confirmLoading: false,
        validatorRules: {
          ruleType: {
            rules: [
              { required: true, message: '请选择策略类型!'},
              ,{
                validator: this.validateRuleType,
              }

            ]
          },
          ruleLevel: {
          },
          isSendMsg: {
            rules: [
              { required: true},
            ]
          },
          msgRate: {
          },
          warnStarttime: {
          },
          warnEndtime: {
          },
          isUsed: {
            rules: [
              { required: true},
            ]
          }
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
      //类型重复校验
      validateRuleType(rule, value, callback){
        let params = {
          tableName: 'sys_warn_rule',
          fieldName: 'rule_type',
          fieldVal: value,
          dataId: this.model.id
        };
        duplicateCheck(params).then((res) => {
          if (res.success) {
            callback()
          } else {
            callback("该策略类型已存在!")
          }
        })
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        if(record.isSendMsg === '0' || record.isSendMsg === '1')
          this.resultMsg = record.isSendMsg;
        if(record.isUsed === '0' || record.isUsed === '1')
          this.resultUsed = record.isUsed;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'ruleType','ruleLevel','isSendMsg','msgRate','warnStarttime','warnEndtime','content','isUsed'))
          //
        })
      },
      ruleLevelChange(val){
        if(val==='4' || val==='5') {
          this.levelShow = true;
          this.validatorRules.ruleLevel= { rules:[{ required: true, message: '请选择报警级别!'}]};
        } else {
          this.levelShow = false;
          this.validatorRules.ruleLevel= {}
        }
      },
      msgChange(val){
        console.log(val)
        if(val.target.value==='0') {
          this.msgShow = true;
          this.validatorRules.msgRate = { rules:[{ required: true, message: '请选择发送频率!'}]};
          this.validatorRules.warnStarttime ={ rules:[{ required: true, message: '请选择发送开始时间!'}]};
          this.validatorRules.warnEndtime ={ rules:[{ required: true, message: '请选择发送结束时间!'}]};
        } else {
          this.msgShow =false;
          this.validatorRules.msgRate ={};
          this.validatorRules.warnStarttime ={};
          this.validatorRules.warnEndtime ={};
        }
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          values.warnStarttime = moment(values.warnStarttime).format('HH:mm:ss');
          values.warnEndtime= moment(values.warnEndtime).format('HH:mm:ss');
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
            formData.isSendMsg = this.resultMsg;
            formData.isUsed = this.resultUsed;
            if(formData.isSendMsg ==='1') {
              formData.warnStarttime = null;
              formData.warnEndtime = null
            }
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