<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen

    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row>
          <a-col span="12">
            <a-form-item label="企业名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['companyId', validatorRules.companyId]" show-search style="width: 100%" placeholder="请输入企业名称" :disabled="disabled"
                        optionFilterProp="children">
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="申报年份" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['reportYear', validatorRules.reportYear]" style="width: 100%" :disabled="disabled"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['documentType', validatorRules.documentType]" :trigger-change="true" dictCode="supervision_document_type" :disabled="disabled"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['documentName', validatorRules.documentName]" :disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="24">
            <a-form-item label="附件上传" :labelCol="labelCols" :wrapperCol="wrapperCols" >
              <j-upload ref="uploadRef" :disabled="true" fileType="file" bizPath="DynamicSupervision"></j-upload>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="申报人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['createName']" placeholder="请输入申报人" :disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="申报时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date :showTime="true" :dateFormat="dateFormat"
                      placeholder="请选择申报时间" v-decorator="['createTime']" :trigger-change="true" style="width: 100%" :disabled="disabled"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="12">
          <a-form-item label="审核结果：" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-radio-group v-decorator="['status']" :disabled="disableSubmit">
              <a-radio value="2">
                通过
              </a-radio>
              <a-radio value="3">
                不通过
              </a-radio>
            </a-radio-group>
          </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="12">
            <a-form-item label="不通过原因：" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['content']" placeholder="备注信息" :rows="2" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
                <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="['updateName']" placeholder="请输入审核人" :disabled="disabled"></a-input>
                </a-form-item>
          </a-col>
          <a-col span="12">
                <a-form-item label="审核时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date :showTime="true" :dateFormat="dateFormat" placeholder="请选择审核时间" v-decorator="['updateTime']" :trigger-change="true" style="width: 100%" :disabled="disabled"/>
                </a-form-item>
          </a-col>
        </a-row>

      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleOk" v-if="title !== '查看'">确定</a-button>
      <a-button type="primary" @click="handleCancel">关闭</a-button>
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
  import {queryCompanyName} from "../../requestAction/request";
  import moment from 'moment'
  import {queryFiles} from "../../requestAction/request";

  export default {
    name: "DynamicSupervisionAuditModal",
    components: {
      JDate,
      JUpload,
      JDictSelectTag,
    },
    props:{

    },
    data () {
      return {
        form: this.$form.createForm(this),
        dateFormat:"YYYY-MM-DD HH:mm:ss",
        title:"操作",
        width:800,
        disabled:true,
        disableSubmit:'',
        result:'',
        items:[],
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
          companyId: {
            rules: [
              { required: true, message: '请选择企业名称!'},
            ]
          },
          status: {
            rules: [
              { required: true, message: '请输入数据状态!'},
            ]
          },
          reportYear: {
            rules: [
              { required: true, message: '请输入申报年份!'},
            ]
          },
          documentType: {
            rules: [
              { required: true, message: '请输入材料类型!'},
            ]
          },
          documentName: {
            rules: [
              { required: true, message: '请输入材料名称!'},
            ]
          },
        },
        url: {
          // add: "/cds/companyDynamicSupervision/add",
          edit: "/cds/companyDynamicSupervision/edit",
          queryFile:"/cds/companyDynamicSupervision/queryFiles"
        },
      }
    },
    created () {
    },
    mounted() {
      this.queryCompanyName();
    },
    methods: {
      //查询企业名称
      queryCompanyName() {
        let that = this;
        queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
          if(res.success){
            that.items = res.result;
          }
        });
      },
      //获取系统时间
      getTime() {
        let  _this =this;
        this.timer =  setInterval(()=>{
          _this.model.updateTime = moment().format(this.dateFormat);
          console.log(_this.model.updateTime );
          this.$nextTick(() => { this.form.setFieldsValue(pick(this.model,'updateTime'))})

        },1000)
      },
      // add () {
      //   this.edit({});
      // },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        //获取时间
        this.getTime();
        this.model.updateTime = moment().format(this.dateFormat);
        if(record.updateTime)
          this.model.updateTime = moment(record.updateTime).format(this.dateFormat);
        this.model.updateName = this.$store.getters.userInfo.realname;

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'companyId', 'reportYear', 'documentType', 'documentName', 'createName', 'createTime', 'content', 'status',
            'updateName', 'updateTime'))
        });

        if(record.id){
          //查询所属文件
          let _this =this;
          queryFiles({id:record.id},this.$data.url.queryFile).then((res)=>{

            _this.$nextTick(() => {

              _this.$refs.uploadRef.initFileListArr(res.result);
            });

          });
        }
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
            formData.updateBy = this.$store.getters.userInfo.id;
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
      // filterOption(input, option) {
      //   return (
      //     option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      //   );
      // }
      // popupCallback(row){
      //   this.form.setFieldsValue(pick(row,'status','companyId','reportYear','documentType','documentName','content','createBy','createTime','updateBy','updateTime'))
      // },

    }

  }
</script>