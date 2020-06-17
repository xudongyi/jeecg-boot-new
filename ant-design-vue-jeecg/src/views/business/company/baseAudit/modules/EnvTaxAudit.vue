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
          <a-col span='12'>
            <a-form-item label="许可证编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag placeholder="请输入后进行选择" v-decorator="['licenceCode', validatorRules.licenceCode]"
                                   dict="company_dirty_allow,licence_code,licence_code" :async="true"
                                   :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="排放口大类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['ventCategory', validatorRules.ventCategory]"
                                 :trigger-change="true" dictCode="ventcategory"
                                 placeholder="请选择排放口大类" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="排放口编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ventCode']" placeholder="请输入排放口编号" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="排放口名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ventName']" placeholder="请输入排放口名称" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="排污口税源编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['taxsourceCode', validatorRules.taxsourceCode]" placeholder="请输入排污口税源编号"
                       :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="排放方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['letMode', validatorRules.letMode]" :trigger-change="true"
                                 dictCode="letmode" placeholder="请选择排放方式" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="排污口位置" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-row>
                <a-col span='24'>
                  <a-input v-decorator="['ventLocate', validatorRules.ventLocate]" placeholder="请输入排污口位置"
                           :disabled="disableSubmit"></a-input>
                </a-col>
              </a-row>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="排污口经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ventLongitude']" placeholder="请输入排污口经度" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="排污口纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ventLatitude']" placeholder="请输入排污口纬度" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24' v-if="model.ventCategory==='1'">
            <a-form-item label="水污染排污去向" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <j-dict-select-tag type="list" v-decorator="['letDirection']" :trigger-change="true"
                                 dictCode="letdirection"
                                 placeholder="请选择水污染排污去向" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12' v-if="model.ventCategory==='0'">
            <a-form-item label="大气污染排放口类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['ventType']" :trigger-change="true" dictCode="venttype"
                                 placeholder="请选择大气污染物排放口类别" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="主管税务科所" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['taxDepartment', validatorRules.taxDepartment]" placeholder="请输入主管税务科所"
                       :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <audit-footer ref="auditFooter" @success="success"></audit-footer>
  </j-modal>
</template>


<script>

  import pick from "lodash.pick";
  import AuditFooter from "./AuditFooter";
  import {queryAduitBase, queryUserByName} from "../../../requestAction/request";
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

  export default {
    name: "EnvTaxAudit",
    components: {
      AuditFooter,
      JDate,
      JUpload,
      JDictSelectTag,
      JSearchSelectTag
    }, data() {
      return {
        companyId: '',
        confirmLoading: true,
        disableSubmit: true,
        visible: false,
        width: 1200,
        title: "基础信息审核-环保税信息",
        form: this.$form.createForm(this),
        model: {},
        asyncSelectValue: "",
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
              {required: false, message: '请输入许可证编号!'},
            ]
          },
          ventCategory: {
            rules: [
              {required: true, message: '请输入排放口大类!'},
            ]
          },
          taxsourceCode: {
            rules: [
              {required: true, message: '请输入排污口税源编号!'},
            ]
          },
          letMode: {
            rules: [
              {required: true, message: '请输入排放方式!'},
            ]
          },
          ventLocate: {
            rules: [
              {required: true, message: '请输入排污口位置!'},
            ]
          },
          taxDepartment: {
            rules: [
              {required: true, message: '请输入主管税务科所!'},
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
              this.form.setFieldsValue(pick(this.model, 'licenceCode', 'ventCategory', 'ventCode', 'ventName', 'taxsourceCode', 'letMode', 'ventLocate', 'ventLongitude', 'ventLatitude', 'letDirection', 'ventType', 'taxDepartment'))
            })


            //判断修改处的  后面需要处理一下
            if (res.cueColor === '') {

            }
          }
        });
        that.$nextTick(() => {
          that.$refs.auditFooter.applyer = this.applyInfo.createBy;
          that.$refs.auditFooter.applyTime = this.applyInfo.createTime;
        })
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