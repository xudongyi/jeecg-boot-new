<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     :validate-status="modalStatus.projectName">
          <a-input v-decorator="['projectName', validatorRules.projectName]" placeholder="请输入项目名称"
                   :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="审批单位" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     :validate-status="modalStatus.examineUnit">
          <a-input v-decorator="['examineUnit']" placeholder="请输入审批单位" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="审批文号" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     :validate-status="modalStatus.examineNum">
          <a-input v-decorator="['examineNum']" placeholder="请输入审批文号" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="审批时间" :labelCol="labelCol" :wrapperCol="wrapperCol"
                     :validate-status="modalStatus.examineTime">
          <j-date placeholder="请选择审批时间" v-decorator="['examineTime']" :trigger-change="true" style="width: 100%"
                  :disabled="disableSubmit"/>
        </a-form-item>
        <a-form-item label="验收附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload ref="uploadRef" :disabled="disableSubmit" fileType="file" bizPath="acceptance"
          ></j-upload>
        </a-form-item>

      </a-form>
    </a-spin>
    <audit-footer ref="auditFooter" @success="success" :disable="applyInfo.isView"></audit-footer>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
      <a-button type="primary" @click="handleOk" v-show="!applyInfo.isView">确认</a-button>
    </template>
  </j-modal>
</template>


<script>

  import pick from "lodash.pick";
  import AuditFooter from "./AuditFooter";
  import {queryAduitBase, queryFiles, queryUserByName} from "../../../requestAction/request";
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'

  export default {
    name: "AcceptanceAudit",
    components: {
      AuditFooter,
      JDate,
      JUpload,
    }, data() {
      return {
        applyInfo: {},
        companyId: '',
        confirmLoading: true,
        disableSubmit: true,
        visible: false,
        width: 1200,
        title: "基础信息审核-竣工验收信息",
        form: this.$form.createForm(this),
        fileList:'',
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },

        validatorRules: {
          projectName: {
            rules: [
              {required: true, message: '请输入项目名称!'},
            ]
          },
        },
        url: {
          queryFile: "/business/companyAcceptance/queryFiles"
        },
        modalStatus: {}

      }

    },
    methods: {

      handleCancel() {
        this.visible = false;
      },
      handleOk() {
        this.confirmLoading = true;

        //提交数据，做数据处理
        //调用
        this.$refs.auditFooter.submit(this.applyInfo);

      },
      //关闭窗口
      success() {
        this.visible = false;
      },
      auditModal(record) {
        this.applyInfo = record;
        let that = this;
        //查询对应的待审批数据  并查询出对应的对比数据
        queryAduitBase({applyId: record.id}).then((res) => {
          if (res.success) {
            that.form.resetFields();
            that.model = Object.assign({}, res.result.info);
            that.visible = true;
            that.$nextTick(() => {
              that.form.setFieldsValue(pick(this.model, 'projectName', 'examineUnit', 'examineNum', 'examineTime'))
            })
            //全部
            if (res.result.cueColor === 'ALL') {
              for (let i = 0, len = res.result.cueField.length; i < len; i++) {
                that.modalStatus[res.result.cueField[i]] = "warning";
              }
            } else {
              for (let i = 0, len = res.result.cueField.length; i < len; i++) {
                that.modalStatus[res.result.cueField[i]] = "error";
              }
            }
          }
        });
        queryFiles({id:record.newId},this.$data.url.queryFile).then((res)=>{
          that.$nextTick(() => {
            that.$refs.uploadRef.initFileListArr(res.result);
          });

        });
        this.$nextTick(() => {
          that.$refs.auditFooter.edit(this.applyInfo);
        });
        if (this.applyInfo.isView) {
        } else {
          that.applyInfo.isView = false;
        }
        this.confirmLoading = false;
      },
      modalFormOk() {

      },
    }, created() {

    }
  }
</script>

<style scoped>

</style>