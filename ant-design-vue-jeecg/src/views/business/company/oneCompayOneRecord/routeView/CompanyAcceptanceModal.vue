<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['projectName', validatorRules.projectName]" placeholder="请输入项目名称"
                   :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="审批单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['examineUnit']" placeholder="请输入审批单位" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="审批文号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['examineNum']" placeholder="请输入审批文号" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="审批时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择审批时间" v-decorator="['examineTime']" :trigger-change="true" style="width: 100%"
                  :disabled="disableSubmit"/>
        </a-form-item>
        <a-form-item label="验收附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload ref="uploadRef" :disabled="disableSubmit" fileType="file" bizPath="acceptance" @change="fileListChange" @delete ="fileDelete"
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

  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'
  import {queryFiles} from '../../../requestAction/request'


  export default {
    name: "CompanyAcceptanceModal",
    components: {
      JDate,
      JUpload,
    },
    data() {
      return {
        form: this.$form.createForm(this),
        fileList:'',
        deleteFiles:[],
        title: "操作",
        disableSubmit: false,
        width: 800,
        visible: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        confirmLoading: false,
        validatorRules: {
          projectName: {
            rules: [
              {required: true, message: '请输入项目名称!'},
            ]
          },
        },
        url: {
          add: "/business/companyAcceptance/add",
          declare: "/business/companyAcceptance/declare",
          edit: "/business/companyAcceptance/edit",
          queryFile:"/business/companyAcceptance/queryFiles"
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

        //查询所属文件
        let _this =this;
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          _this.form.setFieldsValue(pick(this.model, 'projectName', 'examineUnit', 'examineNum', 'examineTime'))
        })
        if(record.id){
          queryFiles({id:record.id},this.url.queryFile).then((res)=>{
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
            formData.companyId = that.companyId;
            formData.fileList = that.fileList;
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
      //申报
      handDeclare() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpUrl = this.url.declare;
            let method = 'put';
            let formData = Object.assign(this.model, values);
            formData.companyId = that.companyId;
            formData.fileList = that.fileList;
            httpAction(httpUrl, formData, method).then((res) => {

              if (res.success) {
                that.$message.success(res.message);
                that.$emit('declare');
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
        this.form.setFieldsValue(pick(row, 'projectName', 'examineUnit', 'examineNum', 'examineTime'))
      },
      fileListChange(newFileList){
        this.fileList = newFileList;
      },
      fileDelete(file){
        this.deleteFiles.push(file.response.message);
      },
    },
    props: {
      companyId: ""
    }
  }
</script>