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
          <a-col span='12'>
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['deviceName', validatorRules.deviceName]" placeholder="请输入设备名称"
                       :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['deviceNumber', validatorRules.deviceNumber]" placeholder="请输入设备编号"
                       :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['deviceType', validatorRules.deviceType]"
                                 :trigger-change="true" dictCode="siteType" placeholder="请选择设备类型" :disabled="disable"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="监测仪器状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['deviceState', validatorRules.deviceState]"
                                 :trigger-change="true" dictCode="siteState" placeholder="请选择监测仪器状态"
                                 :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="污染因子名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['pollutionCode', validatorRules.pollutionCode]" show-search style="width: 100%"
                        placeholder="请输入污染因子名称" optionFilterProp="children" @change="pollutionChange"
                        :disabled="disableSubmit">
                <a-select-option v-for="item in items" :key="item.key" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="因子编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="code" :disabled="disable"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="监测仪器厂家" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-input v-decorator="['deviceFactory']" placeholder="请输入监测仪器厂家" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="检测仪器型号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['deviceModel']" placeholder="请输入检测仪器型号" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="做样周期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sampleCycle']" placeholder="请输入做样周期" :disabled="disableSubmit"
                       addon-after="分钟"></a-input>

            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="量程上限" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['rangeMax', validatorRules.rangeMax]" placeholder="请输入量程上限"
                       :disabled="disableSubmit" :addon-after="unit">
              </a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="量程下限" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['rangeMin']" placeholder="请输入量程下限" :disabled="disableSubmit" :addon-after="unit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="检出限" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['checkoutMax']" placeholder="请输入检出限" :disabled="disableSubmit" :addon-after="unit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="检出限单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['checkoutUnit']" placeholder="请输入检出限单位" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="生产日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择生产日期" v-decorator="['productDate']" :trigger-change="true" style="width: 100%"
                      :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="安装日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择安装日期" v-decorator="['installDate']" :trigger-change="true" style="width: 100%"
                      :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="保质日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择保质日期" v-decorator="['shelfLifeDate']" :trigger-change="true" style="width: 100%"
                      :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['deviceConcatUser']" placeholder="请输入联系人" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['deviceConcatMobile']" placeholder="请输入联系电话" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="设备启用日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择设备启用日期" v-decorator="['useDate']" :trigger-change="true" style="width: 100%"
                      :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="运维单位" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-input v-decorator="['operationCompany']" placeholder="请输入运维单位" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="运维负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['operationUser']" placeholder="请输入运维负责人" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="运维负责人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['operationMobile']" placeholder="请输入运维负责人电话" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="备注" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-textarea v-decorator="['content']" placeholder="请输入备注" :disabled="disableSubmit"></a-textarea>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="附件上传" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <j-upload ref="uploadRef" fileType="file" bizPath="monitorModal" @change="fileListChange"
                        @delete="fileDelete" :disabled="disableSubmit"
              ></j-upload>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
      <a-button type="primary" @click="handleOk" v-if="!disableSubmit">保存</a-button>
    </template>
  </j-modal>
</template>

<script>

  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JUpload from '@/components/jeecg/JUpload'
  import {queryPollution, queryFiles,queryUnit} from '../../../requestAction/request'


  export default {
    name: "SiteMonitorDeviceModal",
    components: {
      JDate,
      JDictSelectTag,
      JUpload,
    },
    data() {
      return {
        form: this.$form.createForm(this),
        unit:"",
        disable: true,
        disableSubmit: false,
        code: "",
        fileList: '',
        items: [],
        title: "操作",
        width: 1200,
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
          deviceName: {
            rules: [
              {required: true, message: '请输入设备名称!'},
              {validator: (rule, value, callback) => validateDuplicateValue('site_monitor_device', 'device_name', value, this.model.id, callback)},
            ]
          },
          deviceNumber: {
            rules: [
              {validator: (rule, value, callback) => validateDuplicateValue('site_monitor_device', 'device_number', value, this.model.id, callback)},
            ]
          },
          deviceType: {
            rules: [
              {required: true, message: '请输入设备类型!'},
            ]
          },
          pollutionCode: {
            rules: [
              {required: true, message: '请输入污染因子!'},
            ]
          },
          deviceState: {
            rules: [
              {required: true, message: '请输入监测仪器状态!'},
            ]
          },
          rangeMax: {
            rules: [
              {required: true, message: '请输入量程上限!'},
            ]
          },
        },
        url: {
          add: "/device/siteMonitorDevice/add",
          edit: "/device/siteMonitorDevice/edit",
          queryFile: "/device/siteMonitorDevice/queryFiles"
        }
      }
    },
    created() {
      let that = this;
      //查询污染因子
      queryPollution().then((res) => {
        if (res.success) {
          that.items = res.result;
        }
      })
    },
    methods: {
      add() {
        this.edit({});
      },
      edit(record) {
        let _this = this;
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          _this.form.setFieldsValue(pick(this.model, 'deviceName', 'deviceNumber', 'deviceType', 'pollutionCode', 'sampleCycle', 'deviceState', 'deviceModel', 'deviceFactory', 'rangeMax', 'rangeMin', 'checkoutMax', 'checkoutUnit', 'productDate', 'installDate', 'shelfLifeDate', 'deviceConcatUser', 'deviceConcatMobile', 'useDate', 'operationCompany', 'operationUser', 'operationMobile', 'content'))
          _this.form.setFieldsValue(pick({deviceType: this.siteType}, 'deviceType'))
          _this.code = this.model.pollutionCode;
          queryUnit({code:this.model.pollutionCode}).then((res) => {
              debugger
            _this.unit = res.result.unit;
          });
        })
        if (record.id) {
          queryFiles({id: record.id}, this.url.queryFile).then((res) => {
            _this.$nextTick(() => {
              _this.$refs.uploadRef.initFileListArr(res.result);
              let arr = [];
              for (var a = 0; a < _this.$refs.uploadRef.fileList.length; a++) {
                arr.push(_this.$refs.uploadRef.fileList[a].response.message)
              }
              if (arr.length > 0) {
                _this.fileList = arr.join(",")
              } else {
                _this.fileList = '';
              }
            });
          });
        } else {
          //清空上传列表
          _this.$nextTick(() => {
            _this.$refs.uploadRef.initFileList("");
            let arr = [];
            for (var a = 0; a < _this.$refs.uploadRef.fileList.length; a++) {
              arr.push(_this.$refs.uploadRef.fileList[a].response.message)
            }
            if (arr.length > 0) {
              _this.fileList = arr.join(",")
            } else {
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
            formData.fileList = that.fileList;
            formData.monitorId = that.monitorId;
            console.log("表单提交数据", formData)
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
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'deviceName', 'deviceNumber', 'deviceType', 'pollutionCode', 'sampleCycle', 'deviceState', 'deviceModel', 'deviceFactory', 'rangeMax', 'rangeMin', 'checkoutMax', 'checkoutUnit', 'productDate', 'installDate', 'shelfLifeDate', 'deviceConcatUser', 'deviceConcatMobile', 'useDate', 'operationCompany', 'operationUser', 'operationMobile', 'content'))
      },
      fileListChange(newFileList) {
        this.fileList = newFileList;
      },
      fileDelete(file) {
        this.deleteFiles.push(file.response.message);
      },
      pollutionChange(key) {
        this.code = key;
        queryUnit({code:this.code}).then((res) => {
          this.unit = res.result.unit;
        });
      }
    }, props: {
      siteType: '',
      monitorId: ''
    }
  }
</script>