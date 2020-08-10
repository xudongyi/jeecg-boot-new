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
              <a-select v-decorator="['companyId', validatorRules.companyId]" show-search style="width: 100%" placeholder="请输入企业名称" :disabled="disableSubmit"
              optionFilterProp="children">
                <a-select-option v-for="item in items" :key="item.key" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="申报年份" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['reportYear', validatorRules.reportYear]" show-search @visible-change="yearChange($event)" :disabled="disableSubmit">
                <a-select-option v-for="item in years" :key="item.value"  :value="item.value">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['documentType', validatorRules.documentType]" :trigger-change="true" dictCode="supervision_document_type" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['documentName', validatorRules.documentName]" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="24" v-if="monitorTag === 'view'">
            <a-form-item label="附件上传" :labelCol="labelCols" :wrapperCol="wrapperCols" >
              <j-upload ref="uploadRef" :disabled="true" fileType="file" bizPath="DynamicSupervision" @change="fileListChange" @delete ="fileDelete"></j-upload>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="24" v-if="monitorTag !== 'view'">
            <a-form-item label="附件上传" :labelCol="labelCols" :wrapperCol="wrapperCols" >
              <j-upload ref="uploadRef" :disabled="disableSubmit" fileType="file" bizPath="DynamicSupervision" @change="fileListChange" @delete ="fileDelete"></j-upload>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row v-if="monitorTag !== 'view'">
          <a-col span="12">
            <a-form-item label="不通过原因：" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['content']" placeholder="备注信息" :rows="2" :disabled="true"/>
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
  import {queryFiles} from "../../../requestAction/request";

  export default {
    name: "CompanyDynamicSupervisionModal",
    components: {
      JDate,
      JUpload,
      JDictSelectTag,
    },
    props:{
      monitor:'',
    },
    data () {
      return {
        form: this.$form.createForm(this),
        dateFormat:"YYYY-MM-DD HH:mm:ss",
        title:"操作",
        width:800,
        years:[],
        items:[],
        disableSubmit:false,
        visible: false,
        model: {},
        fileList:'',
        deleteFiles:[],
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
          add: "/cds/companyDynamicSupervision/add",
          edit: "/cds/companyDynamicSupervision/edit",
          declare:"/cds/companyDynamicSupervision/declare",
          queryFile:"/cds/companyDynamicSupervision/queryFiles"
        },
        monitorTag:''
      }
    },
    created () {
      this.yearChange();
      this.monitorTag = this.monitor;
      let that = this;
      //查询企业名称
      queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
        if(res.success){
          that.items = res.result;
        }
      });
    },

    methods: {
      yearChange () {
        var myDate = new Date()
        var startYear = myDate.getFullYear() - 20// 起始年份
        var endYear = myDate.getFullYear() + 20// 结束年份

        this.years = []
        for (var i = startYear; i <= endYear; i++) {
          this.years.push({value: i, label: i})
        }
      },
      //获取系统时间
      getTime() {
        let  _this =this;
        this.timer =  setInterval(()=>{
          _this.model.createTime = moment().format(this.dateFormat);
          this.$nextTick(() => { this.form.setFieldsValue(pick(this.model,'createTime'))})

        },1000)
      },
      add () {
        this.edit({});
        //获取时间
        this.getTime();
      },
      fileListChange(newFileList){
        this.fileList = newFileList;
      },
      fileDelete(file){
        this.deleteFiles.push(file.response.message);
      },
      edit (record) {
        let _this =this;
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.model.createTime = moment().format(this.dateFormat);
        if(record.createTime)
          this.model.createTime = moment(record.createTime).format(this.dateFormat);
        this.model.createName = this.$store.getters.userInfo.realname;

        this.$nextTick(() => {
          _this.form.setFieldsValue(pick(this.model,'status','companyId','reportYear','documentType','documentName','content','createName','createTime','updateName','updateTime'))
        });

        if(record.id){
          //查询所属文件

          queryFiles({id:record.id},this.$data.url.queryFile).then((res)=>{

            _this.$nextTick(() => {

              _this.$refs.uploadRef.initFileListArr(res.result);
              let arr = [];
              for(var a=0;a< _this.$refs.uploadRef.fileList.length;a++){
                arr.push( _this.$refs.uploadRef.fileList[a].response.message)
              }
              if(arr.length>0){
                _this.fileList  = arr.join(",")
              }else{
                _this.fileList = '';
              }
            });

          });


        }else{
          //清空上传列表
          _this.$nextTick(() => {
            _this.$refs.uploadRef.initFileList("");
            let arr = [];

            for(var a=0;a< _this.$refs.uploadRef.fileList.length;a++){
              arr.push( _this.$refs.uploadRef.fileList[a].response.message)
            }
            if(arr.length>0){
              _this.fileList  = arr.join(",")
            }else{
              _this.fileList = '';
            }
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
            formData.createTime = moment().format(this.dateFormat);
            formData.fileList = that.fileList;
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

      // popupCallback(row){
      //   this.form.setFieldsValue(pick(row,'status','companyId','reportYear','documentType','documentName','content','createBy','createTime','updateBy','updateTime'))
      // },
      handDeclare(){
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpUrl = this.url.declare;
            let method = 'put';
            let formData = Object.assign(this.model, values);
            formData.createTime = moment().format(this.dateFormat);
            formData.fileList = that.fileList;
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
      },

    }

  }
</script>