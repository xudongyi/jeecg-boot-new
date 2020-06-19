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
              <j-date placeholder="请选择有效开始时间" v-decorator="['validStarttime']" :trigger-change="true"
                             style="width: 100%"
                             :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="至" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择有效结束时间" v-decorator="['validEndtime']" :trigger-change="true"
                             style="width: 100%"
                             :disabled="disableSubmit"/>
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
            <a-form-item label="许可内容附件" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <j-upload v-decorator="['files']" :trigger-change="true" :disabled="disableSubmit"></j-upload>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <audit-footer ref="auditFooter" @success ="success" :disable="applyInfo.isView"></audit-footer>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel" >关闭</a-button>
      <a-button type="primary" @click="handleOk" v-show="!applyInfo.isView">确认</a-button>
    </template>  </j-modal>
</template>


<script>

  import pick from "lodash.pick";
  import AuditFooter from "./AuditFooter";
  import {queryAduitBase, queryUserByName} from "../../../requestAction/request";
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'
  import moment from "moment";

  export default {
    name: "SolidWasteAudit",
    components: {
      AuditFooter,
      JDate,
      JUpload,
    }, data() {
      return {
        applyInfo:{},

        endOpen: false,
        companyId: '',
        disableSubmit: true,
        title: "基础信息审核-固废许可证信息",
        form: this.$form.createForm(this),
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
          licenceCode: {
            rules: [
              {required: true, message: '请输入许可证编号!'},
            ]
          },
        },
        url: {}

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
              that.form.setFieldsValue(pick(this.model, 'licenceCode', 'certificateTime', 'validStarttime', 'validEndtime', 'certificateOffice', 'dirtyType', 'files'))
            })


            //判断修改处的  后面需要处理一下
            if (res.cueColor === '') {

            }
          }
        });
        this.$nextTick(() => {
          that.$refs.auditFooter.edit( this.applyInfo) ;
        });
        if(this.applyInfo.isView) {
        }
        else {
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