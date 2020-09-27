<template>
  <div style="width: 100%;height: 100%">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" >
        <a-form-item label="污染源类型"  :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select mode="tags" v-decorator="['sourceType', validatorRules.sourceType]" style="width: 100%" placeholder="请选择污染源类型" @change="handleChange">
            <a-select-option v-for="sourceType in sourceTypes" :key="sourceType.value" :value="sourceType.key">
              {{sourceType.value}}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="重点排污单位" :labelCol="labelCol" :wrapperCol="wrapperCol" >
          <j-dict-select-tag type="radio" v-decorator="['intensiveUnit', validatorRules.intensiveUnit]" :trigger-change="true" dictCode="yn" placeholder="请选择重点排污单位"/>
        </a-form-item>
        <a-form-item label="重点监控企业" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="radio" v-decorator="['intensiveCompany', validatorRules.intensiveCompany]" :trigger-change="true" dictCode="yn" placeholder="请选择重点监控企业"/>
        </a-form-item>
        <a-form-item label="重点监控类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select mode="tags" v-decorator="['keyMonitor']" style="width: 100%" placeholder="请选择污染源类型" @change="handleChange">
            <a-select-option v-for="keyMonitor in keyMonitors" :key="keyMonitor.value" :value="keyMonitor.key">
              {{keyMonitor.value}}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
      <div style="display: flex;align-items: center;justify-content: center;">
        <a-button type="primary" @click="handleOk" style="margin-right: 5px" >保存</a-button>
        <a-button type="primary" @click="reset" style="margin-left: 5px">重置</a-button>
      </div>
    </a-spin>
  </div>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import {loadSourceDirectory} from "../../../requestAction/request";


  export default {
    name: "SourceDirectoryModal",
    components: { 
      JDictSelectTag,
    },
    data () {
      return {
        sourceTypes:[{"key":"0","value":"大气污染源"},{"key":"1","value":"水污染源"},{"key":"2","value":"固体废物污染源"},{"key":"3","value":"噪声污染源"},{"key":"4","value":"辐射污染源"}],
        keyMonitors:[{"key":"0","value":"废水"},{"key":"1","value":"废气"},{"key":"2","value":"危险废物"},{"key":"3","value":"固体废物"},{"key":"4","value":"重金属"},{"key":"5","value":"污水处理厂"}],
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 3 },
          sm: { span: 2 },
        },
        wrapperCol: {
          xs: { span: 6 },
          sm: { span: 8},
        },
        confirmLoading: false,
        validatorRules: {
          sourceType: {
            rules: [
              { required: true, message: '请输入污染源类型!'},
            ]
          },
          intensiveUnit: {
            rules: [
              { required: true, message: '请输入重点排污单位!'},
            ]
          },
          intensiveCompany: {
            rules: [
              { required: true, message: '请输入重点监控企业!'},
            ]
          }
        },
        url: {
          add: "/sourceDirectory/add",
          edit: "/sourceDirectory/edit",
        }
      }
    },
    created () {
    },
    watch: {
      companyId: {
        immediate: true,    // 这句重要
        deep:true,
        handler (val) {
          let that = this;
          let record = {};
          if(val ==null ||val ===""){
            that.edit({});
          }else {
            loadSourceDirectory({companyId: val}).then((res) => {
              if (res.success) {
                record = res.result;
                that.edit(record);
              } else {
                console.log(res.message);
              }
            });
          }
        }
      }
    },
    methods: {
      reset(){
        this.form.resetFields();
      },
      edit (record) {
        debugger
        if(!record){
          record = {};
          record.intensiveUnit = "1";
          record.intensiveCompany = "1";
        }
        record.sourceType = record.sourceType.split(",")
        record.keyMonitor = record.keyMonitor.split(",")
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'sourceType','intensiveUnit','intensiveCompany','keyMonitor'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        debugger
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
            formData.sourceType = formData.sourceType.join(",");
            formData.keyMonitor = formData.keyMonitor.join(",");
            formData.companyId = that.companyId;
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
        this.form.setFieldsValue(pick(row,'companyId','sourceType','intensiveUnit','intensiveCompany','keyMonitor'))
      },

      
    },
    props: {
      companyId: "",
      operationShow: "",
      listType: ""
    },
  }
</script>
<style scoped>
    .ant-row ,.ant-form-item{
      display: flex;
      align-items: center;
      justify-content:center
    }
</style>