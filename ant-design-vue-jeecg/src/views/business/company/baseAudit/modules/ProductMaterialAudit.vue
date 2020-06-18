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
    <a-form :form="form">

      <a-form-item label="类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag type="radio" v-decorator="['outputType']"  @change = "handleTypeChange"  :disabled="disable"
                           :trigger-change="true" dictCode="output_type" placeholder="请选择类别"/>
      </a-form-item>
      <a-form-item label="产品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input v-decorator="['outputName']" placeholder="请输入产品名称" :disabled="disable"></a-input>
      </a-form-item>
      <a-form-item label="产量" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input-number v-decorator="['yield']" placeholder="请输入产量" style="width: 100%" :disabled="disable"/>
      </a-form-item>
      <a-form-item label="最大储量" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input v-decorator="['maxStore']" placeholder="请输入最大储量" :disabled="disable"></a-input>
      </a-form-item>
      <a-form-item label="CAS号" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input v-decorator="['cas']" placeholder="请输入CAS号" :disabled="disable"></a-input>
      </a-form-item>
      <a-form-item label="储存方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag type="list" v-decorator="['storeType']"  :disabled="disable"
                           :trigger-change="true" dictCode="store_type" placeholder="请选择储存方式"/>
      </a-form-item>
      <a-form-item label="危化品类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag type="list" v-decorator="['hazardousChemicalsCategory']"  :disabled="disable"
                           :trigger-change="true" dictCode="hazardous_chemicals_category" placeholder="请选择危化品类别"/>
      </a-form-item>
      <a-form-item label="主要危险性" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag type="list" v-decorator="['mainRisk']" :trigger-change="true" :disabled="disable"
                           dictCode="main_risk" placeholder="请选择主要危险性"/>
      </a-form-item>
      <a-form-item label="重点监管" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag type="list" v-decorator="['supervision']" :disabled="disable"
                           :trigger-change="true" dictCode="yes_or_no" placeholder="请选择重点监管"/>
      </a-form-item>
      <a-form-item label="剧毒" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag type="list" v-decorator="['toxic']" :trigger-change="true" :disabled="disable" dictCode="yes_or_no" placeholder="请选择剧毒"/>
      </a-form-item>
      <a-form-item label="易制毒" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag type="list" v-decorator="['precursorChemicals']" :disabled="disable" :trigger-change="true" dictCode="yes_or_no" placeholder="请选择易制毒"/>
      </a-form-item>
      <a-form-item label="状态" v-show="displayPro" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag type="list"  v-decorator="['status']" :trigger-change="true" :disabled="disable" dictCode="output_status" placeholder="请选择状态"/>
      </a-form-item>
      <a-form-item label="是否领证" v-show="displayPro" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag type="list"  v-decorator="['certified']" :trigger-change="true" :disabled="disable" dictCode="yes_or_no" placeholder="请选择是否领证"/>
      </a-form-item>
      <a-form-item label="主要原材料"  v-show="displayPro" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-textarea v-decorator="['rawMaterials']" rows="4" placeholder="请输入主要原材料" :disabled="disable"/>
      </a-form-item>
      <a-form-item label="主要生产设备" v-show="displayPro" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-textarea v-decorator="['proEquipment']" rows="4" placeholder="请输入主要生产设备" :disabled="disable"/>
      </a-form-item>
      <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-textarea v-decorator="['remake']" rows="4" placeholder="请输入备注" :disabled="disable"/>
      </a-form-item>

    </a-form>

    <audit-footer ref="auditFooter" @success ="success"></audit-footer>

  </j-modal>
</template>

<script>
  import {queryAduitBase} from "../../../requestAction/request"
  import AuditFooter from "../modules/AuditFooter";
  import pick from "lodash.pick";
  import JDictSelectTag from  '@/components/dict/JDictSelectTag.vue'
  import JDate from '@/components/jeecg/JDate'

  export default {
        name: "ProductMaterialAudit",
    components:{
      AuditFooter,JDictSelectTag,JDate
    },data(){
      return{
        applyInfo:'',
        outputType:'',
        confirmLoading:true,
        visible: false,
        width:1200,
        title:"基本信息申报审核-员工信息",
        disable:true,
        form: this.$form.createForm(this),
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
      }

    },
    methods:{
      handleCancel () {
        this.visible = false;
      },
      handleOk(){
        this.confirmLoading=true;

        //提交数据，做数据处理
        //调用
        this.$refs.auditFooter.submit(this.applyInfo);

      },
      //关闭窗口
      success(){
        this.visible = false;
      },
      auditModal(record){
        this.applyInfo = record;
        let that = this;
        //查询对应的待审批数据  并查询出对应的对比数据
        queryAduitBase({applyId:record.id}).then((res)=>{
          if(res.success){
            that.$nextTick(() => {
              this.form.setFieldsValue(pick(res.result.info,'outputType','outputName','yield','maxStore','cas','storeType','hazardousChemicalsCategory','mainRisk','supervision','toxic','precursorChemicals','status','certified','rawMaterials','proEquipment','remake'))

            });

            //判断修改处的  后面需要处理一下
            if(res.cueColor === ''){

            }
          }
        });

        this.$nextTick(() => {
          that.$refs.auditFooter.applyer = this.applyInfo.createBy;
          that.$refs.auditFooter.applyTime = this.applyInfo.createTime;
        });

        this.confirmLoading = false;
      },
      modalFormOk(){

      },
      handleTypeChange(value){
        this.outputType = value;
      }
    },created() {
      console.log("Jmodal",this.title)
    },computed:{
      displayPro:function(){
        console.log(this.outputType)
        return this.outputType==='1';
      }
    }
  }

</script>

<style scoped>

</style>