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

        <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['projectName', validatorRules.projectName]" placeholder="请输入项目名称" :disabled="disable"></a-input>
        </a-form-item>
        <a-form-item label="批复文件号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['approveFilenum', validatorRules.approveFilenum]" placeholder="请输入批复文件号" :disabled="disable"></a-input>
        </a-form-item>
        <a-form-item label="审批单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['approveUnit', validatorRules.approveUnit]" placeholder="请输入审批单位" :disabled="disable"></a-input>
        </a-form-item>
        <a-form-item label="审批时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择审批时间" v-decorator="['approveDate', validatorRules.approveDate]" :trigger-change="true" style="width: 100%" :disabled="disable"/>
        </a-form-item>
        <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-upload ref="uploadRef" :disabled="disable" fileType="file" bizPath="envtrial" @change="fileListChange" @delete ="fileDelete"
                      ></j-upload>
        </a-form-item>
        <!--加一个表格显示-->

      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel" >关闭</a-button>
      <a-button type="primary" @click="handleOk" v-show="!disable">暂存</a-button>
      <a-button type="primary" @click="handDeclare" v-show="!disable">申报</a-button>
    </template>
  </j-modal>
</template>

<script>

  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'
  import {queryFiles} from '../../../../requestAction/request'
  export default {
    name: "CompanyEnvTrialModal",
    components: { 
      JDate,JUpload
    },
    props:{
      companyId:''
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        disable:true,
        model: {},
        fileList:'',
        deleteFiles:[],
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
          projectName: {
            rules: [
              { required: true, message: '请输入项目名称!'},
            ]
          },
          approveFilenum: {
            rules: [
              { required: true, message: '请输入批复文件号!'},
            ]
          },
          approveUnit: {
            rules: [
              { required: true, message: '请输入审批单位!'},
            ]
          },
          approveDate: {
            rules: [
              { required: true, message: '请输入审批时间!'},
            ]
          },
        },
        url: {
          add: "/company/envTrial/add",
          edit: "/company/envTrial/edit",
          apply:"/company/envTrial/editAndApply",
          queryFile:"/company/envTrial/queryFiles"
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      fileListChange(newFileList){
        console.log("文件列表改变",newFileList);
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
        this.$nextTick(() => {
          _this.form.setFieldsValue(pick(this.model,'projectName','approveFilenum','approveUnit','approveDate','annex'))
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
        this.disable = true;
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
            formData.companyId=that.companyId;
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
      handDeclare(){
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          //获取表单数据
          if (!err) {
            that.confirmLoading = true;

            let httpurl =this.url.apply;
            let method = 'post';
            values.companyId = that.companyId;
            let formData = Object.assign(this.model, values);
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
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'projectName','approveFilenum','approveUnit','approveDate','annex'))
      },

      
    }
  }
</script>