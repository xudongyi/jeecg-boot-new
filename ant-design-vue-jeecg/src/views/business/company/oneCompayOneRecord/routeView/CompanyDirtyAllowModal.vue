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
        <a-row>
          <a-col span='24'>
            <a-form-item label="许可证编号" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-input v-decorator="['licenceCode', validatorRules.licenceCode]" placeholder="请输入许可证编号"
                       :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="有效期限" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker  placeholder="请选择有效开始时间" v-decorator="['validStarttime']" :trigger-change="true"
                             style="width: 100%"  :disabled-date="disabledStartDate"
                             :disabled="disableSubmit" @openChange="handleStartOpenChange"  @change="getStartTime" />
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="至" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker placeholder="请选择有效结束时间" v-decorator="['validEndtime']" :trigger-change="true"
                             style="width: 100%" :disabled-date="disabledEndDate" :open="endOpen"
                             :disabled="disableSubmit" @openChange="handleEndOpenChange" @change="getEndTime"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="发证日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择发证日期" v-decorator="['certificateTime']" :trigger-change="true" style="width: 100%"
                      :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="发证机关" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['certificateOffice']" placeholder="请输入发证机关" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="排污类别" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-textarea v-decorator="['dirtyType']" placeholder="请输入排污类别" :disabled="disableSubmit"></a-textarea>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="许可证附件" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <j-upload ref="uploadRef" :disabled="disableSubmit" fileType="file" bizPath="dirtyallow" @change="fileListChange" @delete ="fileDelete"
              ></j-upload>
            </a-form-item>
          </a-col>
        </a-row>
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
  import moment from "moment";
  import {queryFiles} from "../../../requestAction/request";


  export default {
    name: "CompanyDirtyAllowModal",
    components: {
      JDate,
      JUpload,
    },
    data() {
      return {
        validStarttime: "",
        validEndtime: "",
        endOpen: false,
        disableSubmit:false,
        form: this.$form.createForm(this),
        fileList:'',
        deleteFiles:[],
        title: "操作",
        width: 800,
        visible: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        labelCols: {
          xs: {span: 24},
          sm: {span: 3},
        },
        wrapperCols: {
          xs: {
            span: 24
          },
          sm: {
            span: 20

          }
        },
        confirmLoading: false,
        validatorRules: {
          licenceCode: {
            rules: [
              {required: true, message: '请输入许可证编号!'},
            ]
          },
        },
        url: {
          add: "/dirty/companyDirtyAllow/add",
          edit: "/dirty/companyDirtyAllow/edit",
          declare: "/dirty/companyDirtyAllow/declare",
          queryFile:"/dirty/companyDirtyAllow/queryFiles"
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
          this.model.validStarttime = !this.model.validStarttime?null:moment(this.model.validStarttime,this.dateFormat)
          this.model.validEndtime = !this.model.validEndtime?null:moment(this.model.validEndtime,this.dateFormat)
          this.form.setFieldsValue(pick(this.model, 'licenceCode', 'certificateTime', 'validStarttime', 'validEndtime', 'certificateOffice','dirtyType'))
        })
        if(record.id){
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
      handleCancel() {
        this.close()
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
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'licenceCode', 'certificateTime', 'validStarttime', 'validEndtime', 'certificateOffice','dirtyType'))
      },
      disabledStartDate(validStarttime) {
        const validEndtime = this.validEndtime;
        if (!validStarttime || !validEndtime) {
          return false;
        }
        return validStarttime.valueOf() > validEndtime.valueOf();
      },
      disabledEndDate(validEndtime) {
        const validStarttime = this.validStarttime;
        if (!validEndtime || !validStarttime) {
          return false;
        }
        return validStarttime.valueOf() >= validEndtime.valueOf();
      },
      handleStartOpenChange(open) {
        if (!open) {
          this.endOpen = true;
        }
      },
      getStartTime(date,dateString) {
          this.validStarttime=date;
      },
      getEndTime(date,dateString) {
        this.validEndtime=date;
      },
      handleEndOpenChange(open) {
        this.endOpen = open;
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
    },
    // watch: {
    //   startValue(val) {
    //     console.log('startValue', val);
    //   },
    //   endValue(val) {
    //     console.log('endValue', val);
    //   },
    // },
  }
</script>