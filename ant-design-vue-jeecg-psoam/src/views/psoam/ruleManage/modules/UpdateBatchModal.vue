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
      <div style="width: 100%;height: 30px;border-bottom: 1px solid">
        <span style="width: 15%;float: left;font-size: 16px;color: #000000;padding-left: 4px">策略执行对象:</span>
        <span style="width: 7%;float: left;font-size: 16px;color: #000000;">已选中</span>
        <span style="width: 3%;float: left;font-size: 18px;color: #0098A1;"> {{this.selectedRows.length}}</span>
        <span style="width: 50%;float: left;font-size: 16px;color: #000000;">个监测点(更新的策略保存成功后会替换监控点原有的策略！)</span>
      </div>
      <a-form :form="form">
        <a-row>
          <a-col span="8">
            <a-form-item label="策略类型" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <j-dict-select-tag type="list" v-decorator="['ruleType',validatorRules.ruleType]" @change="ruleLevelChange" :trigger-change="true" dictCode="warnType" placeholder="请选择策略类型" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col span="8">
            <a-form-item label="监测类型" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-select v-decorator="['siteType']" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children" :disabled="true">
                <a-select-option v-for="(item,index) in siteTypes" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col span="8">
            <a-form-item label="污染因子" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-select v-decorator="['code',validatorRules.code]" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children" >
                <a-select-option v-for="(item,index) in codes" :key="item.meaning" :value="item.code">
                  {{item.meaning}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-tabs type="card" @change="levelChange" :active-key="levelKey" style="margin-left: 4%">
          <a-tab-pane v-for="(item,index) in levelItems" :key="item.key" :tab="item.value">
          </a-tab-pane>
        </a-tabs>
        <a-row v-show="ruleTypeShow">
          <a-col span="12">
            <a-form-item :label="levelItems[this.levelKey].value+'阈值上限'" :labelCol="labelCols" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['warnValueUp',validatorRules.warnValueUp]" placeholder="请输入阈值上限" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item :label="levelItems[this.levelKey].value+'阈值下限'" :labelCol="labelCols" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['warnValueDown']" placeholder="请输入阈值下限" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row v-show="ruleLevelShow && ruleTypeShow">
          <a-col span="12">
            <a-form-item label="是否留样" :labelCol="labelCols" :wrapperCol="wrapperCol">
              <a-radio-group v-model="resultSample"  :trigger-change="true" >
                <a-radio value="0">
                  是
                </a-radio>
                <a-radio value="1">
                  否
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="是否关阀" :labelCol="labelCols" :wrapperCol="wrapperCol">
              <a-radio-group v-model="resultTap"  :trigger-change="true" >
                <a-radio value="0">
                  是
                </a-radio>
                <a-radio value="1">
                  否
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row v-show="repeatDataCountShow">
          <a-col span="24">
            <a-form-item label="重复数据条数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['repeatDataCount',validatorRules.repeatDataCount]" placeholder="请输入重复数据条数" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="是否发送短信" :labelCol="labelCols" :wrapperCol="wrapperCol">
              <a-radio-group v-model="resultMsg"  :trigger-change="true">
                <a-radio value="0">
                  发送
                </a-radio>
                <a-radio value="1">
                  不发送
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="是否同步短信联系人" :labelCol="labelCols" :wrapperCol="wrapperCol">
              <a-radio-group v-model="resultSynchronize"  :trigger-change="true">
                <a-radio value="0">
                  是
                </a-radio>
                <a-radio value="1">
                  否
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="24" v-show="msgShow">
            <a-form-item label="短信发送频率(次/天)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['msgRate',validatorRules.msgRate]" placeholder="请输入发送频率" style="width: 100%" :max="24" :min="1"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="12" v-show="msgShow">
            <a-form-item label="短信发送时段" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-time-picker format="HH:mm" :minute-step="10" :second-step="60" v-decorator="['msgStartTime',validatorRules.msgStartTime]" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col span="12" v-show="msgShow">
            <a-form-item label="至:" :labelCol="{ xs: { span: 24 },sm: { span: 3 },}" :wrapperCol=" {xs: { span: 24 },sm: { span: 12 }}">
              <a-time-picker format="HH:mm" :minute-step="10" :second-step="60" v-decorator="['msgEndTime',validatorRules.msgEndTime]" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="24">
            <a-form-item label="策略说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['content']" placeholder="请输入策略说明(100字以内)" :maxLength="100" :rows="2" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="24">
            <a-form-item label="策略状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group v-model="resultUsed">
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
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
      <a-button type="primary" @click="handleOk">确定</a-button>
    </template>
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
  import {getAction} from "../../../../api/manage";

  export default {
    name: "UpdateBatchModal",
    components: {
      JDate,
      JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:1000,
        visible: false,
        levelKey:'0',
        levelItems:[{key:'0',value:'报警'},{key:'1',value:'一级预警'},{key:'2',value:'二级预警'},{key:'3',value:'三级预警'},{key:'4',value:'四级预警'}],
        oneRecord:'',
        resultSample:'',
        resultTap:'',
        msgShow:'',
        ruleTypeShow: '',
        ruleLevelShow:'',
        repeatDataCountShow:'',
        resultMsg:'0',
        resultSynchronize:'',
        resultUsed:'0',
        siteTypes:[{key:'0',value:'废水'},{key:'1',value:'废气'},{key:'2',value:'VOCs'}],
        codes:'',
        selectedRows:'',
        resultDate:'',
        mns:'',
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 11 },
        },
        labelCols: {
          xs: { span: 24 },
          sm: { span: 10 },
        },
        wrapperCols: {
          xs: { span: 24 },
          sm: { span: 12 },
        },
        confirmLoading: false,
        validatorRules: {
          ruleType: {
            rules: [
              { required: true, message: '请选择策略类型!'}
            ]
          },
          ruleLevel: {
            rules: [
              { required: true},
            ]
          },
          code: {
            rules: [
              { required: true},
            ]
          },
          repeatDataCount:{},
          warnValueUp:{},
          warnValueDown:{},
          resultSample:{},
          resultTap:{},
          msgRate: {
          },
          msgStartTime: {
          },
          msgEndTime: {
          },
          isUsed: {
            rules: [
              { required: true},
            ]
          }
        },
        url: {
          add: "/wr/warnRule/add",
        }
      }
    },
    created () {
    },
    methods: {
      levelChange(val){
        if(!this.disableSubmit === true)
          this.levelKey = val;
      },
      add (selectionRows) {
        let _this = this;
        var mnList= '';
        this.selectedRows  = selectionRows;
        selectionRows.forEach(e=>{
          mnList += e.id + ",";
        });
        this.mns = mnList;
        console.log("sssss",this.mns)
        getAction("/spc/sysPollutionCode/queryMainByType",{type:selectionRows[0].siteType}).then(res=>{
          _this.codes = res.result;
          _this.setValue()

        });
      },
      ruleLevelChange(val){
        if(val === '0' || val === '1' || val === '2'){
          this.ruleTypeShow = true;
          this.repeatDataCountShow = false;
          this.validatorRules.warnValueUp = {rules: [{required: true, message: '请输入阈值上限!'}]};
          this.validatorRules.repeatDataCount = {};
        }else if(val === '6'){
          this.ruleTypeShow = false;
          this.repeatDataCountShow = true;
          this.validatorRules.warnValueUp = {};
          this.validatorRules.repeatDataCount = {rules: [{required: true, message: '请输入重复数据条数!'}]};
        }else{
          this.ruleTypeShow = false;
          this.repeatDataCountShow = false;
          this.validatorRules.warnValueUp = {};
          this.validatorRules.repeatDataCount = {};
        }
      },
      setValue(){
        let _this = this;
        this.form.resetFields();

        // this.model = Object.assign({}, {...});
        this.visible = true;
        this.$nextTick(() => {
          // console.log(pick(_this.model,'ruleType','siteType','code','warnValueUp','warnValueDown','repeatDataCount','isSynchronize',
          //   'isSendMsg','msgRate','content','isUsed','isSample','isCloseTap'))
          _this.form.setFieldsValue({siteType:this.selectedRows[0].siteType});
          // this.form.setFieldsValue({msgStartTime: this.model.msgStartTime ? moment(this.model.msgStartTime, 'HH:mm:ss') : null
          //   ,msgEndTime: this.model.msgEndTime ? moment(this.model.msgEndTime, 'HH:mm:ss') : null});

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
          values.msgStartTime = moment(values.msgStartTime).format('HH:mm:ss');
          values.msgEndTime= moment(values.msgEndTime).format('HH:mm:ss');
          if (!err) {
            that.confirmLoading = true;
            let formData = Object.assign(this.model, values);
            formData.isSendMsg = this.resultMsg;
            formData.isUsed = this.resultUsed;
            formData.ruleLevel = this.levelKey;
            formData.isSample = this.resultSample;
            formData.isCloseTap = this.resultTap;
            formData.isSynchronize = this.resultSynchronize;
            formData.mns = this.mns;
            if(formData.isSendMsg ==='1') {
              formData.msgStartTime = null;
              formData.msgEndTime = null
            }
            console.log("表单提交数据",formData)
            httpAction(this.url.add,formData,'post').then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
                that.$emit('clear');
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
    },
    mounted(){

    },
    watch:{
      resultMsg:{
        handler(val) {
          if (val === '0') {
            this.msgShow = true;
            this.validatorRules.msgRate = {rules: [{required: true, message: '请选择发送频率!'}]};
            this.validatorRules.msgStartTime = {rules: [{required: true, message: '请选择发送开始时间!'}]};
            this.validatorRules.msgEndTime = {rules: [{required: true, message: '请选择发送结束时间!'}]};
          } else {
            this.msgShow = false;
            this.validatorRules.msgRate = {};
            this.validatorRules.msgStartTime = {};
            this.validatorRules.msgEndTime = {};
          }
        },
        immediate: true
      },
      levelKey:{
        handler(val) {
          if (val === '0') {
            this.ruleLevelShow = true;
            this.validatorRules.resultSample = {rules: [{required: true, message: '请选择!'}]};
            this.validatorRules.resultTap = {rules: [{required: true, message: '请选择!'}]};
          } else {
            this.ruleLevelShow = false;
            this.validatorRules.resultSample = {};
            this.validatorRules.resultTap = {};
          }
        },
        immediate: true
      },
    }
  }
</script>
<style scoped>
</style>