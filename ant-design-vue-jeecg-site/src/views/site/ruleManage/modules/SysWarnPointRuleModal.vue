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
      <a-layout>
          <a-layout-sider theme="light" width="260">
            <a-form :form="form">
              <a-form-item label="站点类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['siteType']" :trigger-change="true" dictCode="siteType" placeholder="请选择站点类型"/>
              </a-form-item>
              <a-form-item label="站点名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input type="list" v-decorator="['siteName']" :trigger-change="true" dictCode="" placeholder="请输入站点名称"/>
              </a-form-item>

            </a-form>
          </a-layout-sider>
          <a-layout-content>

            <a-table
              ref="table"
              size="middle"
              bordered
              rowKey="id"
              :columns="columns"
              :dataSource="dataSource"
              :pagination="ipagination"
              :loading="loading"
              :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
              class="j-table-force-nowrap"
              @change="handleTableChange">

              <span slot="isUsed" slot-scope="isUsed">
                <div :style="{color: isUsed === '停用'? 'red':'black'}">{{isUsed}}</div>
              </span>

            </a-table>

          </a-layout-content>
      </a-layout>



    </a-spin>
  </j-modal>


</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'


  export default {
    name: "SysWarnPointRuleModal",
    mixins:[JeecgListMixin, mixinDevice],
    components: { 
      JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:1200,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 14 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/swpr/sysWarnPointRule/add",
          edit: "/swpr/sysWarnPointRule/edit",
          list: "/swr/sysWarnRule/list",
        },
        columns: [
          {
            title:'策略类型',
            align:"center",
            dataIndex: 'ruleType_dictText'
          },
          {
            title:'是否发送短信',
            align:"center",
            dataIndex: 'isSendMsg_dictText'
          },
          {
            title:'发送频率(次/天)',
            align:"center",
            dataIndex: 'msgRate'
          },
          {
            title:'短信接收时段',
            align:"center",
            dataIndex: 'warnStarttime',
            customRender: function (text,record) {
              if(text)
                return text.substring(0,5) + '~' + (record.warnEndtime).substring(0,5);
            }

          },
          {
            title:'策略说明',
            align:"center",
            dataIndex: 'content'
          },
          {
            title:'策略状态',
            align:"center",
            dataIndex: 'isUsed_dictText',
            scopedSlots: { customRender: 'isUsed' },
          }]
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        console.log("!!!",record)
        //根据策略的 id--- 查询这个一个策略
        if(record.ruleId){
          this.queryParam = {id:record.ruleId};
          this.selectedRowKeys=[record.ruleId]
        }else{
          this.queryParam = {};
          this.selectedRowKeys=[]
        }

        this.loadData();
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'siteName','siteType'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
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

            console.log("表单提交数据",formData)
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
        this.form.setFieldsValue(pick(row,'monitorId','ruleId','isUsed'))
      },

      
    }
  }
</script>