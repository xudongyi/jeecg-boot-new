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

        <a-form-item label="清洁生产报告名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['reportName', validatorRules.reportName]" placeholder="请输入清洁生产报告名称"
                   :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="报告时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择报告时间" v-decorator="['reportTime']" :trigger-change="true" style="width: 100%"
                  :disabled="disableSubmit"/>
        </a-form-item>
        <a-form-item label="清洁生成报告及专家意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload ref="opinionFiles" :disabled="disableSubmit" fileType="file" bizPath="cleanproduct"
                    @change="fileListChange" @delete="fileDelete"
          ></j-upload>
        </a-form-item>
        <a-form-item label="落实情况简要描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['conditionDescribe']" placeholder="请输入落实情况简要描述"
                      :disabled="disableSubmit"></a-textarea>
        </a-form-item>
        <a-form-item label="落实情况附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload ref="describeFiles" :disabled="disableSubmit" fileType="image" bizPath="cleanproduct"
                    @change="imgListChange" @delete="imgDelete"
          ></j-upload>
        </a-form-item>

      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
      <a-button type="primary" @click="handleOk" v-if="!disableSubmit">暂存</a-button>
      <a-button type="primary" @click="handDeclare" v-if="!disableSubmit">申报</a-button>
    </template>
  </j-modal>
</template>

<script>

  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'
  import JImageUpload from '@/components/jeecg/JImageUpload'
  import {queryFiles} from "../../../requestAction/request";


  export default {
    name: "CompanyCleanProductModal",
    components: {
      JDate,
      JUpload,
      JImageUpload,
    },
    data() {
      return {
        form: this.$form.createForm(this),
        fileList:'',
        imgList:'',
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
          reportName: {
            rules: [
              {required: true, message: '请输入清洁生产报告名称!'},
            ]
          },
        },
        url: {
          add: "/cleanProduct/companyCleanProduct/add",
          edit: "/cleanProduct/companyCleanProduct/edit",
          declare: "/cleanProduct/companyCleanProduct/declare",
          queryFile: "/cleanProduct/companyCleanProduct/queryFiles"
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
        let _this = this;
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'reportName', 'reportTime', 'conditionDescribe'))
        })
        if (record.id) {
          queryFiles({id: record.id}, this.$data.url.queryFile).then((res) => {
            _this.$nextTick(() => {
              _this.$refs.opinionFiles.initFileListArr(res.result.fileResult);
              let arr = [];
              for(var a=0;a< _this.$refs.opinionFiles.fileList.length;a++){
                arr.push( _this.$refs.opinionFiles.fileList[a].response.message)
              }
              if(arr.length>0){
                _this.fileList  = arr.join(",")
              }else{
                _this.fileList = '';
              }

              _this.$refs.describeFiles.initFileListArr(res.result.imgResult);
              let arr1 = [];
              for(var a=0;a< _this.$refs.describeFiles.imgList.length;a++){
                arr1.push( _this.$refs.describeFiles.imgList[a].response.message)
              }
              if(arr1.length>0){
                _this.imgList  = arr1.join(",")
              }else{
                _this.imgList = '';
              }
            });
          });
        } else {
          //清空上传列表
          _this.$nextTick(() => {
            _this.$refs.opinionFiles.initFileList("");
            _this.fileList = _this.$refs.opinionFiles.fileList
            _this.$refs.describeFiles.initFileList("");
            _this.imgList = _this.$refs.describeFiles.imgList
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
            formData.companyId = this.companyId;
            formData.fileList = that.fileList;
            formData.imgList = that.imgList;
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
            formData.companyId = this.companyId;
            formData.fileList = that.fileList;
            formData.imgList = that.imgList;
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
        this.form.setFieldsValue(pick(row, 'reportName', 'reportTime', 'conditionDescribe'))
      },
      fileListChange(newFileList) {
        this.fileList = newFileList;
      },
      fileDelete(file) {
        this.deleteFiles.push(file.response.message);
      },
      imgListChange(newImgList) {
        this.imgList = newImgList;
      },
      imgDelete(file) {
        this.deleteFiles.push(file.response.message);
      },

    },
    props: {
      companyId: ""
    }
  }
</script>