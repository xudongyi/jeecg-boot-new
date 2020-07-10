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
            <a-form-item label="污染因子名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['meaning', validatorRules.meaning]" placeholder="请输入污染因子名称" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="新国标污染因子" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['code', validatorRules.code]" placeholder="请输入新国标污染因子" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="12">
            <a-form-item label="老国标污染因子" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['oldCode', validatorRules.oldCode]" placeholder="请输入老国标污染因子" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="污染因子类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['type', validatorRules.type]" :trigger-change="true" dictCode="siteType" placeholder="请选择污染因子类型" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>


        <a-row>
          <a-col span="12">
            <a-form-item label="排放量单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['amountUnit']" placeholder="请输入排放量单位" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="浓度单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['chromaUnit']" placeholder="请输入浓度单位" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="格式值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['format']" placeholder="请输入格式值" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="是否有折算值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['isZs', validatorRules.isZs]" :trigger-change="true" dictCode="yes_or_no" placeholder="请选择是否有折算值" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="是否为主要污染物" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['isImportant']" :trigger-change="true" dictCode="yes_or_no" placeholder="请选择是否为主要污染物" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span="12">

            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
               <span slot="label">
                数据是否重复&nbsp;
                  <a-tooltip title="数据重复是指设备上传的数据在做样周期内一直上传重复数据,例如COD、氨氮等；数据不重复的如PH等。">
                    <a-icon type="question-circle" />
                  </a-tooltip>
                </span>
              <j-dict-select-tag type="list" v-decorator="['isRepeat']" :trigger-change="true" dictCode="yes_or_no" placeholder="请选择数据是否重复" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="异常值上限" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['errorMax']" placeholder="请输入异常值上限" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="异常值下限" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['errorMin']" placeholder="请输入异常值下限" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="是否含有排放量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['isTotal', validatorRules.isTotal]" :trigger-change="true" dictCode="yes_or_no" placeholder="请选择是否含有排放量" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sort']" placeholder="请输入排序" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="是否启用" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['isUse', validatorRules.isUse]" :trigger-change="true" dictCode="yes_or_no" placeholder="请选择是否启用" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['content']" placeholder="请输入备注" :rows="2" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>

<!--        <a-form-item label="计量单位-浓度-数学表达式" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--          <a-input v-decorator="['chromaUnitMath']" placeholder="请输入计量单位-浓度-数学表达式"></a-input>-->
<!--        </a-form-item>-->
<!--        <a-form-item label="计量单位-总量-数学表达式" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--          <a-input v-decorator="['amountUnitMath']" placeholder="请输入计量单位-总量-数学表达式"></a-input>-->
<!--        </a-form-item>-->

      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
      <a-button type="primary" @click="handleOk" v-if="!disableSubmit">确定</a-button>
    </template>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JDictSelectTag from "@/components/dict/JDictSelectTag"


  export default {
    name: "SysPollutionCodeModal",
    components: { 
      JDate,
      JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:1200,
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
          code: {
            rules: [
              { required: true, message: '请输入新国标污染因子!'},
            ]
          },
          meaning: {
            rules: [
              { required: true, message: '请输入污染因子名称!'},
            ]
          },
          oldCode: {
            rules: [
              { required: true, message: '请输入老国标污染因子!'},
            ]
          },
          isZs: {
            rules: [
              { required: true, message: '请输入是否有折算值!'},
            ]
          },
          type: {
            rules: [
              { required: true, message: '请输入污染因子类型!'},
            ]
          },
          isTotal: {
            rules: [
              { required: true, message: '请输入是否含有排放量!'},
            ]
          },
          isUse: {
            rules: [
              { required: true, message: '请输入是否启用!'},
            ]
          },
        },
        url: {
          add: "/spc/sysPollutionCode/add",
          edit: "/spc/sysPollutionCode/edit",
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
          this.form.setFieldsValue(pick(this.model,'code','meaning','oldCode','chromaUnit','chromaUnitMath','amountUnit','amountUnitMath','isZs','format','type','isRepeat','isTotal','isImportant','errorMax','errorMin','sort','isUse','content'))
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
        this.form.setFieldsValue(pick(row,'code','meaning','oldCode','chromaUnit','chromaUnitMath','amountUnit','amountUnitMath','isZs','format','type','isRepeat','isTotal','isImportant','errorMax','errorMin','sort','isUse','content','createBy','createTime','updateBy','updateTime'))
      },

      
    }
  }
</script>