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

        <a-form-item :label="nameLabel" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入名称" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item :label="fileLabel" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload ref="uploadRef" :disabled="disableSubmit" fileType="file" bizPath="prevention" @change="fileListChange" @delete ="fileDelete"
          ></j-upload>
        </a-form-item>

      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
      <a-button type="primary" @click="handleOk"  v-if="!disableSubmit">暂存</a-button>
      <a-button type="primary" @click="handDeclare"  v-if="!disableSubmit">申报</a-button>
    </template>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JUpload from '@/components/jeecg/JUpload'
  import {queryFiles} from '../../../requestAction/request'


  export default {
    name: "CompanyPreventionModal",
    components: { 
      JUpload,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        fileList:'',
        deleteFiles:[],
        disableSubmit:false,
        title:"操作",
        nameLabel:this.header_text+"名称",
        fileLabel:this.header_text+"附件上传",
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
          name: {
            rules: [
              { required: true, message: '请输入名称!'},
            ]
          },
        },
        url: {
          add: "/prevention/companyPrevention/add",
          edit: "/prevention/companyPrevention/edit",
          declare:"/prevention/companyPrevention/declare",
          queryFile:"/prevention/companyPrevention/queryFiles"
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
        let _this =this;
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name'))
        })
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
      },
      //申报
      handDeclare () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpUrl = this.url.declare;
            let method = 'put';
            let formData = Object.assign(this.model, values);
            formData.companyId=this.companyId;
            formData.type=this.type;
            formData.fileList = that.fileList;
            httpAction(httpUrl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('declare');
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
            formData.companyId=this.companyId;
            formData.type=this.type;
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
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'createTime','name','files'))
      },
      fileListChange(newFileList){
        this.fileList = newFileList;
      },
      fileDelete(file){
        this.deleteFiles.push(file.response.message);
      },
    },
    props:{
      type:"",
      companyId:"",
      header_text:""
    }
  }
</script>