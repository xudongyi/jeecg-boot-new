<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen

    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

<!--        <a-form-item label="数据状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--          <j-dict-select-tag type="list" v-decorator="['status', validatorRules.status]" :trigger-change="true" dictCode="statue" placeholder="请选择数据状态"/>-->
<!--        </a-form-item>-->
<!--        <a-form-item label="企业id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--          <a-input v-decorator="['companyId', validatorRules.companyId]" placeholder="请输入企业id"></a-input>-->
<!--        </a-form-item>-->
        <a-row>
          <a-col span="12">
            <a-form-item label="企业名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['companyId',validatorRules.companyId]" show-search style="width: 100%" placeholder="请输入企业名称"
                        :disabled="disableSubmit" optionFilterProp="children">
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="报告日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择报告日期" v-decorator="['reportDate', validatorRules.reportDate]" :trigger-change="true" style="width: 100%" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="报告类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['reportType', validatorRules.reportType]" :trigger-change="true" dictCode="report_type" placeholder="请选择报告类型" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="报告名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['reportName', validatorRules.reportName]" placeholder="请输入报告名称" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="24">
            <a-form-item label="附件上传" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <j-upload :trigger-change="true"></j-upload>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row v-if="monitorTag !== 'view'">
          <a-col span="12">
            <a-form-item label="申报人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['createName']" placeholder="请输入申报人" :disabled="true"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="申报时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date :showTime="true" :dateFormat="dateFormat"
                      placeholder="请选择申报时间" v-decorator="['createTime']" :trigger-change="true" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
        </a-row>

      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
      <a-button type="primary" @click="handleOk" v-if="monitorTag !== 'view' && (!disableSubmit)">暂存</a-button>
      <a-button type="primary" @click="handDeclare" v-if="monitorTag !== 'view' && (!disableSubmit)">申报</a-button>
    </template>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JUpload from '@/components/jeecg/JUpload'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import {queryCompanyName} from "../../../requestAction/request";
  import moment from 'moment'


  export default {
    name: "CompanySupervisoryMonitorModal",
    components: { 
      JDate,
      JUpload,
      JDictSelectTag,
    },
    props: {
      monitor:''
    },
    data () {
      return {
        form: this.$form.createForm(this),
        dateFormat:"YYYY-MM-DD HH:mm:ss",
        title:"操作",
        width:800,
        items:[],
        disableSubmit:false,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCols: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCols: {
          xs: { span: 24 },
          sm: { span: 19 },
        },
        confirmLoading: false,
        validatorRules: {
          status: {
            rules: [
              { required: true, message: '请输入数据状态!'},
            ]
          },
          companyId: {
            rules: [
              { required: true, message: '请输入企业名称!'},
            ]
          },
          reportDate: {
            rules: [
              { required: true, message: '请输入报告日期!'},
            ]
          },
          reportType: {
            rules: [
              { required: true, message: '请输入报告类型!'},
            ]
          },
          reportName: {
            rules: [
              { required: true, message: '请输入报告名称!'},
            ]
          },
        },
        url: {
          add: "/csm/companySupervisoryMonitor/add",
          edit: "/csm/companySupervisoryMonitor/edit",
          declare: "/csm/companySupervisoryMonitor/declare"
        },
      monitorTag:''
      }
    },
    created () {
      this.monitorTag = this.monitor;
      console.log(this.monitorTag==='view');
      let that = this;
      //查询企业名称
      queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
        if(res.success){
          that.items = res.result;
        }
      })
    },
    methods: {
      //获取系统时间
      getTime() {
        let  _this =this;
        this.timer =  setInterval(()=>{
          _this.model.createTime = moment().format(this.dateFormat);
          console.log(_this.model.createTime );
            this.$nextTick(() => { this.form.setFieldsValue(pick(this.model,'createTime'))})

        },1000)
      },
      add () {
        this.edit({});
        //获取时间
        this.getTime();
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.model.createTime = moment().format(this.dateFormat);
        if(record.createTime)
          this.model.createTime =moment(record.createTime).format(this.dateFormat);
        this.model.createName = this.$store.getters.userInfo.realname;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'status','companyId','reportDate','reportType','reportName','content','createName','createTime','updateName','updateTime'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
        if (this.timer) {
          clearInterval(this.timer); // 在Vue实例销毁前，清除我们的定时器
        }
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
        this.form.setFieldsValue(pick(row,'status','companyId','reportDate','reportType','reportName','content','createBy','createTime','updateBy','updateTime'))
      },
      handDeclare(){
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpUrl = this.url.declare;
            let method = 'put';
            let formData = Object.assign(this.model, values);
            formData.companyId=this.companyId;
            httpAction(httpUrl,formData,method).then((res)=>{
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
      }
      
    }
  }
</script>